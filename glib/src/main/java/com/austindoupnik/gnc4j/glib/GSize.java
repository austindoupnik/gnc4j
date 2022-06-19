package com.austindoupnik.gnc4j.glib;

import com.austindoupnik.gnc4j.jna_core.NativeInt;
import com.sun.jna.IntegerType;

public class GSize extends IntegerType {
  private static final long serialVersionUID = -4405709040327044965L;

  public GSize() {
    this(0);
  }

  public GSize(final long value) {
    super(NativeInt.SIZE, value, true);
  }
}
