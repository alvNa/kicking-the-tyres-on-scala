package com.datio.demo.service

import com.datio.demo.actor.Job
import org.apache.logging.log4j.LogManager

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PostgreSqlService extends PostgreSqlService {

}

class PostgreSqlService {
  private val logger = LogManager.getLogger(getClass())

  def get(jobName: String): Option[Future[Job]] = {
    logger.info(getClass + s"get $jobName")
    if (jobName=="no-core-data"){
      None
    }
    else{
      Some(
        Future.successful(Job(jobName,"from postgresql"))
      )
    }
  }

  def add(job: Job): Future[String] = {
    logger.info(getClass + s"add ${job.name}")
    Future.successful(job.status)
  }

  def updateMetronomeRunId(runId: String, metronomeRunId: Option[String]):Future[Unit] = {
    logger.info("update DB()")
    println("update DB()")
    Future.successful()
  }
}