package com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine;

import com.sun.jna.StringArray;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
@SuppressWarnings("unused")
public class EngineGncEngine {
  static {
    nativeRegister(EngineGncEngine.class, "gnc-engine");
  }

  /**
   * gnc_engine_init should be called before gnc engine
   * functions can be used.
   */
  public static native void gnc_engine_init(final int argc, final StringArray argv);

  /**
   * @see #gnc_engine_init(int, StringArray)
   */
  public static void gnc_engine_init(final int argc, final String[] argv) {
    gnc_engine_init(argc, new StringArray(argv));
  }
}
