package com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error codes
 */
@Getter
@RequiredArgsConstructor
public enum GNCNumericErrorCode implements JnaEnum<GNCNumericErrorCode> {
  /**
   * No error
   */
  GNC_ERROR_OK(0),
  /**
   * Argument is not a valid number
   */
  GNC_ERROR_ARG(-1),
  /**
   * Intermediate result overflow
   */
  GNC_ERROR_OVERFLOW(-2),

  /**
   * GNC_HOW_DENOM_FIXED was specified, but argument denominators differed.
   */
  GNC_ERROR_DENOM_DIFF(-3),

  /**
   * GNC_HOW_RND_NEVER  was specified, but the result could not be
   * converted to the desired denominator without a remainder.
   */
  GNC_ERROR_REMAINDER(-4);

  private final int value;
}
