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
    Thread.sleep(1500)
    if (jobName=="core-data"){
      None
    }
    else{
      Some(Future(Job(jobName,"from postgresql")))
    }
  }

  def add(job: Job): Future[String] = {
    logger.info(getClass + s"add ${job.name}")
    Thread.sleep(1000)
    Future(job.name)
  }
}