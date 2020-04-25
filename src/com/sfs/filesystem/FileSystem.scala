package com.sfs.filesystem

import com.sfs.commands.Command
import com.sfs.files.Directory

object FileSystem extends App {
  val root = Directory.ROOT

  try {
    io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
      currentState.show
      if (newLine == "exit") {
        throw new ExitException
      }
      Command.from(newLine)(currentState)
    })
  } catch {
    case e: ExitException => {
      println("Thank you for using me!  Exiting...")
    }
  }

  class ExitException extends RuntimeException

}
