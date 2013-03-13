package de.sciss.midi

object TickRate {
  /** Constructs a timebase given through a correspondence between
    * MIDI ticks and a duration in microseconds.
    *
    * @param  ticks   the length of a sequence in ticks
    * @param  micros  the corresponding duration in microseconds
    */
  def apply(ticks: Long, micros: Long): TickRate = {
    val sec = micros * 1.0e-6
    apply(ticks/sec)
  }
}
/** A timebase specification for MIDI events.
  *
  * @param value  the rate in MIDI ticks per second
  */
final case class TickRate(value: Double) {
  override def toString = f"$productPrefix($value%1.2f ticks/sec.)"
}