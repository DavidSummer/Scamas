// Copyright (C) Dialectics 2016
package org.scamas.smp

import org.scamas.core._

/**
  *  All possible moves performed by the agent
  */
case object Propose extends Move
case object Accept extends Move
case object Reject extends Move
case object Divorce extends Move
case object AcknowledgeDivorce extends Move
