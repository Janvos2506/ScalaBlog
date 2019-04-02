object TicTacToe extends App {

  val line1 = new Line(List("X","O","X"))
  val line2 = new Line(List("X","O","X"))
  val line3 = new Line(List("O","X","O"))
  val board = new Board(List(line1, line2, line3))
  board.getStatus()
}


class Line(val cells: List[String]) {
  def print(): Unit = {
    println(cells(0), cells(1), cells(2))
  }

  def isDominated: String = {
    val x = "XXX"
    val o = "OOO"
    val rowConcatted = cells(0)+cells(1)+cells(2)
    if(rowConcatted == x)
      return "X"
    if(rowConcatted == o)
      return "O"
    return ""
  }

  def isFilled: Boolean = {
    cells.foreach(c => {
      if(c == " ") {
        return false;
      }
    })
    return true;
  }
}

class Board(val lines: List[Line]) {

  val rows = lines
  val cols = lines.indices.map(i => {
    val cells = lines.map(row => row.cells(i))
    new Line(List(cells(0), cells(1), cells(2)))
  })

  val diagonals = List( new Line(List(rows(0).cells(0), rows (1).cells(1), rows (2).cells(2))),
                        new Line(List(rows(0).cells(2), rows (1).cells(1), rows (2).cells(0)))  )

  def getStatus(): Unit = {
    print()
    val empty = hasEmptySpots;
    if( empty == true) {
      return println("Game is not over yet!")
    } else {
      val winner = getWinner

      if(winner == "")
        println("It's a tie!")

      else
        println(winner + " won!")
    }
  }

  def getWinner: String = {
    var dominate = ""
    rows.foreach(row => {
      dominate = row.isDominated
      if(dominate != "")
        return dominate
    })

    cols.foreach(col => {
      dominate = col.isDominated
      if(dominate != "")
        return dominate
    })

    diagonals.foreach(diagonals => {
      dominate = diagonals.isDominated
      if(dominate != "")
        return dominate
    })
    return ""
  }

  def print(): Unit = {
    println("-----Rows-----")
    rows.foreach(f => f.print())
    println("-----Cols-----")
    cols.foreach(f => f.print())
    println("-----Diagonals-----")
    diagonals.foreach(f => f.print())
  }

  def hasEmptySpots: Boolean = {
    lines.foreach(line => {
      if(!line.isFilled)
        return true;
    })
    return false;
  }
}