package com.sfs.files

import com.sfs.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, val contents: String)
  extends DirectoryEntry(parentPath, name) {
  def setContents(newContents: String): File = {
    new File(parentPath, name, newContents)
  }

  def appendContents(newContents: String): File = {
    setContents(s"$contents\n${newContents}")
  }


  def asDirectory: Directory = {
    throw new FileSystemException("A file cannot be converted to a directory")
  }

  def getType: String = "File"

  override def asFile: File = this

  def isDirectory: Boolean = false

  def isFile: Boolean = true
}

object File {
  def empty(parentPath: String, name: String): File = {
    new File(parentPath, name, "")
  }
}