package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

public class UIFreezeLongReadAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Task.Backgroundable task1 = new Task.Backgroundable(e.getProject(), "Some task", true) {
      public void run(@NotNull ProgressIndicator indicator) {
        try {
          ReadAction.compute(() -> {
            Thread.sleep(20000);
            return null;
          });
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      }
    };
    ProgressManager.getInstance().run(task1);
  }
}
