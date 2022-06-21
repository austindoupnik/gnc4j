package com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric;

import com.austindoupnik.gnc4j.glib.GInt;
import com.austindoupnik.gnc4j.glib.GInt64;
import com.austindoupnik.gnc4j.glib.GUInt8;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
@SuppressWarnings("unused")
public class EngineGncNumeric {
  static {
    nativeRegister(EngineGncNumeric.class, "gnc-engine");
  }

  /**
   * Convert a floating-point number to a gnc_numeric.
   * <p>
   * Both 'denom' and 'how' are used as in arithmetic.
   * <p>
   *
   * @param n     The double value that is converted into a gnc_numeric
   * @param denom The denominator of the gnc_numeric return value. If
   *              the 'how' argument contains the GNC_HOW_DENOM_SIGFIG flag, this
   *              value will be ignored.  If GNC_DENOM_AUTO is given an appropriate
   *              power of ten will be used for the denominator (it may be reduced
   *              by rounding if appropriate).
   * @param how   Describes the rounding policy and output
   *              denominator. Watch out: You must specify a rounding policy such
   *              as GNC_HOW_RND_NEVER, otherwise the fractional part of the input
   *              value is silently discarded! Common values for 'how' are
   *              (GNC_HOW_DENOM_REDUCE|GNC_HOW_RND_NEVER) or
   *              (GNC_HOW_DENOM_FIXED|GNC_HOW_RND_NEVER).
   * @return The newly created gnc_numeric rational value.
   */
  public static native gnc_numeric double_to_gnc_numeric(final double n, final GInt64 denom, final GInt how);

  /**
   * Read a gnc_numeric from str, skipping any leading whitespace.
   * Return TRUE on success and store the resulting value in "n".
   * Return NULL on error.
   */
  public static native boolean string_to_gnc_numeric(final String str, final gnc_numeric n);

  /**
   * Create a gnc_numeric object that signals the error condition
   * noted by error_code, rather than a number.
   */
  public static native gnc_numeric gnc_numeric_error(final GNCNumericErrorCode error_code);

  /**
   * Returns a string representation of the given GNCNumericErrorCode.
   */
  public static native String gnc_numeric_errorCode_to_string(final GNCNumericErrorCode error_code);

  /**
   * Convert numeric to floating-point value.
   */
  public static native double gnc_numeric_to_double(final gnc_numeric.ByValue n);

  /**
   * Convert to string. The returned buffer is to be g_free'd by the
   * caller (it was allocated through g_strdup)
   */
  public static native String gnc_numeric_to_string(final gnc_numeric.ByValue n);

  /**
   * Convert to string. Uses a static, non-thread-safe buffer.
   * For internal use only.
   */
  public static native String gnc_num_dbg_to_string(final gnc_numeric.ByValue n);

  /* Comparisons and Predicates */

  /**
   * Check for error signal in value. Returns GNC_ERROR_OK (==0) if
   * the number appears to be valid, otherwise it returns the
   * type of error.  Error values always have a denominator of zero.
   */
  public static native GNCNumericErrorCode gnc_numeric_check(final gnc_numeric a);

  /**
   * Returns 1 if a&gt;b, -1 if b&gt;a, 0 if a == b
   */
  public static native GInt gnc_numeric_compare(final gnc_numeric a, final gnc_numeric b);

  /**
   * Returns 1 if the given gnc_numeric is 0 (zero), else returns 0.
   */
  public static native boolean gnc_numeric_zero_p(final gnc_numeric.ByValue a);

  /**
   * Returns 1 if a &lt; 0, otherwise returns 0.
   */
  public static native boolean gnc_numeric_negative_p(final gnc_numeric.ByValue a);

  /**
   * Returns 1 if a &gt; 0, otherwise returns 0.
   */
  public static native boolean gnc_numeric_positive_p(final gnc_numeric.ByValue a);

  /**
   * Equivalence predicate: Returns TRUE (1) if a and b are
   * exactly the same (have the same numerator and denominator)
   */
  public static native boolean gnc_numeric_eq(final gnc_numeric.ByValue a, final gnc_numeric.ByValue b);

  /**
   * Equivalence predicate: Returns TRUE (1) if a and b represent
   * the same number.  That is, return TRUE if the ratios, when
   * reduced by eliminating common factors, are identical.
   */
  public static native boolean gnc_numeric_equal(final gnc_numeric.ByValue a, final gnc_numeric.ByValue b);

