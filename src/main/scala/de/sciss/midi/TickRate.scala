/*
 *  TickRate.scala
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

object TickRate {
  def tempo(bpm: Double, tpq: Int): TickRate = {
    val tps = tpq / bpm * 60
    apply(tps)
  }

  /** Constructs a timebase given through a correspondence between
    * MIDI ticks and a duration in microseconds.
    *
    * @param  ticks   the length of a sequence in ticks
    * @param  micros  the corresponding duration in microseconds
    */
  def duration(ticks: Long, micros: Long): TickRate = {
    val sec = micros * 1.0e-6
    apply(ticks/sec)
  }
}
/** A timebase specification for MIDI events.
  *
  * @param value  the rate in MIDI ticks per second
  */
final case class TickRate(value: Double) {
  override def toString = f"$productPrefix($value%1.1f ticks/sec.)"
}