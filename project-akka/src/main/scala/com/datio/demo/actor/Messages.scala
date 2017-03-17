package com.datio.demo.actor


case class AddJobRequest(job: Job) {}

case class GetJobRequest(id: String) {}

case class AddJobResponse(id: String) {}

case class Job(name: String, value: String) {}

case class JobNotFoundResponse()

