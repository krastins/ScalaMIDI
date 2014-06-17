/*
 *  Sequence.scala
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

import impl.{SequenceImpl => Impl}
import java.io.File
import javax.sound.{midi => j}
import collection.immutable.{IndexedSeq => IIdxSeq}
import java.net.URL

object Sequence {
  /**
   * Reads a sequence from an external MIDI file.
   *
   * @param path          Path of a standard MIDI file.
   * @param skipUnknown   Whether to skip unknown MIDI events (`true`, default) or not (`false`).
   *                      If `false`, this fails if an unknown event is encountered.
   *
   * @return  the file parsed as a `Sequence`
   */
  def read(path: String, skipUnknown: Boolean = true): Sequence = readFile(new File(path), skipUnknown = skipUnknown)

  /**
   * Reads a sequence from an external MIDI file.
   *
   * @param file          a standard MIDI file.
   * @param skipUnknown   Whether to skip unknown MIDI events (`true`, default) or not (`false`).
   *                      If `false`, this fails if an unknown event is encountered.
   *
   * @return  the file parsed as a `Sequence`
   */
  def readFile(file: File, skipUnknown: Boolean = true): Sequence =
    Impl.fromJava(j.MidiSystem.getSequence(file), skipUnknown = skipUnknown)

  def readURL(url: URL, skipUnknown: Boolean = true): Sequence =
    Impl.fromJava(j.MidiSystem.getSequence(url), skipUnknown = skipUnknown)

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

  /** Converts this object to a Java MIDI equivalent.
    *
    * If this sequence had been created from a MIDI file,
    * this operation does not involve any conversions.
    * Otherwise, a Java sequence is created based on SMPTE 30 fps resolution.
    */
  def toJava: j.Sequence

  /** Writes the sequence to a standard MIDI file. */
  def write(path: String): Unit

  /** Writes the sequence to a standard MIDI file. */
  def writeFile(file: File): Unit
}