import org.apache.spark._
import org.apache.spark.sql._

val config = new SparkConf().setAppName("My Spark SQL app")
val sc = new SparkContext(config)
val sqlContext = new SQLContext(sc)
// val hiveContext = new HiveContext(sc)

val resultSet = sqlContext.sql("SELECT count(1) FROM my_table")
// val resultSet = hiveContext.sql("SELECT count(1) FROM my_hive_table")
