package vkurman.jminesweeper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;

/**
 * <code>JMinesweeperUI</code> updated graphical user interface for Minesweeper game. Most
 * game control functionality has been moved from this class.
 * 
 * <p>
 * Date created: 2014.01.18
 * <p>
 * Date updated: 2016.11.22
 * 
 * @author Vassili Kurman
 * @version 2.0
 */
public class JMinesweeperUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1096394176253637853L;
	private static final String TITLE = "JMinesweeper";
	private static final int FRAME_POSITION_X = 100;
	private static final int FRAME_POSITION_Y = 100;
	public static final int CELL_WIDTH = 27;
	public static final int CELL_HEIGHT = 27;

	private static final Color BORDER_COLOR = Color.BLACK;
	private static final Color BUTTON_COLOR_NEUTRAL = Color.YELLOW;
	private static final Color BUTTON_COLOR_HAPPY = Color.GREEN;
	private static final Color BUTTON_COLOR_SAD = Color.RED;
	private static final Color COVER_PANEL_COLOR = new Color(192, 192, 192);
	private static final Color CONTENT_PANEL_COLOR = new Color(224, 224, 224);

	private static final Font TIMER_FONT = new Font("Times", Font.BOLD, 28);

	private static final Color COLOR_NUMBER_1 = Color.RED;
	private static final Color COLOR_NUMBER_2 = Color.GREEN;
	private static final Color COLOR_NUMBER_3 = Color.BLUE;
	private static final Color COLOR_NUMBER_4 = Color.ORANGE;
	private static final Color COLOR_NUMBER_5 = Color.MAGENTA;
	private static final Color COLOR_NUMBER_6 = Color.CYAN;
	private static final Color COLOR_NUMBER_7 = Color.YELLOW;
	private static final Color COLOR_NUMBER_8 = Color.BLACK;

	private ControlRequestsListener controlRequestsListener;

	private final NumberFormatter nformat;
	private Dimension fieldSize;
	private JLabel lblMines, lblDifficulty, lblTimer;
	private int rows, columns, mines, leftMines;
	private Cell[][] cells;

	private Difficulty difficulty;
	private JPanel contentPane;
	private RoundButton btnNewGame;

	/**
	 * JMinesweeper Constructor.
	 */
	public JMinesweeperUI(ControlRequestsListener controlRequestsListener,
			Difficulty difficulty) {
		this.difficulty = difficulty;
		this.controlRequestsListener = controlRequestsListener;

		nformat = new NumberFormatter(new DecimalFormat("00"));

		mines = difficulty.getMines();
		rows = difficulty.getRows();
		columns = difficulty.getColumns();
		leftMines = mines;

		// Initial Difficulty level is medium
		lblMines = new JLabel(Integer.toString(leftMines));
		lblDifficulty = new JLabel(difficulty.getName());

		lblTimer = new JLabel("00:00");
		lblTimer.setBackground(Color.BLACK);
		lblTimer.setForeground(Color.RED);
		lblTimer.setFont(TIMER_FONT);
		lblTimer.setOpaque(true);

		showUI();

		setVisible(true);
	}

	/**
	 * Builds and displays UI.
	 */
	private void showUI() {
		setResizable(false);
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(new Point(FRAME_POSITION_X, FRAME_POSITION_Y));

		// Initialising JButton
		btnNewGame = new RoundButton();
		btnNewGame.setToolTipText("New Game");
		btnNewGame.setBackground(BUTTON_COLOR_NEUTRAL);
		btnNewGame.setActionCommand("New Game");
		btnNewGame.addActionListener(this);

		setMenuBar();
		newGame();
	}

	/**
	 * Setting application JMenuBar.
	 */
	private void setMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menuBar.add(getFileMenu());
		menuBar.add(getHelpMenu());
	}

	/**
	 * Building and returning File JMenu.
	 * 
	 * @return JMenu
	 */
	private JMenu getFileMenu() {
		JMenu mnFile = new JMenu("File");

		JMenuItem mntmNewGame = new JMenuItem("New Game");
		mntmNewGame.setActionCommand("New Game");
		mntmNewGame.addActionListener(this);
		mnFile.add(mntmNewGame);

		mnFile.addSeparator();

		JMenu mntmDifficulty = new JMenu("Difficulty");
		JMenuItem mntmBeginner = new JMenuItem(Difficulty.BEGINNER.getName()
				+ " (" + Difficulty.BEGINNER.getRows() + "x"
				+ Difficulty.BEGINNER.getColumns() + " : "
				+ Difficulty.BEGINNER.getMines() + " mines)");
		mntmBeginner.setActionCommand(Difficulty.BEGINNER.getName());
		mntmBeginner.addActionListener(this);
		JMenuItem mntmIntermediate = new JMenuItem(
				Difficulty.INTERMEDIATE.getName() + " ("
						+ Difficulty.INTERMEDIATE.getRows() + "x"
						+ Difficulty.INTERMEDIATE.getColumns() + " : "
						+ Difficulty.INTERMEDIATE.getMines() + " mines)");
		mntmIntermediate.setActionCommand(Difficulty.INTERMEDIATE.getName());
		mntmIntermediate.addActionListener(this);
		JMenuItem mntmAdvanced = new JMenuItem(Difficulty.ADVANCED.getName()
				+ " (" + Difficulty.ADVANCED.getRows() + "x"
				+ Difficulty.ADVANCED.getColumns() + " : "
				+ Difficulty.ADVANCED.getMines() + " mines)");
		mntmAdvanced.setActionCommand(Difficulty.ADVANCED.getName());
		mntmAdvanced.addActionListener(this);

		JMenuItem mntmRecords = new JMenuItem("Records");
		mntmRecords.setActionCommand("Records");
		mntmRecords.addActionListener(this);
		mnFile.add(mntmRecords);

		mntmDifficulty.add(mntmBeginner);
		mntmDifficulty.add(mntmIntermediate);
		mntmDifficulty.add(mntmAdvanced);
		mnFile.add(mntmDifficulty);

		mnFile.addSeparator();

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setActionCommand("Exit");
		mntmExit.addActionListener(this);
		mnFile.add(mntmExit);

		return mnFile;
	}

	/**
	 * Building and returning Help JMenu.
	 * 
	 * @return JMenu
	 */
	private JMenu getHelpMenu() {
		JMenu mnHelp = new JMenu("Help");

		JMenuItem mntmRules = new JMenuItem("Rules");
		mntmRules.setActionCommand("Rules");
		mntmRules.addActionListener(this);
		mnHelp.add(mntmRules);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setActionCommand("About");
		mntmAbout.addActionListener(this);
		mnHelp.add(mntmAbout);

		return mnHelp;
	}

	private void resetDifficulty() {
		if (controlRequestsListener == null)
			return;

		controlRequestsListener.setDifficulty(difficulty);

		mines = difficulty.getMines();
		rows = difficulty.getRows();
		columns = difficulty.getColumns();
		leftMines = mines;

		lblMines.setText(Integer.toString(leftMines));
		lblDifficulty.setText(difficulty.getName());

		// Starting new game
		newGame();
	}

	/**
	 * Setting Content Pane.
	 */
	private void setContent() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());

		contentPane.add(getTopPanel(), BorderLayout.PAGE_START);
		contentPane.add(getNewMineField(), BorderLayout.CENTER);
		contentPane.add(getInformationPanel(), BorderLayout.PAGE_END);

		setContentPane(contentPane);
	}

	/**
	 * Returning JPanel containing button to start new game.
	 * 
	 * @return JPanel
	 */
	private JPanel getTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3, 0, 0));

		JPanel lblPanel = new JPanel();
		lblPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 2));
		lblPanel.add(new JLabel(""));

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 2));
		btnPanel.add(btnNewGame);

		JPanel timerPanel = new JPanel();
		timerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 2));
		timerPanel.add(lblTimer);

		panel.add(lblPanel);
		panel.add(btnPanel);
		panel.add(timerPanel);

		return panel;
	}

	/**
	 * Creating new MineField.
	 * 
	 * @return JPanel
	 */
	private JPanel getNewMineField() {
		fieldSize = new Dimension(CELL_WIDTH * columns, CELL_HEIGHT * rows);

		JPanel field = new JPanel();
		field.setLayout(new GridLayout(rows, columns, 0, 0));
		field.setBounds(0, 0, fieldSize.width, fieldSize.height);
		field.setBorder(new LineBorder(BORDER_COLOR));

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Cell c = new Cell(row, column);
				// Creating Cell and adding it to array
				cells[row][column] = c;
				// Adding Cell to the panel
				field.add(c);
			}
		}
		return field;
	}

	/**
	 * Returning JPanel containing information about game difficulty and how
	 * many mines left to open.
	 * 
	 * @return JPanel
	 */
	private JPanel getInformationPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 5, 5));

		JPanel panelDifficulty = new JPanel();
		panelDifficulty.setLayout(new FlowLayout());
		panelDifficulty.add(new JLabel("Difficulty: "));
		panelDifficulty.add(lblDifficulty);

		JPanel panelMines = new JPanel();
		panelDifficulty.setLayout(new FlowLayout());
		panelMines.add(new JLabel("Mines"));
		panelMines.add(lblMines);

		panel.add(panelDifficulty);
		panel.add(panelMines);

		return panel;
	}

	/**
	 * This method starts new game rebuilding Cells, placing mines, etc.
	 */
	private void newGame() {
		// Initialising array length
		cells = new Cell[rows][columns];
		// Resetting leftMines variable
		leftMines = mines;
		// Changing text on Mines label
		lblMines.setText(Integer.toString(leftMines));
		btnNewGame.setBackground(BUTTON_COLOR_NEUTRAL);
		// Setting content panel
		setContent();
		// Placing mines in random cells
		placeMines();
		// Marking cells
		markMineNeighbours();

		if (controlRequestsListener != null) {
			// Setting game in progress to TRUE
			controlRequestsListener.startGame();
			// Resetting timer
			controlRequestsListener.resetTimer();
		}
		// Call to JFrame pack() method to resize

		pack();
	}

	/**
	 * Checking if the game has been won.
	 */
	private boolean checkWin() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				if (!cells[r][c].isOpened()) {
					if (!cells[r][c].isFlagged())
						return false;
					if (cells[r][c].isFlagged()) {
						if (!cells[r][c].hasMine())
							return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method ends the game and displays appropriate dialog box.
	 */
	private void win() {
		if (controlRequestsListener == null)
			return;

		controlRequestsListener.stopGame();
		controlRequestsListener.stopTimer();

		// Displaying Message Dialog
		btnNewGame.setBackground(BUTTON_COLOR_HAPPY);

		controlRequestsListener.win();
	}

	/**
	 * This method ends the game and displays appropriate dialog box.
	 */
	private void gameOver() {
		if (controlRequestsListener == null)
			return;

		controlRequestsListener.stopGame();
		controlRequestsListener.stopTimer();

		// Displaying Message Dialog
		btnNewGame.setBackground(BUTTON_COLOR_SAD);

		JOptionPane.showMessageDialog(JMinesweeperUI.this, "Game over!"
				+ "\nYou lost the game!.", "Game over!!!!",
				JOptionPane.ERROR_MESSAGE);

	}

	/**
	 * This method places mines in the Random Cell.
	 */
	private void placeMines() {
		Random random = new Random();

		for (int mine = mines; mine > 0; mine--) {
			boolean placed = false;
			while (!placed) {
				int r = random.nextInt(rows);
				int c = random.nextInt(columns);

				if (cells[r][c].isEmpty()) {
					cells[r][c].placeMine();
					placed = true;
				}
			}
		}
	}

	/**
	 * This method returns TRUE if specified position is within the bounds of
	 * array.
	 * 
	 * @param row
	 * @param column
	 * @return boolean
	 */
	public boolean isCellPositionValid(int row, int column) {
		return (row >= 0 && row < rows && column >= 0 && column < columns) ? true
				: false;
	}

	/**
	 * This method places Integers representing how many mines Cell has next to
	 * it. NOTE! This method should be used after all mines have been placed in
	 * the Cells.
	 */
	private void markMineNeighbours() {
		// Iterating through every row in the mine field
		for (int fieldRow = 0; fieldRow < rows; fieldRow++) {
			// Iterating through every column in the mine field
			for (int fieldColumn = 0; fieldColumn < columns; fieldColumn++) {
				// Checking if current Cell has mine
				if (!cells[fieldRow][fieldColumn].hasMine()) {
					int counter = 0;

					for (int checkedRow = fieldRow - 1; checkedRow <= fieldRow + 1; checkedRow++) {
						for (int checkedColumn = fieldColumn - 1; checkedColumn <= fieldColumn + 1; checkedColumn++) {
							if (this.isCellPositionValid(checkedRow,
									checkedColumn)) {
								if (cells[checkedRow][checkedColumn].hasMine())
									counter++;
							}
						}
					}
					// Marking the Cell
					if (counter > 0) {
						// Filling the Cell
						cells[fieldRow][fieldColumn].fill(new JLabel(Integer
								.toString(counter)));
					}
				}
			}
		}
	}

	/**
	 * This method returns Color for specified integer.
	 * 
	 * @param number
	 * @return Color
	 */
	private Color getColor(int number) {
		switch (number) {
		case 1:
			return COLOR_NUMBER_1;
		case 2:
			return COLOR_NUMBER_2;
		case 3:
			return COLOR_NUMBER_3;
		case 4:
			return COLOR_NUMBER_4;
		case 5:
			return COLOR_NUMBER_5;
		case 6:
			return COLOR_NUMBER_6;
		case 7:
			return COLOR_NUMBER_7;
		case 8:
			return COLOR_NUMBER_8;
		default:
			return Color.GRAY;
		}
	}

	/**
	 * This method opens all Cells around current empty Cell. This method is
	 * recursive, so that if opened Cell is empty than this method calls itself
	 * to open all Cells around current empty Cell again.
	 * 
	 * @param row
	 * @param column
	 */
	private void findEmptyCells(int row, int column) {
		for (int r = row - 1; r <= row + 1; r++) {
			for (int c = column - 1; c <= column + 1; c++) {
				if (this.isCellPositionValid(r, c)) {
					if (!cells[r][c].hasMine() && !cells[r][c].isOpened()
							&& !cells[r][c].isFlagged()) {
						cells[r][c].open();
						if (cells[r][c].isEmpty()) {
							findEmptyCells(r, c);
						}
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("New Game")) {
			if (controlRequestsListener == null)
				return;

			controlRequestsListener.stopGame();
			controlRequestsListener.stopTimer();

			newGame();
		} else if (command.equals(Difficulty.BEGINNER.getName())) {
			difficulty = Difficulty.BEGINNER;
			resetDifficulty();
		} else if (command.equals(Difficulty.INTERMEDIATE.getName())) {
			difficulty = Difficulty.INTERMEDIATE;
			resetDifficulty();
		} else if (command.equals(Difficulty.ADVANCED.getName())) {
			difficulty = Difficulty.ADVANCED;
			resetDifficulty();
		} else if (command.equals("Records")) {
			if (controlRequestsListener == null)
				return;

			RecordsView view = new RecordsView(JMinesweeperUI.this,
					controlRequestsListener);
			view.setVisible(true);
		} else if (command.equals("Exit")) {
			System.exit(0);
		} else if (command.equals("Rules")) {
			displayRulesDialog();
		} else if (command.equals("About")) {
			displayAboutDialog();
		}
	}

	/**
	 * Displays Message Dialog box containing the name of the author of this
	 * MineSweeper game.
	 */
	private void displayAboutDialog() {
		StringBuffer sb = new StringBuffer("");
		sb.append(TITLE);
		sb.append("\n Designed and developed by Vassili Kurman");
		sb.append("\n 2014-2016");

		final String text = sb.toString();
		// Displaying Message Dialog
		JOptionPane.showMessageDialog(JMinesweeperUI.this, text, "About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays Message Dialog box containing MineSweeper game rules.
	 */
	private void displayRulesDialog() {
		StringBuffer sb = new StringBuffer("");
		sb.append("How to Play Minesweeper\n");
		sb.append("\n *  The object of the game is to uncover all squares that don't contain mines.");
		sb.append("\n *  Left click on a square to uncover it.");
		sb.append("\n *  If you uncover a mine, you lose, and the square you clicked will reveal a mine.");
		sb.append("\n *  If an uncovered square displays a number, that number represents the number of");
		sb.append("\n    neighboring mines adjacent to that square (1 - 8). If there are no mines adjacent,");
		sb.append("\n    the square will be blank. Use these numbers to determine where you think a mine");
		sb.append("\n    might be located.");
		sb.append("\n *  You can mark a square as a mine by right clicking on it. A number at the bottom of");
		sb.append("\n    the frame keeps track of how many mines you have left to find.");

		final String text = sb.toString();
		// Displaying Message Dialog
		JOptionPane.showMessageDialog(JMinesweeperUI.this, text, "Rules",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Cell class for Mine Field. Cell class uses CardLayout to display
	 * appropriate JPanel. When the game started the CELL_COVER JPanel is
	 * displayed. CELL_COVER can be flagged by mouse "right clicking", to
	 * indicate that there is a mine on this Cell. Flag can be removed by mouse
	 * "right clicking" again on the same Cell. When the Cell is opened than
	 * CELL_CONTENT JPanel is displayed. CELL_CONTENT can be empty, indicating
	 * that 0 mines are located around it; can have a number, indicating how
	 * many mines are located around the Cell; and can have a mine in it. Cell
	 * implements MouseListener interface for displaying appropriate JPanel and
	 * requesting to open surrounding Cells in case if this Cell is empty.
	 * 
	 * <p>
	 * Date created: 2014.01.18
	 * 
	 * @author Vassili Kurman
	 * @version 0.1
	 */
	private class Cell extends JPanel implements MouseListener {
		public static final long serialVersionUID = -2364827327566066368L;
		public static final String CELL_CONTENT = "Content";
		public static final String CELL_COVER = "Cover";

		private CardLayout cardLayout;
		private ContentPanel content;
		private CoverPanel cover;
		private JLabel lblContent;
		private boolean open;
		private int row, column;

		/**
		 * Constructor. Row and Column parameters are required to make a search
		 * for empty Cells around opened empty Cell.
		 */
		public Cell(int row, int column) {
			setBounds(0, 0, CELL_WIDTH, CELL_HEIGHT);
			cardLayout = new CardLayout();
			setLayout(cardLayout);
			setBorder(new LineBorder(BORDER_COLOR));

			open = false;

			this.row = row;
			this.column = column;

			lblContent = new JLabel();

			content = new ContentPanel();
			content.setBackground(CONTENT_PANEL_COLOR);
			content.add(lblContent);

			cover = new CoverPanel();
			cover.setBackground(COVER_PANEL_COLOR);

			this.add(content, CELL_CONTENT);
			this.add(cover, CELL_COVER);

			cardLayout.show(this, CELL_COVER);

			// Adding MouseListener
			addMouseListener(this);
		}

		/**
		 * Check if Cell has been flagged.
		 * 
		 * @return boolean
		 */
		public boolean isFlagged() {
			return cover.flagged;
		}

		/**
		 * Check if Cell has been opened.
		 * 
		 * @return boolean
		 */
		public boolean isOpened() {
			return open;
		}

		/**
		 * Check if Cell is empty.
		 * 
		 * @return boolean
		 */
		public boolean isEmpty() {
			return content.empty;
		}

		/**
		 * If Cell is not opened, than it is flagged.
		 */
		public void flag() {
			if (!open) {
				if (cover.flagged) {
					cover.flagged = false;
					leftMines++;
					lblMines.setText(Integer.toString(leftMines));
					cover.repaint();
				} else {
					cover.flagged = true;
					leftMines--;
					lblMines.setText(Integer.toString(leftMines));
					cover.repaint();
				}
			}
		}

		/**
		 * If Cell is empty and not flagged, than mine is placed in the Cell.
		 */
		public boolean placeMine() {
			if (!content.empty || cover.flagged)
				return false;

			content.empty = false;
			content.mine = true;
			lblMines.setText(Integer.toString(leftMines));
			return true;
		}

		/**
		 * If this Cell is not flagged, than it is opened.
		 * 
		 * @return boolean
		 */
		public boolean open() {
			if (!cover.flagged) {
				open = true;

				cardLayout.show(Cell.this, CELL_CONTENT);
				return true;
			}
			return false;
		}

		/**
		 * This method is adding Component to the Cell. This component is JLabel
		 * representing the number from 1 to 8. The numbers are indicating how
		 * many mines are located around this Cell. TRUE is returned if
		 * Component has been successfully added to the Cell, otherwise FALSE is
		 * returned.
		 * 
		 * @param component
		 * @return boolean
		 */
		public boolean fill(Component component) {
			if (!content.empty)
				return false;

			final String text = ((JLabel) component).getText();

			content.empty = false;

			if (!content.mine) {
				lblContent.setForeground(getColor(Integer.parseInt(text)));
				lblContent.setText(text);
			}

			return true;
		}

		/**
		 * This method returns TRUE if Cell is not empty and has mine in it.
		 * 
		 * @return boolean
		 */
		public boolean hasMine() {
			return (!content.empty) ? content.mine : false;
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(CELL_WIDTH, CELL_HEIGHT);
		}

		@Override
		public Dimension getMinimumSize() {
			return new Dimension(CELL_WIDTH, CELL_HEIGHT);
		}

		@Override
		public Dimension getMaximumSize() {
			return new Dimension(CELL_WIDTH, CELL_HEIGHT);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (controlRequestsListener == null)
				return;

			if (controlRequestsListener.isGameRunning()) {
				controlRequestsListener.startTimer();

				if (SwingUtilities.isLeftMouseButton(e)) {
					// Opening Cell
					if (open()) {
						if (content.empty) {
							findEmptyCells(row, column);
						} else if (content.mine) {
							gameOver();
						}

						// Checking if game is won
						if (checkWin())
							win();
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					// Flagging Cell
					flag();

					// Checking if game is won
					if (checkWin())
						win();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		/**
		 * ContentPanel class extends JPanel class and it is used for custom
		 * drawings.
		 * 
		 * <p>
		 * Date created: 2014.01.18
		 * 
		 * @author Vassili Kurman
		 * @version 0.1
		 */
		private class ContentPanel extends JPanel {
			private static final long serialVersionUID = -949764874625808490L;
			private boolean mine, empty;

			/**
			 * Default Constructor.
			 */
			public ContentPanel() {
				empty = true;
				mine = false;
			}

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				if (mine) {
					// Drawing oval
					int x = (int) (CELL_WIDTH * 0.3);
					int y = (int) (CELL_HEIGHT * 0.3);
					int width = (int) (CELL_WIDTH * 0.4);
					int height = (int) (CELL_HEIGHT * 0.4);

					g.setColor(Color.BLACK);
					g.fillOval(x, y, width, height);
					g.setColor(Color.BLACK);
					g.drawOval(x, y, width, height);

					// Drawing main lines
					g.drawLine((int) (CELL_WIDTH * 0.125),
							(int) (CELL_HEIGHT * 0.5),
							(int) (CELL_WIDTH * 0.875),
							(int) (CELL_HEIGHT * 0.5));
					g.drawLine((int) (CELL_WIDTH * 0.5),
							(int) (CELL_HEIGHT * 0.125),
							(int) (CELL_WIDTH * 0.5),
							(int) (CELL_HEIGHT * 0.875));
				}
			}
		}

		/**
		 * CoverPanel class extends JPanel class and it is used for custom
		 * drawings.
		 * 
		 * <p>
		 * Date created: 2014.01.18
		 * 
		 * @author Vassili Kurman
		 * @version 0.1
		 */
		private class CoverPanel extends JPanel {
			private static final long serialVersionUID = 1543993770632213656L;
			private boolean flagged;

			/**
			 * Default Constructor.
			 */
			public CoverPanel() {
				flagged = false;
			}

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				if (flagged) {
					int x = (int) (CELL_WIDTH * 0.25);
					int y = (int) (CELL_HEIGHT * 0.1);
					int width = 3;
					int height = (int) (CELL_HEIGHT * 0.75);

					g.setColor(Color.BLACK);
					g.fillRect(x, y, width, height);
					g.setColor(Color.BLACK);
					g.drawRect(x, y, width, height);

					Polygon p = new Polygon();
					p.addPoint(x + width, y);
					p.addPoint(x + width + (height / 2), y + (height / 4));
					p.addPoint(x + width, y + (height / 2));

					g.setColor(Color.RED);
					g.fillPolygon(p);
					g.setColor(Color.BLACK);
					g.drawPolygon(p);
				}
			}
		}
	}

	/**
	 * RoundButton class extends JButton and makes button round with some custom
	 * drawings.
	 * 
	 * @see http://stackoverflow.com/questions/778222/make-a-button-round
	 */
	public class RoundButton extends JButton {
		private static final long serialVersionUID = 3560503073087032731L;
		private Shape shape;

		/**
		 * Default Constructor.
		 */
		public RoundButton() {
			// These statements enlarge the button so that it
			// becomes a circle rather than an oval.
			Dimension size = getPreferredSize();
			size.width = size.height = Math.max(size.width, size.height);
			setPreferredSize(size);

			// This call causes the JButton not to paint the background.
			// This allows to paint a round background.
			setContentAreaFilled(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (getModel().isArmed()) {
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(getBackground());
			}
			g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

			// Drawing faces
			g.setColor(Color.BLACK);

			g.drawOval((int) (getWidth() * 0.25), (int) (getHeight() * 0.35),
					(int) (getWidth() * 0.1), (int) (getHeight() * 0.1));
			g.fillOval((int) (getWidth() * 0.25), (int) (getHeight() * 0.35),
					(int) (getWidth() * 0.1), (int) (getHeight() * 0.1));

			g.drawOval((int) (getWidth() * 0.65), (int) (getHeight() * 0.35),
					(int) (getWidth() * 0.1), (int) (getHeight() * 0.1));
			g.fillOval((int) (getWidth() * 0.65), (int) (getHeight() * 0.35),
					(int) (getWidth() * 0.1), (int) (getHeight() * 0.1));

			if (getBackground().equals(BUTTON_COLOR_NEUTRAL)) {
				g.drawLine((int) (getWidth() * 0.25),
						(int) (getHeight() * 0.70), (int) (getWidth() * 0.75),
						(int) (getHeight() * 0.70));
			} else if (getBackground().equals(BUTTON_COLOR_HAPPY)) {
				g.drawArc((int) (getWidth() * 0.125), 0,
						(int) (getWidth() * 0.75), (int) (getHeight() * 0.75),
						225, 90);
			} else if (getBackground().equals(BUTTON_COLOR_SAD)) {
				g.drawArc((int) (getWidth() * 0.15),
						(int) (getHeight() * 0.60), (int) (getWidth() * 0.75),
						(int) (getHeight() * 0.75), 45, 90);
			}

			super.paintComponent(g);
		}

		@Override
		protected void paintBorder(Graphics g) {
			g.setColor(getForeground());
			g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
		}

		@Override
		public boolean contains(int x, int y) {
			// If the button has changed size,
			// make a new shape object.
			if (shape == null || !shape.getBounds().equals(getBounds())) {
				shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
			}
			return shape.contains(x, y);
		}
	}

	/**
	 * Adds <code>ControlRequestsListener</code>
	 * 
	 * @param listener
	 */
	public void addControlRequestsListener(ControlRequestsListener listener) {
		controlRequestsListener = listener;
	}

	/**
	 * Updates time on label.
	 * 
	 * @param minutes
	 * @param seconds
	 */
	public void updateTime(int minutes, int seconds) {
		try {
			lblTimer.setText(nformat.valueToString(minutes) + ":"
					+ nformat.valueToString(seconds));
		} catch (ParseException e) {
		}
	}

	/**
	 * Displays dialog to user for his name input.
	 * 
	 * @return String
	 */
	public String requestPlayerName() {
		NameInputDialog dialog = new NameInputDialog(this);
		if (dialog.isOkPressed()) {
			return dialog.getInput();
		} else {
			return null;
		}
	}
}