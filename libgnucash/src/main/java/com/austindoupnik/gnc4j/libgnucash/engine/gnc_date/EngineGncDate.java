package com.austindoupnik.gnc4j.libgnucash.engine.gnc_date;

import com.austindoupnik.gnc4j.glib.GConstPointer;
import com.austindoupnik.gnc4j.glib.GInt;
import com.austindoupnik.gnc4j.glib.GSize;
import com.austindoupnik.gnc4j.glib.GUInt;
import com.austindoupnik.gnc4j.jna_core.SizeT;
import com.sun.jna.Pointer;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.glib.GLibGDate.GDate;
import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
@SuppressWarnings("unused")
public class EngineGncDate {
  static {
    nativeRegister(EngineGncDate.class, "gnc-engine");
  }

  /* Replacements for POSIX functions which use time_t. Time_t is still
   * 32 bits in Microsoft Windows, Apple OSX, and some BSD versions even
   * when the rest of the system is 64-bits, as well as all 32-bit
   * versions of Unix. 32-bit time_t overflows at 03:14:07 UTC on
   * Tuesday, 19 January 2038 and so cannot represent dates after that.
   *
   * These functions use boost::date_time internally.
   */

  /**
   * fill out a time struct from a 64-bit time value.
   *
   * @param secs Seconds since 00:00:01 UTC 01 January 1970 (negative values
   *             are seconds before that moment).
   * @return A struct tm*, allocated on the heap. Must be freed with gnc_tm_free().
   * The time is adjusted for the current local time zone.
   */
  public static native tm gnc_localtime(final time64.ByReference secs);

  /**
   * fill out a time struct from a 64-bit time value adjusted for the current time zone.
   *
   * @param secs Seconds since 00:00:01 UTC 01 January 1970 (negative values
   *             are seconds before that moment)
   * @param time A struct tm* for the function to fill.
   *             The time is adjusted for the current local time zone.
   */
  public static native tm gnc_localtime_r(final time64.ByReference secs, final tm time);

  /**
   * fill out a time struct from a 64-bit time value
   *
   * @param secs Seconds since 00:00:01 UTC 01 January 1970 (negative values
   *             are seconds before that moment)
   * @return A struct tm*, allocated on the heap. Must be freed with gnc_tm_free()
   * The time is UTC.
   */
  public static native tm gnc_gmtime(final time64.ByReference secs);

  /**
   * returns an integer corresponding to locale start of week
   *
   * @return An integer 1=Sunday, 2=Monday etc. If error, return 0.
   */
  public static native GInt gnc_start_of_week();

  /**
   * calculate seconds from the epoch given a time struct
   *
   * @param time A struct tm* containing the date-time information.
   *             The time is understood to be in the current local time zone.
   * @return Seconds since 00:00:01 UTC 01 January 1970 (negative values
   * are seconds before that moment).
   */
  public static native time64 gnc_mktime(final tm time);

  /**
   * calculate seconds from the epoch given a time struct
   *
   * @param time A struct tm* containing the date-time information
   *             The time is understood to be utc.
   * @return Seconds since 00:00:01 UTC 01 January 1970 (negative values
   * are seconds before that moment).
   */
  public static native time64 gnc_timegm(final tm time);

  /**
   * Return a string representation of a date from a 64-bit time value
   *
   * @param secs Seconds since 00:00:01 UTC 01 January 1970 (negative values
   *             are seconds before that moment)
   * @return A string, which must be freed with g_free(), representing the date
   * in the following format:
   * Thu Nov 24 18:22:48 1986\n\0
   * This is equivalent to the strftime format %a %b %H:%M:%S %Y.
   */
  public static native String gnc_ctime(final time64.ByReference secs);

  /**
   * get the current local time
   *
   * @param tbuf time64* which, if not NULL, will be filled in with the same
   *             value as is returned.
   * @return Seconds since 00:00:01 UTC 01 January 1970 (negative values
   * are seconds before that moment)
   */
  public static native time64 gnc_time(final time64.ByReference tbuf);

  /**
   * Find the difference in seconds between two time values
   *
   * @param secs1 The first time value, in Seconds since
   *              00:00:01 UTC 01 January 1970 (negative values are seconds before that moment)
   * @param secs2 The second time value, in Seconds since
   *              00:00:01 UTC 01 January 1970 (negative values are seconds before that moment)
   * @return The difference in seconds between secs1 and secs2. If secs2 is
   * later than secs1 the value will be negative.
   */
  public static native double gnc_difftime(final time64 secs1, final time64 secs2);

