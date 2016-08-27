// Copyright (C) Dialectics 2016
package org.scamas.core

import akka.actor.{Actor, ActorRef}

/**
  * TODO Goal which can be active or achieved
  * @param name of the goal
  *
  * */
sealed class Goal(name : String)
case class ActiveGoal(name: String) extends Goal(name)
case class AchievedGoal(name: String) extends Goal(name)

/*
 * Profile is an immutable state of mind for an agent
 * @param myName is the agent's name
 */
abstract class Profile(val myName: String)

/**
  * Acquaitances corresponds the social environment of an agent
  * @param addresses maps the names to the addresses
  * @param names maps the addresses to the names
  *
  */
final class Acquaintances(val addresses: Map[String, ActorRef], val names: Map[ActorRef, String])
  extends Tuple2[Map[String, ActorRef],Map[ActorRef,String]](addresses,names)

/**
  * Initial empty acquaintance
  */
object Acquaintances {
  val NOADRRESSES = Map[String, ActorRef]()
  val NONAMES = Map[ActorRef, String]()
}


/**
  * Agent which is reactive
  * @param profile of the agent
  *
  * */
abstract class Agent(val profile: Profile) extends Actor{
  import Acquaintances._
  var acquaintances: Acquaintances = new Acquaintances(NOADRRESSES,NONAMES)

  def updateAcquaintances(addresses: Map[String,ActorRef]) = {
    this.acquaintances=new Acquaintances(addresses, addresses map {_.swap})
  }

  def addAcquaintances(name: String, address: ActorRef) = {
    val addresses= acquaintances.addresses + (name -> address)
    this.acquaintances=new Acquaintances(addresses, addresses map {_.swap})
  }

  def defaultReceive(move: Move) = move match {
    case Inform(addresses) =>
      this.updateAcquaintances(addresses)
  }

}

/**
  * Agent which is proactive
  * @param profile of the agent
  * */
abstract class ProactiveAgent(profile: Profile) extends Agent(profile){
}

/**
  * Solution for a multiagent problem
  */
abstract class PartialSolution
abstract class Solution(result: List[PartialSolution])

/**
  * Multiagent system
  *
  * */
abstract class MultiagentSystem extends Actor{
  var boss: ActorRef = _//ref to the main application

  import Acquaintances._
  var acquaintances: Acquaintances = new Acquaintances(NOADRRESSES,NONAMES)

  def updateAcquaintances(addresses: Map[String,ActorRef]) = {
    this.acquaintances=new Acquaintances(addresses, addresses map {_.swap})
  }

  def addAcquaintances(name: String, address: ActorRef) = {
    val addresses= acquaintances.addresses + (name -> address)
    this.acquaintances=new Acquaintances(addresses, addresses map {_.swap})
  }


}
