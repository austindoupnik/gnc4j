package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.Native;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EngineQofUtil {
  static {
    Native.register("gnc-engine");
  }

  /**
   * Initialise the Query Object Framework
   * Use in place of separate init functions (like qof_query_init(),
   * etc.) to protect against future changes.
   */
  public static native void qof_init();

  /**
   * Safely close down the Query Object Framework
   * Use in place of separate close / shutdown functions
   * (like qof_query_shutdown(), etc.) to protect
   * against future changes.
   */
  public static native void qof_close();
}
