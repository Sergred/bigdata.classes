val sc = new SparkContext()

// val rdd = sc.wholeTextFiles("home/serg/projects/advprogramming/bigdata/data/")

val rdd = sc.wholeTextFiles("www.bbc.com/news/")

// rdd.first

def clear(data:(String, String)):(String, String, String, String, String) = {
  val meta = data._1.split("/").last.split("-")
  if (meta.length == 1) ("", "", "", "", "") else {
    // val parsed = scala.xml.XML.loadString(data._2.replaceAll("&", "&amp;"))
    val parsed = scala.xml.parsing.XhtmlParser(scala.io.Source.fromString(data._2.))
    val title = for {
      el <- parsed \\ "h1"
      if (el \ "@class").text == "story-body__h1"
    } yield el.text
    val date = for {
      el <- parsed \\ "li"
      if (el \ "@class").text == "mini-info-list__item"
    } yield (el \ "div" \ "@data-datetime").text
    val article = for {
      el <- parsed \\ "div"
      if (el \ "@class").text == "story-body__inner" && (el \ "@property").text == "articleBody"
    } yield el.text
    (meta.dropRight(1).mkString(" "), meta.last, title(0), date(0), article(0))
  }
}

val cleared = rdd.map(r => clear(r)).filter(!_._1.isEmpty)
cleared.first
