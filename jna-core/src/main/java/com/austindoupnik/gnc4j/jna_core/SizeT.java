package com.austindoupnik.gnc4j.jna_core;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class SizeT extends IntegerType {
  private static final long serialVersionUID = 5359192072713827015L;

  public SizeT() {
    this(0);
  }

  public SizeT(final long value) {
    super(Native.SIZE_T_SIZE, value, true);
  }
}
