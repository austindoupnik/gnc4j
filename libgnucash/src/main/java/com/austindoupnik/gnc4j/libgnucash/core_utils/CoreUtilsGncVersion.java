package com.austindoupnik.gnc4j.libgnucash.core_utils;

import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class CoreUtilsGncVersion {
  static {
    nativeRegister(CoreUtilsGncVersion.class, "core-utils");
  }

  public static native String gnc_version();
}