  /**
   * free a struct tm* created with gnc_localtime() or gnc_gmtime()
   *
   * @param time The struct tm* to be freed.
   */
  public static native void gnc_tm_free(final tm time);

  /* String / DateFormat conversion. */

  /**
   * The string-&gt;value versions return FALSE on success and TRUE on failure
   */
  public static native String gnc_date_dateformat_to_string(QofDateFormat format);

  /**
   * Converts the date format to a printable string.
   * Note the reversed return values!
   *
   * @return FALSE on success, TRUE on failure.
   */
  public static native boolean gnc_date_string_to_dateformat(final String format_string, final QofDateFormat.ByReference format);

  public static native String gnc_date_monthformat_to_string(GNCDateMonthFormat format);

  /**
   * Converts the month format to a printable string.
   * Note the reversed return values!
   *
   * @return FALSE on success, TRUE on failure.
   */
  public static native boolean gnc_date_string_to_monthformat(final String format_string, GNCDateMonthFormat.ByReference format);

  /**
   * print a time64 as a date string per format
   *
   * @param time   The time64 to print
   * @param format A date format conforming to the strftime format rules.
   * @return a raw heap-allocated char* which must be freed.
   */
  public static native String gnc_print_time64(final time64 time, final String format);

  /* GDate time64 setters */

  /**
   * Returns a newly allocated date of the current clock time, taken from
   * time(2). The caller must g_date_free() the object afterwards.
   */
  public static native GDate gnc_g_date_new_today();

  /**
   * Set a GDate to the current day
   *
   * @param gd The date to act on
   */
  public static native void gnc_gdate_set_today(final GDate gd);

  /**
   * Set a GDate to a time64
   *
   * @param gd   the date to act on
   * @param time the time to set it to.
   */
  public static native void gnc_gdate_set_time64(final GDate gd, final time64 time);

  /**
   * convert a time64 on a certain day (localtime) to
   * the time64 representing midday on that day. Watch out - this is *not* the
   * first second of the day, which is returned by various other functions
   * returning a time64.
   */
  public static native time64 time64CanonicalDayTime(final time64 t);

  /**
   * Turns a GDate into a time64, returning the first second of the day
   */
  public static native time64 gdate_to_time64(final GDate.ByValue d);

  /**
   * Convert a day, month, and year to a time64, returning the first second of the day
   */
  public static native time64 gnc_dmy2time64(final GInt day, final GInt month, final GInt year);

  /**
   * Converts a day, month, and year to a time64 representing 11:00:00 UTC
   * 11:00:00 UTC falls on the same time in almost all timezones, the exceptions
   * being the +13, +14, and -12 timezones used by countries along the
   * International Date Line. Since users in those timezones would see dates
   * immediately change by one day, the function checks the current timezone for
   * those changes and adjusts the UTC time so that the date will be consistent.
   */
  public static native time64 gnc_dmy2time64_neutral(final GInt day, final GInt month, final GInt year);

  /**
   * Same as gnc_dmy2time64, but last second of the day
   */
  public static native time64 gnc_dmy2time64_end(final GInt day, final GInt month, final GInt year);

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
   *
   * @return The universal time.
   * <p>
   * XXX Caution: this routine does not handle strings that specify
   * times before January 1 1970.
   */
  public static native time64 gnc_iso8601_to_time64_gmt(final String s);

  /**
   * The gnc_time64_to_iso8601_buff() routine takes the input
   * UTC time64 value and prints it as an ISO-8601 style string.
   * The buffer must be long enough to contain the NULL-terminated
   * string (32 characters + NUL).  This routine returns a pointer
   * to the null terminator (and can thus be used in the 'stpcpy'
   * metaphor of string concatenation).
   * <p>
   * Please note that ISO-8601 strings are a representation of
   * Universal Time (UTC), and as such, they 'store' UTC.  To make them
   * human readable, they show time zone information along with a
   * local-time string.  But fundamentally, they *are* UTC.  Thus,
   * this routine takes a UTC input, and returns a UTC output.
   * <p>
   * The string generated by this routine uses the local time zone
   * on the machine on which it is executing to create the time string.
   */
  public static native Pointer gnc_time64_to_iso8601_buff(final time64 value, final Pointer buff);

