package vkurman.jminesweeper;

/**
 * <code>ControlRequestsListener</code> interface to send requests to controller class.
 *
 * <p>
 * Date : 25 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
public interface ControlRequestsListener {
	
	/**
	 * Request to get records.
	 * 
	 * @return Record[]
	 */
	public Record[] getRecords();
	
	/**
	 * Request to reset records.
	 * 
	 * @return Record[]
	 */
	public Record[] resetRecords();
	
	/**
	 * Check if time is a new record.
	 * 
	 * @param time
	 * @return int - index for new record in array
	 */
	public int isRecord(long time);
	
	/**
	 * Request to insert specified record at specified index in arrays.
	 * 
	 * @param index
	 * @param record
	 */
	public void replaceRecord(int index, Record record);
	
	/**
	 * Checks if game is running. Main purpose of it to control actions from mouse events.
	 * @return
	 */
	public boolean isGameRunning();
	
	/**
	 * Request to start the game. Main purpose of it to enable actions from mouse events.
	 */
	public void startGame();
	
	/**
	 * Request to stop the game. Main purpose of it to prevent actions from mouse events.
	 */
	public void stopGame();
	
	/**
	 * Request about timer status. <code>true</code> is returned if timer is running.
	 * 
	 * @return boolean.
	 */
	public boolean isTimerRunning();
	
	/**
	 * Request to start timer.
	 */
	public void startTimer();
	
	/**
	 * Request to stop timer.
	 */
	public void stopTimer();
	
	/**
	 * Request to reset timer
	 */
	public void resetTimer();
	
	/**
	 * Signals about winning result.
	 */
	public void win();
	
	/**
	 * Request to change difficulty level to specified value.
	 * 
	 * @param difficulty
	 */
	public void setDifficulty(Difficulty difficulty);
	
	/**
	 * Returns difficult level.
	 * 
	 * @return Difficulty
	 */
	public Difficulty getDifficulty();
}