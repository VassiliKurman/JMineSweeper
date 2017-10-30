package vkurman.jminesweeper;

import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.Timer;

/**
 * <code>Stopwatch</code> extends swing timer class and provides some required functionality.
 *
 * <p>
 * Date : 23 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
public class Stopwatch extends Timer {
	
	private static final long serialVersionUID = -7487410361569084944L;

	public Stopwatch(int delay, ActionListener listener) {
		super(delay, listener);
	}

	private long start, stop;

	@Override
	public synchronized void start() {
		start = Calendar.getInstance().getTimeInMillis();
		
		super.start();
	}

	@Override
	public synchronized void stop() {
		stop = Calendar.getInstance().getTimeInMillis();

		super.stop();
	}
	
	/**
	 * Returns time between start and stop in milliseconds
	 * 
	 * @return long
	 */
	public long getDurationTime() {
		return (stop > start) ? stop - start : 0l;
	}
}