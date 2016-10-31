import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.types._

val config = new SparkConf().setAppName("My Spark SQL app")
val sc = new SparkContext(config)
val sqlContext = new SQLContext(sc)

val linesRDD = sc.textFile("path/to/employees.csv")
val rowsRDD = linesRDD.map{row => row.split(",")}
  .map{cols => Row(cols(0), cols(1).trim.toInt, cols(2))}

val schema = StructType(List(
  StructField("name", StringType, false),
  StructField("age", IntegerType, false),
  StructField("gender", StringType, false)
))

val employeesDF = sqlContext.createDataFrame(rowsRDD,schema)

// val sqlContext = new org.apache.spark.sql.hive.HiveContext (sc)
//
// // create a DataFrame from parquet files
// val parquetDF = sqlContext.read
// .format("org.apache.spark.sql.parquet")
// .load("path/to/Parquet-file-or-directory")
//
// // create a DataFrame from JSON files
// val jsonDF = sqlContext.read
// .format("org.apache.spark.sql.json")
// .load("path/to/JSON-file-or-directory")
//
// // create a DataFrame from a table in a Postgres database
// val jdbcDF = sqlContext.read
// .format("org.apache.spark.sql.jdbc")
// .options(Map(
// "url" -> "jdbc:postgresql://host:port/database?user=<USER>&password=<PASS>",
// "dbtable" -> "schema-name.table-name"))
// .load()
//
// // create a DataFrame from a Hive table
// val hiveDF = sqlContext.read
// .table("hive-table-name")
