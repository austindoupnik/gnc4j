package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.libgnucash.engine.EngineAccount.Account;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncNumeric.gnc_numeric;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineTransaction.Transaction;

@UtilityClass
public class EngineSplit {
  static {
    Native.register("gnc-engine");
  }

  public static class Split extends PointerType {

  }

  /**
   * Constructor.
   */
  public static native Split xaccMallocSplit(final QofBook book);

  public static native void xaccSplitSetAccount(final Split split, final Account account);

  public static native void xaccSplitSetParent(final Split split, final Transaction trans);

  /**
   * The xaccSplitSetAmount() method sets the amount in the account's
   * commodity that the split should have.
   * <p>
   * The following four setter functions set the prices and amounts.
   * All of the routines always maintain balance: that is, invoking any
   * of them will cause other splits in the transaction to be modified
   * so that the net value of the transaction is zero.
   * <p>
   * IMPORTANT: The split should be parented by an account before
   * any of these routines are invoked!  This is because the actual
   * setting of amounts/values requires SCU settings from the account.
   * If these are not available, then amounts/values will be set to
   * -1/0, which is an invalid value.  I believe this order dependency
   * is a bug, but I'm too lazy to find, fix &amp; test at the moment ...
   *
   * @note If you use this on a newly created transaction, make sure
   * that the 'value' is also set so that it doesn't remain zero.
   */
  public static native void xaccSplitSetAmount(final Split split, final gnc_numeric amount);

  /**
   * The xaccSplitSetValue() method sets the value of this split in the
   * transaction's commodity.
   *
   * @note If you use this on a newly created transaction, make sure
   * that the 'amount' is also set so that it doesn't remain zero.
   */
  public static native void xaccSplitSetValue(final Split split, final gnc_numeric.ByValue value);

  /**
   * The xaccSplitSetSharePriceAndAmount() method will simultaneously
   * update the share price and the number of shares. This is a utility
   * routine that is equivalent to a xaccSplitSetSharePrice() followed
   * by and xaccSplitSetAmount(), except that it incurs the processing
   * overhead of balancing only once, instead of twice.
   */
  public static native void xaccSplitSetSharePriceAndAmount(
      final Split split,
      final gnc_numeric.ByValue price,
      final gnc_numeric.ByValue amount
  );
}
