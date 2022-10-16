package chess
import kotlin.math.abs

fun startBoard(delim: MutableList<String>, board: MutableList<MutableList<String>>) {
    for (i in 7 downTo 0){
        for (a in 0..7){
            when (i) {
                1 -> board[i][a] = delim[1]
                6 -> board[i][a] = delim[0]
                else -> board[i][a] = delim[3]
            }
        }
    }
}
/*
fun testStartBoard(delim: MutableList<String>, board: MutableList<MutableList<String>>) {
    for (i in 7 downTo 0){
        for (a in 0..7){
            if(i == 4 && a == 7) board[i][a] = delim[0]
            else if(i == 2 && a == 7) board[i][a] = delim[1]
            else board[i][a] = delim[3]
        }
    }
}
*/

fun setBoard(delim: MutableList<String>, tList: MutableList<Int>, board: MutableList<MutableList<String>>) {
    board[tList[3]][tList[2]] = board[tList[1]][tList[0]]
    board[tList[1]][tList[0]] = delim[3]
}

fun printBoard(upRovDelim: String, dwnRovDelim: String, delim: MutableList<String>, board: MutableList<MutableList<String>>) {
    for (i in 7 downTo 0){
        println(upRovDelim)
        print("${i+1} ${delim[2]}")
        for (a in 0..7){
            print(" ${board[i][a]} ${delim[2]}")
        }
        print("\n")
        if (i == 0) {
            println(upRovDelim)
            println(dwnRovDelim)
        }
    }
}

fun charToNum(char: Char): Int {
    return when(char.lowercase()){
        "a" -> 0
        "b" -> 1
        "c" -> 2
        "d" -> 3
        "e" -> 4
        "f" -> 5
        "g" -> 6
        "h" -> 7
        else -> -1
    }
}

fun turnList(turns: String): MutableList<Int> {
    return mutableListOf(
        charToNum(turns[0]),
        turns[1].digitToInt() - 1,
        charToNum(turns[2]),
        turns[3].digitToInt() - 1
    )
}

fun checkPlayerTurn(player: Int, delim: MutableList<String>, turns: String, tList: MutableList<Int>, msgError: MutableList<String>,
                    board: MutableList<MutableList<String>>, pList: MutableList<MutableList<Int>>): String {
    // println(pList)
    return if(board[tList[1]][tList[0]] != delim[1] && player == 0) msgError[0] + turns.substring(0,2)
    else if(board[tList[1]][tList[0]] != delim[0] && player == 1) msgError[1] + turns.substring(0,2)
    else if((board[tList[3]][tList[2]] == delim[0] ||
        board[tList[3]][tList[2]] == delim[1]) && tList[0] == tList[2]) msgError[2]

    else if ((tList[1] == 1 || tList[1] == 6) && (abs(tList[1] - tList[3]) !in 1..2)) {
        msgError[2]     // no more than 2 or 1 cell on the first move
    } else if ((tList[1] != 1 && tList[1] != 6) && (abs(tList[1] - tList[3]) != 1)) {
        msgError[2]     // no more than one cell on all other moves
    }
    else if (enPassant(player, tList, board, delim, pList)) msgError[2] // the move takes place on one line
    else if (player == 0 && (tList[3] - tList[1]) < 1) msgError[2]  // white pawn move
    else if (player == 1 && (tList[1] - tList[3]) < 1) msgError[2]  // back pawn move
    else  msgError[3]
}
// TODO()
fun enPassant(player: Int, tList: MutableList<Int>,
             board: MutableList<MutableList<String>>,
             delim: MutableList<String>, pList: MutableList<MutableList<Int>>): Boolean {
    var a = false
    val pDelim = if(player == 0) 1 else 0
    //println("board= ${board[tList[3]][tList[2]]} != delim.${delim[pDelim]}. && board= ${board[tList[3]][tList[2]]} != .${delim[3]}.")
    if (tList[0] != tList[2]) {
        a = !(board[tList[3]][tList[2]] != delim[pDelim] && board[tList[3]][tList[2]] != delim[3]
                && abs(tList[0] - tList[2]) == 1 && abs(tList[1] - tList[3]) == 1)
        //println("a= $a")
        if (a && ((player == 0 && tList[0] == 4) || (player == 1 && tList[0] == 3)) && (pList[player][2] == tList[0] && pList[player][3] == tList[1])) {
            if (board[tList[1]][tList[0] - 1] == delim[player] && tList[0] - 1 == tList[2]) {
                board[tList[1]][tList[0] - 1] = delim[3]
                a = false
            } else if (board[tList[1]][tList[0] + 1] == delim[player] && tList[0] + 1 == tList[2]) {
                board[tList[1]][tList[0] + 1] = delim[3]
                a = false
            } else a = true
        }
    }
    return a
}

