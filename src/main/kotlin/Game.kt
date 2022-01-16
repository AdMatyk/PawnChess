class Game {
    object Board {
        var startPos = mutableListOf<Int>()
        var endPos = mutableListOf<Int>()
        val matrix = MutableList(8) {MutableList(8) {" "} }
        val rows = listOf('8','7', '6', '5', '4', '3', '2', '1')
        val columns = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
        fun printBoard(){
            println("  +---+---+---+---+---+---+---+---+")
            for (i in matrix.indices) {
                print("${rows[i]} ")
                println(matrix[i].joinToString(" | ", "| ", " |"))
                println("  +---+---+---+---+---+---+---+---+")
            }
            println(columns.joinToString("   ", "    "))
            println()
        }
        fun makecoords(input: String) {
            startPos = mutableListOf(rows.indexOf(input[1]), columns.indexOf(input.first()))
            endPos = mutableListOf(rows.indexOf(input[3]), columns.indexOf(input[2]))
        }
    }
    class Player(var number: String, var color: String, var symbol: String) {
        var name = ""
        var army = MutableList(8){ Pawn() }
        class Pawn() {
            val coords = MutableList(2){0}
            var enPassant = false
        }
    }

    fun startSetup(player1: Player, player2: Player) {
        for (player in setOf(player1, player2)) {
            for (i in player.army) {
                i.coords[0] = if (player == player1) 6 else 1
                i.coords[1] = player.army.indexOf(i)
                Board.matrix[i.coords[0]][i.coords[1]] = player.symbol
            }
        }
    }
    val player1 = Player("First", "white", "W")
    val player2 = Player("Second", "black", "B")
    val board = Board
    var gameOver = false


    fun getName(player: Player) {
        println("${player.number} Player's name:")
        player.name = readLine()!!
    }
    fun newcoords(player: Player, startPos: List<Int>, endPos: List<Int>) {
        for (i in player.army) {
            if (startPos[1] == endPos[1] && ((player == player2 && startPos[0] == 1 && endPos[0] == startPos[0] + 2) ||
                (player == player1 && startPos[0] == 6 && endPos[0] == startPos[0] - 2 ))) i.enPassant = true

            if (i.coords == startPos) {
                i.coords[0] = endPos[0]
                i.coords[1] = endPos[1]
                break
            }
        }
    }
    fun move(player: Player,
             startPos: List<Int>,
             endPos: List<Int>) {
        if (isEnPassant(player, startPos, endPos)) {
            if (player == player1) {
                if (endPos[1] == startPos[1] - 1) {
                player2.army.removeIf { it.coords == listOf(startPos[0], startPos[1] - 1) }
                Board.matrix[startPos[0]][startPos[1] - 1] = " "
                } else {
                    player2.army.removeIf { it.coords == listOf(startPos[0], startPos[1] + 1) }
                    Board.matrix[startPos[0]][startPos[1] + 1] = " "
                }
            } else {
                if (endPos[1] == startPos[1] - 1) {
                    player1.army.removeIf { it.coords == listOf(startPos[0], startPos[1] - 1) }
                    Board.matrix[startPos[0]][startPos[1] - 1] = " "
                } else {
                    player1.army.removeIf { it.coords == listOf(startPos[0], startPos[1] + 1) }
                    Board.matrix[startPos[0]][startPos[1] + 1] = " "
                }
            }
        }
        Board.matrix[startPos[0]][startPos[1]] = " "
        Board.matrix[endPos[0]][endPos[1]] = player.symbol

        if (player == player1) player2.army.removeIf { it.coords == endPos }
        else player1.army.removeIf { it.coords == endPos }
        newcoords(player,startPos, endPos)
    }
    fun getInput(player: Player): String {
        println("${player.name}'s turn:")
        return  readLine()!!
    }
    fun inRegex(input: String): Boolean = input.matches(Regex("\\b[a-h][1-8][a-h][1-8]\\b"))
    fun pawnAtPosition(player: Player, input: String): Boolean {
        Board.makecoords(input)
         for (i in player.army) {
            if (i.coords == Board.startPos) return true
        }
        return false
    }
    fun isForwardMove(player: Player,
                      startPos: List<Int>,
                      endPos: List<Int>): Boolean {
        return(startPos[1] == endPos[1] && player == player1 && endPos[0] == startPos[0] - 1 && Board.matrix[endPos[0]][endPos[1]] == " " ||
                startPos[1] == endPos[1] && player == player1 && startPos[0] == 6 && endPos[0] == startPos[0] - 2 && Board.matrix[endPos[0]+1][endPos[1]] == " " && Board.matrix[endPos[0]][endPos[1]] == " " ||
                startPos[1] == endPos[1] && player == player2 && endPos[0] == startPos[0] + 1 && Board.matrix[endPos[0]][endPos[1]] == " " ||
                startPos[1] == endPos[1] && player == player2 && startPos[0] == 1 && endPos[0] == startPos[0] + 2 && Board.matrix[endPos[0]-1][endPos[1]] == " " && Board.matrix[endPos[0]][endPos[1]] == " " )
    }
    fun isEnPassant(player: Player,
                    startPos: List<Int>,
                    endPos: List<Int>): Boolean {
        return((player == player1 && endPos[0] == startPos[0] - 1  && ((endPos[1] == startPos[1] - 1 && player2.army.any {
            it.coords == listOf(startPos[0], startPos[1] - 1) && it.enPassant
        }) || (endPos[1] == startPos[1] + 1) && player2.army.any { it.coords == listOf(startPos[0], startPos[1] + 1) && it.enPassant }) ||
                player == player2 && endPos[0] == startPos[0] + 1  && ((endPos[1] == startPos[1] - 1 && player1.army.any {
            it.coords == listOf(startPos[0], startPos[1] - 1) && it.enPassant
        }) || (endPos[1] == startPos[1] + 1) && player1.army.any { it.coords == listOf(startPos[0], startPos[1] + 1) && it.enPassant })) && Board.matrix[endPos[0]][endPos[1]] == " ")
    }
    fun noPossibleMoves(player: Player): Boolean {
        if (player == player1) {
            for (i in player.army) {
                when (i.coords[1]) {
                    0 -> if (Board.matrix[i.coords[0] - 1][i.coords[1] + 1] == player2.symbol ||
                        Board.matrix[i.coords[0] - 1][i.coords[1]] == " ") return false
                    7 -> if (Board.matrix[i.coords[0] - 1][i.coords[1] - 1] == player2.symbol ||
                        Board.matrix[i.coords[0] - 1][i.coords[1]] == " ") return false
                    else -> if (Board.matrix[i.coords[0] - 1][i.coords[1] - 1] == player2.symbol ||
                        Board.matrix[i.coords[0] - 1][i.coords[1] + 1] == player2.symbol ||
                        Board.matrix[i.coords[0] - 1][i.coords[1]] == " ") return false
                }
            }
        }
        if (player == player2) {
            for (i in player.army) {
                when (i.coords[1]) {
                    0 -> if (Board.matrix[i.coords[0] + 1][i.coords[1] + 1] == player1.symbol ||
                        Board.matrix[i.coords[0] + 1][i.coords[1]] == " ") return false
                    7 -> if (Board.matrix[i.coords[0] + 1][i.coords[1] - 1] == player1.symbol ||
                        Board.matrix[i.coords[0] + 1][i.coords[1]] == " ") return false
                    else -> if (Board.matrix[i.coords[0] + 1][i.coords[1] - 1] == player1.symbol ||
                        Board.matrix[i.coords[0] + 1][i.coords[1] + 1] == player1.symbol ||
                        Board.matrix[i.coords[0] + 1][i.coords[1]] == " "
                    ) return false
                }
            }
        }
        println("Stalemate!")
        return true
    }
    fun isCapture(player: Player,
                  startPos: List<Int>,
                  endPos: List<Int>): Boolean {
        return(endPos[1] == startPos[1] - 1 && player == player1 && endPos[0] == startPos[0] - 1 && Board.matrix[endPos[0]][endPos[1]] == player2.symbol ||
                endPos[1] == startPos[1] + 1 && player == player1 && endPos[0] == startPos[0] - 1 && Board.matrix[endPos[0]][endPos[1]] == player2.symbol ||
                endPos[1] == startPos[1] - 1 && player == player2 && endPos[0] == startPos[0] + 1 && Board.matrix[endPos[0]][endPos[1]] == player1.symbol ||
                endPos[1] == startPos[1] + 1 && player == player2 && endPos[0] == startPos[0] + 1 && Board.matrix[endPos[0]][endPos[1]] == player1.symbol
        )

    }
    fun checkWinConditions():Boolean {
        if (Board.matrix[0].contains(player1.symbol) ||
                player2.army.isEmpty()) {
            println("White Wins!")
            return true
        }
        if (Board.matrix[7].contains(player2.symbol) ||
                player1.army.isEmpty()) {
            println("Black Wins!")
            return true
        }
        return false
    }
    fun turn(player: Player) {
        for (i in player.army) i.enPassant = false
        while (true) {
            val input = getInput(player)
            if (inRegex(input)) {
                if (!pawnAtPosition(player, input)) {
                    println("No ${player.color} pawn at ${input[0]}${input[1]}")
                    continue
                } else if (isForwardMove(player, Board.startPos, Board.endPos)) break
                else if (isCapture(player, Board.startPos, Board.endPos)) break
                else if (isEnPassant(player, Board.startPos, Board.endPos)) break
                else {
                    println("Invalid input")
                    continue
                }
            } else if (input == "exit") {
                gameOver = true
                break
            } else println("Invalid input")
        }

        if (!gameOver) move(player, Board.startPos, Board.endPos)
    }
    fun battle() {
        getName(player1)
        getName(player2)
        startSetup(player1, player2)
        loop@while (true) {
            for (player in listOf(player1, player2)) {
                Board.printBoard()
                if (checkWinConditions() || noPossibleMoves(player)) break@loop
                turn(player)
                if (gameOver) break@loop
            }
        }
        println("Bye!")


    }

}