package de.sciss.midi

object PlaySatie extends App {
  import de.sciss.midi._

  val sq  = Sequence.read("vexation.mid")
  val pl  = Sequencer.open()
  pl.play(sq)
//  pl.stop()
  val t   = sq.tracks(1)  // second of three tracks
  val ev  = t.events      // all events in that track
  val pch = ev.collect { case Event(_, NoteOn(_, pch, _)) => pch }  // pitches
  pch.map(_ % 12).toSet.toList.sorted // pitch classes (all twelve!)
  println("Pitch classes: " + pch)
}