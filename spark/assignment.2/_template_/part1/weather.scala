// ================ PRE-PROCESSING CODE =================
// Anything necessary to get the answers later.

// val sc = new SparkContext()

case class Weather(val date: String, val lat: Int, val lon: Int, val elev: Int, val wind: Int, val squal: String, val temp: Int, val tqual: String, val pres: Int, val pqual: String)

def parse(line: String): Weather = {
  val date = line.substring(15, 23)
  val lat = line.substring(28, 34).toInt
  val lon = line.substring(34, 41).toInt
  val elev = line.substring(46, 51).toInt
  val wind = line.substring(65, 69).toInt
  val squal = line.substring(69, 70)
  // val temp = line.substring(87, 92).toDouble/10
  val temp = line.substring(87, 92).toInt
  val tqual = line.substring(92, 93)
  val pres = line.substring(99, 104).toInt
  val pqual = line.substring(104, 105)
  Weather(date, lat, lon, elev, wind, squal, temp, tqual, pres, pqual)
}

// val rdd = sc.textFile("projects/advprogramming/bigdata/spark/assignment.2/data/weather/2012/").map(parse(_))

val rdd = sc.textFile("/home/big-data/datasets/weather/2012/").map(parse(_))
// rdd.first

val sweden = rdd.filter(_.lat != 99999).filter(_.lon != 999999).filter(_.elev != 9999).filter(_.wind != 9999).filter(_.temp != 9999).filter(_.pres != 99999).filter(_.squal == "1").filter(_.tqual == "1").filter(_.pqual == "1").filter(_.lat > 55382).filter(_.lat < 69047).filter(_.lon > 11391).filter(_.lon < 24034)

// import org.apache.spark.storage.StorageLevel._
// rdd.persist(MEMORY_AND_DISK)
// sweden.persist(MEMORY_AND_DISK)

sweden.persist()

/** QUESTION 1
* How many measures (records) are there?
*
* Answer: 1514332
*/

val count = sweden.count()
// count: Long = 1514332

/** QUESTION 2
* Which is the highest elevation? Give latitude and longitude.
* Answer:
*
* - Elevetaion = 1146
* - Latitude = 67,917
* - Longitude = 18,600
*/

val maxelev = sweden.reduce((a, b) => if (a.elev > b.elev) a else b)
// maxelev: Weather = Weather(20121213,67917,18600,1146,20,1,-124,1,10215,1)
maxelev.elev
// res1: Int = 1146
maxelev.lat
// res2: Int = 67917
maxelev.lon
// res3: Int = 18600

/** QUESTION 3
* Which is the average wind speed in the country?
*
* Answer: 37.76
*/

val avgwind = sweden.map(_.wind).mean
// avgwind: Double = 37.75939292044281

/** QUESTION 4
* Which is the average temperature per month? Sort from heights to lowest.
*
* Answer:
* - January = ???
* - February = ???
* - March = ???
* ...
*/

val avgtemp = sweden.groupBy(_.date.substring(4, 6).toInt).map(r => (r._1, r._2.map(_.temp).reduce(_ + _).toDouble/(10*r._2.size))).sortBy(_._2, false).take(12)
// avgtemp: Array[(Int, Double)] = Array((7,14.879008141329217), (8,14.116240164998855), (6,11.319174477736237), (9,9.910506724485618), (5,8.090628200469126), (10,4.469014933270621), (11,2.2523962374530453), (4,2.1900031114895846), (3,1.4266762959396566), (1,-3.8700073608515937), (2,-5.66338860838438), (12,-6.23192790618755))

sweden.toDF().write.parquet("2010-parquet")
