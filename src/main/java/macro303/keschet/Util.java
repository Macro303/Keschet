package macro303.keschet;

import macro303.board_game.Board;
import macro303.board_game.Colour;
import macro303.board_game.Coordinates;
import macro303.board_game.Square;
import macro303.keschet.pieces.Emperor;
import macro303.keschet.pieces.Merchant;
import macro303.keschet.pieces.Piece;
import macro303.keschet.pieces.Scholar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import static macro303.keschet.Direction.*;

/**
 * Created by Macro303 on 2018-02-08.
 */
public abstract class Util {
	public static final int SIZE = 10;
	private static final Logger LOGGER = LogManager.getLogger(Util.class);

	@NotNull
	public static Direction calculateDirection(@NotNull Square start, @NotNull Square end) {
		LOGGER.debug("=====Calculate Direction=====");
		int horizontal = end.getCoordinates().getRow() - start.getCoordinates().getRow();
		int vertical = end.getCoordinates().getCol() - start.getCoordinates().getCol();
		boolean diagonal = Math.abs(horizontal) == Math.abs(vertical);
		LOGGER.debug("Horizontal: " + horizontal);
		LOGGER.debug("Vertical: " + vertical);
		LOGGER.debug("Diagonal: " + diagonal);
		if (horizontal == 0 && vertical < 0 && !diagonal)
			return NORTH;
		if (horizontal > 0 && vertical < 0 && diagonal)
			return NORTH_EAST;
		if (horizontal > 0 && vertical == 0 && !diagonal)
			return EAST;
		if (horizontal > 0 && vertical > 0 && diagonal)
			return SOUTH_EAST;
		if (horizontal == 0 && vertical > 0 && !diagonal)
			return SOUTH;
		if (horizontal < 0 && vertical > 0 && diagonal)
			return SOUTH_WEST;
		if (horizontal < 0 && vertical == 0 && !diagonal)
			return WEST;
		if (horizontal < 0 && vertical < 0 && diagonal)
			return NORTH_WEST;
		return INVALID;
	}

	private static boolean validDirection(@NotNull Piece piece, @NotNull Direction direction) {
		LOGGER.debug("=====Valid Direction=====");
		boolean contains = false;
		for (Direction validDirection : piece.getValidDirections())
			if (validDirection == direction)
				contains = true;
		return contains;
	}

	public static int calculateDistance(@NotNull Square start, @NotNull Square end, @NotNull Direction direction) {
		LOGGER.debug("=====Calculate Distance=====");
		switch (direction) {
			case EAST:
			case WEST:
				return Math.abs(start.getCoordinates().getRow() - end.getCoordinates().getRow());
			case NORTH:
			case SOUTH:
				return Math.abs(start.getCoordinates().getCol() - end.getCoordinates().getCol());
			case NORTH_EAST:
			case SOUTH_EAST:
			case SOUTH_WEST:
			case NORTH_WEST:
				return Math.abs(start.getCoordinates().getRow() - end.getCoordinates().getRow());
			default:
				return 0;
		}
	}

	private static boolean validDistance(@NotNull Piece piece, int distance) {
		LOGGER.debug("=====Valid Distance=====");
		return piece.getMaxDistance() >= distance && distance > 0;
	}

