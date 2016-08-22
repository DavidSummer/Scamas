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

abstract class StateOfMind

abstract class PartialSolution
abstract class Solution(result: List[PartialSolution]) extends StateOfMind


/**
  * Agent which is reactive
  * @param name of the agent
  * @param acquaintances
  *
  * */
abstract class Agent(var acquaintances: Map[String, ActorRef]) extends Actor {
  this:StateOfMind=>
}

/**
  * Agent which is reactive
  * @param name of the agent
  * @param acquaintances
  *
  * */
abstract class ProactiveAgent(override var acquaintances: Map[String, ActorRef],
                              goals: Seq[Goal]) extends Agent(acquaintances) {
  this:StateOfMind=>
}

/**
  * Multiagent system
  * @param name of the agent
  * @param acquaintances
  *
  * */
abstract class MultiagentSystem(override var acquaintances: Map[String, ActorRef])
  extends Agent(acquaintances) {
  this:Solution=>
  var boss: ActorRef = _//ref to the main application

}

