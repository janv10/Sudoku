package sudoku_prog3_342;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class MenuItem extends JMenuItem implements ActionListener {

	/*
	 * Graphical user interface, where the MenuItem was made
	 */
	private Display gui;

	// Variable declarations
	public int rows = 4;
	public int cols = 4;

	// Create a menuBar
	JMenuBar menuBar;
	JMenu menu, subMenu;
	JCheckBoxMenuItem checkBox;
	JMenuItem load, store, exit;
	JMenuItem howToPlay, howToUse, aboutAuthors; // For Help Menu
	JMenuItem single, hidden, locked, naked, fillIn; // For Hints Menu
	JTextArea textArea;

	/**
	 * Constructor of the menu item.
	 *
	 * @param gui
	 *            GUI where the item was made
	 * @param string
	 *            string to be displayed as name
	 */
	public MenuItem(Display gui, String string) {
		super(string);					// call constructor of parent
		this.gui = gui; 					// set GUI
		this.addActionListener(this); 	// add action listener
	}

	/**
	 * Does the action of the item.
	 *
	 * @param ae
	 *            action event
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// get the source of the event
		MenuItem menu = (MenuItem) ae.getSource();
		// do the action of each owns item

		/* Save into a file */
		if (menu == gui.saveGame) {
			String str;
			int a;
			int b;
			JFileChooser fileChooser = new JFileChooser();
			int ret = fileChooser.showSaveDialog(store);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file == null) {
					return;
				}
				if (!file.getName().toLowerCase().endsWith(".txt")) {
					file = new File(file.getParentFile(), file.getName() + ".txt");
				}
				try {
					PrintWriter writer = new PrintWriter(file, "UTF-8");
					for (int i = 0; i < 9; i++) {
						for (int j = 0;j< 9; j++) {
							if (gui.sudokuArray[i][j] != 0) {
								a = i+1;
								b = j+1;
								str = a+ " " + b + " "+ gui.sudokuArray[i][j];
								writer.println(str);
							}
							
							
						}
					}
					writer.close();
					
				} catch (Exception e1) {
					// e1.printStackTrace();
				}
			}
		}

		/* Load Puzzle from file */
		else if (menu == gui.loadGame) {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int retVal = jfc.showOpenDialog(null);
			int x;
			int y;
			int i,j,k;
			int val;
			if (retVal == JFileChooser.APPROVE_OPTION) {
	            File file = jfc.getSelectedFile();
	            BufferedReader br = null;
	    		FileReader fr = null;

	    		for (i = 0; i < 81; i++) {
	    			gui.nakedPairs[i] = -1;
	    		}
	    		for (i = 0; i < 9; i++) {
	    			for (j = 0; j < 9; j++) {
	    				gui.sudokuArray[i][j] = 0;
	    				gui.solvedSudokuArray[i][j] = 0;
	    				for (k = 0; k < 9; k++) {
	    					gui.possibilities[i][j][k] = 1;
	    					gui.solvedPossibilities[i][j][k] = 1;
	    				}
	    			}
	    		}
	    		
	    		try {
	    			fr = new FileReader(file);
	    			br = new BufferedReader(fr);

	    			String sCurrentLine;

	    			while ((sCurrentLine = br.readLine()) != null) {
	    				x = Character.getNumericValue(sCurrentLine.charAt(0));
	    				y = Character.getNumericValue(sCurrentLine.charAt(2));
	    				val = Character.getNumericValue(sCurrentLine.charAt(4));
	    				gui.sudokuArray[x - 1][y - 1] = val;
	    				gui.solvedSudokuArray[x - 1][y - 1] = val;
	    				for (k = 0; k < 9; k++) {
	    					gui.possibilities[x - 1][y - 1][k] = 0;
	    					gui.solvedPossibilities[x - 1][y - 1][k] = 0;
	    				}
	    				gui.possibilities[x - 1][y - 1][val - 1] = 1;
	    				gui.solvedPossibilities[x - 1][y - 1][val - 1] = 1;
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
	        } 
			for (i = 0; i < 9; i++) {
    			for (j = 0; j < 9; j++) {
    				if (gui.sudokuArray[i][j] == 0) {
    					gui.buttons[i][j].setText("");
    					gui.addListeners();
    				} else {
    					gui.buttons[i][j].setText("" + gui.sudokuArray[i][j]);
    				}
    			}
			
			}
			
			
		}

		/* Exit the game */
		else if (menu == gui.exitGame) {
			System.exit(0);
		}

		/* Display information about the game */
		else if (menu == gui.gameAbout) {
			JOptionPane.showMessageDialog(null, "Sudoku", "About game", JOptionPane.PLAIN_MESSAGE);
		}

		/* Display information about the authors */
		else if (menu == gui.authorAbout) {
			JOptionPane.showMessageDialog(null, "Author:", "About author", JOptionPane.PLAIN_MESSAGE);
		}

		/* Implementation of the single algorithm */
		else if (menu == gui.single) {
			gui.single();
		}

		/* Implementation of the hidden algorithm */
		else if (menu == gui.hidden) {
			gui.hidden();
		}

		/* Implementation of the locked candidate algorithm */
		else if (menu == gui.locked) {
			gui.locked();
		}

		/* Implementation of the naked pairs algorithm */
		else if (menu == gui.naked) {
			gui.naked();
		}

	}
}
