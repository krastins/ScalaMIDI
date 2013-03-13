/*
 *  Track.scala
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

import impl.{TrackImpl => Impl}
import collection.immutable.{IndexedSeq => IIdxSeq}

object Track {
  /** Creates a new track from a sequence of events.
    * This adds an `EndOfTrack` marker unless the last event is such a meta message.
    *
    * @param events the events that constitute the track
    * @param ticks  the length of the track in ticks. If `-1` (default), the length is the time stamp of the last event
    * @param rate   the timebase for the track
    */
  def apply(events: IIdxSeq[Event], ticks: Long = -1)(implicit rate: TickRate): Track = Impl(events, ticks)
}
trait Track {
  /** The MIDI events within this track. */
  def events: IIdxSeq[Event]

  /** The length of the sequence in ticks. */
  def ticks: Long

  /** The timebase of the sequence in ticks per second. */
  def rate: TickRate

  /** The duration of the sequence in seconds. */
  def duration: Double

//  /** Converts this object to a Java MIDI equivalent. */
//  def toJava: j.Track
}