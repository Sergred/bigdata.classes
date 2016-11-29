val sc = new SparkContext()

val df = spark.read.option("header", "true").option("inferSchema", "true").csv("/home/serg/projects/advprogramming/bigdata/r/vehicles.csv")

df.select("trany").show
df.select("trany").map(r => r(0).toString.split(" ")).show
df.select("trany").map(r => r(0).toString.split(" ")(0)).show
