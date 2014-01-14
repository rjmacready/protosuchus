import java.io.File
import org.apache.pdfbox.pdfparser.PDFParser
import java.io.FileInputStream
import org.apache.pdfbox.util.PDFTextStripper
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.FileOutputStream
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.namefind.TokenNameFinderModel
import opennlp.tools.namefind.NameFinderME
import opennlp.tools.postag.POSModel
import opennlp.tools.chunker.ChunkerModel
import opennlp.tools.parser.ParserModel
import opennlp.tools.parser.ParserFactory
import opennlp.tools.cmdline.parser.ParserTool
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.chunker.ChunkerME


object NplBeta {
	
  val docsbasedir = ".\\workspace\\protosuchus\\testblobs\\docs\\"
                                                  //> docsbasedir  : String = .\workspace\protosuchus\testblobs\docs\
  val outbasedir = ".\\workspace\\protosuchus\\testblobs\\outs\\"
                                                  //> outbasedir  : String = .\workspace\protosuchus\testblobs\outs\
  
  val docsToAnalyse = List(new File(docsbasedir, "c.02.pdf")) //, new File(docsbasedir, "c.02.pdf"), new File(docsbasedir, "c.03.pdf"))
                                                  //> docsToAnalyse  : List[java.io.File] = List(.\workspace\protosuchus\testblob
                                                  //| s\docs\c.02.pdf)
  //for(pdfDoc <- docsToAnalyse) {
	val pdfDoc = docsToAnalyse(0)             //> pdfDoc  : java.io.File = .\workspace\protosuchus\testblobs\docs\c.02.pdf
  
	val pdfParser = new PDFParser(new FileInputStream(pdfDoc.getAbsolutePath()))
                                                  //> pdfParser  : org.apache.pdfbox.pdfparser.PDFParser = org.apache.pdfbox.pdfp
                                                  //| arser.PDFParser@2a41b68d
	pdfParser.parse()
	val pdfCd = pdfParser.getDocument()       //> pdfCd  : org.apache.pdfbox.cos.COSDocument = org.apache.pdfbox.cos.COSDocum
                                                  //| ent@47d9726f
	val pdfStripper = new PDFTextStripper()   //> pdfStripper  : org.apache.pdfbox.util.PDFTextStripper = org.apache.pdfbox.u
                                                  //| til.PDFTextStripper@2db118c5
	val pdfPddoc = new PDDocument(pdfCd)      //> pdfPddoc  : org.apache.pdfbox.pdmodel.PDDocument = org.apache.pdfbox.pdmode
                                                  //| l.PDDocument@63643de1
	val pdfText = pdfStripper.getText(pdfPddoc)
                                                  //> pdfText  : String = "2006-2-5 0
                                                  //| Retrospective and Prospective for
                                                  //| Unifying Theories of Programming
                                                  //| Eric Hehner
                                                  //| Department of Computer Science, University of Toronto,
                                                  //| Toronto ON M5S 2E4 Canada
                                                  //| hehner@cs.utoronto.ca
                                                  //| Abstract  This paper presents a personal account of developments leading 
                                                  //| to Unifying Theories of Programming, and some opinions about the 
                                                  //| direction the work should take in the future.  It also speculates on 
                                                  //| consequences the work will have for all of computer science.
                                                  //| 0  UTP and me
                                                  //| My introduction to formal methods was the book a Discipline of Programming 
                                                  //| [2] 
                                                  //| by Edsger Dijkstra in 1976.  I wrote a small contribution in a paper named 
                                                  //| 
                                                  //| do considered od [14] in that same year.  In that paper I proposed recursiv
                                                  //| e 
                                                  //| refinement as a way of composing programs, and a different way of generatin
                                                  //| g the 
                                                  //| sequence of approximations for loop semantics that is more g
                                                  //| Output exceeds cutoff limit.
  	
