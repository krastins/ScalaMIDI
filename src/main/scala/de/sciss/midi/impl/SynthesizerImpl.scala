/*
 *  SynthesizerImpl.scala
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
import collection.breakOut

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

    def close() { peer.close() }

    lazy val instruments: Map[String, Instrument] = {
      peer.getAvailableInstruments.map(ij => ij.getName -> InstrumentImpl.fromJava(ij))(breakOut)
    }

    def load(instrument: Instrument) {
      peer.loadInstrument(instrument.peer)
    }
  }
}