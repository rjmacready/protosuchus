package app.lucene

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
import java.io.FileNotFoundException


class LQuery (root : String) {
  private var _root : File = null
  
  if(root != null) {
    _root = new File(root)
    if(!_root.exists()) {
      throw new FileNotFoundException(_root.getAbsolutePath())
    }
  }
  
  def makeQuery(strquery : String) : Array[(ScoreDoc, IndexSearcher)] = {
	  val idxDir = new File(_root, "testblobs\\idx")
	  
	  val field = "contents"
	  
	  val reader : IndexReader = DirectoryReader.open(FSDirectory.open(idxDir));
	  
	  val searcher : IndexSearcher = new IndexSearcher(reader);
	  
	  val analyzer : Analyzer = new StandardAnalyzer(Version.LUCENE_30);
	  
	  val parser = new QueryParser(Version.LUCENE_30, field, analyzer)
	  
	  val query = parser.parse(strquery)
	  	  
	  val results = searcher.search(query, 100)
	  
	  val hits = results.scoreDocs
	  
	  val collection = for {
	    hit <- hits
	  } yield (hit, searcher);
	
	  return collection;
	}
  
}

object Query extends LQuery(null)
