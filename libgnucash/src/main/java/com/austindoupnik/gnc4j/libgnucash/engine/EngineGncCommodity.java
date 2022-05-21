package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import com.sun.jna.PointerType;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineGncCommodity {
  static {
    nativeRegister(EngineGncCommodity.class, "gnc-engine");
  }

  public static class gnc_commodity extends PointerType {

  }

  /**
   * Create a new commodity. This function allocates a new commodity
   * data structure, populates it with the data provided, and then
   * generates the dynamic names that exist as part of a commodity.
   *
   * @param book                The book that the new commodity will belong to.
   * @param fullname            The complete name of this commodity. E.G. "Acme
   *                            Systems, Inc."
   * @param commodity_namespace An aggregation of commodities. E.G. ISO4217,
   *                            Nasdaq, Downbelow, etc.
   * @param mnemonic            An abbreviation for this stock.  For publicly
   *                            traced stocks, this field should contain the stock ticker
   *                            symbol. This field is used to get online price quotes, so it must
   *                            match the stock ticker symbol used by the exchange where you want
   *                            to get automatic stock quote updates.  E.G. ACME, ACME.US, etc.
   * @param cusip               A string containing the CUSIP code or similar
   *                            UNIQUE code for this commodity like the ISIN. The stock ticker is
   *                            NOT appropriate as that goes in the mnemonic field.
   * @param fraction            The smallest division of this commodity
   *                            allowed. I.E. If this is 1, then the commodity must be traded in
   *                            whole units; if 100 then the commodity may be traded in 0.01
   *                            units, etc.
   * @return A pointer to the new commodity.
   * @note This function does not check to see if the commodity exists
   * before adding a new commodity.
   */
  public static native gnc_commodity gnc_commodity_new(
      final QofBook book,
      final String fullname,
      final String commodity_namespace,
      final String mnemonic,
      final String cusip,
      final int fraction
  );

  /**
   * A gnc_commodity_table is a database of commodity info.
   */
  public static class gnc_commodity_table extends PointerType {

  }

  /**
   * Returns the commodity table associated with a book.
   */
  public static native gnc_commodity_table gnc_commodity_table_get_table(final QofBook book);

  public static native gnc_commodity gnc_commodity_table_lookup(final gnc_commodity_table table, final String commodity_namespace, final String mnemonic);

  public static native gnc_commodity gnc_commodity_table_insert(final gnc_commodity_table table, final gnc_commodity comm);
}
