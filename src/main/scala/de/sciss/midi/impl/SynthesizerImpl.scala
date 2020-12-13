/*
 *  SynthesizerImpl.scala
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

private[midi] object SynthesizerImpl {
  def open(): Synthesizer = {
    val sj = j.MidiSystem.getSynthesizer
    if (!sj.isOpen) sj.open()
    fromJava(sj)
  }

  def fromJava(synth: j.Synthesizer): Synthesizer = {
    new Impl(synth)
  }

  private final class Impl(val peer: j.Synthesizer) extends Synthesizer {
    override def toString = s"midi.Synthesizer(${peer.getDeviceInfo.getName})@${hashCode().toHexString}"

    def close(): Unit = peer.close()

    lazy val instruments: Map[String, Instrument] = {
      peer.getAvailableInstruments.map(ij => ij.getName -> InstrumentImpl.fromJava(ij)).to(Map)
    }

    def load(instrument: Instrument): Unit =
      peer.loadInstrument(instrument.peer)
  }
}