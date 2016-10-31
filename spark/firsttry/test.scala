// val config = new SparkConf().setMaster("spark://host:port").setAppName("big app")
// val sc = new SparkContext(config)

val sc = new SparkContext()

// RDDs

// parallelize
// val xs = (1 to 10000).toList
// val rdd = sc.parallelize(xs)

// files
// val rdd = sc.textFile("hdfs://namenode:9000/path/to/file-or-directory")
// val rdd = sc.textFile("hdfs://namenode:9000/path/to/directory/*.gz")
// val rdd = sc.wholeTextFiles("path/to/my-data/*.txt")

// key-value pairs
// val rdd = sc.sequenceFile[String, String]("some-file")

// Operations - distributed and lazy

// map
// val lines = sc.textFile("...")
// val lengths = lines map { l => l.length}

// filter
// val lines = sc.textFile("...")
// val longLines = lines filter { l => l.length > 80}

// flatMap
// takes function which returns sequence and flatten it into new RDD
// val lines = sc.textFile("...")
// val words = lines flatMap { l => l.split(" ")}

// mapPartitions - takes iterator
// val lines = sc.textFile("...")
// val lengths = lines mapPartitions { iter => iter.map { l => l.length}}

// union, intersection, subtract, distinct, cartesian [product]
// val linesFile1 = sc.textFile("...")
// val linesFile2 = sc.textFile("...")
// val linesFromBothFiles = linesFile1.union(linesFile2)

// zip, zipWithIndex
// val alphabets = sc.parallelize(List("a", "b", "c", "d"))
// val alphabetsWithIndex = alphabets.zip

// groupBy - expensive operator, may shuffle the data
// case class Customer(name: String, age: Int, gender: String, zip: String)
// val lines = sc.textFile("...")
// val customers = lines map { l => {
//   val a = l.split(",")
//   Customer(a(0), a(1).toInt, a(2), a(3))
// }}
// val groupByZip = customers.groupBy { c => c.zip }

// keyBy
// The difference between groupBy and keyBy is that the second item in a returned pair is a collection of
// elements in the first case, while it is a single element in the second case.
// case class Person(name: String, age: Int, gender: String, zip: String)
// val lines = sc.textFile("...")
// val people = lines map { l => {
//  val a = l.split(",")
//  Person(a(0), a(1).toInt, a(2), a(3))
//  }
// }
// val keyedByZip = people.keyBy { p => p.zip}

// sortBy
// val numbers = sc.parallelize(List(3,2, 4, 1, 5))
// val sorted = numbers.sortBy(x => x, true)
// case class Person(name: String, age: Int, gender: String, zip: String)
// val lines = sc.textFile("...")
// val people = lines map { l => {
//  val a = l.split(",")
//  Person(a(0), a(1).toInt, a(2), a(3))
//  }
// }
// val sortedByAge = people.sortBy( p => p.age, true)

// pipe
// The pipe method allows you to execute an external program in a forked process. It captures the output of the
// external program as a String and returns an RDD of Strings.

// randomSplit
// The randomSplit method splits the source RDD into an array of RDDs. It takes the weights of the splits as input.
// val numbers = sc.parallelize((1 to 100).toList)
// val splits = numbers.randomSplit(Array(0.6, 0.2, 0.2))

// coalesce
// The coalesce method reduces the number of partitions in an RDD. It takes an integer input and returns a
// new RDD with the specified number of partitions.
// val numbers = sc.parallelize((1 to 100).toList)
// val numbersWithOnePartition = numbers.coalesce(1)
// The coalesce method should be used with caution since reducing the number of partitions reduces the
// parallelism of a Spark application. It is generally useful for consolidating partitions with few elements. For
// example, an RDD may have too many sparse partitions after a filter operation. Reducing the partitions may
// provide performance benefit in such a case.

// repartition
// The repartition method takes an integer as input and returns an RDD with specified number of partitions.
// It is useful for increasing parallelism. It redistributes data, so it is an expensive operation.
// The coalesce and repartition methods look similar, but the first one is used for reducing the number
// of partitions in an RDD, while the second one is used to increase the number of partitions in an RDD.
// val numbers = sc.parallelize((1 to 100).toList)
// val numbersWithOnePartition = numbers.repartition(4)

// sample
// The sample method returns a sampled subset of the source RDD. It takes three input parameters. The first
// parameter specifies the replacement strategy. The second parameter specifies the ratio of the sample size to
// source RDD size. The third parameter, which is optional, specifies a random seed for sampling.
// val numbers = sc.parallelize((1 to 100).toList)
// val sampleNumbers = numbers.sample(true, 0.2)

// keys, values

// mapValues
// val kvRdd = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3)))
// val valuesDoubled = kvRdd mapValues { x => 2*x }

// join
// The join method takes an RDD of key-value pairs as input and performs an inner join on the source and input
// RDDs. It returns an RDD of pairs, where the first element in a pair is a key found in both source and input RDD
// and the second element is a tuple containing values mapped to that key in the source and input RDD.
// val pairRdd1 = sc.parallelize(List(("a", 1), ("b",2), ("c",3)))
// val pairRdd2 = sc.parallelize(List(("b", "second"), ("c","third"), ("d","fourth")))
// val joinRdd = pairRdd1.join(pairRdd2)
// joinRdd.take(4) foreach println
// (b,(2,second))
// (c,(3,third))

