package vkurman.jminesweeper;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

/**
 * Main entry point class for JMinesweeper Game. This is re-designed my basic
 * JMinesweeper game with 3 levels of difficulty and added record table.
 *
 * <p>
 * Date : 23 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
public class JMinesweeper implements ControlRequestsListener, TimerListener {

	public static final String extension = ".txt";
	public static final char separator = ':';

	private JMinesweeperUI gui;
	private Stopwatch stopwatch;
	private ClockListener clockListener;
	private Record[] records = new Record[10];
	private boolean running;

	private Difficulty difficulty;

	private JMinesweeper() {
		running = false;
		difficulty = Difficulty.INTERMEDIATE;

		for (int i = 0; i < records.length; i++) {
			records[i] = new Record();
		}

		readRecords();

		clockListener = new ClockListener();
		clockListener.setTimerListener(this);

		stopwatch = new Stopwatch(1000, clockListener);
		stopwatch.setInitialDelay(0);
	}

	@Override
	public Record[] getRecords() {
		return records;
	}

	private void readRecords() {
		try {
			FileInputStream fis = new FileInputStream(difficulty.getName()
					+ extension);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String strLine;
			// read content of file
			for (int i = 0; i < records.length; i++) {
				if ((strLine = br.readLine()) != null) {
					int index = strLine.indexOf(separator);
					records[i].setName(strLine.substring(0, index));
					records[i].setTime(Long.parseLong(strLine.substring(
							index + 1, strLine.length())));
				}
			}
			// Close the input stream
			br.close();
		} catch (IOException e) {
			System.out.println("***Error reading records***");
		}
	}

	/**
	 * Saving records to the file.
	 */
	private void saveRecords() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					difficulty.getName() + extension));
			for (int i = 0; i < records.length; i++) {
				out.write(records[i].getName() + JMinesweeper.separator
						+ records[i].getTime());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			System.out.println("***Error saving records***");
		}
	}

	/**
	 * Replaces records starting from supplied index and saves to the file.
	 * 
	 * @param index
	 * @param record
	 */
	public void replaceRecord(int index, Record record) {
		if (index < 0 || index > records.length - 1)
			return;

		Record temp;

		for (int i = index; i < records.length; i++) {
			temp = records[i];
			records[i] = record;
			record = temp;
		}

		saveRecords();
	}

	/**
	 * Resetting records and writing to file
	 */
	@Override
	public Record[] resetRecords() {
		for (int i = 0; i < records.length; i++) {
			records[i].setName(Record.defaultName);
			records[i].setTime(Record.defaultTime);
		}

		saveRecords();

		return records;
	}

	/**
	 * Checking if time supplied is a new record.
	 * 
	 * @param time
	 * @return int - -1 if not a record, otherwise returns index
	 */
	@Override
	public int isRecord(long time) {
		for (int i = 0; i < records.length; i++) {
			if (records[i].getTime() == 0l || time < records[i].getTime()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isGameRunning() {
		return running;
	}

	@Override
	public void startGame() {
		running = true;
	}

	@Override
	public void stopGame() {
		running = false;
	}

	@Override
	public boolean isTimerRunning() {
		return stopwatch.isRunning();
	}

	@Override
	public void startTimer() {
		if (!stopwatch.isRunning()) {
			stopwatch.start();
		}
	}

	@Override
	public void stopTimer() {
		if (stopwatch.isRunning()) {
			stopwatch.stop();
		}
	}

	@Override
	public void resetTimer() {
		clockListener.resetCounter();
	}

	@Override
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;

		readRecords();
	}

	@Override
	public Difficulty getDifficulty() {
		return difficulty;
	}

	@Override
	public void updateTime(int minutes, int seconds) {
		if (gui == null)
			return;

		gui.updateTime(minutes, seconds);
	}

	@Override
	public void win() {
		long time = stopwatch.getDurationTime();
		int i = isRecord(time);

		if (i >= 0 && i < records.length) {
			String name = getPlayerName();
			if (name != null) {
				replaceRecord(i, new Record(name, time));

				{
					// Displaying records table
					RecordsView view = new RecordsView(gui, this);
					view.setVisible(true);
				}

				name = null;
			}
		} else {
			JOptionPane.showMessageDialog(null, "CONGRATULATIONS!!!"
					+ "\nYou WON the Game!", "Win!!!",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Requests GUI to get input from user.
	 * 
	 * @return String
	 */
	public String getPlayerName() {
		return gui.requestPlayerName();
	}

	/**
	 * Displaying main GUI.
	 */
	private void buildAndShowUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				gui = new JMinesweeperUI(JMinesweeper.this, difficulty);
			}
		});
	}

	/**
	 * JMinesweeperUI entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JMinesweeper game = new JMinesweeper();
		game.buildAndShowUI();
	}
}