package com.datio.demo


import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.datio.demo.actor._

import scala.concurrent.duration._

/**
  * Created by AlvaroNav on 7/3/17.
  */
object AkkaApp extends App{

  implicit val timeout = Timeout(5 seconds)
  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val log = Logging(system, getClass)

  log.info(s">>> ${getClass.getName()} Initialising actors")
  //val managerActor = system.actorOf(Props[ManagerActor], "manager")
  lazy val managerActor = system.actorOf(Props(new ManagerActor()), "managerActor")
  //Actor's first call with message
  val future = managerActor ? AddJobRequest(Job("core-data","new"))


/*
  future onSuccess {
    case response: String =>
      log.info(s">>> ---------------------------------")
      log.info(s">>> ${response}")
      log.info(s">>> ---------------------------------")
    case None => log.error("error")
  }*/

 //system.terminate()
}
