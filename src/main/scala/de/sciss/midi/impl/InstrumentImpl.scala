/*
 *  InstrumentImpl.scala
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

private[midi] object InstrumentImpl {
  def fromJava(ij: j.Instrument): Instrument = new Impl(ij)

  private final class Impl(val peer: j.Instrument) extends Instrument {
    override def toString = s"midi.Instrument(name = $name)"

    def name: String = peer.getName
  }
}