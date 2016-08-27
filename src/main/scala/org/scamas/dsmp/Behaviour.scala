// Copyright (C) Dialectics 2016
package org.scamas.dsmp

import akka.actor.{ActorRef, FSM}
import org.scamas.core._
import org.scamas.smp.Individual


/**
  * States the negotiator can be in
  */
sealed trait NegotiatorState
case object Free extends NegotiatorState
case object Married extends NegotiatorState

/**
  * Internal mutable internal state of mind for the SMP agents
  * @param partner is the current one
  * @param concessionLevel where the partner is in the preference list
  */
final class StateOfMind(var partner: String, var concessionLevel: Int) extends Tuple2[String,Int](partner,concessionLevel)
// Initial stateOfMind
object StateOfMind {
  val PHANTOM: String = "phantom"// initial partner
}


/**
  * Proactive agent for the stable marriage problem
  * @param profile of the agent
  */
class Proposer(profile: Individual) extends ProactiveAgent(profile) with FSM[NegotiatorState,StateOfMind]  {
  import StateOfMind._
  val debug = true

  // A negotiator  begins its existence as free with a concession level of 0
  startWith(Free,new StateOfMind(PHANTOM,0))

  // Method to determinate the target of a proposal
  def biddingStrategy(stateOfMind: StateOfMind) : String =  profile.preferences(stateOfMind.concessionLevel)

  // Either the proposer is free
  when(Free) {
    // If the proactive agent is triggered
    case Event(Start,stateOfMind) =>
      acquaintances.addresses(biddingStrategy(stateOfMind)) ! Propose
      stay using stateOfMind
    // If an acceptance is received
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
      self ! Start
      goto(Free) using  new StateOfMind(PHANTOM,concessionLevel)
  }

  // Whatever the state is
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
    case _ -> _ =>  if (debug) println(profile.myName+" get married with "+nextStateData.partner+" at concessionLevel "+ nextStateData.concessionLevel)
  }

  // Finally starting it up using initialize, which performs the transition into the initial state and sets up timers (if required).
  initialize()
}

/**
  * Reactive agent for the stable marriage problem
  * @param profile of the agent
  */
class Disposer(profile: Individual) extends Agent(profile) with FSM[NegotiatorState,StateOfMind] {
  import StateOfMind._

  val debug=true

  // Method to determinate the acceptance of a proposal
  def acceptabilityCriteria(sender: ActorRef, stateOfMind: StateOfMind) = {
    if (profile.regret(acquaintances.names(sender)) < stateOfMind.concessionLevel) true
    false
  }


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
    case Event(Propose,stateOfMind) if acceptabilityCriteria(sender,stateOfMind: StateOfMind)=>
      stay replying Reject
    // The proposal is better than the current regret
    case Event(Propose,stateOfMind) if (!acceptabilityCriteria(sender,stateOfMind: StateOfMind)) =>
      val previousPartner= stateOfMind.partner
      acquaintances.addresses(previousPartner) ! Reject
      val newPartner = acquaintances.names(sender)
      stay using new StateOfMind(newPartner,profile.regret(newPartner))replying Accept
  }

  // Whatever the state is
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
    case _ -> _ =>  if (debug) println(profile.myName+" get married with "+nextStateData.partner+" at concessionLevel "+ nextStateData.concessionLevel)
  }

  // Finally starting it up using initialize, which performs the transition into the initial state and sets up timers (if required).
  initialize()
}