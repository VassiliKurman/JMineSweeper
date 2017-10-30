package vkurman.jminesweeper;

/**
 * <code>Record</code> class represents record in the table of records.
 *
 * <p>
 * Date : 22 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
class Record {

	public static final String defaultName = "Unknown";
	public static final long defaultTime = 0l;

	private String name;
	private long time;

	/**
	 * Constructor
	 */
	public Record() {
		this.name = defaultName;
		this.time = defaultTime;
	}

	public Record(long time) {
		this.name = defaultName;
		this.time = time;
	}

	public Record(String name, long time) {
		this.name = name;
		this.time = time;
	}

	/**
	 * Returns player name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Name setter
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns time in milliseconds.
	 * 
	 * @return time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Time setter
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}
}