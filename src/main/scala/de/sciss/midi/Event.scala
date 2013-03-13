/*
 *  Event.scala
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

import javax.sound.{midi => j}

/** A MIDI event is a time tag and an associated MIDI message.
  *
  * @param  tick    the time of the event. This must be interpreted in the context of some `TickRate` timebase
  * @param  message the message associated with this event, such as `NoteOn` or `NoteOff`
  */
final case class Event(tick: Long, message: Message) {
  def toJava: j.MidiEvent = new j.MidiEvent(message.toJava, tick)
}