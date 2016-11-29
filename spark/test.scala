val sc = new SparkContext()

val rdd = sc.textFile("logs")

// Actions - may take time
// rdd.first
// rdd.count

val windows = rdd.filter(_.contains("windows"))
windows.count

def getOs(row: String): String = if (row.contains("Windows")) "Windows" else "Linux"
def getMethod(row: String): String = if (row.contains("PUT")) "PUT" else "GET"

val rdd1 = rdd.map(r => (getOS(r), getMethod(r)))
val rdd1 = rdd.map(r => (getOS(r), 1))

rdd1.reduceByKey(_ + _).foreach(println)
