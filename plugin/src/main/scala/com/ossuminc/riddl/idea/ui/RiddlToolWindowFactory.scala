package com.ossuminc.riddl.idea.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class RiddlToolWindowFactory extends ToolWindowFactory{
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    ()
  }
}
