// val lines = ssc.socketTextStream("localhost", 9999)
// val lines = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_ONLY)
// val lines = ssc.textFileStream("input_directory")

// actorStream

// map, flatMap, repartition, union, filter

// count, reduce, countByValue

// for key-value pairs

// cogroup
// This example used the cogroup method to find the words with the same length from two DStreams.
// val lines1 = ssc.socketTextStream("localhost", 9999)
// val words1 = lines1 flatMap {line => line.split(" ")}
// val wordLenPairs1 = words1 map {w => (w.length, w)}
// val wordsByLen1 = wordLenPairs1.groupByKey
// val lines2 = ssc.socketTextStream("localhost", 9998)
// val words2 = lines2 flatMap {line => line.split(" ")}
// val wordLenPairs2 = words2 map {w => (w.length, w)}
// val wordsByLen2 = wordLenPairs2.groupByKey
// val wordsGroupedByLen = wordsByLen1.cogroup(wordsByLen2)

// join
// val lines1 = ssc.socketTextStream("localhost", 9999)
// val words1 = lines1 flatMap {line => line.split(" ")}
// val wordLenPairs1 = words1 map {w => (w.length, w)}
// val lines2 = ssc.socketTextStream("localhost", 9998)
// val words2 = lines2 flatMap {line => line.split(" ")}
// val wordLenPairs2 = words2 map {w => (w.length, w)}
// val wordsSameLength = wordLenPairs1.join(wordLenPairs2)

// left, right and full outer joins

// groupByKey
// The groupByKey method groups elements within each RDD of a DStream by their keys. It returns a new
// DStream by applying groupByKey to each RDD in the source DStream.
// val lines = ssc.socketTextStream("localhost", 9999)
// val words = lines flatMap {line => line.split(" ")}
// val wordLenPairs = words map {w => (w.length, w)}
// val wordsByLen = wordLenPairs.groupByKey

// reduceByKey
// The reduceByKey method returns a new DStream of key-value pairs, where the value for each key is obtained by
// applying a user-provided reduce function on all the values for that key within an RDD in the source DStream.
// The following example counts the number of times a word occurs within each DStream micro-batch.
// val lines = ssc.socketTextStream("localhost", 9999)
// val words = lines flatMap {line => line.split(" ")}
// val wordPairs = words map { word => (word, 1)}
// val wordCounts = wordPairs.reduceByKey(_ + _)

// saveAsTextFiles, saveAsObjectFiles, saveAsHadoopFiles, print

// transform
// The transform method returns a DStream by applying an RDD => RDD function to each RDD in the source
// DStream. It takes as argument a function that takes an RDD as argument and returns an RDD. Thus, it gives
// you direct access to the underlying RDDs of a DStream.
// This method allows you to use methods provided by the RDD API, but which do not have equivalent
// operations in the DStream API. For example, sortBy is a transformation available in the RDD API, but not in
// the DStream API. If you want to sort the elements within each RDD of a DStream, you can use the transform
// method as shown in the following example.
// val lines = ssc.socketTextStream("localhost", 9999)
// val words = lines.flatMap{line => line.split(" ")}
// val sorted = words.transform{rdd => rdd.sortBy((w)=> w)}
// The transform method is also useful for applying machine and graph computation algorithms to data
// streams. The machine learning and graph processing libraries provide classes and methods that operate at
// the RDD level. Within the transform method, you can use the API provided by these libraries.

// updateStateByKey
// The updateStateByKey method allows you to create and update states for each key in a DStream of key-
// value pairs. You can use this method to maintain any information about each distinct key in a DStream.
// For example, you can use the updateStateByKey method to keep a running count of each distinct word
// in a DStream, as shown in the following example.
// val lines = ssc.socketTextStream("localhost", 9999)
// val words = lines.flatMap{line => line.split(" ")}
// val wordPairs = words.map{word => (word, 1)}
// // create a function of type (xs: Seq[Int], prevState: Option[Int]) => Option[Int]
// val updateState = (xs: Seq[Int], prevState: Option[Int]) => {
//   prevState match {
//     case Some(prevCount) => Some(prevCount + xs.sum)
//     case None => Some(xs.sum)
//   }
// }
// val runningCount = wordPairs.updateStateByKey(updateState)

// foreachRDD
// resultDStream.foreachRDD { rdd =>
//   rdd.foreachPartition { iterator =>
//     val dbConnection = ConnectionPool.getConnection()
//     val statement = dbConnection.createStatement()
//     iterator.foreach { element =>
//       val result = statement.executeUpdate("...")
//       // check the result
//     }
//     statement.close()
//     // return connection to the pool
//     dbConnection.close()
//   }
// }

// window
// val lines = ssc.socketTextStream("localhost", 9999)
// val words = lines flatMap {line => line.split(" ")}
// val windowLen = 30
// val slidingInterval = 10
// val window = words.window(Seconds(windowLen), Seconds(slidingInterval))
// val longestWord = window reduce { (word1, word2) =>
//   if (word1.length > word2.length) word1 else word2 }
// longestWord.print()

// countByWindow, countByValueAndWindow, reduceByWindow,reduceByKeyAndWindow
