import java.io.FileInputStream
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.sentdetect.SentenceDetectorME
import java.io.File
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.tokenize.TokenizerME
import org.apache.lucene.store.BaseDirectoryWrapper
import opennlp.tools.namefind.TokenNameFinderModel
import opennlp.tools.namefind.NameFinderME
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.chunker.ChunkerModel
import opennlp.tools.cmdline.chunker.ChunkerMETool
import opennlp.tools.chunker.ChunkerME
import opennlp.tools.parser.ParserModel
import opennlp.tools.parser.ParserFactory
import opennlp.tools.cmdline.parser.ParserTool
import opennlp.tools.cmdline.coref.CoreferencerTool


object NplAlpha {
  println("Welcome to the Scala worksheet")
  

  
  
  val analyse = "Intuitionistic type theory (also known as constructive type theory, or Martin-Lof type theory) is a type theory and an alternative foundation of mathematics based on the principles of mathematical constructivism. Intuitionistic type theory was introduced by Per Martin-Lof, a Swedish mathematician and philosopher, in 1972. Martin-Lof has modified his proposal a few times; his 1971 impredicative formulation was inconsistent as demonstrated by Girard's paradox. Later formulations were predicative. He proposed both intensional and extensional variants of the theory."
  
  
  val modelsbasedir = ".\\workspace\\protosuchus\\testblobs\\models\\";
 
  val sentfile = new File(modelsbasedir, "en-sent.bin").getAbsolutePath()
  val sentstream = new FileInputStream(sentfile)
  val sentmodel = new SentenceModel(sentstream)
 	val detector = new SentenceDetectorME(sentmodel)
 	
  val sentences = detector.sentDetect(analyse)

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
              
		
              
	for(sent <- sentences) {
		val topParses = ParserTool.parseLine(sent, parser, 1)
		topParses(0).show()

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
  
}