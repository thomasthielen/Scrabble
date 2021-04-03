package gameentities;

import java.util.ArrayList;

public class TestClass {
	public static void main(String[] args) {
		Board board = new Board();
		ArrayList<Square> list = board.getSquareList();

		int i = 1;
		for (Square s : list) {
			if (i < 15) {
				System.out.print(s.getPremium() + "\t");
				i++;
			} else {
				System.out.println(s.getPremium() + "\t");
				i = 1;
			}
		}
	}
}
