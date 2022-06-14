package com.austindoupnik.gnc4j.glib;

import com.sun.jna.IntegerType;
import com.sun.jna.NativeLong;

public class GInt64 extends IntegerType {
  private static final long serialVersionUID = 940289737961374932L;

  public GInt64() {
    this(0);
  }

  public GInt64(final long value) {
    super(NativeLong.SIZE, value, false);
  }
}