// leftOuterJoin, rightOuterJoin, fullOuterJoin
// The leftOuterJoin method takes an RDD of key-value pairs as input and performs a left outer join on the
// source and input RDD. It returns an RDD of key-value pairs, where the first element in a pair is a key from
// source RDD and the second element is a tuple containing value from source RDD and optional value from
// the input RDD. An optional value from the input RDD is represented with Option type.
// val pairRdd1 = sc.parallelize(List(("a", 1), ("b",2), ("c",3)))
// val pairRdd2 = sc.parallelize(List(("b", "second"), ("c","third"), ("d","fourth")))
// val leftOuterJoinRdd = pairRdd1.leftOuterJoin(pairRdd2)
// leftOuterJoinRdd.take(4) foreach println
// (a,(1,None))
// (b,(2,Some(second)))
// (c,(3,Some(third)))

// subtractByKey
// The subtractByKey method takes an RDD of key-value pairs as input and returns an RDD of key-value pairs
// containing only those keys that exist in the source RDD, but not in the input RDD.
// val pairRdd1 = sc.parallelize(List(("a", 1), ("b",2), ("c",3)))
// val pairRdd2 = sc.parallelize(List(("b", "second"), ("c","third"), ("d","fourth")))
// val resultRdd = pairRdd1.subtractByKey(pairRdd2)

// groupByKey
// The groupByKey method returns an RDD of pairs, where the first element in a pair is a key from the source
// RDD and the second element is a collection of all the values that have the same key. It is similar to the
// groupBy method that we saw earlier. The difference is that groupBy is a higher-order method that takes as
// input a function that returns a key for each element in the source RDD. The groupByKey method operates on
// an RDD of key-value pairs, so a key generator function is not required as input.
// val pairRdd = sc.parallelize(List(("a", 1), ("b",2), ("c",3), ("a", 11), ("b",22), ("a",111)))
// val groupedRdd = pairRdd.groupByKey()
// The groupByKey method should be avoided. It is an expensive operation since it may shuffle data. For
// most use cases, better alternatives are available.

// reduceByKey
// The higher-order reduceByKey method takes an associative binary operator as input and reduces values with
// the same key to a single value using the specified binary operator.
// A binary operator takes two values as input and returns a single value as output. An associative operator
// returns the same result regardless of the grouping of the operands.
// The reduceByKey method can be used for aggregating values by key. For example, it can be used for
// calculating sum, product, minimum or maximum of all the values mapped to the same key.
// val pairRdd = sc.parallelize(List(("a", 1), ("b",2), ("c",3), ("a", 11), ("b",22), ("a",111)))
// val sumByKeyRdd = pairRdd.reduceByKey((x,y) => x+y)
// val minByKeyRdd = pairRdd.reduceByKey((x,y) => if (x < y) x else y)
// The reduceByKey method is a better alternative than groupByKey for key-based aggregations or merging.

// saveAsTextFile
// The saveAsTextFile method saves the elements of the source RDD in the specified directory on any
// Hadoop-supported file system. Each RDD element is converted to its string representation and stored as a
// line of text.
// val numbersRdd = sc.parallelize((1 to 10000).toList)
// val filteredRdd = numbersRdd filter { x => x % 1000 == 0}
// filteredRdd.saveAsTextFile("numbers-as-text")

// saveAsObjectFile
// The saveAsObjectFile method saves the elements of the source RDD as serialized Java objects in the
// specified directory.
// val numbersRdd = sc.parallelize((1 to 10000).toList)
// val filteredRdd = numbersRdd filter { x => x % 1000 == 0}
// filteredRdd.saveAsObjectFile("numbers-as-object")

// saveAsSequenceFile
// The saveAsSequenceFile method saves an RDD of key-value pairs in SequenceFile format. An RDD of key-
// value pairs can also be saved in text format using the saveAsTextFile.
// val pairs = (1 to 10000).toList map {x => (x, x*2)}
// val pairsRdd = sc.parallelize(pairs)
// val filteredPairsRdd = pairsRdd filter { case (x, y) => x % 1000 ==0 }
// filteredPairsRdd.saveAsSequenceFile("pairs-as-sequence")
// filteredPairsRdd.saveAsTextFile("pairs-as-text")

// persist
// val lines = sc.textFile("...")
// lines.persist()
// The persist method supports the following common storage options:
// •	 MEMORY_ONLY: When an application calls the persist method with the MEMORY_ONLY
// flag, Spark stores RDD partitions in memory on the worker nodes using deserialized
// Java objects. If an RDD partition does not fit in memory on a worker node, it is
// computed on the fly when needed.
// val lines = sc.textFile("...")
// lines.persist(MEMORY_ONLY)
// •	 DISK_ONLY: If persist is called with the DISK_ONLY flag, Spark materializes RDD
// partitions and stores them in a local file system on each worker node. This option
// can be used to persist intermediate RDDs so that subsequent actions do not have to
// start computation from the root RDD.
// •	 MEMORY_AND_DISK: In this case, Spark stores as many RDD partitions in memory as
// possible and stores the remaining partitions on disk.
// •	 MEMORY_ONLY_SER: In this case, Spark stores RDD partitions in memory as serialized
// Java objects. A serialized Java object consumes less memory, but is more CPU-
// intensive to read. This option allows a trade-off between memory consumption and
// CPU utilization.
// •	 MEMORY_AND_DISK_SER: Spark stores in memory as serialized Java objects as many
// RDD partitions as possible. The remaining partitions are saved on disk.

// Shared variables, broadcast variable, accumulators

// Current maximum - .reduce((a, b) => if (a > b) a else b)
// .reduceByKey((x, y) => x + y)
