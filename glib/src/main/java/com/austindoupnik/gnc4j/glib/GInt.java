package com.austindoupnik.gnc4j.glib;

import com.austindoupnik.gnc4j.jna_core.NativeInt;
import com.sun.jna.IntegerType;

public class GInt extends IntegerType {
  private static final long serialVersionUID = -7213593607768345169L;

  public GInt() {
    this(0);
  }

  public GInt(final long value) {
    super(NativeInt.SIZE, value, false);
  }
}
