// Copyright (C) Dialectics 2016

package org.scamas.smp

import akka.actor.{ActorRef, Props}
import org.scamas.core._

class Marriage(husband: String, val wife: String) extends PartialSolution
class Matching(var result: List[Marriage]) extends Solution(result){
  //Add a marriage
  def add(m: Marriage) : Matching = { new Matching(m :: result)}

}

/**
  * Solver for the stable marriage problem
  * @param name of the agent
  * @param addresses
  */
class Solver(override var addresses: Map[String, ActorRef],
             var men: Seq[Individual],
             var women: Seq[Individual])
              extends MultiagentSystem(addresses) {
  this: Matching =>
  /**
    * Method invoked when a message is received by the solver
    */
  override def receive: Receive = {
    // When the works should be done
    case Start => {
      boss= sender// note the reference to the application
      //TODO
      men.foreach{ case man =>
        val proposer =context.actorOf(Props(classOf[Proposer]), man.name)
        proposer ! Start
      }
    }

    // When a work is done
    case Stop(marriage: Marriage) => {
      this.add(marriage)
      if (result.size==men.size){
       boss !  Halt(this)
      }
    }
  }
}