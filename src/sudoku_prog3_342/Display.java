package sudoku_prog3_342;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Display extends JPanel {
	public int[][] sudokuArray; // contains the array that contains the current user board
	public int[][] solvedSudokuArray; // contains the array that contains solution board for the sudoku puzzle
	public int[][][] possibilities; // contains all of the possibilities for each cell of the user/display board
	public int[][][] solvedPossibilities; // contains all of the possibilities for each cell, used in the process of
											// solving
	public int[] nakedPairs; // contains all potential naked pairs

	public int chckFill = 0;

	private static final long serialVersionUID = 1L;

	/* Declare and Initiate Access Members */
	public int digit = 100;
	public MenuItem saveGame;
	public MenuItem loadGame;
	public MenuItem exitGame;
	public MenuItem single;
	public MenuItem hidden;
	public MenuItem locked;
	public MenuItem naked;
	public JMenuItem aboutSudoku;
	public JMenuItem gameAbout;
	public JMenuItem authorAbout;
	public JCheckBoxMenuItem checkFill;

	public boolean eraser = false;

	private JMenuBar menuBar;
	private JMenu file;
	private JMenu hints;
	private JMenu help;
	private Color green = new Color(19, 141, 117);
	private Color blue = new Color(133, 193, 233);
	private Color darkBlue = new Color(13, 100, 147);
	private Color grey = new Color(234, 237, 237);
	private Color black = new Color(0, 0, 0);
	private Color red = new Color(255, 0, 0);

	ButtonHandler bh1 = new ButtonHandler();
	
	JButton[][] buttons = new JButton[9][9];
	JButton[] controlbuttons = new JButton[9];
	JPanel p[][] = new JPanel[3][3];
	JPanel bigPanel = new JPanel();
	JPanel digitPanel = new JPanel();

	public Display() { // Initializes, edits, and displays all of the buttons and Icons for the GUI
		setPuzzle();
		setLayout(null); // default window layout
		JFrame frame = new JFrame("Sudoku");

		frame.add(bigPanel, BorderLayout.CENTER);
		frame.add(digitPanel, BorderLayout.EAST);
		digitPanel.setLayout(new BoxLayout(digitPanel, BoxLayout.Y_AXIS));
		digitPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		
		DigitHandler handler = new DigitHandler();
		EraseHandler eraserhandler = new EraseHandler();

		/* Add Digit Buttons to the grid and modify */
		int c = 1;
		for (int i = 0; i < 10; i++) {
			if(i == 9) {
				MyButton eraseX = new MyButton("X");
				eraseX.setForeground(black); 
				eraseX.setContentAreaFilled(false);
				eraseX.setBackground(null); 
				eraseX.setHoverBackgroundColor(darkBlue);
				eraseX.addActionListener(eraserhandler);
				digitPanel.add(eraseX);
			}
			else {
				MyButton num = new MyButton();
				num.setForeground(black); // Green text for Digits
				//num.setContentAreaFilled(false);
				// num.setBorder(null);
				num.setBackground(null); // Blue background for Digit background
				num.setHoverBackgroundColor(darkBlue); // Dark blue for the background when clicked
				controlbuttons[i] = num;
				controlbuttons[i].setText("" + c);
				controlbuttons[i].addActionListener(handler);
				digitPanel.add(controlbuttons[i]);
				c++;
			}
			

		}

		/* Erase Digit Button*/
		
	
		

		/* Set up 9x9 grid */
		bigPanel.setLayout(new GridLayout(3, 3, 5, 5));

		bigPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				p[x][y] = new JPanel(new GridLayout(3, 3));
				bigPanel.add(p[x][y]);
			}
		}

		for (int x = 0; x <= 8; x++) {
			for (int y = 0; y <= 8; y++) {
				MyButton num = new MyButton();
				//num.setForeground(black);
				//num.setBackground(grey); 
				//num.setContentAreaFilled(false);
				num.setHoverBackgroundColor(darkBlue);
				//num.setPressedBackgroundColor(blue);

				buttons[x][y] = num;
				if (sudokuArray[x][y] == 0) {
					buttons[x][y].setText("");
					buttons[x][y].addActionListener(bh1);
				} else {
					buttons[x][y].setText("" + sudokuArray[x][y]);
				}

			}
		}

		int counter = 1;

		for (int u = 0; u <= 2; u++) {
			for (int i = 0; i <= 2; i++) {
				for (int x = 0; x <= 2; x++) {
					for (int y = 0; y <= 2; y++) {
						p[u][i].add(buttons[x + u * 3][y + i * 3]);
						counter++;
					}
				}
				counter = 1;
			}
		}

		/* File Menu */
		menuBar = new JMenuBar();
		file = new JMenu();
		file.setText("File");
		menuBar.add(file);

		file.addSeparator();

		saveGame = new MenuItem(this, "Save");
		file.add(saveGame);

		loadGame = new MenuItem(this, "Load");
		file.add(loadGame);
		file.addSeparator();

		exitGame = new MenuItem(this, "Exit");
		file.add(exitGame);

		/* Hints Menu */
		hints = new JMenu();
		hints.setText("Hints");
		menuBar.add(hints);

		checkFill = new JCheckBoxMenuItem("Check fill");
		checkFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckFill == 0) {
					chckFill = 1;
				} else {
					chckFill = 0;
				}
			}
		});
		hints.add(checkFill);
		hints.addSeparator();

		single = new MenuItem(this, "Single Alogrithm");
		hints.add(single);

		hidden = new MenuItem(this, "Hidden Alogrithm");
		hints.add(hidden);

		locked = new MenuItem(this, "Locked Alogrithm");
		hints.add(locked);

		naked = new MenuItem(this, "Naked Pairs Alogrithm");
		hints.add(naked);
		hints.addSeparator();

		/* About Menu */
		help = new JMenu();
		help.setText("Help");
		menuBar.add(help);

		/* Display Information about the game - with title */
		gameAbout = new JMenuItem("About Game");
		gameAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Place digits on the grid by first clicking on one of the digit buttons and then by clicking on one of the cells in the grid.\n"
								+ "Use Eraser Button to clear cell(s) in the grid. \n"
								+ "Hint Menu: \n\t\t\tCheck On Fill mode will print error when invalid attempt is made\n"
								+ "\t\t\tSingle Algorithm fills in single blank cell.\n"
								+ "\t\t\tHidden Single Algorithm fills in single blank cell.\n"
								+ "\t\t\tLocked Candidate Algorithm reduces the number of candidate values. \n"
								+ "\t\t\tNaked Pairs Algorithm reduces the number of possible candidate values. \n",
						"About Game", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		help.add(gameAbout);

		/* Display information about Sudoku - with title */
		aboutSudoku = new JMenuItem("About Sudoku");
		aboutSudoku.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"The objective is to fill a 9x9 grid so that each column,"
								+ " each row, and each of the nine 3x3 boxes (also called blocks or regions) "
								+ "contains the digits from 1 to 9. ",
						"About Sudoku", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		help.add(aboutSudoku);

		/* Display information about the authors - with title */
		authorAbout = new JMenuItem("About Authors");
		authorAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(null,
								"Authors:\n" + "\t\t\tPatrick O'Connell (oconne16)\n"
										+ "\t\t\tJahnvi Patel (jpate201)\n" + "\t\t\tDeep Mehta (dmehta22)\n",
								"About Authors", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		help.add(authorAbout);

		/* Add the frame to the menu bar */
		frame.setJMenuBar(menuBar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(700, 700);
		frame.setVisible(true);

	}

	class MyButton extends JButton {
		private static final long serialVersionUID = 1L;
		/* Declare access members */
		private Color hoverBackgroundColor;
		private Color pressedBackgroundColor;

		public MyButton() {
			this(null);
		}

		public MyButton(String text) {
			super(text);
			super.setContentAreaFilled(false);
		}

		/* Identify when a button is pressed and get it's colors */
		protected void paintComponent(Graphics g) {
			if (getModel().isPressed()) {
				g.setColor(pressedBackgroundColor);
			} else if (getModel().isRollover()) {
				g.setColor(hoverBackgroundColor);
			} else {
				g.setColor(getBackground());
			}
			g.fillRect(0, 0, getWidth(), getHeight());
			super.paintComponent(g);
		}

		/* Get content area filled - must be changed from default */
		public void setContentAreaFilled(boolean b) {
		}

		/* Get initial and pressed button colors */
		public Color getHoverBackgroundColor() {
			return hoverBackgroundColor;
		}

		public void setHoverBackgroundColor(Color hoverBackgroundColor) {
			this.hoverBackgroundColor = hoverBackgroundColor;
		}

		public Color getPressedBackgroundColor() {
			return pressedBackgroundColor;
		}

		public void setPressedBackgroundColor(Color pressedBackgroundColor) {
			this.pressedBackgroundColor = pressedBackgroundColor;
		}

	}

	public void updateUserBoard(int digit, int a, int b) {
		sudokuArray[a][b] = digit;
		for (int i = 0; i < 9; i++) {
			possibilities[a][b][i] = 0;
		}
		possibilities[a][b][digit - 1] = 1;
		updateBoard(possibilities);
	}

	public int checkFillArray(int dig, int a, int b) {
		int i, j;
		if (solvedSudokuArray[a][b] == dig) {
			return 1;
		}
		return 0;
	}

	public int checkForWin(JButton[][] buttons) {
		int i, j;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				if (buttons[i][j].getText().equals(Integer.toString(solvedSudokuArray[i][j]))) {
					// nothing
				} else {
					return 0;
				}
			}
		}
		return 1;
	}

	/* Single Implementation of the algorithm */
	public void single() {
		System.out.printf("Yes\n");
		singleAlgorithm(sudokuArray, possibilities);
		updateBoard(possibilities);
		setDisplayBoard();
	}

	/* Hidden Implementation of the algorithm */
	public void hidden() {
		hiddenSingleAlgorithm(sudokuArray, possibilities);
		updateBoard(possibilities);
		setDisplayBoard();
	}

	/* Naked Implementation of the algorithm */
	public void naked() {
		nakedPairsAlgorithm(possibilities, nakedPairs);
		updateBoard(possibilities);
		setDisplayBoard();
		JOptionPane.showMessageDialog(null,
				"...the naked pairs algorithm has successfully been implemented\nand the possibilities list has been updated.",
				"Update Successful", JOptionPane.PLAIN_MESSAGE);
	}

	/* Locked Implementation of the algorithm */
	public void locked() {
		lockedCandidateAlgorithm(possibilities);
		updateBoard(possibilities);
		setDisplayBoard();
		JOptionPane.showMessageDialog(null,
				"...the locked candidates algorithm has successfully been implemented\nand the possibilities list has been updated.",
				"Update Successful", JOptionPane.PLAIN_MESSAGE);
	}

	/*
	 * Set ActionListener to the buttons on the grid. This records the event and
	 * sets appropriate functions when buttons are pressed
	 */
	class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int checkNum = 1;
			for (int i = 0; i <= 8; i++) {
				for (int j = 0; j <= 8; j++) {
					if (e.getSource() == buttons[i][j]) {
						if (chckFill == 1) {
							checkNum = checkFillArray(digit, i, j);
						}
						if (checkNum == 1) {
							String temp = buttons[i][j].getText();
							//buttons[i][j].setBackground(new Color(82, 190, 128));
							//buttons[i][j].setForeground(new Color(82, 190, 128));

							if (digit > 0 && digit < 10) {
								buttons[i][j].setText("" + Integer.toString(digit));
								updateUserBoard(digit, i, j);
							}
							if (eraser) {
								buttons[i][j].setText("");
								eraser = false;
							}
						} else {
							JOptionPane.showMessageDialog(null, "Value does not belong.\n.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			if (checkForWin(buttons) == 1) {
				JOptionPane.showMessageDialog(null, "You have successfully solved the puzzle.\n.", "CONGRATULATIONS!",
						JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	/* Set ActionListener to Digit Buttons */
	class DigitHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < 9; i++) {
				if (e.getSource() == controlbuttons[i]) {
					digit = Integer.parseInt(controlbuttons[i].getText());
					controlbuttons[i].setBackground(new Color(82, 190, 128));
				}

				else {
					controlbuttons[i].setBackground(new JButton().getBackground());
				}

			}

		}
	}

	/* Set ActionListener to Erase Button */
	class EraseHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			eraser = true;

		}
	}

	public void addListeners() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sudokuArray[i][j] == 0) {
					buttons[i][j].addActionListener(bh1);
				} else {
					buttons[i][j].addActionListener(bh1);
				}
			}
		
		}
	}
	
	public void setDisplayBoard() {
		int change = 0;
		for (int x = 0; x <= 8; x++) {
			for (int y = 0; y <= 8; y++) {
				if (buttons[y][x].getText().equals("")) {
					if (sudokuArray[y][x] == 0) {
						// do nothing
					} else {
						change = 1;
						buttons[y][x].setText("" + sudokuArray[y][x]);
						buttons[y][x].setBorder(new LineBorder(Color.GREEN, 3));
						System.out.printf("updated %d %d\n", y, x);
					}
				} else if (buttons[y][x].getText().equals(Integer.toString(sudokuArray[y][x]))) {
					// do nothing
				} else {
					change = 1;
					buttons[y][x].setText("" + sudokuArray[y][x]);
					buttons[y][x].setBorder(new LineBorder(Color.GREEN, 3));
					System.out.printf("updated %d %d\n", y, x);
				}
			}
		}
		if (change == 0) {
			JOptionPane.showMessageDialog(null, "There is no cell to fill with this algorithm at this time...",
					"No Change Made.", JOptionPane.PLAIN_MESSAGE);
		}
	}

	
	/**
	 * This method is used to update the possibilities boards after one of the four
	 * algorithms is run
	 * 
	 * @param array
	 */
	public void updateBoard(int[][][] array) { 

		int i, j, k;
		int sum = 0;
		int indexi = 0;
		int indexj = 0;
		int indexk = 0;
		int m;
		int n;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				sum = 0;
				for (k = 0; k < 9; k++) {
					if (array[i][j][k] == 1) {
						sum += 1;
						indexi = i;
						indexj = j;
						indexk = k;
					}
				}
				if (sum == 1) {
					for (m = 0; m < 9; m++) {
						array[m][indexj][indexk] = 0;
					}
					for (n = 0; n < 9; n++) {
						array[indexi][n][indexk] = 0;
					}
					if (indexi == 0 || indexi == 1 || indexi == 2) {
						if (indexj == 0 || indexj == 1 || indexj == 2) {
							for (m = 0; m <= 2; m++) {
								for (n = 0; n <= 2; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
						if (indexj == 3 || indexj == 4 || indexj == 5) {
							for (m = 0; m <= 2; m++) {
								for (n = 3; n <= 5; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
						if (indexj == 6 || indexj == 7 || indexj == 8) {
							for (m = 0; m <= 2; m++) {
								for (n = 6; n <= 8; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}

					} else if (indexi == 3 || indexi == 4 || indexi == 5) {
						if (indexj == 0 || indexj == 1 || indexj == 2) {
							for (m = 3; m <= 5; m++) {
								for (n = 0; n <= 2; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
						if (indexj == 3 || indexj == 4 || indexj == 5) {
							for (m = 3; m <= 5; m++) {
								for (n = 3; n <= 5; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
						if (indexj == 6 || indexj == 7 || indexj == 8) {
							for (m = 3; m <= 5; m++) {
								for (n = 6; n <= 8; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
					} else if (indexi == 6 || indexi == 7 || indexi == 8) {
						if (indexj == 0 || indexj == 1 || indexj == 2) {
							for (m = 6; m <= 8; m++) {
								for (n = 0; n <= 2; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
						if (indexj == 3 || indexj == 4 || indexj == 5) {
							for (m = 6; m <= 8; m++) {
								for (n = 3; n <= 5; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
						if (indexj == 6 || indexj == 7 || indexj == 8) {
							for (m = 6; m <= 8; m++) {
								for (n = 6; n <= 8; n++) {
									array[m][n][indexk] = 0;
								}
							}
						}
					}
					array[indexi][indexj][indexk] = 1;
				}
			}
		}
	}

	/**
	 * Used when a cell only has one possible value because the other values are
	 * already resolved in a cell that share a row, column or box
	 * 
	 * @param sArray
	 * @param array
	 */
	public void singleAlgorithm(int[][] sArray, int[][][] array) { 
		int i, j, k = 0, l;
		int sum = 0;
		int zeroLocation = 0;
		int returnI = -1, returnJ = -1, returnK = -1;
		int returnVal = -1;
		int[] numArr;
		numArr = new int[9];

		for (i = 0; i < 9; i++) {
			sum = 0;
			for (l = 0; l < 9; l++) {
				numArr[l] = 0;
			}
			for (j = 0; j < 9; j++) {
				if (sArray[i][j] != 0) {
					sum++;
					numArr[sArray[i][j] - 1] = 1;
				} else {
					returnI = i;
					returnJ = j;
				}
			}
			if (sum == 8) {

				while (numArr[k] != 0) {
					zeroLocation++;
					k++;
				}
				k += 1;
				sArray[returnI][returnJ] = k;
				return;

			}
		}

		k = 0;
		zeroLocation = 0;
		for (j = 0; j < 9; j++) {
			sum = 0;
			for (l = 0; l < 9; l++) {
				numArr[l] = 0;
			}
			for (i = 0; i < 9; i++) {
				if (sArray[i][j] != 0) {
					sum++;
					numArr[sArray[i][j] - 1] = 1;
				} else {
					returnI = i;
					returnJ = j;
				}
			}
			if (sum == 8) {

				while (numArr[k] != 0) {
					zeroLocation++;
					k++;
				}
				k += 1;
				sArray[returnI][returnJ] = k;
				return;
			}
		}

		for (i = 0; i < 9; i += 3) {
			for (j = 0; j < 9; j += 3) {
				sum = 0;
				for (l = 0; l < 9; l++) {
					numArr[l] = 0;
				}
				if (sArray[i][j] != 0) {
					sum++;
					numArr[sArray[i][j] - 1] = 1;
				} else {
					returnI = i;
					returnJ = j;
				}
				if (sArray[i + 1][j] != 0) {
					sum++;
					numArr[sArray[i + 1][j] - 1] = 1;
				} else {
					returnI = i + 1;
					returnJ = j;
				}
				if (sArray[i + 2][j] != 0) {
					sum++;
					numArr[sArray[i + 2][j] - 1] = 1;
				} else {
					returnI = i + 2;
					returnJ = j;
				}
				if (sArray[i][j + 1] != 0) {
					sum++;
					numArr[sArray[i][j + 1] - 1] = 1;
				} else {
					returnI = i;
					returnJ = j + 1;
				}
				if (sArray[i + 1][j + 1] != 0) {
					sum++;
					numArr[sArray[i + 1][j + 1] - 1] = 1;
				} else {
					returnI = i + 1;
					returnJ = j + 1;
				}
				if (sArray[i + 2][j + 1] != 0) {
					sum++;
					numArr[sArray[i + 2][j + 1] - 1] = 1;
				} else {
					returnI = i + 2;
					returnJ = j + 1;
				}
				if (sArray[i][j + 2] != 0) {
					sum++;
					numArr[sArray[i][j + 2] - 1] = 1;
				} else {
					returnI = i;
					returnJ = j + 2;
				}
				if (sArray[i + 1][j + 2] != 0) {
					sum++;
					numArr[sArray[i + 1][j + 2] - 1] = 1;
				} else {
					returnI = i + 1;
					returnJ = j + 2;
				}
				if (sArray[i + 2][j + 2] != 0) {
					sum++;
					numArr[sArray[i + 2][j + 2] - 1] = 1;
				} else {
					returnI = i + 2;
					returnJ = j + 2;
				}
				if (sum == 8) {

					while (numArr[k] != 0) {
						zeroLocation++;
						k++;
					}
					k += 1;
					sArray[returnI][returnJ] = k;
					return;

				}
			}
		}
	}

	/**
	 * Used when cell must have the value because no other cell in a row column or
	 * box could have that value.
	 * 
	 * @param sArray
	 * @param array
	 * @return
	 */
	public int hiddenSingleAlgorithm(int[][] sArray, int[][][] array) { 
		int sum = 0;
		int i, j, k;
		int returnI = -1, returnJ = -1, returnK = -1;
		int returnVal = -1;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				sum = 0;
				for (k = 0; k < 9; k++) {
					if (array[i][j][k] == 1) {
						sum += 1;
						returnI = i;
						returnJ = j;
						returnK = k;
					}
				}
				if (sum == 1 && sArray[returnI][returnJ] == 0) {
					sArray[returnI][returnJ] = (returnK + 1);
					returnVal = ((((returnI + 1) * 10) + returnJ + 1));
					System.out.printf("%d\n", returnVal);
					return returnVal;
				}
			}
		}
		return 1234;
	}

	/**
	 * Clears the columns in the box containing the locked candidates
	 * 
	 * @param array
	 * @param row
	 * @param type
	 * @param k
	 */
	public void lockedCandidateRowClear(int[][][] array, int row, int type, int k) { 
		int i;
		int row1 = -1;
		int row2 = -1;
		if (row % 3 == 0) {
			row1 = row + 1;
			row2 = row + 2;
		}
		if (row % 3 == 1) {
			row1 = row + 1;
			row2 = row - 1;
		}
		if (row % 3 == 2) {
			row1 = row - 1;
			row2 = row - 2;
		}
		if (type == 1) {
			for (i = 0; i < 3; i++) {
				array[row1][i][k] = 0;
				array[row2][i][k] = 0;
			}
		}
		if (type == 2) {
			for (i = 3; i < 6; i++) {
				array[row1][i][k] = 0;
				array[row2][i][k] = 0;
			}
		}
		if (type == 3) {
			for (i = 6; i < 9; i++) {
				array[row1][i][k] = 0;
				array[row2][i][k] = 0;
			}
		}
	}

	/**
	 * Clears the columns in the box containing the locked candidates
	 * 
	 * @param array
	 * @param col
	 * @param type
	 * @param k
	 */
	public void lockedCandidateColClear(int[][][] array, int col, int type, int k) { 
		int i;
		int col1 = -1;
		int col2 = -1;
		if (col % 3 == 0) {
			col1 = col + 1;
			col2 = col + 2;
		}
		if (col % 3 == 1) {
			col1 = col + 1;
			col2 = col - 1;
		}
		if (col % 3 == 2) {
			col1 = col - 1;
			col2 = col - 2;
		}
		if (type == 1) {
			for (i = 0; i < 3; i++) {
				array[i][col1][k] = 0;
				array[i][col2][k] = 0;
			}
		}
		if (type == 2) {
			for (i = 3; i < 6; i++) {
				array[i][col1][k] = 0;
				array[i][col2][k] = 0;
			}
		}
		if (type == 3) {
			for (i = 6; i < 9; i++) {
				array[i][col1][k] = 0;
				array[i][col2][k] = 0;
			}
		}

	}

	/**
	 * Clears candidates from one group row, col, or quad when it is shown that it
	 * must be in another row col or quad
	 * 
	 * @param array
	 */
	public void lockedCandidateAlgorithm(int[][][] array) { 
		int i, j, k, l, m, n;
		int sum = 0;
		for (k = 0; k < 9; k++) {
			for (i = 0; i < 9; i++) {
				sum = 0;
				for (j = 3; j < 9; j++) {
					if (array[i][j][k] == 1) {
						sum += 1;
					}
				}
				if (sum == 0) {
					lockedCandidateRowClear(array, i, 1, k);
				}
				sum = 0;
				for (j = 0; j < 3; j++) {
					if (array[i][j][k] == 1) {
						sum += 1;
					}
				}
				for (j = 6; j < 9; j++) {
					if (array[i][j][k] == 1) {
						sum += 1;
					}
				}
				if (sum == 0) {
					lockedCandidateRowClear(array, i, 2, k);
				}
				sum = 0;
				for (j = 0; j < 6; j++) {
					if (array[i][j][k] == 1) {
						sum += 1;
					}
				}
				if (sum == 0) {
					lockedCandidateRowClear(array, i, 3, k);
				}
				sum = 0;

			}
		}
		sum = 0;
		for (l = 0; l < 9; l++) {
			for (m = 0; m < 9; m++) {
				sum = 0;
				for (n = 3; n < 9; n++) {
					if (array[n][m][l] == 1) {
						sum += 1;
					}
				}
				if (sum == 0) {
					lockedCandidateColClear(array, m, 1, l);
				}
				sum = 0;
				for (n = 0; n < 3; n++) {
					if (array[n][m][l] == 1) {
						sum += 1;
					}
				}
				for (n = 6; n < 9; n++) {
					if (array[n][m][l] == 1) {
						sum += 1;
					}
				}
				if (sum == 0) {
					lockedCandidateColClear(array, m, 2, l);
				}
				sum = 0;
				for (n = 0; n < 6; n++) {
					if (array[n][m][l] == 1) {
						sum += 1;
					}
				}
				if (sum == 0) {
					lockedCandidateColClear(array, m, 3, l);
				}
				sum = 0;

			}
		}
	}

	
	/**
	 * Checks to see of two cells have the exact same candidate list
	 * 
	 * @param array
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param nakedPairs
	 * @return
	 */
	public int nakedPairHelperSameCandidate(int[][][] array, int a, int b, int c, int d, int[] nakedPairs) { 
		int i;
		for (i = 0; i < 9; i++) {
			if (array[a][b][i] != array[c][d][i]) {
				return 0;
			}
		}
		return 1;
	}

	
	/**
	 * Checks to see if a cell is a potential naked pair
	 * 
	 * @param array
	 * @param a
	 * @param b
	 * @param nakedPairs
	 * @return
	 */
	public int nakedPairHelperTwoCandidates(int[][][] array, int a, int b, int[] nakedPairs) { 
		int sum = 0;
		int i;
		for (i = 0; i < 9; i++) {
			if (array[a][b][i] == 1) {
				sum++;
			}
		}
		if (sum == 2) {
			return 1;
		}
		return 0;
	}
	
	
	/**
	 * Returns the combined value of the values from the candidate lists for the
	 * naked pairs algorithm
	 * 
	 * @param array
	 * @param a
	 * @param b
	 * @return
	 */
	public int nakedPairsHelperGetPossibilities(int[][][] array, int a, int b) { 
		int ret = -1;
		int i;
		for (i = 0; i < 9; i++) {
			if (array[a][b][i] == 1) {
				ret = i * 10;
				break;
			}
		}
		for (i = 8; i > -1; i--) {
			if (array[a][b][i] == 1) {
				ret += i;
				break;
			}
		}
		return ret;
	}

	/**
	 *  When naked pairs are found in a row, clears all other candidates from other
	 * cells in the row
	 * @param array
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param num1
	 * @param num2
	 */
	public void nakedPairsClearRow(int[][][] array, int a, int b, int c, int d, int num1, int num2) { 
		int i;
		for (i = 0; i < 9; i++) {
			if (i != b && i != d) {
				array[a][i][num1] = 0;
				array[a][i][num2] = 0;
			}
		}
	}

	/**
	 * When naked pairs are found in a column, clears all other candidates from
	 * other cells in the column
	 * 
	 * @param array
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param num1
	 * @param num2
	 */
	public void nakedPairsClearCol(int[][][] array, int a, int b, int c, int d, int num1, int num2) { 
		int i;
		for (i = 0; i < 9; i++) {
			if (i != a && i != c) {
				array[i][b][num1] = 0;
				array[i][b][num2] = 0;
			}
		}
	}


	/**
	 * When naked pairs are found in a quadrant, clears all other candidates from
	 * other cells in the quadrant - Prototype function for Naked Pairs
	 * 
	 * @param array
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param q1
	 * @param q2
	 * @param num1
	 * @param num2
	 */
	public void nakedPairsClearQuad(int[][][] array, int a, int b, int c, int d, int q1, int q2, int num1, int num2) { 
		int i;
		int j;
		for (i = q1; i < q1 + 3; i++) {
			for (j = q2; j < q2 + 3; j++) {
				if ((i != a || j != b) && (i != c || j != d)) {
					array[i][j][num1] = 0;
					array[i][j][num2] = 0;
				}
			}
		}
	}
	
	/**
	 * When two cells contain two candidates and they are the same, these candidates
	 * must be removed from rows, cols, or quads if they are shared
	 * 
	 * @param array
	 * @param nakedArray
	 */
	public void nakedPairsAlgorithm(int[][][] array, int[] nakedArray) { 
		int i, j, l = 0;
		int a, b, c, d;
		int poss;
		int num1;
		int num2;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				if (nakedPairHelperTwoCandidates(array, i, j, nakedArray) == 1) {
					nakedArray[l] = (10 * i) + j;
					l++;
				}
			}
		}
		l = 0;
		while (nakedArray[l] != -1) {
			l++;
		}
		for (i = 0; i < l; i++) {
			for (j = i + 1; j < l; j++) {
				a = nakedArray[i] / 10;
				b = nakedArray[i] % 10;
				c = nakedArray[j] / 10;
				d = nakedArray[j] % 10;
				if (nakedPairHelperSameCandidate(array, a, b, c, d, nakedArray) == 1) {
					poss = nakedPairsHelperGetPossibilities(array, a, b);
					num1 = poss / 10;
					num2 = poss % 10;
					if (a == c) {
						nakedPairsClearRow(array, a, b, c, d, num1, num2);
					}
					if (b == c) {
						nakedPairsClearCol(array, a, b, c, d, num1, num2);
					}
					if (a % 3 == 0 && b % 3 == 0 && (c == a + 1 || c == a + 2 || c == a)
							&& (d == b + 1 || d == b + 2 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a, b, num1, num2);
					}
					if (a % 3 == 0 && b % 3 == 1 && (c == a + 1 || c == a + 2 || c == a)
							&& (d == b + 1 || d == b - 1 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a, b - 1, num1, num2);
					}
					if (a % 3 == 0 && b % 3 == 2 && (c == a + 1 || c == a + 2 || c == a)
							&& (d == b - 1 || d == b - 2 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a, b - 2, num1, num2);
					}
					if (a % 3 == 1 && b % 3 == 0 && (c == a + 1 || c == a - 1 || c == a)
							&& (d == b + 1 || d == b + 2 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a - 1, b, num1, num2);
					}
					if (a % 3 == 1 && b % 3 == 1 && (c == a + 1 || c == a - 1 || c == a)
							&& (d == b + 1 || d == b - 1 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a - 1, b - 1, num1, num2);
					}
					if (a % 3 == 1 && b % 3 == 2 && (c == a + 1 || c == a - 1 || c == a)
							&& (d == b - 1 || d == b - 2 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a - 1, b - 2, num1, num2);
					}
					if (a % 3 == 2 && b % 3 == 0 && (c == a - 1 || c == a - 2 || c == a)
							&& (d == b + 1 || d == b + 2 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a - 2, b, num1, num2);
					}
					if (a % 3 == 2 && b % 3 == 1 && (c == a - 1 || c == a - 2 || c == a)
							&& (d == b + 1 || d == b - 1 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a - 2, b - 1, num1, num2);
					}
					if (a % 3 == 2 && b % 3 == 2 && (c == a - 1 || c == a - 2 || c == a)
							&& (d == b - 1 || d == b - 2 || d == b)) {
						nakedPairsClearQuad(array, a, b, c, d, a - 2, b - 2, num1, num2);
					}
				}
			}
		}
	}
	
	/**
	 * Prototype function prints all possibilities
	 * 
	 * @param array
	 *            3D array to be printed
	 */
	public void printPossibilities(int[][][] array) {
		int i, j, k;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				for (k = 0; k < 9; k++) {
					System.out.print(array[i][j][k]);
				}
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Prototype function prints the sudoku
	 * 
	 * @param array
	 *            2D Array to be printed
	 */
	public void printSudoku(int[][] array) {
		int i, j, k;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Driver method to read in file, print the possibilities and sudoku
	 */
	public void setPuzzle() {
		int i, j, k, x, y, val;

		sudokuArray = new int[9][9];
		solvedSudokuArray = new int[9][9];
		possibilities = new int[9][9][9];
		solvedPossibilities = new int[9][9][9];

		nakedPairs = new int[81];
		for (i = 0; i < 81; i++) {
			nakedPairs[i] = -1;
		}
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				sudokuArray[i][j] = 0;
				solvedSudokuArray[i][j] = 0;
				for (k = 0; k < 9; k++) {
					possibilities[i][j][k] = 1;
					solvedPossibilities[i][j][k] = 1;
				}
			}
		}
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader("test3.txt");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				x = Character.getNumericValue(sCurrentLine.charAt(0));
				y = Character.getNumericValue(sCurrentLine.charAt(2));
				val = Character.getNumericValue(sCurrentLine.charAt(4));
				sudokuArray[x - 1][y - 1] = val;
				solvedSudokuArray[x - 1][y - 1] = val;
				for (k = 0; k < 9; k++) {
					possibilities[x - 1][y - 1][k] = 0;
					solvedPossibilities[x - 1][y - 1][k] = 0;
				}
				possibilities[x - 1][y - 1][val - 1] = 1;
				solvedPossibilities[x - 1][y - 1][val - 1] = 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		System.out.print("\nUser Sudoku Array:\n");
		printSudoku(sudokuArray);
		System.out.print("\nUser Sudoku Array Possibilities:\n");
		printPossibilities(possibilities);
		System.out.print("\nSolved Sudoku Array:\n");
		printSudoku(solvedSudokuArray);
		System.out.print("\nSolved Sudoku Array Possibilities:\n");
		printPossibilities(solvedPossibilities);

		for (i = 0; i < 81; i++) {
			updateBoard(solvedPossibilities);
			lockedCandidateAlgorithm(solvedPossibilities);
			updateBoard(solvedPossibilities);
			nakedPairsAlgorithm(solvedPossibilities, nakedPairs);
			updateBoard(solvedPossibilities);
			singleAlgorithm(solvedSudokuArray, solvedPossibilities);
			updateBoard(solvedPossibilities);
			hiddenSingleAlgorithm(solvedSudokuArray, solvedPossibilities);
		}

		System.out.print("\nUser Sudoku Array: \n");
		printSudoku(sudokuArray);
		System.out.print("\nUser Sudoku Array Possibilities:\n");
		printPossibilities(possibilities);
		System.out.print("\nSolved Sudoku Array:\n");
		printSudoku(solvedSudokuArray);
		System.out.print("\nSolved Sudoku Array Possibilities:\n");
		printPossibilities(solvedPossibilities);
	}
}