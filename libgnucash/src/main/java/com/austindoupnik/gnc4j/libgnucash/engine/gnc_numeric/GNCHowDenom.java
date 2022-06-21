package com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * How to compute a denominator, or'ed into the "how" field.
 */
@Getter
@RequiredArgsConstructor
public enum GNCHowDenom implements JnaEnum<GNCHowDenom> {
  /**
   * Use any denominator which gives an exactly correct ratio of
   * numerator to denominator. Use EXACT when you do not wish to
   * lose any information in the result but also do not want to
   * spend any time finding the "best" denominator.
   */
  GNC_HOW_DENOM_EXACT(0x10),

  /**
   * Reduce the result value by common factor elimination,
   * using the smallest possible value for the denominator that
   * keeps the correct ratio. The numerator and denominator of
   * the result are relatively prime.
   */
  GNC_HOW_DENOM_REDUCE(0x20),

  /**
   * Find the least common multiple of the arguments' denominators
   * and use that as the denominator of the result.
   */
  GNC_HOW_DENOM_LCD(0x30),

  /**
   * All arguments are required to have the same denominator,
   * that denominator is to be used in the output, and an error
   * is to be signaled if any argument has a different denominator.
   */
  GNC_HOW_DENOM_FIXED(0x40),

  /**
   * Round to the number of significant figures given in the rounding
   * instructions by the GNC_HOW_DENOM_SIGFIGS () macro.
   */
  GNC_HOW_DENOM_SIGFIG(0x50);

  private final int value;
}
