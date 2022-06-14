package com.austindoupnik.gnc4j.glib;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface GFunc extends Callback {
  void invoke(final Pointer data, final Pointer userData);
}
