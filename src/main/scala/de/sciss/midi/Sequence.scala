package de.sciss.midi

import impl.{SequenceImpl => Impl}
import java.io.File
import javax.sound.{midi => j}
import collection.immutable.{IndexedSeq => IIdxSeq}

object Sequence {
  /**
   * Reads a sequence from an external MIDI file.
   *
   * @param path  Path of a standard MIDI file.
   *
   * @return  the file parsed as a `Sequence`
   */
  def read(path: String): Sequence = read(new File(path))

  /**
   * Reads a sequence from an external MIDI file.
   *
   * @param file  a standard MIDI file.
   *
   * @return  the file parsed as a `Sequence`
   */
  def read(file: File): Sequence = {
    Impl.fromJava(j.MidiSystem.getSequence(file))
  }

  /** Creates a new sequence from a given list of tracks */
  def apply(tracks: IIdxSeq[Track]): Sequence = Impl(tracks)
}

/** A `Sequence` represents a collection of MIDI tracks, and typically is the result of
  * reading in a standard MIDI file.
  */
trait Sequence {
  /** All tracks of the sequence. */
  def tracks: IIdxSeq[Track]

  /** The length of the sequence in ticks. */
  def ticks: Long

  /** The timebase of the sequence in ticks per second. */
  def rate: TickRate

  /** The duration of the sequence in seconds. */
  def duration: Double

  /** Converts this object to a Java MIDI equivalent. */
  def toJava: j.Sequence
}