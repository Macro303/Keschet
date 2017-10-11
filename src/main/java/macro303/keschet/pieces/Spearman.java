package macro303.keschet.pieces;

import macro303.keschet.ConsoleColour;
import macro303.keschet.Direction;

public class Spearman extends Piece {

	public Spearman(ConsoleColour teamColour) {
		super(teamColour, 2, "P", new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST});
	}

	@Override
	public String toString() {
		return "Spearman{} " + super.toString();
	}
}