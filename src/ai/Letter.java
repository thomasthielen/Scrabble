package ai;

public class Letter {
	private int x;
	private int y;
	private char c;
	private boolean column;
	
	public Letter(int x, int y,char c, boolean column) {
		this.x = x;
		this.y = y;
		this.column = column;
		this.c = c;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean isColumn() {
		return this.column;
	}
	
	public char getLetter() {
		return this.c;
	}
	
}
