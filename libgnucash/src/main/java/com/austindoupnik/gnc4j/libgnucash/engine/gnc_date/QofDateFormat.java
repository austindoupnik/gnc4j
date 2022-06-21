package com.austindoupnik.gnc4j.libgnucash.engine.gnc_date;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum for determining a date format
 */
@Getter
@RequiredArgsConstructor
public enum QofDateFormat implements JnaEnum<QofDateFormat> {
  /**
   * United states: mm/dd/yyyy
   */
  QOF_DATE_FORMAT_US(0),
  /**
   * Britain: dd/mm/yyyy
   */
  QOF_DATE_FORMAT_UK(1),
  /**
   * Continental Europe: dd.mm.yyyy
   */
  QOF_DATE_FORMAT_CE(2),
  /**
   * ISO: yyyy-mm-dd
   */
  QOF_DATE_FORMAT_ISO(3),
  /**
   * Take from locale information
   */
  QOF_DATE_FORMAT_LOCALE(4),
  /**
   * UTC: 2004-12-12T23:39:11Z
   */
  QOF_DATE_FORMAT_UTC(5),
  /**
   * Used by the check printing code
   */
  QOF_DATE_FORMAT_CUSTOM(6),
  /**
   * No Fancy Date Format, use Global
   */
  QOF_DATE_FORMAT_UNSET(7);

  public static QofDateFormat findByValue(final int v) {
    return JnaEnum.findByValue(QofDateFormat.class, v);
  }

  public static class ByReference extends JnaEnumByReference<QofDateFormat> {
    @Override
    protected QofDateFormat findByValue(int value) {
      return QofDateFormat.findByValue(value);
    }
  }

  private final int value;
}
