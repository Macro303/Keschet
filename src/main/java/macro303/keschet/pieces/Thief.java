package macro303.keschet.pieces;

import macro303.keschet.ConsoleColour;
import macro303.keschet.Direction;

public class Thief extends Piece {

	public Thief(ConsoleColour teamColour) {
		super(teamColour, 1, "T", new Direction[]{Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST});
	}

	@Override
	public String toString() {
		return "Thief{} " + super.toString();
	}
}