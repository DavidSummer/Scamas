package org.scamas.core

/**
  *  All possible moves performed by the agent
  */
class Move
case object Start extends Move// start the computation
case class Stop(content: PartialSolution) extends  Move//// agent is stopped and report the computation result
case class Halt(content: Solution) extends  Move// computation is stopped and report the computation result