  /**
   * Equivalence predicate:
   * Convert both a and b to denom using the
   * specified DENOM and method HOW, and compare numerators
   * the results using gnc_numeric_equal.
   * <p>
   * For example, if a == 7/16 and b == 3/4,
   * gnc_numeric_same(a, b, 2, GNC_HOW_RND_TRUNC) == 1
   * because both 7/16 and 3/4 round to 1/2 under truncation. However,
   * gnc_numeric_same(a, b, 2, GNC_HOW_RND_ROUND) == 0
   * because 7/16 rounds to 1/2 under unbiased rounding but 3/4 rounds
   * to 2/2.
   */
  public static native GInt gnc_numeric_same(final gnc_numeric.ByValue a, final gnc_numeric.ByValue b, final GInt64 denom, final GInt how);

  /* Arithmetic Operations */

  /**
   * Return a+b.
   */
  public static native gnc_numeric.ByValue gnc_numeric_add(final gnc_numeric.ByValue a, final gnc_numeric.ByValue b, final GInt64 denom, final GInt how);

  /**
   * Return a-b.
   */
  public static native gnc_numeric.ByValue gnc_numeric_sub(final gnc_numeric.ByValue a, final gnc_numeric.ByValue b, final GInt64 denom, final GInt how);

  /**
   * Multiply a times b, returning the product.  An overflow
   * may occur if the result of the multiplication can't
   * be represented as a ratio of 64-bit int's after removing
   * common factors.
   */
  public static native gnc_numeric.ByValue gnc_numeric_mul(final gnc_numeric.ByValue a, final gnc_numeric.ByValue b, final GInt64 denom, final GInt how);

  /**
   * Division.  Note that division can overflow, in the following
   * sense: if we write x=a/b and y=c/d  then x/y = (a*d)/(b*c)
   * If, after eliminating all common factors between the numerator
   * (a*d) and the denominator (b*c),  then if either the numerator
   * and/or the denominator are *still* greater than 2^63, then
   * the division has overflowed.
   */
  public static native gnc_numeric.ByValue gnc_numeric_div(final gnc_numeric.ByValue x, final gnc_numeric.ByValue y, final GInt64 denom, final GInt how);

  /**
   * Returns a newly created gnc_numeric that is the negative of the
   * given gnc_numeric value. For a given gnc_numeric "a/b" the returned
   * value is "-a/b".
   */
  public static native gnc_numeric.ByValue gnc_numeric_neg(final gnc_numeric.ByValue a);

  /**
   * Returns a newly created gnc_numeric that is the absolute value of
   * the given gnc_numeric value. For a given gnc_numeric "a/b" the
   * returned value is "|a/b|".
   */
  public static native gnc_numeric.ByValue gnc_numeric_abs(final gnc_numeric.ByValue a);

  /* Change Denominator */

  /**
   * Change the denominator of a gnc_numeric value to the
   * specified denominator under standard arguments
   * 'denom' and 'how'.
   */
  public static native gnc_numeric.ByValue gnc_numeric_convert(final gnc_numeric.ByValue n, final GInt64 denom, final GInt how);

  /**
   * Return input after reducing it by Greater Common Factor (GCF)
   * elimination
   */
  public static native gnc_numeric.ByValue gnc_numeric_reduce(final gnc_numeric.ByValue n);

  /**
   * Attempt to convert the denominator to an exact power of ten without
   * rounding.
   *
   * @param a                  the ::gnc_numeric value to convert
   * @param max_decimal_places the number of decimal places of the
   *                           converted value. This parameter may be @c NULL.
   * @return TRUE if <i>a</i> has been converted or was already decimal.
   * Otherwise, FALSE is returned and <i>a</i> and <i>max_decimal_places</i>
   * remain unchanged.
   */
  public static native boolean gnc_numeric_to_decimal(final gnc_numeric a, final GUInt8.ByReference max_decimal_places);

  /**
   * Invert a gnc_numeric.
   * Much faster than dividing 1 by it.
   *
   * @param num The number to be inverted
   * @return a gnc_numeric that is the inverse of num
   */
  public static native gnc_numeric.ByValue gnc_numeric_invert(final gnc_numeric num);
}
