package com.intellij.performance.issues.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class UIFreezeLongUIOperationAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    for (int i = 0; i < 5000; i++) {
      BigInteger.valueOf(5).pow(100000);
    }
  }
}
