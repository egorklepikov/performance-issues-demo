package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

//Example taken from https://stackoverflow.com/questions/20134769/how-to-get-java-stacks-when-jvm-cant-reach-a-safepoint
public class SafePoints extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Task.Backgroundable safePointTask = new Task.Backgroundable(e.getProject(), "Safe points blocker", true) {
      public void run(@NotNull ProgressIndicator indicator) {
        // Spawn a background thread to compute an enormous number.
        new Thread(() -> {
          try {
            Thread.sleep(5000);
          } catch (InterruptedException ignored) {
          }
          BigInteger.valueOf(5).pow(100000000);
        }).start();
        // Loop, allocating memory and periodically logging progress, so illustrate GC pause times.
        byte[] b;
        for (int outer = 0; ; outer++) {
          long startMs = System.currentTimeMillis();
          for (int inner = 0; inner < 100000; inner++) {
            b = new byte[1000];
          }
          if (System.currentTimeMillis() - startMs > 20) {
            System.out.println("Iteration " + outer + " took " + (System.currentTimeMillis() - startMs) + " ms");
          }
        }
      }
    };
    ProgressManager.getInstance().run(safePointTask);
  }
}
