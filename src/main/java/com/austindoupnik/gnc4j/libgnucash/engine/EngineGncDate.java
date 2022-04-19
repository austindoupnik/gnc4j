package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EngineGncDate {
  static {
    Native.register("gnc-engine");
  }

  /**
   * Many systems, including Microsoft Windows and BSD-derived Unixes
   * like Darwin, are retaining the int-32 typedef for time_t. Since
   * this stops working in 2038, we define our own:
   */
  public static class time64 extends NativeLong {

  }

  /**
   * The gnc_iso8601_to_time64_gmt() routine converts an ISO-8601 style
   * date/time string to time64.  Please note that ISO-8601 strings
   * are a representation of Universal Time (UTC), and as such, they
   * 'store' UTC.  To make them human readable, they show time zone
   * information along with a local-time string.  But fundamentally,
   * they *are* UTC.  Thus, this routine takes a UTC input, and
   * returns a UTC output.
   * <p>
   * For example: 1998-07-17 11:00:00.68-0500
   * is 680 milliseconds after 11 o'clock, central daylight time
   * It is also 680 milliseconds after 16:00:00 hours UTC.
   * \return The universal time.
   * <p>
   * XXX Caution: this routine does not handle strings that specify
   * times before January 1 1970.
   */
  public static native time64 gnc_iso8601_to_time64_gmt(final String s);
}
