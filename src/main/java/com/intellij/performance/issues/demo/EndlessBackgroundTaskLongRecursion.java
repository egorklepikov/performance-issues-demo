package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class EndlessBackgroundTaskLongRecursion extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Task.Backgroundable task1 = new Task.Backgroundable(e.getProject(), "Some task", true) {
      public void run(@NotNull ProgressIndicator indicator) {
        while (true) {
          doSomethingRecursively();
        }
      }

      private void doSomethingRecursively() {
        BigInteger.valueOf(5);
        try {
          Thread.sleep(300);
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }
        doSomethingRecursively();
      }
    };
    ProgressManager.getInstance().run(task1);
  }
}
