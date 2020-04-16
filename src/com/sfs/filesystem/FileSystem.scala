package com.sfs.filesystem

import java.util.Scanner

import com.sfs.commands.Command
import com.sfs.files.Directory

object FileSystem extends App {
  val root = Directory.ROOT
  var state = State(root, root)
  val scanner = new Scanner(System.in)

  var active = true

  while(active) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input)(state)
    if (input == "exit") {
      println("Thank you for using me!  Exiting...")
      active = false
    }
  }
}
