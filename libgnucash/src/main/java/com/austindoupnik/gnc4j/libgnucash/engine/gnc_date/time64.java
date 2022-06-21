package com.austindoupnik.gnc4j.libgnucash.engine.gnc_date;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

/**
 * Many systems, including Microsoft Windows and BSD-derived Unixes
 * like Darwin, are retaining the int-32 typedef for time_t. Since
 * this stops working in 2038, we define our own:
 */
public class time64 extends NativeLong {
  private static final long serialVersionUID = 350018756716653128L;

  public static class ByReference extends NativeLongByReference {

  }
}
