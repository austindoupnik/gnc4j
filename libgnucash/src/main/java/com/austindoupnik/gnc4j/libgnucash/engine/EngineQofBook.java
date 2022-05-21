package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.PointerType;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineQofBook {
  static {
    nativeRegister(EngineQofBook.class, "gnc-engine");
  }

  public static class QofBook extends PointerType {

  }
}
