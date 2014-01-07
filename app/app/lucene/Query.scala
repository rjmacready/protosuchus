package lucene

import java.io.File
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.search.ScoreDoc


object Query {
  
	def makeQuery(strquery : String) : Array[(ScoreDoc, IndexSearcher)] = {
	  var idxDir = new File("testblobs\\idx")
	  
	  val field = "contents"
	  
	  val reader : IndexReader = DirectoryReader.open(FSDirectory.open(idxDir));
	  
	  val searcher : IndexSearcher = new IndexSearcher(reader);
	  
	  val analyzer : Analyzer = new StandardAnalyzer(Version.LUCENE_46);
	  
	  val parser = new QueryParser(Version.LUCENE_46, field, analyzer)
	  
	  val query = parser.parse(strquery)
	  	  
	  val results = searcher.search(query, 100)
	  
	  val hits = results.scoreDocs
	  
	  val collection = for {
	    hit <- hits
	  } yield (hit, searcher);

	  return collection;
	}

	/*private def test {
	  var docDir = new File("testblobs\\docs")
	  var idxDir = new File("testblobs\\idx")
	  
	  val field = "contents"
	  
	  val reader : IndexReader = DirectoryReader.open(FSDirectory.open(idxDir));
	  val searcher : IndexSearcher = new IndexSearcher(reader);
	  val analyzer : Analyzer = new StandardAnalyzer(Version.LUCENE_46);
	  
	  
	  val parser = new QueryParser(Version.LUCENE_46, field, analyzer)
	  
	  val query = parser.parse("coq static analysis")
	  	  
	  val results = searcher.search(query, 100)
	  println("totalHits: " + results.totalHits)
	  val hits = results.scoreDocs
	  
	  val end = Math.min(results.totalHits, 100)
	  
	  for(i <- 0 until end) {
		val hit = hits(i)
	    val score = hit.score  
	    val doc = searcher.doc(hit.doc)
		val path = doc.get("path")
				
		  
		println("doc at " + path + " (" + score + ")")
	  }
	  
	  reader.close()
	  
	  println("hello search!")
	}
*/
}