/**
* University of Skövde | Big data programming | Assignment 2: Spark
*
* HOW TO DEPLOY:
* http://spark.apache.org/docs/latest/quick-start.html#self-contained-applications
*/

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/** SQL FUNCTIONS FOR DATA FRAMES e.g. avg("..."), orderBy(desc("...")).
* http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.functions$ */
import org.apache.spark.sql.functions._


object Comments {
	def main(arg: Array[String]) = {
		/** SPARK CONFIGURATION
		* http://spark.apache.org/docs/latest/configuration.html
		*
		* The app name is shown in the Spark web UI: localhost:4040
		 */
		val conf = new SparkConf().setAppName("Reddit comments")


		/** Create Spark context in case you're working with RDDs. */
		val sc = new SparkContext(conf)
		// You can silence the console output a bit...
		// sc.setLogLevel("WARN")


		/** Create Spark session for working with data frames. */
		val spark = SparkSession.builder().getOrCreate()
		// Needed import if you want to use $ for columns e.g. $"author"...
		import spark.implicits._



		// PATH TO DATA. Check if it is correct.
		val dataPath = "/home/big-data/datasets/reddit/2010"
		// PATH TO STOP WORDS FILE.
		val swPath = "res/stopwords.txt"
		// val dataPath = "projects/advprogramming/bigdata/spark/assignment.2/data/reddit/2010"
		// val swPath = "projects/advprogramming/bigdata/spark/assignment.2/_template_/part2/reddit/res/stopwords.txt"

		val df = spark.read.parquet(dataPath) // read all parquet files which are stored in dataPath directory
		// df.printSchema
		// df.show

		// --------------------------------------------------------------
		// TASK 1
		// --------------------------------------------------------------

		val hours = df.select(hour(from_unixtime($"created_utc")).as("hour")).groupBy($"hour").count.orderBy("hour")
		// selected created_utc column
		// converted created_utc value from string to date
		// exstracted hour from date
		// renamed it to "hour"
		// grouped by hour
		// counted it
		hours.write.csv("commentcount")
		// The results are written to commentcount.csv

		// --------------------------------------------------------------
		// TASK 2
		// --------------------------------------------------------------

		val ups = df.select($"subreddit", $"ups").groupBy("subreddit").agg(avg("ups").as("average")).orderBy(desc("average"))
		// selected subreddit and ups columns
		// grouped by subreddit
		// applied avg aggregate function to ups column
		// ordered by average
		ups.write.csv("upsaverage")
		// The results are written to upsaverage.csv

		// --------------------------------------------------------------
		// TASK 3
		// --------------------------------------------------------------

		val highest = df.select(weekofyear(from_unixtime($"created_utc")).as("week"), $"ups", $"body").orderBy(desc("ups")).groupBy($"week").agg(first("ups").as("ups"), first("body").as("comment")).orderBy($"week")
		// selected created_utc, ups and body columns
		// extracted a week number created_utc
		// renamed it to "week"
		// ordered by ups
		// grouped by week
		// applied "first" aggregate function to ups and body columns
		// order by week
		highest.write.parquet("weekheights")
		// The results are written to weekheights parquet file

		// --------------------------------------------------------------
		// TASK 4
		// --------------------------------------------------------------

		val stop = sc.textFile(swPath).collect
		val count = df.select($"body").explode("body", "word")((line: String) => line.replace(".", " ").replace("-", " ").replace(",", " ").split(" +").filter(!stop.contains(_))).groupBy("word").count.orderBy(desc("count")).limit(200)
		// selected body column
		// excluded ".", ",", "-" and all stop words from each comment
		// exploded each comment - replaced each record by several records which contain one word out of the comment
		// grouped by each word
		// count rows in each group
		// ordered by count
		// limited by 200 lines
		count.write.csv("wordcount")
		// The results are written to wordcount csv file

		// --------------------------------------------------------------
		sc.stop
	}
}
