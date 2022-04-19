package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.Native;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EngineGncSession {
  static {
    Native.register("gnc-engine");
  }

  public static native EngineQofSession.QofSession gnc_get_current_session();
}
