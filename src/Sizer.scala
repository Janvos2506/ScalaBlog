import scala.io._
import scala.actors._
import Actor._

// START:loader
object PageLoader {
  def getPageSize(url : String) = Source.fromURL(url).mkString.length
  def getLinks(url : String) = Source.fromURL(url).mkString.length*2
}
// END:loader
object Sizer extends App {
  val urls = List("http://www.amazon.com/",
    "http://www.twitter.com/",
    "http://www.google.com/",
    "http://www.cnn.com/" )

  // START:time
  def timeMethod(method: () => Unit) = {
    val start = System.nanoTime
    method()
    val end = System.nanoTime
    println("Method took " + (end - start)/1000000000.0 + " seconds.")
  }
  // END:time

  // START:sequential
  def getPageSizeSequentially() = {
    for(url <- urls) {
      println("Size for " + url + ": " + PageLoader.getPageSize(url))
    }
  }
  // END:sequential

  // START:concurrent
  def getPageSizeConcurrently() = {
    val caller = self

    for(url <- urls) {
      actor { caller ! (url, PageLoader.getPageSize(url)) }
      actor { caller ! ("link", PageLoader.getLinks(url)) }
    }

    for(i <- 1 to urls.size*2) {
      receive {
        case (url, size) =>
          println("Size for " + url + ": " + size)
        case ("link", size) =>
          println("Size for " + size)
      }
    }
  }
  // END:concurrent

  // START:script
  println("Sequential run:")
  timeMethod { getPageSizeSequentially }

  println("Concurrent run")
  timeMethod { getPageSizeConcurrently }
  // END:script
}

