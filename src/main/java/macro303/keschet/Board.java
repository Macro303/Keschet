package macro303.keschet;

import macro303.keschet.pieces.Emperor;
import macro303.keschet.pieces.Piece;

import java.util.ArrayList;
import java.util.Objects;

public class Board {
	private Square[][] board = new Square[11][11];

	Board() {
		for (int column = 0; column < board.length; column++) {
			for (int row = 0; row < board[column].length; row++) {
				board[column][row] = new Square();
			}
		}
	}

	public static Direction calculateDirection(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
		if (Objects.equals(start.getLeft(), end.getLeft()) && start.getRight() - end.getRight() > 0)
			return Direction.NORTH;
		if (start.getLeft() - end.getLeft() > 0 && start.getRight() - end.getRight() > 0)
			return Direction.NORTH_EAST;
		if (start.getLeft() - end.getLeft() > 0 && Objects.equals(start.getRight(), end.getRight()))
			return Direction.EAST;
		if (start.getLeft() - end.getLeft() > 0 && start.getRight() - end.getRight() < 0)
			return Direction.SOUTH_EAST;
		if (Objects.equals(start.getLeft(), end.getLeft()) && start.getRight() - end.getRight() < 0)
			return Direction.SOUTH;
		if (start.getLeft() - end.getLeft() < 0 && start.getRight() - end.getRight() < 0)
			return Direction.SOUTH_WEST;
		if (start.getLeft() - end.getLeft() < 0 && Objects.equals(start.getRight(), end.getRight()))
			return Direction.WEST;
		if (start.getLeft() - end.getLeft() < 0 && start.getRight() - end.getRight() > 0)
			return Direction.NORTH_WEST;
		return Direction.INVALID;
	}

	public static int calculateDistance(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
		Direction direction = calculateDirection(start, end);
		if (direction == Direction.INVALID)
			return 0;
		if (direction == Direction.NORTH || direction == Direction.SOUTH)
			return start.getRight() - end.getRight();
		else
			return start.getLeft() - end.getLeft();
	}

	public void draw() {
		Console.showTitle("Board");
		for (int column = 0; column < board.length; column++) {
			for (int row = 0; row < board[column].length; row++) {
				if (column == 0)
					System.out.print((row == 0 ? "" : " ") + row + (row == 10 ? "" : " "));
				else if (row == 0)
					System.out.print(column + (column == 10 ? "" : " "));
				else
					Console.showSquare(getSquare(new Pair<>(row, column)).getPiece());
			}
			System.out.println();
		}
	}

	public int countPieces(Team team) {
		int counter = 0;
		for (int column = 0; column < board.length; column++)
			for (int row = 0; row < board[column].length; row++) {
				Square square = getSquare(new Pair<>(row, column));
				if (row != 0 && column != 0 && square.getPiece() != null && square.getPiece().getTeamColour() == team.getColour() && !(square.getPiece() instanceof Emperor))
					counter++;
			}
		return counter;
	}

	public boolean pieceStillOnBoard(Class clazz, Console.Colour teamColour) {
		for (int column = 0; column < board.length; column++)
			for (int row = 0; row < board[column].length; row++) {
				Square square = getSquare(new Pair<>(row, column));
				if (row != 0 && column != 0 && square.getPiece() != null && square.getPiece().getTeamColour() == teamColour && square.getPiece().getClass() == clazz)
					return true;
			}
		return false;
	}

	public Square getSquare(Pair<Integer, Integer> square) {
		return board[square.getRight()][square.getLeft()];
	}

	public ArrayList<Piece> getAllSurroundingPieces(Pair<Integer, Integer> square) {
		ArrayList<Piece> pieces = new ArrayList<>();
		if (square.getLeft() >= 1 && square.getLeft() <= 10 && square.getRight() >= 1 && square.getRight() <= 10) {
			if (getSquare(new Pair<>(square.getLeft() - 1, square.getRight())).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft() - 1, square.getRight())).getPiece());
			if (getSquare(new Pair<>(square.getLeft() - 1, square.getRight() - 1)).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft() - 1, square.getRight() - 1)).getPiece());
			if (getSquare(new Pair<>(square.getLeft(), square.getRight() - 1)).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft(), square.getRight() - 1)).getPiece());
			if (getSquare(new Pair<>(square.getLeft() + 1, square.getRight() - 1)).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft() + 1, square.getRight() - 1)).getPiece());
			if (getSquare(new Pair<>(square.getLeft() + 1, square.getRight())).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft() + 1, square.getRight())).getPiece());
			if (getSquare(new Pair<>(square.getLeft() + 1, square.getRight() + 1)).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft() + 1, square.getRight() + 1)).getPiece());
			if (getSquare(new Pair<>(square.getLeft(), square.getRight() + 1)).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft(), square.getRight() + 1)).getPiece());
			if (getSquare(new Pair<>(square.getLeft() - 1, square.getRight() + 1)).getPiece() != null)
				pieces.add(getSquare(new Pair<>(square.getLeft() - 1, square.getRight() + 1)).getPiece());
		}
		return pieces;
	}
}
