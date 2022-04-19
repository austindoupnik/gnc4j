package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.Native;
import com.sun.jna.PointerType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EngineQofBook {
  static {
    Native.register("gnc-engine");
  }

  public static class QofBook extends PointerType {

  }
}
