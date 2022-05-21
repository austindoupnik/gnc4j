package com.austindoupnik.gnc4j.libgnucash.engine;

import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineGncSession {
  static {
    nativeRegister(EngineGncSession.class, "gnc-engine");
  }

  public static native EngineQofSession.QofSession gnc_get_current_session();
}
