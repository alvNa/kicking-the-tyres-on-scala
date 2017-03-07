package com.datio.demo.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import com.datio.demo.service.MetronomeService

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

object ManagerActor {
  def props(): Props = Props(classOf[ManagerActor])
}

/**
  * This actor manages others actors for getting plans and materials
  * before ordering the building of the Death Star.
  */
class ManagerActor(val metronomeService: MetronomeService = MetronomeService) extends Actor with ActorLogging {

  protected lazy val persistenceJobActor = context.actorOf(Props[PersistenceJobActor], "PersistenceJobActor")
  implicit val resolveTimeout = Timeout(5 seconds)
  import context._

  def receive: Receive = {
    case addJobRequest: AddJobRequest => handleAddJobRequest(addJobRequest.job)

  }

  private def handleAddJobRequest(job: Job) = {
    log.info(s"${getClass.getName()} Add job ...")
    //val sendAux = sender
    val future = persistenceJobActor ? GetJobRequest("core-data")

    future match {
      case job : Job => {
        val f1: Future[String] = metronomeService.add(job)
        val f2: Future[String] = persistenceJobActor ? AddJobRequest(job) collect {
          case x : String => x
        }

        val p = Promise[Future[String]]()
        p.success(f1).success(f2)

        p.future pipeTo sender
      }
      case x: JobNotFoundResponse => log.info("error $x")
        sender ! x
    }
  }

}