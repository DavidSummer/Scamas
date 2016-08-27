// Copyright (C) Dialectics 2016
package org.scamas.smp

import org.scamas.core.{Profile, PartialSolution ,Solution}

/**
  * Profile for the SMP
  * @param myName of the individual
  * @param preferences list of the potential partners from the preferred partner to the least preferred
  */
class Individual(override val myName: String, val preferences: Array[String]) extends Profile(myName){
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
  * Class representing partial/complete solution of SMP
  */
class Marriage(husband: String, val wife: String) extends PartialSolution
class Matching(var result: List[Marriage]) extends Solution(result){
  //Add a marriage
  def add(m: Marriage) : Matching = { new Matching(m :: result)}
}
