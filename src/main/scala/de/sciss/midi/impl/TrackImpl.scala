/*
 *  TrackImpl.scala
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
package impl

import javax.sound.{midi => j}
import collection.immutable.{IndexedSeq => IIdxSeq}

private[midi] object TrackImpl {
  def fromJava(tj: j.Track, sq: Sequence, skipUnknown: Boolean = true): Track = {
    val rate = sq.rate
    new FromJava(tj, rate, skipUnknown = skipUnknown)
  }

  def apply(events: IIdxSeq[Event], ticks: Long = -1L)(implicit rate: TickRate): Track = {
    val ticks1  = if (ticks < 0L) events.lastOption.map(_.tick).getOrElse(0L) else ticks
    val events1 = events.lastOption match {
      case Some(Event(_, MetaMessage.EndOfTrack)) => events
      case _ => events :+ Event(ticks1, MetaMessage.EndOfTrack)
    }
    new Apply(events1, ticks1, rate)
  }

  private sealed trait Impl extends Track {
    final def duration: Double = ticks / rate.value

    override def toString = f"midi.Track(# events = ${events.size}, dur = $duration%1.2f sec.)@${hashCode().toHexString}"
  }

  private final class Apply(val events: IIdxSeq[Event], val ticks: Long, val rate: TickRate) extends Impl {
//    def toJava: j.Track = ???
  }

  private final class FromJava(peer: j.Track, val rate: TickRate, skipUnknown: Boolean) extends Impl {
    def ticks: Long = peer.ticks()

    lazy val events: IIdxSeq[Event] = {
      val sz = peer.size()
      if (skipUnknown) {
        Vector.tabulate(sz) { i =>
          val evj = peer.get(i)
          val j = Message.fromJava(evj.getMessage)
          Event(evj.getTick, j)
        }
      } else {
        (0 until sz).flatMap { i =>
          val evj = peer.get(i)
          val j = Message.fromJavaOption(evj.getMessage)
          j.map(m => Event(evj.getTick, m))
        }
      }
    }

//    def toJava: j.Track = peer
  }
}