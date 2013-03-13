package de.sciss.midi

object CreateSequence extends App {
  val ms  = (64 to 72).flatMap { pch => NoteOn(0, pch, 80) :: NoteOff(0, pch, 0) :: Nil }
  implicit val rate = TickRate(400.0e6)
  val ev  = ms.zipWithIndex.map { case (m, i) => Event((i * 0.25 * rate.value).toLong, m) }
  val mx  = ev.map(_.tick).max
  val t   = Track(ev)
  val sq  = Sequence(Vector(t))
//  println("Resolution = " + sq.toJava.getResolution)
  val pl  = Sequencer.open()
  pl.play(sq)
  Thread.sleep(((t.duration + 1) * 1000).toLong)
  pl.close()
}