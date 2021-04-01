package gameentities;

/**
 * Implementation of the Square objects.
 * <p>
 * The attributes include the coordinates, the premium square status and, if
 * placed, the Tile on top of it.
 * 
 * @author tthielen
 */
public class Square {
	private boolean dls = false; // Double Letter Square
	private boolean tls = false; // Triple Letter Square
	private boolean dws = false; // Double Word Square
	private boolean tws = false; // Triple Word Square

	private boolean star = false; // Square on (8,8) through which the first word has to be played

	private int posX; // On X-Axis: [1,15]
	private int posY; // On Y-Axis: [1,15]
	
	private Tile tile; //This variable saves the Tile which is put on the Square

	/**
	 * Constructor: Creates a Square object, saves the coordinates and checks
	 * whether those require a premium square.
	 * 
	 * @author tthielen
	 * @param posX
	 * @param posY
	 */
	public Square(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;

		switch (posX) {

		case 1:
		case 15:
			if (posY == 1 || posY == 8 || posY == 15) {
				this.tws = true;
			} else if (posY == 4 || posY == 12) {
				this.dls = true;
			}
			break;

		case 2:
		case 14:
			if (posY == 2 || posY == 14) {
				this.dws = true;
			} else if (posY == 6 || posY == 10) {
				this.tls = true;
			}
			break;

		case 3:
		case 13:
			if (posY == 3 || posY == 13) {
				this.dws = true;
			} else if (posY == 7 || posY == 9) {
				this.dls = true;
			}
			break;

		case 4:
		case 12:
			if (posY == 4 || posY == 12) {
				this.dws = true;
			} else if (posY == 1 || posY == 8 || posY == 15) {
				this.dls = true;
			}
			break;

		case 5:
		case 11:
			if (posY == 5 || posY == 11) {
				this.dws = true;
			}
			break;

		case 6:
		case 10:
			if (posY == 2 || posY == 6 || posY == 10 || posY == 14) {
				this.tls = true;
			}
			break;

		case 7:
		case 9:
			if (posY == 3 || posY == 7 || posY == 9 || posY == 13) {
				this.dls = true;
			}
			break;

		case 8:
			if (posY == 1 || posY == 15) {
				this.tws = true;
			} else if (posY == 4 || posY == 12) {
				this.dls = true;
			} else if (posY == 8) {
				this.star = true;
			}
			break;

		default:
			System.out.println("Error: Invalid coordinates!");
		}

	}
}
