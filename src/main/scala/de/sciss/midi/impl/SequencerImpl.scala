/*
 *  SequencerImpl.scala
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

private[midi] object SequencerImpl {
  def open(): Sequencer = {
    val sj = j.MidiSystem.getSequencer
    if (!sj.isOpen) sj.open()
    fromJava(sj)
  }

  def fromJava(sj: j.Sequencer): Sequencer = new Impl(sj)

  private final class Impl(val peer: j.Sequencer) extends Sequencer {
    override def toString = s"midi.Sequencer(${peer.getDeviceInfo.getName})@${hashCode().toHexString}"

    def isPlaying: Boolean = peer.isRunning

    def play(sequence: Sequence): Unit = {
      if (isPlaying) stop()
      peer.setSequence(sequence.toJava)
      peer.start()
    }

    def stop (): Unit = peer.stop ()
    def close(): Unit = peer.close()
  }
}