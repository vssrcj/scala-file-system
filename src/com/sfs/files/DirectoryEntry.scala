package com.sfs.files

abstract class DirectoryEntry(val parentPath: String, val name: String) {
  def path: String = {
    val separatorIfNecessary = {
      if (Directory.ROOT_PATH.equals(parentPath)) ""
      else Directory.SEPARATOR
    }
    s"$parentPath$separatorIfNecessary$name"
  }

  def asDirectory: Directory

  def asFile: File

  def getType: String

  def isFile: Boolean

  def isDirectory: Boolean
}
