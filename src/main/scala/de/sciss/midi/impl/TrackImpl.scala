/*
 *  TrackImpl.scala
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
        (0 until sz).flatMap { i =>
          val evj = peer.get(i)
          val j = Message.fromJavaOption(evj.getMessage)
          j.map(m => Event(evj.getTick, m))
        }
      } else {
        Vector.tabulate(sz) { i =>
          val evj = peer.get(i)
          val j = Message.fromJava(evj.getMessage)
          Event(evj.getTick, j)
        }
      }
    }

//    def toJava: j.Track = peer
  }
}