fun previousMove(player: Int, tList: MutableList<Int>, pList: MutableList<MutableList<Int>>) {
        if (player == 0) pList[0] = tList
        else pList[1] = tList
}

fun lastLineOrNothing(delim: MutableList<String>, board: MutableList<MutableList<String>>): Int {
    var stop = 0
    var yW = 0
    var yB = 0

    if(delim[1] in board[7]) stop = 1
    if(delim[0] in board[0]) stop = 2

    for (i in 0..board.lastIndex) {
        if(delim[1] in board[i]) yW++
        if(delim[0] in board[i]) yB++
    }
    if (yW == 0) stop = 2
    else if (yB == 0) stop = 1

    return stop
}

 fun sTalemate(player: Int, delim: MutableList<String>, board: MutableList<MutableList<String>>): Int {
     var stop = 0
     var yW = 0
     var yB = 0
     var wCount = 0
     var bCount = 0
     // var flagW = ""

     for (i in 1 until board.lastIndex) {
         for (a in 0..board[i].lastIndex) {

             // flagW = if (a < board[i].lastIndex) board[i + 1][a + 1] else a
             if (player == 0) {

                 if (delim[1] == board[i][a]) wCount++
                 if ( a!= 0 && a < board[i].lastIndex &&
                     delim[1] == board[i][a] &&
                     delim[0] == board[i + 1][a] &&
                     delim[3] == board[i + 1][a - 1] &&
                     delim[3] == board[i + 1][a + 1]
                 ) yW++
                 else if ( a == 0 &&
                     delim[1] == board[i][a] &&
                     delim[0] == board[i + 1][a] &&
                     delim[3] == board[i + 1][1]
                 ) yW++
                 else if ( a == board[i].lastIndex &&
                     delim[1] == board[i][a] &&
                     delim[0] == board[i + 1][a] &&
                     delim[3] == board[i + 1][a - 1]
                 ) yW++
             }

             if (player == 1) {
                 if (delim[0] == board[i][a]) bCount++
                 if ( a!= 0 && a < board[i].lastIndex &&
                     delim[0] == board[i][a] &&
                     delim[1] == board[i + 1][a] &&
                     delim[3] == board[i - 1][a - 1] &&
                     delim[3] == board[i - 1][a + 1]
                 ) yB++
                 else if ( a == 0 &&
                     delim[0] == board[i][a] &&
                     delim[1] == board[i - 1][a] &&
                     delim[3] == board[i - 1][1]
                 ) yB++
                 else if ( a == board[i].lastIndex &&
                     delim[0] == board[i][a] &&
                     delim[1] == board[i - 1][a] &&
                     delim[3] == board[i - 1][a - 1]
                 ) yB++
             }
         }
     }
   // if (player == 0) println("wCount=$wCount yW=$yW")
   // else println("bCount=$bCount yB=$yB")
    if (wCount == yW && wCount != 0 || bCount == yB && bCount != 0 ) stop = 5
     return stop
 }
