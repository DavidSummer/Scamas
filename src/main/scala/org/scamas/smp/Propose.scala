// Copyright (C) Dialectics 2016
package org.scamas.smp

import org.scamas.core.Move

/**
  *  All possible moves performed by the agent
  */

case object SMPMove extends Move
object Propose extends SMPMove
object Accept extends SMPMove
object Reject extends SMPMove
