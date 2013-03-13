package de.sciss.midi

import org.scalatest.FunSpec

/*
 *  to run only this test
 *  test-only de.sciss.midi.MessageSpec
 */
class MessageSpec extends FunSpec {
  describe("Type safe MIDI messages") {
    it("should be convertible forth and back") {

      def forthBack(m: Message) {
        assert(Message.fromJava(m.toJava) === m)
      }

      forthBack(NoteOn(3, 4, 5))
      forthBack(NoteOff(6, 7, 8))
      forthBack(ProgramChange(9, 10))
      forthBack(ControlChange(11, 12, 13))

      import MetaMessage._

//      val smpte = SMPTEOffset(SMPTEOffset.Format.value(29.97), hours = 3, minutes = 4, seconds = 5, frames = 6, subframes = 7)
//      val smpteBack @ SMPTEOffset(_) = Message.fromJava(smpte.toJava)
//      println(s"IN ${smpte.code.toHexString} OUT ${smpteBack.code.toHexString}")
      forthBack(SMPTEOffset(SMPTEOffset.Format.value(29.97), hours = 3, minutes = 4, seconds = 5, frames = 6, subframes = 7))
      forthBack(SMPTEOffset(SMPTEOffset.Format.value(29.97), hours = 3, minutes = 4, seconds = 5, frames = 6, subframes = 7))
      forthBack(SMPTEOffset(SMPTEOffset.Format.code(1), hours = 7, minutes = 6, seconds = 5, frames = 4, subframes = 3))
      forthBack(KeySignature(23, KeySignature.Major))
      forthBack(KeySignature(45, KeySignature.Major))
      forthBack(EndOfTrack)
      forthBack(TimeSignature(7, 8, 23, 45))
      forthBack(Tempo(400000))
      forthBack(Copyright("copyright"))
      forthBack(TrackName("track"))
      forthBack(InstrumentName("instrument"))
      forthBack(Lyrics("lyrics"))
      forthBack(Marker("marker"))
      forthBack(CuePoint("cue"))
    }
  }
}