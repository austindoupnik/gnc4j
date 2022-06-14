package com.austindoupnik.gnc4j.glib;

import com.austindoupnik.gnc4j.jna_core.NativeInt;
import com.sun.jna.IntegerType;

public class GUInt extends IntegerType {
  private static final long serialVersionUID = 2001653811616876137L;

  public GUInt() {
    this(0);
  }

  public GUInt(final long value) {
    super(NativeInt.SIZE, value, true);
  }
}
