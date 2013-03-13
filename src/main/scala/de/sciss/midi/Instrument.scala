package de.sciss.midi

import javax.sound.{midi => j}

trait Instrument {
  def name: String
  private[midi] def peer: j.Instrument
}