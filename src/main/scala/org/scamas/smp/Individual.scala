// Copyright (C) Dialectics 2016
package org.scamas.smp

import akka.actor.{ActorRef, FSM}
import org.scamas.core.{Agent, ProactiveAgent, Start, StateOfMind}

class Individual(var name: String, var list: Array[String]) extends StateOfMind


/**
  * Some states the negotiator can be in
  */
sealed trait NegotiatorState
case object Free extends NegotiatorState
case object Married extends NegotiatorState

/**
  * passive agent for the stable marriage problem
  * TODO distinguish activa agent extending ProactiveAgent
  * @param name of the agent
  * @param acquaintances
  */
abstract class Negotiator(override var acquaintances: Map[String, ActorRef]) extends Agent(acquaintances) with FSM[NegotiatorState,Integer]  {
  this:Individual=>
  import context._

  // A negotiator  begins its existence as free with a concession level of 0
  startWith(Free,0)

  when(Free) {
    case Event(Start,_) =>
      goto(Free) //TODO
  }

  // When a chopstick is taken by a hakker
  // It will refuse to be taken by other hakkers
  // But the owning hakker can put it back
  when(Married) {
    case Event(Propose,_) =>
      //TODO
  }



}
