package com.austindoupnik.gnc4j.glib;

import com.austindoupnik.gnc4j.jna_core.NativeInt;
import com.sun.jna.IntegerType;

public class GUInt32 extends IntegerType {
  private static final long serialVersionUID = 1315930053516505840L;

  public GUInt32() {
    this(0);
  }

  public GUInt32(final long value) {
    super(NativeInt.SIZE, value, true);
  }
}
