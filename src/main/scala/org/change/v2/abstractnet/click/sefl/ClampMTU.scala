package org.change.v2.abstractnet.click.sefl

import org.change.v2.abstractnet.generic.{ConfigParameter, ElementBuilder, GenericElement, Port}
import org.change.v2.analysis.expression.concrete._
import org.change.v2.analysis.processingmodels.instructions._
import org.change.v2.analysis.processingmodels.{Instruction, LocationId}
import org.change.v2.util.canonicalnames._

class ClampMTU(name: String,
               elementType: String,
               inputPorts: List[Port],
               outputPorts: List[Port],
               configParams: List[ConfigParameter])
  extends GenericElement(name,
    elementType,
    inputPorts,
    outputPorts,
    configParams) {

  override def instructions: Map[LocationId, Instruction] = Map(
    inputPortName(0) -> InstructionBlock(
      Constrain(IPLength, :<=:(ConstantValue(configParams(0).value.toInt))),
      Forward(outputPortName(1))
    )
  )
}

class ClampMTUElementBuilder(name: String, elementType: String)
  extends ElementBuilder(name, elementType) {

  addInputPort(Port())
  addOutputPort(Port())

  override def buildElement: GenericElement = {
    new ClampMTU(name, elementType, getInputPorts, getOutputPorts, getConfigParameters)
  }
}

object ClampMTU {
  private var unnamedCount = 0

  private val genericElementName = "ClampMTU"

  private def increment {
    unnamedCount += 1
  }

  def getBuilder(name: String): ClampMTUElementBuilder = {
    increment;
    new ClampMTUElementBuilder(name, "ClampMTU")
  }

  def getBuilder: ClampMTUElementBuilder =
    getBuilder(s"$genericElementName-$unnamedCount")
}
