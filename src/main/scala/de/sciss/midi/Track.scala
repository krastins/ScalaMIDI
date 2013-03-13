package de.sciss.midi

import impl.{TrackImpl => Impl}
import collection.immutable.{IndexedSeq => IIdxSeq}
import javax.sound.{midi => j}

object Track {
  def apply(events: IIdxSeq[Event], ticks: Long)(implicit rate: TickRate): Track = Impl(events, ticks)
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

  /** Converts this object to a Java MIDI equivalent. */
  def toJava: j.Track
}