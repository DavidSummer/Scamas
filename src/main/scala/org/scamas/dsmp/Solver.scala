// Copyright (C) Dialectics 2016

package org.scamas.dsmp

import akka.actor.{ActorRef, Props}
import scala.collection.mutable.Queue


import org.scamas.core._
import org.scamas.smp._

/**
  * Solver for the stable marriage problem
  * @param name of the agent
  * @param addresses
  */
class Solver(var men: Seq[Individual],
             var women: Seq[Individual])
              extends MultiagentSystem {
  this: Matching =>
  /**
    * Method invoked when a message is received by the solver
    */
  override def receive: Receive = {
    // When the works should be done
    case Start => {
      boss= sender// note the reference to the application
      //TODO
      // The negotiators
      var proactiveAgents : Queue[ActorRef]= Queue()
      women.foreach{ case woman =>
        val disposer = context.actorOf(Props(classOf[Disposer],woman), name =woman.myName)
        super.addAcquaintances(woman.myName,disposer)

      }
      men.foreach{ case man =>
        val proposer = context.actorOf(Props(classOf[Proposer],man), name =man.myName)
        addAcquaintances(man.myName,proposer)
        proactiveAgents += proposer
      }
      acquaintances.addresses.foreach{ case (name,agent) =>
          agent ! Inform(acquaintances.addresses)

      }
      for (proactiveAgent<- proactiveAgents){
        proactiveAgent ! Start
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