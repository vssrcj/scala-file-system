package com.sfs.commands
import com.sfs.files.{Directory, DirectoryEntry}
import com.sfs.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirectoryEntry = {
    Directory.empty(state.wd.path, name)
  }
}
