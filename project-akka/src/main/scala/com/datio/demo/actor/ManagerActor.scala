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
    //val sendAux = sender
    originalSender = Some(sender)
    val future = getPersistenceJobActor() ? GetJobRequest("core-data")
    /*val future2: Future[String] = metronomeService.add(job)
    val future3: Future[String] = getPersistenceJobActor() ? AddJobRequest(job) collect {
      case x: String => x
    }*/
   /* future andThen {
      case Success(x:Job) => {val f1: Future[String] = metronomeService.add(job)
        val f2: Future[String] = getPersistenceJobActor() ? AddJobRequest(job) collect {
          case x: String => x
        }

        val p = Promise[Future[String]]()
        p.success(f1).success(f2)

        p.future }
      case Failure(ex) => {
        Future.successful("")
      }
    } pipeTo sender*/
/*
    (future1,future2,future3) => (future1 =>

      ,f2,f3)*/
/*
    future collect {
      case job: Job => {


        val p = Promise[Future[String]]()
        p.success(f1).success(f2)
        p.future
      }
      case x: Any =>
        log.info("error $x")
      //  sender ! x
        Future.successful("")
    } pipeTo sender
    */

    future pipeTo sender
  }

}