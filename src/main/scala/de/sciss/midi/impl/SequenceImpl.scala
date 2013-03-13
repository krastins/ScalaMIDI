/*
 *  SequenceImpl.scala
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
import collection.breakOut
import java.io.File

private[midi] object SequenceImpl {
  def fromJava(sj: j.Sequence, skipUnknown: Boolean = true): Sequence = new FromJava(sj, skipUnknown = skipUnknown)

  def apply(tracks: IIdxSeq[Track]): Sequence = {
    tracks match {
      case head +: tail =>
        require(tail.forall(_.rate == head.rate), "Cannot mix tracks with different time bases")
        val ticks   = (head.ticks /: tail) { case (m, t) => math.max(m, t.ticks) }
        val rate    = head.rate
        new Apply(tracks, ticks, rate)

      case _ =>
        sys.error("Sequences with no tracks currently not supported")
    }
  }

  private sealed trait Impl extends Sequence {
    protected def numTracks: Int

    final def duration: Double = ticks / rate.value

    override def toString = f"midi.Sequence(# tracks = $numTracks, dur = $duration%1.2f sec.)@${hashCode().toHexString}"

    final def write(path: String) { writeFile(new File(path)) }

    def writeFile(file: File) {
      val sj  = toJava
      val fmt = j.MidiSystem.getMidiFileTypes(sj).headOption.getOrElse(
        sys.error("No supported MIDI format found to write sequence")
      )
      j.MidiSystem.write(sj, fmt, file)
    }
  }

  private final class Apply(val tracks: IIdxSeq[Track], val ticks: Long, val rate: TickRate) extends Impl {
    protected def numTracks = tracks.size

    def toJava: j.Sequence = {
      val div: Float  = j.Sequence.SMPTE_30
      val res: Int    = (rate.value / div + 0.5).toInt
      val sj = new j.Sequence(div, res, numTracks)
      val tjs = sj.getTracks
      assert(tjs.length == tracks.size)
      var i = 0
      while (i < tjs.length) {
        val t   = tracks(i)
        val tj  = tjs(i)
        t.events.foreach(e => tj.add(e.toJava))
        i += 1
      }
      sj
    }
  }

  private final class FromJava(val peer: j.Sequence, skipUnknown: Boolean) extends Impl {
    self =>

    protected def numTracks = peer.getTracks.length

    lazy val tracks: IIdxSeq[Track] = peer.getTracks.map(tj =>
      TrackImpl.fromJava(tj, self, skipUnknown = skipUnknown))(breakOut)

    def ticks: Long = peer.getTickLength

    lazy val rate = TickRate(ticks = ticks, micros = peer.getMicrosecondLength)

//    def notes: IIdxSeq[OffsetNote] = tracks.flatMap(_.notes)

    def toJava: j.Sequence = peer
  }
}