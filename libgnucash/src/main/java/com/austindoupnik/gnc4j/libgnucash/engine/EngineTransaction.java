package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.PointerType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncCommodity.gnc_commodity;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;

@UtilityClass
public class EngineTransaction {
  static {
    nativeRegister(EngineTransaction.class, "gnc-engine");
  }

  public static class Transaction extends PointerType {

  }

  /**
   * The xaccMallocTransaction() will malloc memory and initialize it.
   * Once created, it is usually unsafe to merely "free" this memory;
   * the xaccTransDestroy() method should be called.
   */
  public static native Transaction xaccMallocTransaction(final QofBook book);

  /**
   * The xaccTransBeginEdit() method must be called before any changes
   * are made to a transaction or any of its component splits.  If
   * this is not done, errors will result.
   */
  public static native void xaccTransBeginEdit(final Transaction trans);

  /**
   * The xaccTransCommitEdit() method indicates that the changes to the
   * transaction and its splits are complete and should be made
   * permanent. Note this routine may result in the deletion of the
   * transaction, if the transaction is "empty" (has no splits), or
   * of xaccTransDestroy() was called on the transaction.
   */
  public static native void xaccTransCommitEdit(final Transaction trans);

  /**
   * The xaccTransRollbackEdit() routine rejects all edits made, and
   * sets the transaction back to where it was before the editing
   * started.  This includes restoring any deleted splits, removing
   * any added splits, and undoing the effects of xaccTransDestroy,
   * as well as restoring share quantities, memos, descriptions, etc.
   */
  public static native void xaccTransRollbackEdit(final Transaction trans);

  /**
   * Sets the transaction Number (or ID) field; rather than use this function
   * directly, see 'gnc_set_num_action' in engine/engine-helpers.c &amp; .h which
   * takes a user-set book option for selecting the source for the num-cell (the
   * transaction-number or the split-action field) in registers/reports into
   * account automatically
   */
  public static native void xaccTransSetNum(final Transaction trans, final String num);

  /**
   * Sets the transaction Description
   */
  public static native void xaccTransSetDescription(final Transaction transaction, final String description);

  /**
   * Set the commodity of this transaction.
   */
  public static native void xaccTransSetCurrency(final Transaction transaction, final gnc_commodity currency);

  /**
   * The xaccTransSetDate() method does the same thing as
   * xaccTransSetDate[Posted]Secs(), but takes a convenient
   * day-month-year format.
   * (Footnote: this shouldn't matter to a user, but anyone modifying
   * the engine should understand that when xaccTransCommitEdit() is
   * called, the date order of each of the component splits will be
   * checked, and will be restored in ascending date order.)
   */
  public static native void xaccTransSetDate(final Transaction trans, final int day, final int mon, final int year);

  /**
   * @see #xaccTransSetDate(Transaction, int, int, int)
   */
  public static void xaccTransSetDate(final Transaction trans, final LocalDate d) {
    xaccTransSetDate(trans, d.getDayOfMonth(), d.getMonthValue(), d.getYear());
  }

  /**
   * Gets the transaction Description
   */
  public static native String xaccTransGetDescription(final Transaction trans);
}
