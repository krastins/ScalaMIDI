package de.sciss.midi
package impl

import javax.sound.{midi => j}
import collection.immutable.{IndexedSeq => IIdxSeq}
import collection.breakOut

private[midi] object SequenceImpl {
  def fromJava(sj: j.Sequence): Sequence = new FromJava(sj)

  def apply(tracks: IIdxSeq[Track]): Sequence = {
    tracks match {
      case head +: tail =>
        require(tail.forall(_.rate == head.rate), "Cannot mix tracks with different time bases")
        val ticks   = (0L /: tail) { case (m, t) => math.max(m, t.ticks) }
        val rate    = head.rate
        new Apply(tracks, ticks, rate)

      case _ =>
        sys.error("Sequences with no tracks currently not supported")
    }
  }

  private sealed trait Impl extends Sequence {
    protected def numTracks: Int

    final def duration: Double = ticks * rate.value

    override def toString = f"midi.Sequence(# tracks = $numTracks, dur = $duration%1.2f sec.)@${hashCode().toHexString}"
  }

  private final class Apply(val tracks: IIdxSeq[Track], val ticks: Long, val rate: TickRate) extends Impl {
    protected def numTracks = tracks.size

    def toJava = ???
  }

  private final class FromJava(val peer: j.Sequence) extends Impl {
    self =>

    protected def numTracks = peer.getTracks.length

    lazy val tracks: IIdxSeq[Track] = peer.getTracks.map(tj => TrackImpl.fromJava(tj, self))(breakOut)

    def ticks: Long = peer.getTickLength

    lazy val rate = TickRate(ticks = ticks, micros = peer.getMicrosecondLength)

//    def notes: IIdxSeq[OffsetNote] = tracks.flatMap(_.notes)

    def toJava: j.Sequence = peer
  }
}