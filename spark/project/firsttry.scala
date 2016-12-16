import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
// import org.apache.spark._

import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext

object Streamer {

  case class Article(val website: String, val keywords: String, val id: Int, val date: String, val title: String, val body: String)

  def parse(data: String): Article = {
    val parsed = data.split("%%")
    Article(parsed(0), parsed(1), parsed(2).toInt, parsed(3), parsed(4), parsed(5))
  }

	def main(arg: Array[String]) = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("Article Streamer")

    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.sparkContext.setLogLevel("WARN")

    // Checkpoint is needed for stateful transformations
		// Spark should have write permissions on given folder
		ssc.checkpoint("checkpoint")

    // val dstream = ssc.textFileStream("../../../data/pool/")
    val dstream = ssc.textFileStream("/home/serg/projects/advprogramming/data/")

    // val data = dstream.window(Seconds(1), Seconds(1))

    val cleared = dstream.map(parse)
    cleared.map(r => (1, 1)).reduceByKeyAndWindow(_ + _, Seconds(1)).print

    ssc.start
    ssc.awaitTermination
	}
}
