package com.austindoupnik.gnc4j.jna_core;

import com.sun.jna.IntegerType;

public class UnsignedInt extends IntegerType {
  private static final long serialVersionUID = -3803930675951555632L;

  public UnsignedInt() {
    this(0);
  }

  public UnsignedInt(final long value) {
    super(NativeInt.SIZE, value, true);
  }
}
