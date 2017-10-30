package vkurman.jminesweeper;

/**
 * <code>Difficulty</code> lists possible levels of difficulty with major set of variables.
 *
 * <p>
 * Date : 25 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
public enum Difficulty {
	
	BEGINNER("Beginner", 9, 9, 10),
	INTERMEDIATE("Intermediate", 16, 16, 40),
	ADVANCED("Advanced", 16, 32, 99);
	
	private String name;
	private int rows = 16;
	private int columns = 16;
	private int mines = 40;
	
	private Difficulty(String name, int rows, int columns, int mines){
		this.name = name;
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;
	}
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int getMines() {
		return mines;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString(){
		return name;
	}
}