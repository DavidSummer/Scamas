package org.scamas.core

import akka.actor.ActorRef

/**
  *  All possible moves performed by the agent
  */
class Move
case class Inform(addresses: Map[String,ActorRef]) extends Move// inform the agent about their social environment
case object AcknowledgeInform extends Move // acknowledge the informance
case object Start// start the computation
case class Halt(content: Solution) extends  Move// computation is stopped and report the computation result
case object Trigger extends Move// Trigger the goal of a proactive agent
case class Stop(content: PartialSolution) extends  Move// agent has reached its goal
