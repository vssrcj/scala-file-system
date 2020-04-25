package com.sfs.commands
import com.sfs.files.DirectoryEntry
import com.sfs.filesystem.State

class Ls extends Command {

  def createNiceOutPut(contents: List[DirectoryEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      s"${entry.name}[${entry.getType}]\n${createNiceOutPut(contents.tail)}"
    }
  }

  override def apply(state: State): State = {
    val contents = state.wd.contents
    val niceOutput = createNiceOutPut(contents);
    state.setMessage(niceOutput)
  }
}
