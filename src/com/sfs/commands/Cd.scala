package com.sfs.commands
import com.sfs.files.{Directory, DirectoryEntry}
import com.sfs.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {
  override def apply(state: State): State = {
    // 1. find root
    val root = state.root
    val wd = state.wd

    // 2. find the absolute path of the directory I want to cd to
    val absolutePath = {
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) s"${wd.path}${dir}"
      else s"${wd.path}${Directory.SEPARATOR}${dir}"
    }

    // 3. find the directory to cd to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 4. change the state given the new directory
    if (destinationDirectory == null || !destinationDirectory.isDirectory) {
      state.setMessage(s"$dir: no such directory")
    } else {
      State(root, destinationDirectory.asDirectory)
    }
  }

  def doFindEntry(root: Directory, path: String): DirectoryEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirectoryEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val entry = currentDirectory.findEntry(path.head)
        if (entry == null || !entry.isDirectory) null
        else findEntryHelper(entry.asDirectory, path.tail)
      }
    }

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String] = List()): List[String]= {
      /*
        /a/b => [a, b]
        /a/.. => []
        /a/b/.. => [a]
      */
      if (path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      }
      else {
        collapseRelativeTokens(path.tail, result :+ path.head)
      }
    }

    // 1. tokens
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    // 1.5 collapse relative tokens
    /*
      ["a", "."] => ["a"]
      /a/../ => ["a", ".."] => []
     */

    val newTokens = collapseRelativeTokens(tokens)
    if (newTokens == null) null
    // 2. navigate to the correct entry
    else findEntryHelper(root, newTokens)
  }

}
