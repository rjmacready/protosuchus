package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import app.lucene.Query
import app.lucene.Indexer
import views.html.defaultpages.badRequest


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
  
}