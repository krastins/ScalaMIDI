package de.sciss.midi

/** A MIDI event is a time tag and an associated MIDI message.
  *
  * @param  tick    the time of the event. This must be interpreted in the context of some `TickRate` timebase
  * @param  message the message associated with this event, such as `NoteOn` or `NoteOff`
  */
final case class Event(tick: Long, message: Message)