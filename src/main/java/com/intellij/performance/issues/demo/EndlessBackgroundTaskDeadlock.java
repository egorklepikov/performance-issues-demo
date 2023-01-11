package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class EndlessBackgroundTaskDeadlock extends AnAction {
  private final Lock lock1 = new ReentrantLock(true);
  private final Lock lock2 = new ReentrantLock(true);

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Task.Backgroundable task1 = new Task.Backgroundable(e.getProject(), "First task", true) {
      public void run(@NotNull ProgressIndicator indicator) {
        lock1.lock();
        System.out.println("lock1 acquired, waiting to acquire lock2.");
        try {
          sleep(50);
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }
        lock2.lock();
        System.out.println("lock2 acquired");

        System.out.println("executing first operation.");

        lock2.unlock();
        lock1.unlock();
      }
    };
    Task.Backgroundable task2 = new Task.Backgroundable(e.getProject(), "Second task", true) {
      public void run(@NotNull ProgressIndicator indicator) {
        lock2.lock();
        System.out.println("lock2 acquired, waiting to acquire lock1.");
        try {
          sleep(50);
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }

        lock1.lock();
        System.out.println("lock1 acquired");

        System.out.println("executing second operation.");

        lock2.unlock();
        lock1.unlock();
      }
    };
    ProgressManager.getInstance().run(task1);
    ProgressManager.getInstance().run(task2);
  }
}