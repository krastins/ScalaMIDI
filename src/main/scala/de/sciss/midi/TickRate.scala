package de.sciss.midi

object TickRate {
  def ticksPerSecond(tps: Double)(ticks: Long): TickRate = {
    val micros = (ticks / tps * 1.0e6 + 0.5).toLong
    apply(ticks, micros)
  }
}
/** A timebase specification for MIDI events.
  * A timebase is given through a correspondence between
  * MIDI ticks and a duration in microseconds.
  *
  * @param ticks  the amount of ticks used to calculate the rate
  * @param micros the amount of micros used to calculate the rate
  */
final case class TickRate(ticks: Long, micros: Long) {
  override def toString = s"$productPrefix(ticks = $ticks, micros = $micros)"

  /** The rate represented by the timebase, in ticks per second */
  def ticksPerSecond: Double = {
    val sec = micros * 1.0e-6
    ticks / sec
  }
}