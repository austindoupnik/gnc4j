package com.austindoupnik.gnc4j.libgnucash.core_utils;

import com.sun.jna.Native;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CoreUtilsGncVersion {
  static {
    Native.register("core-utils");
  }

  public static native String gnc_version();
}
