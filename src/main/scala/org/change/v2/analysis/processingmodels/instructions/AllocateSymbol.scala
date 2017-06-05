package org.change.v2.analysis.processingmodels.instructions

import org.change.v2.analysis.processingmodels.Instruction
import org.change.v2.analysis.memory.{State, TagExp, Intable}

/**
 * Author: Radu Stoenescu
 * Don't be a stranger,  symnetic.7.radustoe@spamgourmet.com
 */
case class AllocateSymbol(id: String) extends Instruction {
  /**
   *
   * A state processing block produces a set of new states based on a previous one.
   *
   * @param s
   * @return
   */
  override def apply(s: State, v: Boolean): (List[State], List[State]) = {
    optionToStatePair(if (v) s.addInstructionToHistory(this) else s, s"Cannot allocate $id") (s => {
      s.memory.Allocate(id)
    })
  }
 override def toString = s"Allocate($id)"

}

case class AllocateRaw(a: Intable, size: Int) extends Instruction {
  /**
   *
   * A state processing block produces a set of new states based on a previous one.
   *
   * @param s
   * @return
   */
  override def apply(s: State, v: Boolean): (List[State], List[State]) = a(s) match {
    case Some(int) => optionToStatePair(if (v) s.addInstructionToHistory(this) else s, s"Cannot allocate at $a size $size")(s => {
      s.memory.Allocate(int, size)
    })
    case None => Fail(TagExp.brokenTagExpErrorMessage)(s,v)
  }
  
  override def toString = s"Allocate($a, $size)"
  
}

object Allocate {
  def apply(id: String): Instruction = AllocateSymbol(id)
  def apply(a: Intable, size: Int): Instruction = AllocateRaw(a, size)
}