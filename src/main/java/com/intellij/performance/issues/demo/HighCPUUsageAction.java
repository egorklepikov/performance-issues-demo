package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class HighCPUUsageAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    new First().start();
    new Second().start();
  }

  private static class First extends Thread {
    public First() {
      setName("First");
    }

    @Override
    public void run() {
      while (true) {
        a();
        b();
      }
    }

    private void a(){
      BigInteger.valueOf(5).pow(10);
    }

    private void b() {
      BigInteger.valueOf(5).pow(10);
    }
  }

  private static class Second extends Thread {
    public Second() {
      setName("Second");
    }

    @Override
    public void run() {
      while (true) {
        c();
      }
    }

    private void c() {
      BigInteger.valueOf(5).pow(10);
    }
  }
}
