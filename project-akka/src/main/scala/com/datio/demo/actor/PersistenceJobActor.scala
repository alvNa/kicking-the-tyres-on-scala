package com.datio.demo.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import com.datio.demo.service.{MetronomeService, PostgreSqlService}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object PersistenceJobActor {
  def props(): Props = Props(new PersistenceJobActor(PostgreSqlService))
}

class PersistenceJobActor(val postgreSqlService: PostgreSqlService = PostgreSqlService) extends Actor with ActorLogging {
  import context._

  def receive = {
    case addJobRequest : AddJobRequest => handleAddJobRequest(addJobRequest.job)
    case getJobRequest : GetJobRequest => handleGetJobRequest(getJobRequest.id)
    case updateRequest : UpdateMetronomeRunRequest => updateMetronomeRunId(updateRequest)
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

  def updateMetronomeRunId(req: UpdateMetronomeRunRequest ) = {
    log.info(s"[PersistenceJobActor] update with ${req}")
    postgreSqlService.updateMetronomeRunId(req.uuid, Some(req.metronomeRunId)) pipeTo sender
    log.info(s"[PersistenceJobActor] updated")
  }

}