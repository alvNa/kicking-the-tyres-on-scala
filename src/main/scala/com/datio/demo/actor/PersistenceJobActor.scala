package com.datio.demo.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.datio.demo.service.PostgreSqlService

import scala.concurrent.Future

object PersistenceJobActor {
  def props(): Props = Props(new PersistenceJobActor())
}

class PersistenceJobActor(val postgreSqlService: PostgreSqlService = PostgreSqlService) extends Actor with ActorLogging {
  import context._

  def receive = {
    case addJobRequest : AddJobRequest => handleAddJobRequest(addJobRequest.job)
    case getJobRequest : GetJobRequest => handleGetJobRequest(getJobRequest.id)
    case x => log.info(s"[PersistenceJobActor] Unexpected message $x")
  }

  private def handleAddJobRequest(job : Job):Future[String] = {
     postgreSqlService.add(job) pipeTo sender
  }

  private def handleGetJobRequest(jobName : String)  = {
    postgreSqlService.get(jobName) match {
      case Some(job) => job pipeTo sender
      case None => sender ! JobNotFoundResponse()
    }
  }

}