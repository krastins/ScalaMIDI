package de.sciss.midi
package impl

import javax.sound.{midi => j}
import collection.immutable.{IndexedSeq => IIdxSeq}

private[midi] object TrackImpl {
  def fromJava(tj: j.Track, sq: Sequence, skipUnknown: Boolean = true): Track = {
    val rate = sq.rate
    new FromJava(tj, rate, skipUnknown = skipUnknown)
  }

  def apply(events: IIdxSeq[Event], ticks: Long)(implicit rate: TickRate): Track = new Apply(events, ticks, rate)

  private sealed trait Impl extends Track {
    final def duration: Double = ticks * rate.value

    override def toString = f"midi.Track(# events = ${events.size}, dur = $duration%1.2f sec.)@${hashCode().toHexString}"
  }

  private final class Apply(val events: IIdxSeq[Event], val ticks: Long, val rate: TickRate) extends Impl {
//    def toJava: j.Track = ???
  }

  private final class FromJava(peer: j.Track, val rate: TickRate, skipUnknown: Boolean) extends Impl {
    def ticks: Long = peer.ticks()

    lazy val events: IIdxSeq[Event] = {
      val sz = peer.size()
      if (skipUnknown) {
        Vector.tabulate(sz) { i =>
          val evj = peer.get(i)
          val j = Message.fromJava(evj.getMessage)
          Event(evj.getTick, j)
        }
      } else {
        (0 until sz).flatMap { i =>
          val evj = peer.get(i)
          val j = Message.fromJavaOption(evj.getMessage)
          j.map(m => Event(evj.getTick, m))
        }
      }
    }

//    def toJava: j.Track = peer
  }
}