  /* QofDateFormat functions */

  /**
   * The qof_date_format_get routine returns the date format that
   * the date printing will use when printing a date, and the scanning
   * routines will assume when parsing a date.
   *
   * @return the one of the enumerated date formats.
   */
  public static native QofDateFormat qof_date_format_get();

  /**
   * The qof_date_format_set() routine sets date format to one of
   * US, UK, CE, OR ISO.  Checks to make sure it's a legal value.
   * Args: QofDateFormat: enumeration indicating preferred format
   */
  public static native void qof_date_format_set(final QofDateFormat df);

  /**
   * This function returns a strftime formatting string for printing an
   * all numeric date (e.g. 2005-09-14).  The string returned is based
   * upon the location specified.
   *
   * @param df The date style (us, uk, iso, etc) that should be provided.
   * @return A formatting string that will print a date in the
   * requested style
   */
  public static native String qof_date_format_get_string(final QofDateFormat df);

  /**
   * This function returns a strftime formatting string for printing a
   * date using words and numbers (e.g. 2005-September-14).  The string
   * returned is based upon the location specified.
   *
   * @param df The date style (us, uk, iso, etc) that should be provided.
   * @return A formatting string that will print a date in the
   * requested style
   */
  public static native String qof_date_text_format_get_string(final QofDateFormat df);

  /* ======================================================== */

  /**
   * The qof_date_completion_set() routing sets the date completion method to
   * one of QOF_DATE_COMPLETION_THISYEAR (for completing the year to
   * the current calendar year) or QOF_DATE_COMPLETION_SLIDING (for
   * using a sliding 12-month window). The sliding window starts
   * 'backmonth' months before the current month (0-11)
   */
  public static native void qof_date_completion_set(final QofDateCompletion dc, final int backmonths);

  /**
   * dateSeparator
   * Return the field separator for the current date format
   * <p>
   * Args:   none
   * <p>
   * Return: date character
   * <p>
   * Globals: global dateFormat value
   */
  public static native char dateSeparator();

  /*
   * @warning HACK ALERT -- the scan and print routines should probably
   * be moved to somewhere else. The engine really isn't involved with
   * things like printing formats. This is needed mostly by the GUI and
   * so on.  If a file-io thing needs date handling, it should do it
   * itself, instead of depending on the routines here.
   */

  /* qof_format_time takes a format specification in UTF-8 and a broken-down time,
   *  tries to call strftime with a sufficiently large buffer and, if successful,
   *  return a newly allocated string in UTF-8 for the printing result.
   *
   *  @param format A format specification in UTF-8.
   *
   *  @param tm A broken-down time.
   *
   *  @return A newly allocated string on success, or NULL otherwise.
   */
  /* gchar *qof_format_time(const gchar *format, const struct tm *tm); */

  /**
   * qof_strftime calls qof_format_time to print a given time and afterwards tries
   * to put the result into a buffer of fixed size.
   *
   * @param buf    A buffer.
   * @param max    The size of buf in bytes.
   * @param format A format specification in UTF-8.
   * @param tm     A broken-down time.
   * @return The number of characters written, not include the null byte, if the
   * complete string, including the null byte, fits into the buffer.  Otherwise 0.
   */
  public static native GSize qof_strftime(final Pointer buf, final GSize max, final String format, final tm tm);

  /**
   * qof_print_date_dmy_buff
   * Convert a date as day / month / year integers into a localized string
   * representation
   *
   * @param buff   pointer to previously allocated character array; its size
   *               must be at lease MAX_DATE_LENTH bytes.
   * @param buflen length of the buffer, in bytes.
   * @param day    day of the month as 1 ... 31
   * @param month  month of the year as 1 ... 12
   * @param year   year (4-digit)
   * @return number of characters printed
   * <p>
   * Globals: global dateFormat value
   **/
  public static native SizeT qof_print_date_dmy_buff(final Pointer buff, final SizeT buflen, final int day, final int month, final int year);

  /**
   * Convenience: calls through to qof_print_date_dmy_buff().
   **/
  public static native SizeT qof_print_date_buff(final Pointer buff, final SizeT buflen, final time64 secs);

  /**
   * Convenience; calls through to qof_print_date_dmy_buff().
   **/
  public static native SizeT qof_print_gdate(final Pointer buf, final SizeT bufflen, final GDate gd);

