package com.sfs.filesystem

import com.sfs.files.Directory

// TODO: Ask why State can't be case class

/**
 *
 * @param root Root Directory
 * @param wd Working Directory
 * @param output The output of last command result
 */
class State(val root: Directory, val wd: Directory, val output: String) {
  def show: Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }

  def setMessage(message: String): State =
    State(root, wd, message)
}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, wd: Directory, output: String = ""): State =
    new State(root, wd, output)
}
