package com.sfs.commands
import com.sfs.files.{Directory, File}
import com.sfs.filesystem.State

import scala.annotation.tailrec

class Echo(args: List[String]) extends Command {
  override def apply(state: State): State = {
    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val content = createContent(args, args.length - 2)

      if (">>".equals(operator)) {
        doEcho(state, content, filename, append = true)
      } else if (">".equals(operator)) {
        doEcho(state, content, filename, append = false)
      } else {
        state.setMessage(createContent(args, args.length))
      }
    }
  }

  // topIndex non inclusive
  def createContent(args: List[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, acc: String): String = {
      if (currentIndex >= topIndex) acc
      else createContentHelper(currentIndex + 1, s"$acc ${args(currentIndex)}")
    }

    createContentHelper(0, "")
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    /*
    * If path is empty, then fail
    * else no more things to explore = path.tail.isEmpty
    *   find the file to create/add content to
    *   if file not found, create file
    *   else replace or append content to the file
        else
        * replace or append content to the file
        * replace the entry with the filename with the NEW file
      else
      * find the next directory to navigate
      * call the gRAE recursively on that
      * if recursive call failed, fail
      * else replace entry with the NEW directory after the recursive call
     */
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)
      if (dirEntry == null) {
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      } else if (dirEntry.isDirectory) {
        currentDirectory
      } else if (append) {
        currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
      } else {
        currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
      }
    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)
      if (newNextDirectory == nextDirectory) {
        currentDirectory
      } else {
        currentDirectory.replaceEntry(path.head, newNextDirectory)
      }
    }
  }

  def doEcho(state: State, content: String, filename: String, append: Boolean): State = {
    if (filename.contains(Directory.SEPARATOR)) {
      state.setMessage("Echo: filename must not contain separators")
    } else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ filename, content, append)
      if (newRoot == state.root) {
        state.setMessage(filename + ": no such file")
      } else {
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
      }
    }
  }
}