  /**
   * Convenience; calls through to qof_print_date_dmy_buff().
   * Return: string, which should be freed when no longer needed.
   **/
  public static native String qof_print_date(final time64 secs);

  /*
   * Date Start/End Adjustment routines
   * Given a time value, adjust it to be the beginning or end of that day.
   */

  /**
   * The gnc_tm_set_day_neutral() inline routine will set the appropriate
   * fields in the struct tm to indicate 10:59am of that day.
   */
  public static native void gnc_tm_set_day_neutral(final tm tm);

  /**
   * The gnc_time64_get_day_start() routine will take the given time in
   * seconds and adjust it to the first second of that day.
   */
  public static native time64 gnc_time64_get_day_start(final time64 time_val);

  /**
   * The gnc_time64_get_day_neutral() routine will take the given time in
   * seconds and adjust it to 10:59am of that day.
   */
  public static native time64 gnc_time64_get_day_neutral(final time64 time_val);

  /**
   * The gnc_time64_get_day_end() routine will take the given time in
   * seconds and adjust it to the last second of that day.
   */
  public static native time64 gnc_time64_get_day_end(final time64 time_val);

  /**
   * Get the numerical last date of the month. (28, 29, 30, 31)
   */
  public static native int gnc_date_get_last_mday(final int month, final int year);

  /* Today's Date */

  /**
   * The gnc_tm_get_today_start() routine takes a pointer to a struct
   * tm and fills it in with the first second of the today.
   */
  public static native void gnc_tm_get_today_start(final tm tm);

  /**
   * The gnc_tm_get_today_end() routine takes a pointer to a struct
   * tm and fills it in with the last second of the today.
   */
  public static native void gnc_tm_get_today_end(final tm tm);

  /**
   * The gnc_time64_get_today_start() routine returns a time64 value
   * corresponding to the first second of today.
   */
  public static native time64 gnc_time64_get_today_start();

  /**
   * The gnc_time64_get_today_end() routine returns a time64 value
   * corresponding to the last second of today.
   */
  public static native time64 gnc_time64_get_today_end();

  /**
   * Make a timestamp in YYYYMMDDHHMMSS format.
   *
   * @return A pointer to the generated string.
   * @note The caller owns this buffer and must g_free it when done.
   */
  public static native String gnc_date_timestamp();

  /**
   * Localized DOW abbreviation.
   *
   * @param buf_len at least MIN_BUF_LEN
   * @param dow     struct tm semantics: 0=sunday .. 6=saturday
   **/
  public static native void gnc_dow_abbrev(final Pointer buf, final int buf_len, final int dow);

  /* GDate hash table support */

  /**
   * Compares two GDate*'s for equality; useful for using GDate*'s as
   * GHashTable keys.
   */
  public static native GInt gnc_gdate_equal(final GConstPointer gda, final GConstPointer gdb);

  /**
   * Provides a "hash" of a GDate* value; useful for using GDate*'s as
   * GHashTable keys.
   */
  public static native GUInt gnc_gdate_hash(final GConstPointer gd);

  /* GDate to time64 conversions */

  /**
   * Returns the GDate in which the time64 occurs.
   *
   * @param t The time64
   * @return A GDate for the day in which the time64 occurs.
   */
  public static native GDate time64_to_gdate(final time64 t);

  /**
   * The gnc_time64_get_day_start() routine will take the given time in
   * GLib GDate format and adjust it to the first second of that day.
   */
  public static native time64 gnc_time64_get_day_start_gdate(final GDate date);

  /**
   * The gnc_time64_get_day_end() routine will take the given time in
   * GLib GDate format and adjust it to the last second of that day.
   */
  public static native time64 gnc_time64_get_day_end_gdate(final GDate date);

  /* Date Manipulation */

