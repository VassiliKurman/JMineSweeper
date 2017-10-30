package vkurman.jminesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ClockListener class implements ActionListener for Swing Timer. This
 * ActionListener updates UI timer after specified period, in this case
 * every 1000 milliseconds.
 * 
 * @see http://stackoverflow.com/questions/5528939
 */
public class ClockListener implements ActionListener {

	private static final int MAX_SECONDS_TO_DISPLAY = 59;
	private static final int MAX_MINUTES_TO_DISPLAY = 99;
	private static final int SECONDS = 60;
	private static final int MINUTES = 60;
	
	private TimerListener timerListener;
	
	private int seconds;
	private int minutes;

	@Override
	public void actionPerformed(ActionEvent e) {
		seconds %= SECONDS;
		minutes %= MINUTES;

		// Stopping timer if it gets to top limit.
		if (minutes <= MAX_MINUTES_TO_DISPLAY) {
			if (seconds <= MAX_SECONDS_TO_DISPLAY) {
				
				// Update time listener
				if(timerListener != null){
					timerListener.updateTime(minutes, seconds);
				}
				
				seconds++;
				if (seconds == SECONDS) {
					minutes++;
				}
			}
		}
	}

	/**
	 * This method is resetting timer counter on JLabel to 0;
	 */
	public void resetCounter() {
		seconds = 0;
		minutes = 0;

		if(timerListener != null){
			timerListener.updateTime(minutes, seconds);
		}
	}
	
	/**
	 * Sets timer listener.
	 * 
	 * @param listener
	 */
	public void setTimerListener(TimerListener listener) {
		timerListener = listener;
	}
}