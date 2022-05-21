package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.libgnucash.engine.EngineGncCommodity.gnc_commodity;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineGncNumeric.gnc_numeric;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import com.sun.jna.PointerType;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineGncPriceDb {
  static {
    nativeRegister(EngineGncPriceDb.class, "gnc-engine");
  }

  public static class GNCPrice extends PointerType {

  }

  /**
   * Price source enum. Be sure to keep in sync with the source_name array in
   * gnc-pricedb.c. These are in preference order, so for example a quote with
   * PRICE_SOURCE_EDIT_DLG will overwrite one with PRICE_SOURCE_FQ but not the
   * other way around.
   */
  @AllArgsConstructor
  public enum PriceSource {
    /**
     * "user:price-editor"
     */
    PRICE_SOURCE_EDIT_DLG(0),
    /**
     * "Finance::Quote"
     */
    PRICE_SOURCE_FQ(1),
    /**
     * "user:price"
     */
    PRICE_SOURCE_USER_PRICE(2),
    /**
     * "user:xfer-dialog"
     */
    PRICE_SOURCE_XFER_DLG_VAL(3),
    /**
     * "user:split-register"
     */
    PRICE_SOURCE_SPLIT_REG(4),
    /**
     * "user:split-import"
     */
    PRICE_SOURCE_SPLIT_IMPORT(5),
    /**
     * "user:stock-split"
     */
    PRICE_SOURCE_STOCK_SPLIT(6),
    /**
     * "user:stock-transaction"
     */
    PRICE_SOURCE_STOCK_TRANSACTION(7),
    /**
     * "user:invoice-post"
     */
    PRICE_SOURCE_INVOICE(8),
    /**
     * "temporary"
     */
    PRICE_SOURCE_TEMP(9),
    /**
     * "invalid"
     */
    PRICE_SOURCE_INVALID(10);

    private final int value;
  }


  public static class GNCPriceDB extends PointerType {

  }

  /**
   * gnc_price_create - returns a newly allocated and initialized price
   * with a reference count of 1.
   */
  public static native GNCPrice gnc_price_create(final QofBook book);

  public static native void gnc_price_begin_edit(final GNCPrice p);

  public static native void gnc_price_commit_edit(final GNCPrice p);

  public static native void gnc_price_set_commodity(final GNCPrice p, final gnc_commodity c);

  public static native void gnc_price_set_currency(final GNCPrice p, final gnc_commodity c);

  public static native void gnc_price_set_time64(final GNCPrice p, EngineGncDate.time64 t);

  public static native void gnc_price_set_source(final GNCPrice p, final int source);

  /**
   * @see #gnc_price_set_source(GNCPrice, int)
   */
  public static void gnc_price_set_source(final GNCPrice p, final PriceSource source) {
    gnc_price_set_source(p, source.value);
  }

  public static native void gnc_price_set_source_string(final GNCPrice p, final String s);

  public static native void gnc_price_set_typestr(final GNCPrice p, final String type);

  public static native void gnc_price_set_value(final GNCPrice p, final gnc_numeric.ByValue value);

  /**
   * Return the pricedb associated with the book
   *
   * @param book The QofBook holding the pricedb
   * @return The GNCPriceDB associated with the book.
   */
  public static native GNCPriceDB gnc_pricedb_get_db(final QofBook book);

  /**
   * Begin an edit.
   */
  public static native void gnc_pricedb_begin_edit(final GNCPriceDB db);

  /**
   * Commit an edit.
   */
  public static native void gnc_pricedb_commit_edit(final GNCPriceDB db);

  /**
   * Add a price to the pricedb.
   * <p>
   * You may drop your reference to the price (i.e. call unref) after this
   * succeeds, whenever you're finished with the price.
   *
   * @param db The pricedb
   * @param p  The GNCPrice to add.
   * @return TRUE if the price was added, FALSE otherwise.
   */
  public static native boolean gnc_pricedb_add_price(final GNCPriceDB db, final GNCPrice p);
}
