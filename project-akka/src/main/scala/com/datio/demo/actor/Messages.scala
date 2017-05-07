package com.datio.demo.actor


case class AddJobRequest(job: Job) {}

case class GetJobRequest(id: String) {}

case class AddJobResponse(id: String) {}

case class Job(name: String, value: String, status: String="") {}

case class JobNotFoundResponse(){}

case class UpdateMetronomeRunRequest(uuid: String, metronomeRunId: String){}

case class MetronomeJobStatus(//completedAt: Option[String] = None,
                              //createdAt: Option[String] = None,
                              jobId: String,
                              runId: String,
                              status: String
                              ) {}