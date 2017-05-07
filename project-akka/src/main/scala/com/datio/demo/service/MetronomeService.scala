package com.datio.demo.service

import com.datio.demo.actor.{Job,MetronomeJobStatus}

import scala.concurrent.Future
import org.apache.logging.log4j.LogManager
import scala.concurrent.ExecutionContext.Implicits.global


object MetronomeService extends MetronomeService {

}

class MetronomeService {
  private val logger = LogManager.getLogger(getClass())

  def get(jobId: String): Option[Future[MetronomeJobStatus]] = {
    logger.info(getClass + s"get $jobId")
    //Thread.sleep(4500)
    if (jobId=="no-core-data"){
      None
    }
    else{
      //Future.successful(Job(jobName,"from postgresql"))
      val status = MetronomeJobStatus(jobId,"134","RUNNING")
      Some(Future.successful(status))
    }
  }

  def add(jobId: String): Future[MetronomeJobStatus] = {
    logger.info(getClass + s"add ${jobId}")
    //Thread.sleep(4000)
    //Future.successful(job.name)
    val randomId = "134"
    val status = MetronomeJobStatus(jobId,randomId,"RUNNING")
    Future.successful(status)
  }
}
