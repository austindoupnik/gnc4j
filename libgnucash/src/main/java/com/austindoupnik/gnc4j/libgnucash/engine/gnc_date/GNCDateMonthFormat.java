package com.austindoupnik.gnc4j.libgnucash.engine.gnc_date;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This is how to format the month, as a number, an abbreviated string,
 * or the full name.
 */
@Getter
@RequiredArgsConstructor
public enum GNCDateMonthFormat implements JnaEnum<GNCDateMonthFormat> {
  GNCDATE_MONTH_NUMBER(0),
  GNCDATE_MONTH_ABBREV(1),
  GNCDATE_MONTH_NAME(2);

  public static GNCDateMonthFormat findByValue(final int v) {
    return JnaEnum.findByValue(GNCDateMonthFormat.class, v);
  }

  public static class ByReference extends JnaEnumByReference<GNCDateMonthFormat> {
    @Override
    protected GNCDateMonthFormat findByValue(int value) {
      return GNCDateMonthFormat.findByValue(value);
    }
  }

  private final int value;
}
