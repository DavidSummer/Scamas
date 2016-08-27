package org.scamas.core

import akka.actor.ActorRef

/**
  *  All possible moves performed by the agent
  */
class Move
case class Inform(addresses: Map[String,ActorRef]) extends Move// inform the agent about their social environment
case object AcknowledgeInform extends Move // acknowledge the informance
case object Start extends Move// start the computation
case class Stop(content: PartialSolution) extends  Move// agent is stopped and report the computation result
case class Halt(content: Solution) extends  Move// computation is stopped and report the computation result
