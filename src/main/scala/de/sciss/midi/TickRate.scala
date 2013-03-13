/*
 *  TickRate.scala
 *  (ScalaMIDI)
 *
 *  Copyright (c) 2013 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either
 *  version 2, june 1991 of the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License (gpl.txt) along with this software; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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