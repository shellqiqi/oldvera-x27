package org.change.v2.analysis.expression.concrete

import org.change.v2.analysis.expression.abst.{FloatingExpression, Expression}
import org.change.v2.analysis.memory.State
import org.change.v2.analysis.z3.Z3Util
import z3.scala.{Z3Solver, Z3AST}
import org.change.utils.RepresentationConversion._
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by radu on 3/24/15.
 */
case class ConstantValue(value: Long, isIp : Boolean  = false, isMac : Boolean = false) 
  extends Expression with FloatingExpression {
  @JsonIgnore
  def ast = Z3Util.z3Context.mkNumeral(value.toString(), Z3Util.defaultSort)

  override def toZ3(solver: Option[Z3Solver] = None): (Z3AST, Option[Z3Solver]) = (ast, solver)

  /**
   * A floating expression may include unbounded references (e.g. symbol ids)
   *
   * Given a context (the state) it can produce an evaluable expression.
   * @param s
   * @return
   */
  override def instantiate(s: State): Either[Expression, String] = Left(this)

  override def toString = {
    if (isMac)
      "[" + numberToMac(value) +  "]"
    else if (!isIp)
      s"[$value]"
    else
      "[" + numberToIp(value) +  "]"
  }
}
