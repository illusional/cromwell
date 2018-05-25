package cromwell.engine.workflow.workflowstore

import cats.data.NonEmptyList
import cromwell.core.{WorkflowId, WorkflowSourceFilesCollection}
import cromwell.database.sql.tables.WorkflowStoreEntry.WorkflowStoreState.WorkflowStoreState
import cromwell.engine.workflow.workflowstore.SqlWorkflowStore.WorkflowSubmissionResponse

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait WorkflowStore {

  def initialize(implicit ec: ExecutionContext): Future[Unit]

  def abortAllRunning()(implicit ec: ExecutionContext): Future[Unit]

  def aborting(id: WorkflowId)(implicit ec: ExecutionContext): Future[Option[Boolean]]

  def stats(implicit ec: ExecutionContext): Future[Map[WorkflowStoreState, Int]]

  /**
    * Adds the requested WorkflowSourceFiles to the store and returns a WorkflowId for each one (in order)
    * for tracking purposes.
    */
  def add(sources: NonEmptyList[WorkflowSourceFilesCollection])(implicit ec: ExecutionContext): Future[NonEmptyList[WorkflowSubmissionResponse]]

  /**
    * Retrieves up to n workflows which have not already been pulled into the engine and sets their pickedUp
    * flag to true
    */
  def fetchStartableWorkflows(n: Int, cromwellId: String, heartbeatTtl: FiniteDuration)(implicit ec: ExecutionContext): Future[List[WorkflowToStart]]

  def writeWorkflowHeartbeats(workflowIds: List[WorkflowId])(implicit ec: ExecutionContext): Future[Int]

  def remove(id: WorkflowId)(implicit ec: ExecutionContext): Future[Boolean]

  def switchOnHoldToSubmitted(id: WorkflowId)(implicit ec: ExecutionContext): Future[Unit]
}
