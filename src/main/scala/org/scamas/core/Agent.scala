// Copyright (C) Dialectics 2016
package org.scamas.core

import akka.actor.{Actor, ActorRef}

/**
  * Goal which can be active or achieved
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
abstract class Profil(val myName: String)

/**
  * Class to represents the bijections between the name and the adresses of the agents
  * Acquaitances corresponds to the Internal mutable internal state of mind for an agent
  *
  */
final class Acquaintances(val addresses: Map[String, ActorRef], val names: Map[ActorRef, String])
  extends Tuple2[Map[String, ActorRef],Map[ActorRef,String]](addresses,names)
object Acquaintances {
  val NOADRRESSES = Map[String, ActorRef]()
  val NONAMES = Map[ActorRef, String]()
}


/**
  * Agent which is reactive
  * @param addresses
  *
  * */
abstract class Agent(val profil: Profil) extends Actor{
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
  * @param addresses
  * TODO override def preStart(): Unit = super.preStart()
  * */
abstract class ProactiveAgent(profil: Profil) extends Agent(profil){
}


/**
  * Solution for a multiagent problem
  */
abstract class PartialSolution
abstract class Solution(result: List[PartialSolution])

/**
  * Multiagent system
  * @param addresses
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