 	{
 		val modelsbasedir = ".\\workspace\\protosuchus\\testblobs\\models\\";
	 
	  val sentfile = new File(modelsbasedir, "en-sent.bin").getAbsolutePath()
	  val sentstream = new FileInputStream(sentfile)
	  val sentmodel = new SentenceModel(sentstream)
	 	val detector = new SentenceDetectorME(sentmodel)
	 	
	  val sentences = detector.sentDetect(pdfText)
	
	  println("result: " + sentences.length + " sentences.")
	  val tokfile = new File(modelsbasedir, "en-token.bin").getAbsolutePath()
	  val tokstream = new FileInputStream(tokfile)
	  val tokmodel = new TokenizerModel(tokstream)
	  val tokenizer = new TokenizerME(tokmodel)
	              
	  
	  val nmffile = new File(modelsbasedir, "en-ner-person.bin").getAbsolutePath()
	  val nmfstream = new FileInputStream(nmffile)
	  val nmfmodel = new TokenNameFinderModel(nmfstream)
	  val nameFinder = new NameFinderME(nmfmodel)
	  
	  val posmfile = new File(modelsbasedir, "en-pos-maxent.bin").getAbsolutePath()
	  val posmstream = new FileInputStream(posmfile)
		val posmmodel = new POSModel(posmstream)
		val chunkfile = new File(modelsbasedir, "en-chunker.bin").getAbsolutePath()
	  val chunkstream = new FileInputStream(chunkfile)
	  val chunkmodel = new ChunkerModel(chunkstream)
	    
	  val parserfile = new File(modelsbasedir, "en-parser-chunking.bin").getAbsolutePath()
	  val parserstream = new FileInputStream(parserfile)
	  val parsermodel = new ParserModel(parserstream)
	  val parser = ParserFactory.create(parsermodel)
	              
			
	  var i = 0;
		for(sent <- sentences) {
			val topParses = ParserTool.parseLine(sent, parser, 1)

			val parsesOutname = new File(outbasedir, pdfDoc.getName() + i + ".out").getAbsolutePath()
			i = i + 1
			val parsesStream = new FileOutputStream(parsesOutname)
			val sb = new StringBuffer()
			topParses(0).show(sb)
			Console.withOut(parsesStream)(println(sb.toString()))
			parsesStream.close()
	
			val posmpostagger = new POSTaggerME(posmmodel)
			val chunker = new ChunkerME(chunkmodel)
	
			val tokens = tokenizer.tokenizePos(sent)
			val words = for(tok <- tokens) yield sent.substring(tok.getStart(), tok.getEnd())
			//for(w <- words) {
			//	println(w)
			//}
			
			val tags = posmpostagger.tag(words)
			//println("tags")
			//for(t <- tags) {
			//	println(t)
			//}
			
			val chunked = chunker.chunk(words, tags)
			//for(c <- chunked) {
			//	println(c)
			//}
			
			//println("probs: " + posmpostagger.probs())
			//println("probs: " + chunker.probs());
			
			//println("topseq")
			val topseq = posmpostagger.topKSequences(words)
			val topseq2 = chunker.topKSequences(words, tags)
			//for (s <- topseq) {
			//	println(s)
			//}
			
			//for (((w, t), c) <- (words zip tags) zip chunked) {
			//		println(w + " ; " + t + " ; " + c)
			//}
			
			val names = nameFinder.find(words)
			//println("Sentence with " + tokens.length + " tokens, " + names.length + " names")
		}
	  nameFinder.clearAdaptiveData()
	  
	  parserstream.close()
	  chunkstream.close()
	  nmfstream.close()
	  tokstream.close()
	  sentstream.close()
 		
 		
 	}                                         //> result: 348 sentences.
                                                  //| Couldn't find parse for: I remember Butler Lampson saying that I should jus
                                                  //| t assume there's one extra variable, and get on with it.
                                                  //| Couldn't find parse for: In 1990, Theo Norvell was my PhD student, and he s
                                                  //| uggested parallel by merge as a way of defining parallel composition that i
                                                  //| s both implementable and insensitive to frame.
                                                  //| Couldn't find parse for: To refer to theories that talk about whether a com
                                                  //| putation terminates, and the result upon termination, we commonly use the w
                                                  //| ords “total correctness”, suggesting that nothing else is of interest.
                                                  //| Couldn't find parse for: 2006-2-5 Retrospective and Prospective for Unifyin
                                                  //| g Theories of Programming 4 When we say “There are a number of issues to di
                                                  //| scuss.”,
                                                  //| Couldn't find parse for: Knowing what II , assignment, conditional, and seq
                                                  //| uential composition refine is sufficient for proof of this refinement; we d
                                                  //| o not need any further theory
                                                  //| Output exceeds cutoff limit.
  	
	val pdfOutname = new File(outbasedir, pdfDoc.getName() + ".out").getAbsolutePath()
                                                  //> pdfOutname  : String = C:\scala\eclipse\.\workspace\protosuchus\testblobs\o
                                                  //| uts\c.02.pdf.out
	
	
	val pdfOut = new FileOutputStream(pdfOutname)
                                                  //> pdfOut  : java.io.FileOutputStream = java.io.FileOutputStream@2c3a9ea0
	Console.withOut(pdfOut)(println(pdfText))
	pdfOut.close()
	
	pdfPddoc.close()
	pdfCd.close()
  //}
}