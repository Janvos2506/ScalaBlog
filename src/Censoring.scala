import scala.io.Source

object Censoring extends App {
  val text = new Text("Shoot this dude he is eating Darn")
  println(text.getWordCount)
  println(text.getCensoredText)
}

trait wordCounter {
  val stringList: List[String]
  def getWordCount : Int = {
    stringList.foldLeft(0)((sum, value) => sum + 1)
  }
}

trait censorable {
  private var wordsToCensor = getCensorship
  private def getCensorship: Map[String, String] = {
    var map = Map(""->"")
    val fileLines = Source.fromFile("censorship.txt").getLines()
    for(line <- fileLines) {
      val pair = line.split(":")
      map += (pair(0) -> pair(1))
    }
    map
  }

  def tryGetValue(words: Map[String, String], str: String): String = {
    if(words.contains(str))
      return words.get(str).mkString("")
    str
  }

  def censor(stringList: List[String]): String = {
    val censored = stringList.foldLeft("")((sum, value) => {sum + tryGetValue(wordsToCensor, value) + " "})
    //Map returns a List of Nothing object?? need to convert it to strings, then convert the strings to one big string
    censored
  }
}

class Text(val text: String) extends wordCounter with censorable {
  //Regex returned een iterator en geen list met matches, hier moest ik dan nog toList op aanroepen
  //https://stackoverflow.com/questions/41031166/scala-extends-vs-with
  override val stringList: List[String] = ("([\\w]+)".r findAllIn text).toList

  def getCensoredText : String = {
    val censored = censor(stringList)
    return censored;
  }
}