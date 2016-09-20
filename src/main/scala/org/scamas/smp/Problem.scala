// Copyright (C) Dialectics 2016
package org.scamas.smp

import org.scamas.core.{Profile, PartialSolution ,Solution}

/**
  * immutable Profile an agent for the SMP
  * @param myName of the individual
  * @param preferences list of the potential partners from the preferred partner to the least preferred
  */
class Individual(override val myName: String, val preferences: Array[String]) extends Profile(myName){
  /**
    * Evaluate the regret of the individual
    * The regret of an individual is 0 with the first partner in the list,
    * 1 with the second partner in the list, ...
    * @param potentialPartner to evaluate
    */
  def regret(potentialPartner: String) : Int = {
    val position= preferences.indexOf(potentialPartner)
    if (position==(-1)) preferences.length
    else position
  }

  /**
    * Returns true if  a and b are rational and a is preferred to b
    * false if b is rational and a is not rational
    * false if a and b are irrational
    * @param a
    * @param b
    * @return
    */
  def prefers(a: String, b: String): Boolean ={
    for (ind <- preferences){
      if (ind==a) return true
      if (ind==b) return false
    }
    return false
  }

}


/**
  * Internal mutable internal state of mind for the SMP agents
  * @param partner is the current one
  * @param concessionLevel where the partner is in the preference list
  */
final class StateOfMind(var partner: String, var concessionLevel: Int) extends Tuple2[String,Int](partner,concessionLevel){

  //Return true if the partner is free
  def free() = { partner==StateOfMind.PHANTOM }
}
// Initial stateOfMind
object StateOfMind {
  val PHANTOM: String = "phantom"// initial partner
}

/**
  * Class representing partial/complete solution of SMP
  */
class Marriage(val husband: String, val wife: String) extends PartialSolution
class Matching(var result: List[Marriage]) extends Solution(result){
  //Returns the number of marriages
  def size() = result.size

  //Add a marriage
  def add(m: Marriage) : Matching = { new Matching(m :: result)}

  //Remove a marriage
  def remove(m: Marriage) : Matching = new Matching(result.filterNot(elm => elm == m ))

  //Remove a couple
  def remove(husband: String, wife: String) : Matching ={
      remove(new Marriage(husband,wife))
  }

  //Compare two marriages based on the husband name
  def comparator (m1: Marriage, m2: Marriage) = (m1.husband compareToIgnoreCase m2.wife) < 0

  //Nice print of a marriage
  override def toString(): String = {
    val sortedMarriages=result.sortWith(comparator)
    var s: String=""
    for (m<-sortedMarriages){
      s+=m+"\n"
    }
    s
  }



}
