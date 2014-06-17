/*
 *  Event.scala
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

import javax.sound.{midi => j}

/** A MIDI event is a time tag and an associated MIDI message.
  *
  * @param  tick    the time of the event. This must be interpreted in the context of some `TickRate` timebase
  * @param  message the message associated with this event, such as `NoteOn` or `NoteOff`
  */
final case class Event(tick: Long, message: Message) {
  def toJava: j.MidiEvent = new j.MidiEvent(message.toJava, tick)
}