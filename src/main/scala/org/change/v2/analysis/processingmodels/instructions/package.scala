package org.change.v2.analysis.processingmodels

import org.change.v2.analysis.memory.{State, MemorySpace}

/**
 * Author: Radu Stoenescu
 * Don't be a stranger,  symnetic.7.radustoe@spamgourmet.com
 */
package object instructions {
  
  private val isDebugging = false
  

  def optionToStatePair(previousState: State, error: ErrorCause,
      forceFail : Boolean = false)(block: State => Option[MemorySpace]): (List[State], List[State]) = {
    val nm = block(previousState)
    nm.map(m => (
        List(State(m, previousState.history, None, previousState.instructionHistory)), 
        Nil)).
      getOrElse(
            (Nil, 
             // DD: Consider these flags
             if (isDebugging || forceFail)
               List(
                 State(previousState.memory, 
                  previousState.history, 
                  Some(error), 
                  previousState.instructionHistory)
               )
             else
               Nil
            )
      )
  }

  def stateToError(previousState: State, error: ErrorCause) = optionToStatePair(previousState, error, true)(_ => None)
}