	public static boolean validMovement(@NotNull Board board, @NotNull Square start, @NotNull Square end) {
		LOGGER.debug("=====Valid Movement=====");
		Piece piece = (Piece) start.getItem();
		Direction direction = calculateDirection(start, end);
		int distance = calculateDistance(start, end, direction);
		boolean directionValid = piece != null && validDirection(piece, direction);
		if (piece != null && piece.getClass() == Merchant.class) {
			boolean blocked = checkForBlocking(board, start, direction, distance);
			boolean selfTaking = false;
			boolean scholarBlocked = false;
			boolean merchantAbility = checkForEmperor(board, end, piece.getTeamColour());
			if (!merchantAbility)
				merchantAbility = validDistance(piece, distance);
			if (end.getItem() != null) {
				selfTaking = ((Piece) end.getItem()).getTeamColour() == piece.getTeamColour();
				scholarBlocked = checkForScholar(board, end);
			}
			LOGGER.debug("Direction: " + directionValid);
			LOGGER.debug("Merchant Ability: " + merchantAbility);
			LOGGER.debug("Blocked: " + blocked);
			LOGGER.debug("Self Taking: " + selfTaking);
			LOGGER.debug("Scholar Blocked: " + scholarBlocked);
			return directionValid && merchantAbility && !blocked && !selfTaking && !scholarBlocked;
		} else if (piece != null) {
			boolean distanceValid = validDistance(piece, distance);
			boolean blocked = checkForBlocking(board, start, direction, distance);
			boolean selfTaking = false;
			boolean scholarBlocked = false;
			if (end.getItem() != null) {
				selfTaking = ((Piece) end.getItem()).getTeamColour() == piece.getTeamColour();
				scholarBlocked = checkForScholar(board, end);
			}
			LOGGER.debug("Direction: " + directionValid);
			LOGGER.debug("Distance: " + distanceValid);
			LOGGER.debug("Blocked: " + blocked);
			LOGGER.debug("Self Taking: " + selfTaking);
			LOGGER.debug("Scholar Blocked: " + scholarBlocked);
			return directionValid && distanceValid && !blocked && !selfTaking && !scholarBlocked;
		}
		return false;
	}

	private static boolean checkForBlocking(@NotNull Board board, @NotNull Square start, @NotNull Direction direction, int distance) {
		LOGGER.debug("=====Check For Blocking=====");
		int tempRow = start.getCoordinates().getRow();
		int tempCol = start.getCoordinates().getCol();
		boolean blocked = false;
		for (int i = 0; i < distance; i++) {
			if (direction == NORTH || direction == NORTH_EAST || direction == NORTH_WEST) {
				tempCol--;
			} else if (direction == SOUTH || direction == SOUTH_EAST || direction == SOUTH_WEST) {
				tempCol++;
			}
			if (direction == EAST || direction == NORTH_EAST || direction == SOUTH_EAST) {
				tempRow++;
			} else if (direction == WEST || direction == NORTH_WEST || direction == SOUTH_WEST) {
				tempRow--;
			}
			if (direction == INVALID)
				blocked = true;
			Square tempSquare = board.getSquare(new Coordinates(tempRow, tempCol));
			LOGGER.debug("Square: " + tempSquare);
			if (tempSquare != null && tempSquare.getItem() != null && i != distance - 1)
				blocked = true;
		}
		return blocked;
	}

	public static boolean checkForScholar(@NotNull Board board, @NotNull Square location) {
		LOGGER.debug("=====Check For Scholar=====");
		boolean exists = false;
		Piece piece = (Piece) location.getItem();
		assert piece != null;
		Square tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() - 1, location.getCoordinates().getCol()));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("North: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() - 1, location.getCoordinates().getCol() + 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("North-East: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow(), location.getCoordinates().getCol() + 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("East: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() + 1, location.getCoordinates().getCol() + 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("South-East: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() + 1, location.getCoordinates().getCol()));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("South: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() + 1, location.getCoordinates().getCol() - 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("South-West: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow(), location.getCoordinates().getCol() - 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("West: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() - 1, location.getCoordinates().getCol() - 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Scholar.class && ((Piece) tempSquare.getItem()).getTeamColour() == piece.getTeamColour())
			exists = true;
		LOGGER.debug("North-West: " + exists);
		return exists;
	}

	public static boolean checkForEmperor(@NotNull Board board, @NotNull Square location, @NotNull Colour teamColour) {
		LOGGER.debug("=====Check For Emperor=====");
		boolean exists = false;
		Square tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() - 1, location.getCoordinates().getCol()));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("North: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() - 1, location.getCoordinates().getCol() + 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("North-East: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow(), location.getCoordinates().getCol() + 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("East: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() + 1, location.getCoordinates().getCol() + 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("South-East: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() + 1, location.getCoordinates().getCol()));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("South: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() + 1, location.getCoordinates().getCol() - 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("South-West: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow(), location.getCoordinates().getCol() - 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("West: " + exists);
		tempSquare = board.getSquare(new Coordinates(location.getCoordinates().getRow() - 1, location.getCoordinates().getCol() - 1));
		if (tempSquare != null && tempSquare.getItem() != null && tempSquare.getItem().getClass() == Emperor.class && ((Piece) tempSquare.getItem()).getTeamColour() == teamColour)
			exists = true;
		LOGGER.debug("North-West: " + exists);
		return exists;
	}
}