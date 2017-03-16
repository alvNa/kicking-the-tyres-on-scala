package com.datio.demo.service

import com.datio.demo.actor.Job

import scala.concurrent.Future
import org.apache.logging.log4j.LogManager
import scala.concurrent.ExecutionContext.Implicits.global


object MetronomeService extends MetronomeService {

}

class MetronomeService {
  private val logger = LogManager.getLogger(getClass())

  def get(jobName: String): Option[Future[Job]] = {
    logger.info(getClass + s"get $jobName")
    //Thread.sleep(4500)
    if (jobName=="no-core-data"){
      None
    }
    else{
      Some(Future.successful(Job(jobName,"from postgresql")))
    }
  }

  def add(job: Job): Future[String] = {
    logger.info(getClass + s"add ${job.name}")
    //Thread.sleep(4000)
    Future.successful(job.name)
  }
}
