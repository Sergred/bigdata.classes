import org.apache.spark._
import org.apache.spark.streaming._

// val config = new SparkConf().setMaster("spark://host:port").setAppName("big streaming app")
// val batchInterval = 10
// The batch size can be as small as 500 milliseconds.
// val ssc = new StreamingContext(config, Seconds(batchInterval))
// Alternatively, if you already have an instance of the SparkContext class, you can use it to create an
// instance of the StreamingContext class.
// val sc = new SparkContext(conf)
// val ssc = new StreamingContext(sc, Seconds(batchInterval))

// Nothing really happens until the start method is called on an instance
// of the StreamingContext class. A Spark Streaming
// application begins receiving data after it calls the start method.
// ssc.start()

// The checkpoint method tells to periodically checkpoint data.
// It takes the name of a directory as an argument. For a production application, the
// checkpoint directory should be on a fault-tolerant storage system such as HDFS.
// ssc.checkpoint("path-to-checkpoint-directory")

object StreamProcessingApp {
  def main(args: Array[String]): Unit = {
    val interval = args(0).toInt
    val conf = new SparkConf()
    val ssc = new StreamingContext(conf, Seconds(interval))

    // add your application specific data stream processing logic here

    ssc.start()
    ssc.awaitTermination()
  }
}
