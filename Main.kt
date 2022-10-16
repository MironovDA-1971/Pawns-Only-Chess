package chess

fun main() {
    val player = MutableList(2){""}
    println(" Pawns-Only Chess")
    println("First Player's name:")
    player[0] = readln() // white
    println("Second Player's name:")
    player[1] = readln() // black

    val upRovDelim = "  +---+---+---+---+---+---+---+---+"
    val dwnRovDelim ="    a   b   c   d   e   f   g   h"
    val delim =  mutableListOf("B", "W", "|", " ")
    val board = MutableList(8) { MutableList(8){""} }

    val msgError = MutableList(4){""}
    msgError[0] = "No white pawn at "
    msgError[1] = "No black pawn at "
    msgError[2] = "Invalid Input"
    msgError[3] = ""

    val winMessage = MutableList(3){""}
    winMessage[0] = "White Wins!"
    winMessage[1] = "Black Wins!"
    winMessage[2] = "Stalemate!"

    var endGame: Int
    var outError: String

    val pList = MutableList(2) { MutableList(4){0} }

    startBoard(delim, board)
    // testStartBoard(delim, board)
    printBoard(upRovDelim, dwnRovDelim, delim, board)

    var numPlayer = 0
    val patternEx = "exit"
    //
    val regex= Regex("[a-hA-H][1-8][a-hA-H][1-8]")
    val regexExit= Regex(patternEx)
    //
    do {
        // Test on Stalemate!
        endGame = sTalemate(numPlayer, delim, board)
        if (endGame == 5) break

        println("${player[numPlayer]}\'s turn:")
        /*
        else printBoard(upRovDelim, dwnRovDelim, delim, board)
        */


        // read Turns
        val turns = readln()
        if (!regex.matches(turns) && !regexExit.matches(turns)) {
            println( msgError[2])
        } else if(turns != "" && turns != patternEx) {
            outError = checkPlayerTurn(numPlayer, delim, turns, turnList(turns), msgError, board, pList)
            if (outError == "") {
                // displaying a chessboard with a move
                setBoard(delim, turnList(turns), board)
                printBoard(upRovDelim, dwnRovDelim, delim, board)
                previousMove(numPlayer, turnList(turns), pList)
                //println(pList)
                numPlayer = (numPlayer + 1) % 2
            } else {
                println(outError)
            }
        }
        endGame = lastLineOrNothing(delim, board)
        // println("endGame = $endGame")
        if(endGame != 0) break
    } while(!regexExit.matches(turns))

    when (endGame) {
        1 -> println(winMessage[0])
        2 -> println(winMessage[1])
        5 -> println(winMessage[2])
    }
    println("Bye!")
}