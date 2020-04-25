package com.sfs.files

import com.sfs.filesystem.FileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirectoryEntry])
  extends DirectoryEntry (parentPath, name) {
  def removeEntry(entryName: String): Directory = {
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(x => x.name != entryName))
  }

  def replaceEntry(entryName: String, newEntry: DirectoryEntry): Directory = {
    new Directory(
      parentPath,
      name,
      contents.filter(content => !content.name.equals(entryName)) :+ newEntry
    )
  }

  def findEntry(entryName: String): DirectoryEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirectoryEntry]): DirectoryEntry = {
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)
    }

    findEntryHelper(entryName, contents)
  }

  def addEntry(newEntry: DirectoryEntry): Directory = {
    new Directory(parentPath, name, contents :+ newEntry)
  }

  def findDescendant(path: List[String]): Directory = {
    if (path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)
  }

  def findDescendant(relativePath: String): Directory = {
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)
  }

  def getAllFoldersInPath: List[String] = {
    path.substring(1).split(Directory.SEPARATOR).toList.filter(x => !x.isEmpty)
  }

  def hasEntry(name: String): Boolean = {
    findEntry(name) != null
  }

  def asDirectory: Directory = this

  def getType: String = "Directory"

  def asFile: File = throw new FileSystemException("A directory cannot be converted to a directory!")

  def isRoot: Boolean = parentPath.isEmpty

  def isDirectory: Boolean = true

  def isFile: Boolean = false

}

/*
  Like the "static" members of Directory.
 */
object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = {
    new Directory(parentPath, name, List())
  }
}
