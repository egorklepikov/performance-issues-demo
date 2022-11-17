package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class GCOverheat extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Task.Backgroundable task = new Task.Backgroundable(e.getProject(), "GCOverheat", false) {
      public void run(@NotNull ProgressIndicator indicator) {
        Runtime env = Runtime.getRuntime();
        List<Long[]> arrayList = new ArrayList<>();
        while ((env.maxMemory() - env.totalMemory()) + env.freeMemory() > 30 * 1024 * 1024) {
          setTitle("Available heap: " + humanReadableByteCountBin((env.maxMemory() - env.totalMemory()) + env.freeMemory()));
          arrayList.add(new Long[65536]);
          try {
            Thread.sleep(30);
          } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    };
    ProgressManager.getInstance().run(task);
  }

  public static String humanReadableByteCountBin(long bytes) {
    long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
    if (absB < 1024) {
      return bytes + " B";
    }
    long value = absB;
    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
    for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
      value >>= 10;
      ci.next();
    }
    value *= Long.signum(bytes);
    return String.format("%.1f %ciB", value / 1024.0, ci.current());
  }
}
