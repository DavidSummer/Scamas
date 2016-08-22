// Copyright (C) Dialectics 2016

package org.scamas.smp

import akka.actor.ActorRef
import org.scamas.core._

class Marriage(husband: String, val wife: String) extends PartialSolution
class Matching(var result: List[Marriage]) extends Solution(result){
  //Add a marriage
  def add(m: Marriage) : Matching = { new Matching(m :: result)}

}

/**
  * Solver for the stable marriage problem
  * @param name of the agent
  * @param acquaintances
  */
class Solver(override var acquaintances: Map[String, ActorRef],
             var men: Seq[Individual],
             var women: Seq[Individual])
              extends MultiagentSystem(acquaintances) {
  this: Matching =>




  /**
    * Method invoked when a message is received by the solver
    */
  override def receive: Receive = {
    // When the works should be done
    case Start => {
      boss= sender// note the reference to the application
      men.foreach{ case man =>
        acquaintances(man.name) ! Start
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