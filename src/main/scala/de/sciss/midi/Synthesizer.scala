/*
 *  Synthesizer.scala
 *  (ScalaMIDI)
 *
 *  Copyright (c) 2013-2014 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.midi

import impl.{SynthesizerImpl => Impl}
import javax.sound.{midi => j}

object Synthesizer {
  def open(): Synthesizer = Impl.open()
  def piano(): Synthesizer = {
    val res = open()
    res.load(res.instruments("Piano"))
    res
  }
}
trait Synthesizer {
  private[midi] def peer: j.Synthesizer
  def close(): Unit
  def instruments: Map[String, Instrument]
  def load(instrument: Instrument): Unit
}