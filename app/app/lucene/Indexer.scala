package app.lucene

import java.io.File
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.store.Directory
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.util.Version
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.IndexWriter
import java.io.FileInputStream
import org.apache.lucene.document.Document
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.util.PDFTextStripper
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.lucene.document.StringField
import org.apache.lucene.document.LongField
import org.apache.lucene.document.TextField
import java.io.StringReader
import org.apache.lucene.document.Field
import org.apache.lucene.index.Term
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

object Indexer {
  
	def indexDirOrFile(dirOrFile : String) {
	  val docDir = new File(dirOrFile)
	  // TODO check if exists!
	  println("Indexing " + dirOrFile)
	  
	  val idxDir = new File("testblobs\\idx")
	  
	  val dir : Directory = FSDirectory.open(idxDir)
	  
	  val analyzer : Analyzer= new StandardAnalyzer(Version.LUCENE_46)
	  val iwc : IndexWriterConfig = new IndexWriterConfig(Version.LUCENE_46, analyzer)
	  
	  iwc.setOpenMode(OpenMode.CREATE_OR_APPEND)
	  
	  val writer = new IndexWriter(dir, iwc)
	  
	  // index stuff!
	  indexDocs(writer, docDir)
	  
	  writer.close()
	  
	  //println("hello from scala")
	}
	
	def indexDocs(writer : IndexWriter, docDirOrFile : File) {
	  //println("Indexing " + docDirOrFile.getAbsolutePath())
	  
	  if(docDirOrFile.canRead()) {
	    if(docDirOrFile.isDirectory()) {
	      val files = docDirOrFile.list();
	      files.foreach(file => indexDocs(writer, new File(docDirOrFile, file)))
	    } else {
	      
	      try {
	        val fis : FileInputStream = new FileInputStream(docDirOrFile)
	        
	        try {
	          
	          if(docDirOrFile.getName().endsWith(".pdf")) {
	        	  //println("* pdf indexing");
	        	  
	        	  try {
	        	    val doc = new Document()
	        	  
		        	  val pdfParser = new PDFParser(fis)
		        	  pdfParser.parse()
		        	  val cd = pdfParser.getDocument()
		        	  val stripper = new PDFTextStripper()
		        	  val pddoc = new PDDocument(cd)
		        	  val text = stripper.getText(pddoc)
		        	  pddoc.close()	        	  
		        	  cd.close()
		        	  
		        	  doc.add(new StringField("path", docDirOrFile.getPath(), Field.Store.YES))
		        	  doc.add(new LongField("modified", docDirOrFile.lastModified(), Field.Store.NO))
		        	  doc.add(new TextField("contents", new StringReader(text)))
		        	  
		        	  //println("* updating " + docDirOrFile.getPath());
		        	  writer.updateDocument(new Term("path", docDirOrFile.getPath()), doc)
	        	  }
	        	  
	          } else {	          
	        	  // NAIL!
	        	  return;
	            
	        	  //println("* regular indexing");
	        	  
		          val doc = new Document()
		          doc.add(new StringField("path", docDirOrFile.getPath(), Field.Store.YES))	          
		          doc.add(new LongField("modified", docDirOrFile.lastModified(), Field.Store.NO))	          
		          doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))))	
		          
		          //println("* updating " + docDirOrFile.getPath());
		          writer.updateDocument(new Term("path", docDirOrFile.getPath()), doc)
	          }
	        } finally {
	          fis.close()
	        }
	      }catch {
	        case ex : FileNotFoundException => return 
	      }
	      
	      
	    }
	  }
	}
}