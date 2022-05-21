package com.austindoupnik.gnc4j.libgnucash.engine;

import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineQofLog {
  static {
    nativeRegister(EngineQofLog.class, "gnc-engine");
  }

  /**
   * If @a log_to_filename is "stderr" or "stdout" (exactly,
   * case-insensitive), then those special files are used; otherwise, the
   * literal filename as given, as qof_log_init_filename(gchar*)
   **/
  public static native void qof_log_init_filename_special(final String log_to_filename);

  /**
   * Set the logging level of the given log_module.
   **/
  public static native void qof_log_set_level(final String module, final int level);

  public static native int qof_log_level_from_string(final String str);
}
