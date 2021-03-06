package org.change.v2.abstractnet.click.sefl

import org.change.v2.abstractnet.generic.{ConfigParameter, ElementBuilder, GenericElement, Port}
import org.change.v2.analysis.expression.concrete._
import org.change.v2.analysis.memory.Tag
import org.change.v2.analysis.processingmodels.instructions._
import org.change.v2.analysis.processingmodels.{Instruction, LocationId}
import org.change.v2.util.canonicalnames._
import org.change.v2.util.conversion.RepresentationConversion._

class EtherEncap(name: String,
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
      CreateTag("L2", Tag("L3") - 112),
      Allocate(EtherSrc, 48),
      Assign(EtherSrc,
        if (configParams(1).value.matches("-"))
          SymbolicValue()
        else
          ConstantValue(macToNumber(configParams(1).value))
      ),
      Allocate(EtherDst, 48),
      Assign(EtherDst,
        if (configParams(2).value.matches("-"))
          SymbolicValue()
        else
          ConstantValue(macToNumber(configParams(2).value))
      ),
      //Constrain(EtherSrc, :~:(:==:(:@(EtherDst)))),
      //Constrain(EtherDst, :~:(:==:(:@(EtherSrc)))),
      Allocate(Tag("L2") + EtherTypeOffset, 16),
      Assign(Tag("L2") + EtherTypeOffset, ConstantValue(java.lang.Long.decode(configParams(0).value))),
      Forward(outputPortName(0))
    )
  )
}

class EtherEncapElementBuilder(name: String, elementType: String)
  extends ElementBuilder(name, elementType) {

  addInputPort(Port())
  addOutputPort(Port())

  override def buildElement: GenericElement = {
    new EtherEncap(name, elementType, getInputPorts, getOutputPorts, getConfigParameters)
  }
}

object EtherEncap {
  private var unnamedCount = 0

  private val genericElementName = "etherencap"

  private def increment {
    unnamedCount += 1
  }

  def getBuilder(name: String): EtherEncapElementBuilder = {
    increment;
    new EtherEncapElementBuilder(name, "EtherEncap")
  }

  def getBuilder: EtherEncapElementBuilder =
    getBuilder(s"$genericElementName-$unnamedCount")
}
