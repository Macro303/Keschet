package macro303.base.pieces

import macro303.base.Colour
import macro303.base.Direction

class Thief(teamColour: Colour) : Piece(
	teamColour = teamColour,
	maxDistance = 1,
	symbol = "T",
	validDirections = arrayOf(
		Direction.NORTH,
		Direction.NORTH_EAST,
		Direction.EAST,
		Direction.SOUTH_EAST,
		Direction.SOUTH,
		Direction.SOUTH_WEST,
		Direction.WEST,
		Direction.NORTH_WEST
	)
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Thief) return false
		if (!super.equals(other)) return false
		return true
	}

	override fun hashCode(): Int {
		return super.hashCode()
	}

	override fun toString(): String {
		return "Thief() ${super.toString()}"
	}
}