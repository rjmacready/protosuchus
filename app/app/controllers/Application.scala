package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import lucene.Query


object Application extends Controller {
	val queryForm = Form(
	  "strquery" -> nonEmptyText
	)

  def index = Action {
    Ok(views.html.index())
  }
  
  def queryResult = Action { implicit request =>    
    queryForm.bindFromRequest.fold(
        errors => {
        	BadRequest(views.html.index()) 
        },
        querystr => {
        	val result = Query.makeQuery(querystr)
        	val top10 = result.take(10)
	    	Ok(views.html.queryResult(querystr, top10))
	    })    
  }

}