package com.sfs.commands
import com.sfs.filesystem.State

class UnknownCommand extends Command  {
  override def apply(state: State): State =
    state.setMessage("Command not found!")
}
