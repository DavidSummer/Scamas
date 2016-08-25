// Copyright (C) Dialectics 2016
package org.scamas.core

import akka.actor.{Actor,ActorRef}


/**
  * Goal which can be active or achieved
  * @param name of the goal
  *
  * */
sealed class Goal(name : String)
case class ActiveGoal(name: String) extends Goal(name)
case class AchievedGoal(name: String) extends Goal(name)


/*
 * Preferences are immutable state of mind for agents
 */
abstract class Preferences

/**
  * Solution for a multiagent problem
 */
abstract class PartialSolution
abstract class Solution(result: List[PartialSolution])


/**
  * Agent which is reactive
  * @param addresses
  *
  * */
abstract class Agent(val addresses: Map[String, ActorRef]) extends Actor {
  this:Preferences=>
  val acqquaintances = addresses map {_.swap}
}

/**
  * Agent which is proactive
  * @param addresses
  * TODO override def preStart(): Unit = super.preStart()
  * */
abstract class ProactiveAgent(override val addresses: Map[String, ActorRef],
                              goals: Seq[Goal]) extends Agent(addresses) {
  this:Preferences=>
}

/**
  * Multiagent system
  * @param addresses
  *
  * */
abstract class MultiagentSystem(var addresses: Map[String, ActorRef])
  extends Actor{
  this:Solution=>
  var boss: ActorRef = _//ref to the main application
}
