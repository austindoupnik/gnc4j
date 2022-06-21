package com.austindoupnik.gnc4j.libgnucash.engine.gnc_commodity;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The quote source type enum account types are used to determine how
 * the transaction data in the account is displayed.  These values
 * can be safely changed from one release to the next.
 */
@Getter
@RequiredArgsConstructor
public enum QuoteSourceType implements JnaEnum<QuoteSourceType> {
  /**
   * This quote source pulls from a single
   * specific web site.  For example, the
   * yahoo_australia source only pulls from
   * the yahoo web site.
   */
  SOURCE_SINGLE(0),
  /**
   * This quote source may pull from multiple
   * web sites.  For example, the australia
   * source may pull from ASX, yahoo, etc.
   */
  SOURCE_MULTI(1),
  /**
   * This is a locally installed quote source
   * that gnucash knows nothing about. May
   * pull from single or multiple
   * locations.
   */
  SOURCE_UNKNOWN(2),
  SOURCE_MAX(3),
  /**
   * The special currency quote source.
   */
  SOURCE_CURRENCY(SOURCE_MAX.getValue());

  private final int value;
}
