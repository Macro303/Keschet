package github.macro.pieces

import github.macro.Direction
import github.macro.players.Player

/**
 * Created by Macro303 on 2018-02-09.
 */
class General(player: Player) : Piece(
	player = player,
	distance = 10,
	symbol = "G",
	directions = arrayOf(
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
//	private static final String symbol = "\uD83C\uDF96";
//	private static final String symbol = "🎖";
//	private static final String symbol = "G";

	override fun toString(): String = "General() ${super.toString()}"
}