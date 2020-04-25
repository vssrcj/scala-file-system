package com.sfs.commands
import com.sfs.files.{DirectoryEntry, File}
import com.sfs.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirectoryEntry = {
    File.empty(state.wd.path, name)
  }
}
