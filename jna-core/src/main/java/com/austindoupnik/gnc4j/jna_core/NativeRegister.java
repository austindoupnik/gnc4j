package com.austindoupnik.gnc4j.jna_core;

import com.sun.jna.Native;

import java.util.ArrayList;
import java.util.List;

public class NativeRegister {
  public static void nativeRegister(final Class<?> cls, final String... libNames) {
    final List<UnsatisfiedLinkError> exs = new ArrayList<>();
    for (final String libName : libNames) {
      try {
        Native.register(cls, libName);
        return;
      } catch (final UnsatisfiedLinkError ex) {
        exs.add(ex);
      }
    }
    final RuntimeException ex = new RuntimeException("Unable to load library with name: " + String.join(", ", libNames));
    exs.forEach(ex::addSuppressed);
    throw ex;
  }
}
