package com.austindoupnik.gnc4j.glib;

import com.sun.jna.IntegerType;
import com.sun.jna.ptr.ByteByReference;

public class GUInt8 extends IntegerType {
  private static final long serialVersionUID = -4952746932558422547L;

  public static class ByReference extends ByteByReference {

  }

  public GUInt8() {
    this(0);
  }

  public GUInt8(final long value) {
    super(1, value, true);
  }
}
