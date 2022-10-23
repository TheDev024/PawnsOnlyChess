package chess

import kotlin.math.abs

class PawnsOnlyChess {
    private var currentColor = "White"
    private var opponentColor = "Black"

    private lateinit var firstPlayer: String
    private lateinit var secondPlayer: String
    private lateinit var currentPlayer: String

    private var enPassant: Boolean = false
    private var passantX = 0; private var passantY = 0

    private val board = MutableList(8) {
        MutableList(8) { row ->
            when (row) {
                1 -> 'W'; 6 -> 'B'; else -> ' '
            }
        }
    }

    fun startGame() {
        println("Pawns-Only Chess")
        getPlayerNames()
        currentPlayer = firstPlayer
        printBoard()

        game@ while (true) {
            val move = getMove()
            if (move == "exit") break
            if (moveIsValid(move)) {
                printBoard()

                val endRank = if (currentColor == "White") 8 else 1 // get the end rank for current currentColor
                var total = 0 // count of opposite pawns

                for (row in board) { // win conditions
                    var rowIndex = 1
                    for (cell in row) {
                        val upC = currentColor[0]
                        if (rowIndex == endRank && cell == upC) { // check if any pawn reached end rank
                            println("$currentColor Wins!")
                            break@game
                        }

                        // count pawns of each currentColor
                        if (cell == opponentColor[0]) total += 1
                        rowIndex += 1
                    }
                }

                if (total == 0) { // check if all opposite pawns captured
                    println("$currentColor Wins!")
                    break
                }

                // change the turn and currentColor
                currentPlayer = if (currentPlayer == firstPlayer) secondPlayer else firstPlayer
                currentColor = if (currentColor == "White") "Black" else "White"
                opponentColor = if (opponentColor == "White") "Black" else "White"

                if (!canMove()) break
            }
        }
        println("Bye!")
    }

    /**
     * Checks if the current player has a valid move
     * @return true if there is a valid move, false otherwise
     */
    private fun canMove(): Boolean {
        val (dir, start) = if (currentColor == "White") Pair(1, 2) else Pair(-1, 7)
        val upC = currentColor[0]
        val opposite = opponentColor[0]
        var x = 0
        for (row in board) {
            var y = 0
            for (cell in row) {
                if (cell == upC) {
                    if (board[x][y + dir] == ' ') return true
                    if (y == start && board[x][y + 2 * dir] == ' ') return true
                    if (enPassant && passantY == y + dir && abs(passantX - x) == 1) return true
                    try {
                        if (board[x + 1][y + dir] == opposite) return true
                    } catch (e: IndexOutOfBoundsException) {
                        if (board[x - 1][y + dir] == opposite) return true
                    }
                }
                y += 1
            }
            x += 1
        }
        println("Stalemate!")
        return false
    }

    /**
     * Checks validity of the move
     * @param move coordinates of move in string format
     * @return true if move is valid, false otherwise
     */
    private fun moveIsValid(move: String): Boolean {
        if (Regex("([a-h][1-8]){2}").matches(move)) {
            // get current direction of move and starting position for pawns of current currentColor
            val (dir, startY) = if (currentColor == "White") Pair(1, 1) else Pair(-1, 6)

            // parse coordinates of the move
            val x1 = move[0] - 'a'; val y1 = move[1] - '1' // initial coordinates
            val x2 = move[2] - 'a'; val y2 = move[3] - '1' // final coordinates

            if (board[x1][y1] == currentColor[0]) { // check if there is current player's pawn in the initial coordinate
                if (abs(x2 - x1) in 0..1) { // check for x range of the move
                    if (enPassant && x2 == passantX && y2 == passantY) { // check if the attack is en passant
                        board[passantX][passantY - dir] = ' '; board[x1][y1] = ' '
                        board[passantX][passantY] = currentColor[0]
                        enPassant = false; passantX = 0; passantY = 0 // reset en passant conditions
                        return true
                    }
                    if (y1 == startY && y2 - y1 == 2 * dir) {
                        enPassant = true; passantX = x2; passantY =
                            y2 - dir // set en passant conditions for the next move
                        board[x1][y1] = ' '; board[x2][y2] = currentColor[0]
                        return true
                    }
                    if ((abs(x2 - x1) == 1 && (y2 - y1) * dir == 1 && board[x2][y2] == opponentColor[0]) || (x2 - x1 == 0 && board[x2][y2] == ' ' && (y2 - y1 == dir))
                    ) {
                        board[x1][y1] = ' '; board[x2][y2] = currentColor[0]
                        enPassant = false; passantX = 0; passantY = 0 // reset en passant conditions if set
                        return true
                    }
                }
            } else {
                println("No ${currentColor.lowercase()} pawn at ${move.subSequence(0..1)}")
                return false
            }
        }
        println("Invalid Input")
        return false
    }

    /**
     * Gets coordinates to move
     * @return coordinates or 'exit'
     */
    private fun getMove(): String {
        println("$currentPlayer's turn:")
        return readln()
    }

    /**
     * Output current state of chessboard
     */
    private fun printBoard() {
        for (i in 8 downTo 1) {
            print("  +")
            for (j in 1..8) print("---+")
            println()
            print("$i |")
            for (j in 0..7) print(" ${board[j][i - 1]} |")
            println()
        }
        print("  +")
        for (j in 1..8) print("---+")
        println()
        print(" ")
        for (c in 'a'..'h') print("   $c")
        println()
    }

    /**
     * Get player names from console and assign them to the matching variables
     */
    private fun getPlayerNames() {
        println("First Player's name:")
        firstPlayer = readln()
        println("Second Player's name:")
        secondPlayer = readln()
    }

}

fun main() {
    val chess = PawnsOnlyChess()
    chess.startGame()
}