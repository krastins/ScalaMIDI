package de.sciss.midi

import impl.{SequencerImpl => Impl}

object Sequencer {
  /** Opens the system's default MIDI sequencer. */
  def open() = Impl.open()
}
/** A `Sequencer` is a real-time scheduler capable of playing back a MIDI sequence using an associated output device. */
trait Sequencer {
  def play(sequence: Sequence): Unit
  def stop() : Unit
  def isPlaying: Boolean

  def close() : Unit
}