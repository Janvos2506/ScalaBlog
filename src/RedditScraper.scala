import java.io.{File, PrintWriter}
import scala.actors._
import Actor._
import scala.io.Source

object RedditScraper extends App {
  val pages = List("subreddits/leagueoflegends.html", "subreddits/gaming.html","subreddits/sports.html","subreddits/thenetherlands.html")

  timeMethod {printPostTilesSequentialy}
  timeMethod {getPageSizeConcurrently}

  def printPostTilesSequentialy(): Unit = {
    pages.foreach(page => RedditPostCounter.printTitleWordCount(page))
  }
  def timeMethod(method: () => Unit) = {
    val start = System.nanoTime
    method()
    val end = System.nanoTime
    println("Method took " + (end - start)/1000000000.0 + " seconds.")
  }

  def getPageSizeConcurrently(): Unit = {
    val caller = self

    for(page <- pages) {
      actor { caller ! (page, RedditPostCounter.printTitleWordCount(page)) }
    }
  }
}


object RedditPostCounter {
  val TAG = 0
  val INNERTEXT = 1
  val CLASSES = 2
  val ID = 3

  private def convertMapToJson(counts: Map[String, Int]): String = {
    "{\n" + counts.map(c => "\"" + c._1 + "\": " + c._2 + ", \n").mkString("") + "}"
  }
  private def getSubredditName(url: String): String = {
    url.split("subreddits/")(1).split(".html")(0)
  }
  def printTitleWordCount(url: String): Unit = {
    val titles = getRedditPostTitles(url)
      .mkString(" ")
      .split(" ")
      .map(s => removeString(s, "\r"))
      .map(s => removeString(s, "\n"))
      .map(s => removeString(s, "-"))
      .map(s => removeString(s, "/"))
      .filter(s => s != "")
    val counts = countWords(titles)
    val pw = new PrintWriter(new File("results/"+getSubredditName(url) + ".json"))
    pw.write(convertMapToJson(counts))
    pw.close
  }
  private def countWords(words: Array[String]) : Map[String, Int] = {
    var counts = Map("" -> 0)
    words.foreach(word => {
      if(counts.contains(word))
        counts = counts + (word -> (counts(word) + 1))
      else
        counts = counts + (word -> 1)
    })
    counts
  }
  private def removeString(str: String, query: String) : String = {
    val splittedString = str.split(query)
    if(splittedString.nonEmpty)
      return splittedString(0)
    else
      return ""
  }
  private def getRedditPostTitles(url: String) = {
    parseHTML( loadPage(url))
      .filter(element => hasClass(element.asInstanceOf[Array[Any]], "title"))
      .filter(element => hasSelector(element.asInstanceOf[Array[Any]], "a"))
      .map(element => element.asInstanceOf[Array[Any]])
      .map(element => element(INNERTEXT))
  }
  private def hasClass(element: Array[Any], className: String): Boolean = element(CLASSES).asInstanceOf[Array[String]].contains(className)
  private def hasId(element: Array[Any], id: String): Boolean = element(CLASSES).asInstanceOf[Array[String]].contains(id)
  private def hasSelector(element: Array[Any], id: String): Boolean = element(TAG).asInstanceOf[String] == id
  private def loadPage(url: String) : String = Source.fromFile(url).mkString
  private def getNextTag(html: String, index: Int, tag: Array[String] = Array("")): Array[String] = {
    val nextChar = html(index+1)
    if(nextChar == '>') {
      val innerText = getStringUntil(html, index+1, '<')
      Array(tag(0), innerText)
    }
    else {
      getNextTag(html, index+1, Array(tag(0) + nextChar))
    }
  }
  private def getStringUntil(html: String, index: Int, ending: Char, str:String = "") : String = {
    if(html.length <= index+1)
      return html
    val nextChar = html(index + 1)
    if(nextChar == ending)
      str
    else {
      getStringUntil(html, index+1, ending, str + nextChar)
    }
  }
  private def extractProperties(element: Any, attr: String): Array[String] = {
    val idsRaw = element.asInstanceOf[String].split(attr +"=\"")
    if(idsRaw.length > 1) idsRaw(1).split("\"")(0).split(" ")
    else Array()
  }
  private def parseHTML(html: String) : Array[Any] = {
    html.indices.map(index => if(html(index) == '<') getNextTag(html, index))
      .filter(_ != (())) //Remove boxedUnits
      .map(element => element.asInstanceOf[Array[Any]])
      .map(element => Array(element(0), element(1), extractProperties(element(0), "class"))) //Add the classes
      .map(element => Array(element(0), element(1), element(2), extractProperties(element(0), "id"))) //Add the id(s)
      .map(element => Array(element(0).asInstanceOf[String].split(" ")(0),element(1),element(2),element(3))) //Remove all the fluff from the selector
      .toArray
  }
}