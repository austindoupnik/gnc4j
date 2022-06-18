package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.glib.GInt;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.StringArray;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.glib.GLibGList.GList;
import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineGncEngine {
  static {
    nativeRegister(EngineGncEngine.class, "gnc-engine");
  }

  interface TransactionCallback extends Callback {
    GInt invoke(final Transaction t, final Pointer data);
  }

  @NoArgsConstructor
  public static class GNCLot extends PointerType {
    public GNCLot(final Pointer p) {
      super(p);
    }
  }

  @NoArgsConstructor
  public static class gnc_quote_source extends PointerType {

  }

  /**
   * @brief Transaction in Gnucash.
   * A Transaction is a piece of business done; the transfer of money
   * from one account to one or more other accounts. Each Transaction is
   * divided into one or more Splits (usually two).
   * <p>
   * This is the typename for a transaction. The actual structure is
   * defined in the private header TransactionP.h, but no one outside
   * the engine should include that file. Instead, access that data only
   * through the functions in Transaction.h .
   */
  public static class Transaction extends PointerType {

  }

  /**
   * @brief An article that is bought and sold.
   * A Commodity is the most general term of what an account keeps track
   * of. Usually this is a monetary currency, but it can also be a stock
   * share or even a precious metal. Every account keeps track of
   * exactly one gnc_commodity.
   * <p>
   * (Up to version 1.6.x, we used to have currencies and
   * securities. Now these concepts have been merged into this
   * gnc_commodity. See the comments at xaccAccountSetCommodity() for
   * more about that.)
   * <p>
   * This is the typename for a gnc_commodity. The actual structure is
   * defined in a private source file. For accessing that data, only use
   * the functions in gnc-commodity.h .
   */
  public static class gnc_commodity extends PointerType {

  }

  /**
   * @brief A gnc_commodity_namespace is an collection of commodities.
   */
  public static class gnc_commodity_namespace extends PointerType {

  }

  /**
   * @brief A gnc_commodity_table is a database of commodity info.
   */
  public static class gnc_commodity_table extends PointerType {

  }

  public class LotList extends GList {

  }

  public class SplitList extends GList {

  }

  /**
   * gnc_engine_init should be called before gnc engine
   * functions can be used.
   */
  public static native void gnc_engine_init(final int argc, final StringArray argv);

  /**
   * @see #gnc_engine_init(int, StringArray)
   */
  public static void gnc_engine_init(final int argc, final String[] argv) {
    gnc_engine_init(argc, new StringArray(argv));
  }
}