  /**
   * This function modifies a GDate to set it to the first day of the
   * month in which it falls.  For example, if this function is called
   * with a date of 2003-09-24 the date will be modified to 2003-09-01.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_month_start(final GDate date);

  /**
   * This function modifies a GDate to set it to the last day of the
   * month in which it falls.  For example, if this function is called
   * with a date of 2003-09-24 the date will be modified to 2003-09-30.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_month_end(final GDate date);

  /**
   * This function modifies a GDate to set it to the first day of the
   * month prior to the one in which it falls.  For example, if this
   * function is called with a date of 2003-09-24 the date will be
   * modified to 2003-08-01.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_prev_month_start(final GDate date);

  /**
   * This function modifies a GDate to set it to the last day of the
   * month prior to the one in which it falls.  For example, if this
   * function is called with a date of 2003-09-24 the date will be
   * modified to 2003-08-31.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_prev_month_end(final GDate date);

  /**
   * This function modifies a GDate to set it to the first day of the
   * quarter in which it falls.  For example, if this function is called
   * with a date of 2003-09-24 the date will be modified to 2003-09-01.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_quarter_start(final GDate date);

  /**
   * This function modifies a GDate to set it to the last day of the
   * quarter in which it falls.  For example, if this function is called
   * with a date of 2003-09-24 the date will be modified to 2003-12-31.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_quarter_end(final GDate date);

  /**
   * This function modifies a GDate to set it to the first day of the
   * quarter prior to the one in which it falls.  For example, if this
   * function is called with a date of 2003-09-24 the date will be
   * modified to 2003-06-01.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_prev_quarter_start(final GDate date);

  /**
   * This function modifies a GDate to set it to the last day of the
   * quarter prior to the one in which it falls.  For example, if this
   * function is called with a date of 2003-09-24 the date will be
   * modified to 2003-07-31.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_prev_quarter_end(final GDate date);

  /**
   * This function modifies a GDate to set it to the first day of the
   * year in which it falls.  For example, if this function is called
   * with a date of 2003-09-24 the date will be modified to 2003-01-01.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_year_start(final GDate date);

  /**
   * This function modifies a GDate to set it to the last day of the
   * year in which it falls.  For example, if this function is called
   * with a date of 2003-09-24 the date will be modified to 2003-12-31.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_year_end(final GDate date);

  /**
   * This function modifies a GDate to set it to the first day of the
   * year prior to the one in which it falls.  For example, if this
   * function is called with a date of 2003-09-24 the date will be
   * modified to 2002-01-01.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_prev_year_start(final GDate date);

  /**
   * This function modifies a GDate to set it to the last day of the
   * year prior to the one in which it falls.  For example, if this
   * function is called with a date of 2003-09-24 the date will be
   * modified to 2002-12-31.
   *
   * @param date The GDate to modify.
   */
  public static native void gnc_gdate_set_prev_year_end(final GDate date);

  /**
   * This function modifies a GDate to set it to the first day of the
   * fiscal year in which it falls.  For example, if this function is
   * called with a date of 2003-09-24 and a fiscal year ending July
   * 31st, the date will be modified to 2003-08-01.
   *
   * @param date     The GDate to modify.
   * @param year_end A GDate containing the last month and day of the
   *                 fiscal year.  The year field of this argument is ignored.
   */
  public static native void gnc_gdate_set_fiscal_year_start(final GDate date, final GDate year_end);

  /**
   * This function modifies a GDate to set it to the last day of the
   * fiscal year in which it falls.  For example, if this function is
   * called with a date of 2003-09-24 and a fiscal year ending July
   * 31st, the date will be modified to 2004-07-31.
   *
   * @param date     The GDate to modify.
   * @param year_end A GDate containing the last month and day of the
   *                 fiscal year.  The year field of this argument is ignored.
   */
  public static native void gnc_gdate_set_fiscal_year_end(final GDate date, final GDate year_end);

  /**
   * This function modifies a GDate to set it to the first day of the
   * fiscal year prior to the one in which it falls.  For example, if
   * this function is called with a date of 2003-09-24 and a fiscal
   * year ending July 31st, the date will be modified to 2002-08-01.
   *
   * @param date     The GDate to modify.
   * @param year_end A GDate containing the last month and day of the
   *                 fiscal year.  The year field of this argument is ignored.
   */
  public static native void gnc_gdate_set_prev_fiscal_year_start(final GDate date, final GDate year_end);

  /**
   * This function modifies a GDate to set it to the last day of the
   * fiscal year prior to the one in which it falls.  For example, if
   * this function is called with a date of 2003-09-24 and a fiscal
   * year ending July 31st, the date will be modified to 2003-07-31.
   *
   * @param date     The GDate to modify.
   * @param year_end A GDate containing the last month and day of the
   *                 fiscal year.  The year field of this argument is ignored.
   */
  public static native void gnc_gdate_set_prev_fiscal_year_end(final GDate date, final GDate year_end);
}
