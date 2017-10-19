package macro303.base.pieces

import macro303.base.Board
import macro303.base.Colour
import macro303.base.Direction
import macro303.base.IBoard
import org.apache.logging.log4j.LogManager

class Merchant(teamColour: Colour) : Piece(teamColour = teamColour, maxDistance = 1, symbol = "M", validDirections = arrayOf(Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST)) {

	fun validMovement(start: Pair<Int, Int>, end: Pair<Int, Int>, board: Board): Boolean {
		val valid = board.getAllSurroundingPieces(end).any { it is Emperor && it.teamColour === teamColour } && validDirections.contains(IBoard.calculateDirection(start = start, end = end)) || super.validMovement(start = start, end = end)
		LOGGER.trace("boolean validMovement(Pair<Int, Int>, Pair<Int, Int>) = $valid")
		return valid
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Merchant) return false
		if (!super.equals(other)) return false
		return true
	}

	override fun hashCode(): Int {
		return super.hashCode()
	}

	override fun toString(): String {
		return "Merchant() ${super.toString()}"
	}

	companion object {
		private val LOGGER = LogManager.getLogger(Merchant::class.java)
	}
}