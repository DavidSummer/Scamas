// Copyright (C) Dialectics 2016
package org.scamas.smp


import org.scamas.core.{Start, Stop}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._



/**
  * Main app to test distributed Gale-Shapley solver
  * */
object Main {
  val debug= true
  val system = ActorSystem("SMPDemonstration")//The Actor system
  val TIMEOUTVALUE=50 seconds// default timeout of a run
  implicit val timeout = Timeout(TIMEOUTVALUE)// TODO make default Duration.Inf

  /**
    * Run the Actor system with the following default dispatcher and write a CSV file nbwWorkers,speedup
    */
  def main(args: Array[String]): Unit ={

    val x1= new Individual("x1",Array("y2","y1","y3"))
    val x2= new Individual("x2",Array("y3","y2","y1"))
    val x3= new Individual("x3",Array("y1","y3","y2"))
    val men= List(x1,x2,x3)
    val y1= new Individual("y1",Array("x2","x1","x3"))
    val y2= new Individual("y2",Array("x3","x2","x1"))
    val y3= new Individual("y3",Array("x1","x3","x2"))
    val women= List(y1,y2,y3)
    // Launch a new system
    val solver= system.actorOf(Props(classOf[Solver],men, women), name = "solver")

    // The current thread is blocked and it waits for the solver to "complete" the Future with it's reply.
    val future = solver ? Start
    val result = Await.result(future, timeout.duration).asInstanceOf[Stop]
    println("That's all folk !")

  }
}
