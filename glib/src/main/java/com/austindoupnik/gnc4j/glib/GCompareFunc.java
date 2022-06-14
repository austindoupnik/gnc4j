package com.austindoupnik.gnc4j.glib;

import com.sun.jna.Callback;

public interface GCompareFunc extends Callback {
  GInt invoke(final GConstPointer a, final GConstPointer b);
}
