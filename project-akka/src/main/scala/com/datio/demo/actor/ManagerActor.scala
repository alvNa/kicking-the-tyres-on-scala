package com.datio.demo.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import com.datio.demo.service.{MetronomeService, PostgreSqlService}

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

object ManagerActor {
  def props(): Props = Props(new ManagerActor(MetronomeService))

  //def props(): Props = Props(classOf[ManagerActor])
}

/**
  * This actor manages others actors for getting plans and materials
  * before ordering the building of the Death Star.
  */
class ManagerActor(val metronomeService: MetronomeService = MetronomeService) extends Actor with ActorLogging {

  implicit val resolveTimeout = Timeout(5 seconds)

  import context._

  var originalSender: Option[ActorRef] = None

  private def getPersistenceJobActor() = {
    context.child("PersistenceJobActor")
      .getOrElse(context.actorOf(Props(classOf[PersistenceJobActor], PostgreSqlService), "PersistenceJobActor"))
  }

  def receive: Receive = {
    case addJobRequest: AddJobRequest => handleAddJobRequest(addJobRequest.job)
    case jobNotFound: JobNotFoundResponse => {
      log.info("error $x")
      originalSender.get ! jobNotFound
    }
    case x =>
      log.info(s"$x")
  }

  private def handleAddJobRequest(job: Job) = {
    log.info(s"${getClass.getName()} Add job ...")
    val sendAux = sender

    //originalSender = Some(sender)

    val f1: Future[MetronomeJobStatus] = metronomeService.add(job.name)

    val f2 = for {
      metJobStatus:MetronomeJobStatus <- {
        log.info(s"Fut num 1")
        f1.mapTo[MetronomeJobStatus]
      }
      updMet:Unit <- {
        log.info(s"Fut num 2")
        val req = UpdateMetronomeRunRequest(job.name,metJobStatus.runId)
        (getPersistenceJobActor() ? req).mapTo[Unit]
      }
    } yield updMet

    f2 pipeTo sendAux
  }

}