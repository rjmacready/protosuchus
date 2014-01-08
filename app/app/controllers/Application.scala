package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import app.lucene.Query
import app.lucene.Indexer
import views.html.defaultpages.badRequest
import play.api.libs.iteratee.Iteratee
import play.api.libs.iteratee.Enumerator
import play.api.libs.concurrent.Promise
import org.joda.time.Seconds
import java.util.concurrent.TimeUnit
import concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }
	
  def playindex = Action {
    Ok(views.html.playindex("Your new application is ready."))
  }
  
  val queryForm = Form(
	  "strquery" -> nonEmptyText
  )
  
  def queryResult = Action { implicit request =>    
    queryForm.bindFromRequest.fold(
        errors => {
        	BadRequest(views.html.index()) 
        },
        querystr => {
        	val result = Query.makeQuery(querystr)
        	val top10 = result.take(30)
	    	Ok(views.html.queryResult(querystr, top10))
	    })    
  }
  
  val indexmeForm = Form(
      "indexme" -> nonEmptyText
  )
  
  def indexDirOrFile = Action { implicit request =>
    indexmeForm.bindFromRequest.fold(
        erros => {
          BadRequest(views.html.index()) 
        },
        indexme => {
          Indexer.indexDirOrFile(indexme)
          Redirect(routes.Application.index)
        }
    )
  }
  
  def statusFeed() = WebSocket.using[String] { implicit request =>
    var i = 0
  	val getval = { 
      i += 1
      println("beep")
      i.toString() 
    }
  	
  	val in = Iteratee.ignore[String]
  	val out = Enumerator.repeatM {
  	  Promise.timeout(getval, 1, TimeUnit.SECONDS)
  	}
  	
  	(in, out)
  }
  
  def indexing = Action { implicit request => 
    Ok(views.html.indexing(request))
  }
  
}