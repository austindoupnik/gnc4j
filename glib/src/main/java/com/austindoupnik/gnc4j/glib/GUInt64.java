package com.austindoupnik.gnc4j.glib;

import com.sun.jna.IntegerType;
import com.sun.jna.NativeLong;

public class GUInt64 extends IntegerType {
  private static final long serialVersionUID = -2571179496793354622L;

  public GUInt64() {
    this(0);
  }

  public GUInt64(final long value) {
    super(NativeLong.SIZE, value, true);
  }
}
