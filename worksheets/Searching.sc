object Searching {
  //println("Welcome to the Scala worksheet")

// this might need some tweaking ...
	val query = new app.lucene.LQuery(".\\workspace\\protosuchus\\")
                                                  //> query  : app.lucene.LQuery = app.lucene.LQuery@53659d6f
	val result = query.makeQuery("scala")     //> result  : Array[(org.apache.lucene.search.ScoreDoc, org.apache.lucene.search
                                                  //| .IndexSearcher)] = Array((doc=87 score=0.51799524 shardIndex=-1,IndexSearche
                                                  //| r(StandardDirectoryReader(segments_2:7 _1(4.6):c108); executor=null)), (doc=
                                                  //| 88 score=0.5164172 shardIndex=-1,IndexSearcher(StandardDirectoryReader(segme
                                                  //| nts_2:7 _1(4.6):c108); executor=null)), (doc=86 score=0.4711383 shardIndex=-
                                                  //| 1,IndexSearcher(StandardDirectoryReader(segments_2:7 _1(4.6):c108); executor
                                                  //| =null)))
	
	val top = result.take(1)                  //> top  : Array[(org.apache.lucene.search.ScoreDoc, org.apache.lucene.search.In
                                                  //| dexSearcher)] = Array((doc=87 score=0.51799524 shardIndex=-1,IndexSearcher(S
                                                  //| tandardDirectoryReader(segments_2:7 _1(4.6):c108); executor=null)))
	
	
	val hit = top(0)._1                       //> hit  : org.apache.lucene.search.ScoreDoc = doc=87 score=0.51799524 shardInde
                                                  //| x=-1
	val searcher = top(0)._2                  //> searcher  : org.apache.lucene.search.IndexSearcher = IndexSearcher(StandardD
                                                  //| irectoryReader(segments_2:7 _1(4.6):c108); executor=null)
	
	
	
}