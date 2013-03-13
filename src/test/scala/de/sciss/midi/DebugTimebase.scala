package de.sciss.midi

object DebugTimebase extends App {
  val sqj     = Sequence.read("vexation.mid").toJava
  val micros  = sqj.getMicrosecondLength
  println(micros)
}