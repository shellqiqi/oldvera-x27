package org.change.v2.abstractnet.click.sefl

import org.change.v2.abstractnet.generic.{ConfigParameter, ElementBuilder, GenericElement, Port}
import org.change.v2.analysis.expression.concrete.ConstantValue
import org.change.v2.analysis.processingmodels.instructions._
import org.change.v2.analysis.processingmodels.{Instruction, LocationId}
import org.change.v2.util.canonicalnames._
import org.change.v2.util.conversion.RepresentationConversion

class HostEtherFilter(name: String,
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
      Constrain(EtherDst, :==:(ConstantValue(RepresentationConversion.macToNumber(configParams(0).value)))),
      Forward(outputPortName(0))
    )
  )
}

class HostEtherFilterElementBuilder(name: String, elementType: String)
  extends ElementBuilder(name, elementType) {

  addInputPort(Port())
  addOutputPort(Port())

  override def buildElement: GenericElement = {
    new HostEtherFilter(name, elementType, getInputPorts, getOutputPorts, getConfigParameters)
  }
}

object HostEtherFilter {
  private var unnamedCount = 0

  private val genericElementName = "hostEtherFilter"

  private def increment {
    unnamedCount += 1
  }

  def getBuilder(name: String): HostEtherFilterElementBuilder = {
    increment;
    new HostEtherFilterElementBuilder(name, "HostEtherFilter")
  }

  def getBuilder: HostEtherFilterElementBuilder =
    getBuilder(s"$genericElementName-$unnamedCount")
}
