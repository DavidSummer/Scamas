// Copyright (C) Dialectics 2016
package org.scamas.smp

import akka.actor.{ActorRef, FSM}
import org.scamas.core._

/**
  * Profile for the SMP
  * @param name of the individual
  * @param Profile list of the potential partners from the preferred partner to the least preferred
  */
class Individual(override val myName: String, val preferences: Array[String]) extends Profil(myName){
  /**
  * Method to evaluate the regret of the individual
  * The regret of an individual is 0 with the first partner in the list,
  * 1 with the second partner in the list, ...
  * @param potentialPartner to evaluate
   */
  def regret(potentialPartner: String) : Int = {
    val position= preferences.indexOf(potentialPartner)
    if (position==(-1)) preferences.length
    else position
  }
}


/**
  * States the negotiator can be in
  * TODO Do not assume the warranty of delivery for message
  */
sealed trait NegotiatorState
case object Free extends NegotiatorState
case object Married extends NegotiatorState

/**
  * Internal mutable internal state of mind for the SMP agents
  */
final class StateOfMind(var partner: String, var concessionLevel: Int) extends Tuple2[String,Int](partner,concessionLevel)
object StateOfMind {
  val PHANTOM: String = "phantom"
}


/**
  * Proactive agent for the stable marriage problem
  * TODO abstract away the notion of goal
  * @param name of the agent
  * @param addresses
  */
class Proposer(profile: Individual) extends ProactiveAgent(profile) with FSM[NegotiatorState,StateOfMind]  {
  import StateOfMind._
  val debug = true

  // A negotiator  begins its existence as free with a concession level of 0
  startWith(Free,new StateOfMind(PHANTOM,0))

  // Either the proposer is free
  when(Free) {
    // If the proactive agent is triggered
    case Event(Start,stateOfMind) =>
      acquaintances.addresses(profile.preferences(0)) ! Propose
      stay using stateOfMind
    // If an acceptance is recieved
    case Event(Accept,stateOfMind) =>
      val partner= acquaintances.names(sender)
      val concessionLevel =profile.regret(acquaintances.names(sender))
      goto(Married) using new StateOfMind(partner,concessionLevel)
  }

  // Or the proposer is married
  when(Married){
    //If the proposer received a divorce
    case Event(Divorce,stateOfMind) =>
      val concessionLevel = stateOfMind.concessionLevel+1
      val potentialPartner = profile.preferences(concessionLevel)
      acquaintances.addresses(potentialPartner) ! Propose
      goto(Free) using  new StateOfMind(PHANTOM,concessionLevel)
  }

  whenUnhandled {
    case Event(m: Move, s) =>
      defaultReceive(m)
      stay
    case Event(e, s) =>
      println(profile.myName+": ERROR  unexpected event {} in state {}/{}", e, stateName, s)
      stay
  }

  //  Associates actions with a transition instead of with a state and even, e.g. debugging
  onTransition {
    case _ -> _ =>  if (debug) println(profile.myName+" get married with {} at concessionLevel {}", nextStateData.partner, nextStateData.concessionLevel)
  }

  // Finally starting it up using initialize, which performs the transition into the initial state and sets up timers (if required).
  initialize()
}

/**
  * Reactive agent for the stable marriage problem
  * @param name of the agent
  * @param addresses
  */
class Disposer(profile: Individual) extends Agent(profile) with FSM[NegotiatorState,StateOfMind] {
  import StateOfMind._

  val debug=true
  // A negotiator  begins its existence as free with a concession level of 0
  startWith(Free,new StateOfMind(PHANTOM,0))

  // Either the agent is free
  when(Free) {
    case Event(Propose,_) =>
      val proposer= acquaintances.names(sender)
      goto(Married) using new StateOfMind(proposer,profile.regret(proposer)) replying Accept
  }

  // Or the agent is married
  when(Married) {
    // The proposal is worst than the current regret
    case Event(Propose,stateOfMind) if profile.regret(acquaintances.names(sender)) < stateOfMind.concessionLevel =>
      stay replying Reject
    // The proposal is better than the current regret
    case Event(Propose,stateOfMind) if profile.regret(acquaintances.names(sender)) >= stateOfMind.concessionLevel =>
      val previousPartner= stateOfMind.partner
      acquaintances.addresses(previousPartner) ! Reject
      val newPartner = acquaintances.names(sender)
      stay using new StateOfMind(newPartner,profile.regret(newPartner))replying Accept
  }

  // Common code for both states
  whenUnhandled {
    case Event(m: Move, s) =>
      defaultReceive(m)
      stay
    case Event(e, s) =>
      println(profile.myName+": ERROR  unexpected event {} in state {}/{}", e, stateName, s)
      stay
  }

  //  Associates actions with a transition instead of with a state and even, e.g. debugging
  onTransition {
    case _ -> _ =>  if (debug) println(profile+" get married with {} at concessionLevel {}", nextStateData.partner, nextStateData.concessionLevel)
  }

  // Finally starting it up using initialize, which performs the transition into the initial state and sets up timers (if required).
  initialize()
}