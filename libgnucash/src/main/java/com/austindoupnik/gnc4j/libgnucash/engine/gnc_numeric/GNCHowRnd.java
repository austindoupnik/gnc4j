package com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Rounding/Truncation modes for operations.
 * Rounding instructions control how fractional parts in the specified
 * denominator affect the result. For example, if a computed result is
 * "3/4" but the specified denominator for the return value is 2, should
 * the return value be "1/2" or "2/2"?
 * <p>
 * Watch out: You \e must specify a rounding policy such as
 * GNC_HOW_RND_NEVER, otherwise the fractional part of the input value
 * might silently get discarded!
 * <p>
 * Possible rounding instructions are:
 */
@Getter
@RequiredArgsConstructor
public enum GNCHowRnd implements JnaEnum<GNCHowRnd> {
  /**
   * Round toward -infinity
   */
  GNC_HOW_RND_FLOOR(0x01),

  /**
   * Round toward +infinity
   */
  GNC_HOW_RND_CEIL(0x02),

  /**
   * Truncate fractions (round toward zero)
   */
  GNC_HOW_RND_TRUNC(0x03),

  /**
   * Promote fractions (round away from zero)
   */
  GNC_HOW_RND_PROMOTE(0x04),

  /**
   * Round to the nearest integer, rounding toward zero
   * when there are two equidistant nearest integers.
   */
  GNC_HOW_RND_ROUND_HALF_DOWN(0x05),

  /**
   * Round to the nearest integer, rounding away from zero
   * when there are two equidistant nearest integers.
   */
  GNC_HOW_RND_ROUND_HALF_UP(0x06),

  /**
   * Use unbiased ("banker's") rounding. This rounds to the
   * nearest integer, and to the nearest even integer when there
   * are two equidistant nearest integers. This is generally the
   * one you should use for financial quantities.
   */
  GNC_HOW_RND_ROUND(0x07),

  /**
   * Never round at all, and signal an error if there is a
   * fractional result in a computation.
   */
  GNC_HOW_RND_NEVER(0x08);

  private final int value;
}
