package github.macro

import github.macro.console.Colour
import github.macro.pieces.Emperor
import github.macro.pieces.Merchant
import github.macro.pieces.Piece
import github.macro.pieces.Scholar
import github.macro.players.Player
import org.apache.logging.log4j.LogManager
import kotlin.math.abs

/**
 * Created by Macro303 on 2020-Aug-24
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)

	@JvmStatic
	val P1_COLOUR = Colour.YELLOW

	@JvmStatic
	val P2_COLOUR = Colour.RED

	private fun checkForBlocking(board: Board, start: Square, end: Square): Boolean {
		var row = start.row
		var col = start.col
		val direction = calculateDirection(start, end) ?: return false
		val distance = calculateDistance(start, end, direction)
		var blocked = false
		for (i in 0 until distance) {
			when (direction) {
				Direction.NORTH -> row--
				Direction.NORTH_EAST -> {
					row--
					col++
				}
				Direction.EAST -> col++
				Direction.SOUTH_EAST -> {
					row++
					col++
				}
				Direction.SOUTH -> row++
				Direction.SOUTH_WEST -> {
					row++
					col--
				}
				Direction.WEST -> col--
				Direction.NORTH_WEST -> {
					row--
					col--
				}
			}
			val temp = board.getSquare(row, col)
			if (temp?.piece != null && i != distance - 1)
				blocked = true
		}
		return blocked
	}

	@JvmStatic
	fun validMovement(board: Board, start: Square, end: Square): Boolean {
		val piece: Piece = start.piece ?: return false

		val blocked = checkForBlocking(board, start, end)
		var selfTaking = false
		var scholarBlocked = false
		end.piece?.let {
			selfTaking = piece.player == end.piece?.player
			scholarBlocked = checkForScholar(board, end, piece.player)
		}

		if (piece is Merchant && checkForEmperor(board, end, piece.player))
			return !blocked && !selfTaking && !scholarBlocked
		val direction = calculateDirection(start, end) ?: return false
		if (!piece.directions.contains(direction))
			return false
		val distance = calculateDistance(start, end, direction)
		if (piece.distance < distance && distance > 0)
			return false
		return !blocked && !selfTaking && !scholarBlocked
	}

	@JvmStatic
	fun calculateDirection(start: Square, end: Square): Direction? {
		val horizontal = end.col - start.col
		val vertical = end.row - start.row
		val diagonal = abs(horizontal) == abs(vertical)
		return when {
			horizontal == 0 && vertical < 0 && !diagonal -> Direction.NORTH
			horizontal > 0 && vertical < 0 && diagonal -> Direction.NORTH_EAST
			horizontal > 0 && vertical == 0 && !diagonal -> Direction.EAST
			horizontal > 0 && vertical > 0 && diagonal -> Direction.SOUTH_EAST
			horizontal == 0 && vertical > 0 && !diagonal -> Direction.SOUTH
			horizontal < 0 && vertical > 0 && diagonal -> Direction.SOUTH_WEST
			horizontal < 0 && vertical == 0 && !diagonal -> Direction.WEST
			horizontal < 0 && vertical < 0 && diagonal -> Direction.NORTH_WEST
			else -> null
		}
	}

	@JvmStatic
	fun calculateDistance(start: Square, end: Square, direction: Direction): Int {
		return when (direction) {
			Direction.EAST, Direction.WEST, Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST -> abs(
				start.col - end.col
			)
			Direction.NORTH, Direction.SOUTH -> abs(start.row - end.row)
			else -> 0
		}
	}

	@JvmStatic
	fun checkForEmperor(board: Board, location: Square, player: Player): Boolean {
		var exists = false
		for (row in -1..1) {
			for (col in -1..1) {
				val temp = board.getSquare(location.row + row, location.col + col) ?: continue
				temp.piece ?: continue
				if (temp.piece is Emperor && (temp.piece as Emperor).player == player)
					exists = true
			}
		}
		return exists
	}

	@JvmStatic
	fun checkForScholar(board: Board, location: Square, player: Player): Boolean {
		var exists = false
		for (row in -1..1) {
			for (col in -1..1) {
				val temp = board.getSquare(location.row + row, location.col + col) ?: continue
				temp.piece ?: continue
				if (temp.piece is Scholar && (temp.piece as Scholar).player == player)
					exists = true
			}
		}
		return exists
	}
}