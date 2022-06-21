package com.austindoupnik.gnc4j.libgnucash.engine.gnc_date;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum for date completion modes (for dates entered without year)
 */
@Getter
@RequiredArgsConstructor
public enum QofDateCompletion implements JnaEnum<QofDateCompletion> {
  /**
   * use current year
   */
  QOF_DATE_COMPLETION_THISYEAR(0),
  /**
   * use sliding 12-month window
   */
  QOF_DATE_COMPLETION_SLIDING(1);

  private final int value;
}
