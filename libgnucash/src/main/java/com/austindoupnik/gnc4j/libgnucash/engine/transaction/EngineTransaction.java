package com.austindoupnik.gnc4j.libgnucash.engine.transaction;

import com.austindoupnik.gnc4j.glib.GUInt;
import com.austindoupnik.gnc4j.libgnucash.engine.account.Account;
import com.austindoupnik.gnc4j.libgnucash.engine.account.GncGUID;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_date.time64;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine.SplitList;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine.Transaction;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine.gnc_commodity;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric.gnc_numeric;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static com.austindoupnik.gnc4j.glib.GLibGDate.GDate;
import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncPriceDb.PriceSource;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineSplit.Split;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineSplit.xaccSplitSetParent;
import static com.austindoupnik.gnc4j.libgnucash.engine.gnc_commodity.EngineGncCommodity.MonetaryList;

@UtilityClass
@SuppressWarnings("unused")
public class EngineTransaction {
  static {
    nativeRegister(EngineTransaction.class, "gnc-engine");
  }

  /* Transaction creation and editing */

  /**
   * The xaccMallocTransaction() will malloc memory and initialize it.
   * Once created, it is usually unsafe to merely "free" this memory;
   * the xaccTransDestroy() method should be called.
   */
  public static native Transaction xaccMallocTransaction(final QofBook book);

  /**
   * Destroys a transaction.
   * Each split in transaction @a trans is removed from its
   * account and destroyed as well.
   * <p>
   * If the transaction has not already been opened for editing with
   * ::xaccTransBeginEdit() then the changes are committed immediately.
   * Otherwise, the caller must follow up with either
   * ::xaccTransCommitEdit(), in which case the transaction and
   * split memory will be freed, or xaccTransRollbackEdit(), in which
   * case nothing at all is freed, and everything is put back into
   * original order.
   *
   * @param trans the transaction to destroy
   */
  public static native void xaccTransDestroy(final Transaction trans);

  /**
   * The xaccTransClone() method will create a complete copy of an
   * existing transaction.
   */
  public static native Transaction xaccTransClone(final Transaction t);

  /**
   * The xaccTransCloneNoKvp() method will create a complete copy of an
   * existing transaction except that the KVP slots will be empty.
   */
  public static native Transaction xaccTransCloneNoKvp(final Transaction t);

  /**
   * Equality.
   *
   * @param ta             First transaction to compare
   * @param tb             Second transaction to compare
   * @param check_guids    If TRUE, try a guid_equal() on the GUIDs of both
   *                       transactions if their pointers are not equal in the first place.
   *                       Also passed to subsidiary calls to xaccSplitEqual.
   * @param check_splits   If TRUE, after checking the transaction data
   *                       structures for equality, also check all splits attached to the
   *                       transaction for equality.
   * @param check_balances If TRUE, when checking splits also compare
   *                       balances between the two splits.  Balances are recalculated
   *                       whenever a split is added or removed from an account, so YMMV on
   *                       whether this should be set.
   * @param assume_ordered If TRUE, assume that the splits in each
   *                       transaction appear in the same order.  This saves some time looking
   *                       up splits by GncGUID, and is required for checking duplicated
   *                       transactions because all the splits have new GUIDs.
   */
  public static native boolean xaccTransEqual(
      final Transaction ta,
      final Transaction tb,
      final boolean check_guids,
      final boolean check_splits,
      final boolean check_balances,
      final boolean assume_ordered
  );

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
   * The xaccTransIsOpen() method returns TRUE if the transaction
   * is open for editing. Otherwise, it returns false.
   * XXX this routine should probably be deprecated.  its, umm,
   * hard to imagine legitimate uses (but it is used by
   * the import/export code for reasons I can't understand.)
   */
  public static native boolean xaccTransIsOpen(final Transaction trans);

  /**
   * The xaccTransLookup() subroutine will return the
   * transaction associated with the given id, or NULL
   * if there is no such transaction.
   */
  public static native Transaction xaccTransLookup(final GncGUID guid, final QofBook book);

  /*################## Added for Reg2 #################*/

  /**
   * Copy a transaction to the 'clipboard' transaction using
   * dupe_transaction. The 'clipboard' transaction must never
   * be dereferenced.
   */
  public static native Transaction xaccTransCopyToClipBoard(final Transaction from_trans);

  /**
   * Copy a transaction to another using the function below without
   * changing any account information.
   */
  public static native void xaccTransCopyOnto(final Transaction from_trans, final Transaction to_trans);

  /**
   * This function explicitly must robustly handle some unusual input.
   * <p>
   * 'from_trans' may be a duped trans (see xaccDupeTransaction), so its
   * splits may not really belong to the accounts that they say they do.
   * <p>
   * 'from_acc' need not be a valid account. It may be an already freed
   * Account. Therefore, it must not be dereferenced at all.
   * <p>
   * Neither 'from_trans', nor 'from_acc', nor any of 'from's splits may be modified
   * in any way.
   * <p>
   * 'no_date' if TRUE will not copy the date posted.
   * <p>
   * The 'to_trans' transaction will end up with valid copies of from's
   * splits.  In addition, the copies of any of from's splits that were
   * in from_acc (or at least claimed to be) will end up in to_acc.
   */
  public static native void xaccTransCopyFromClipBoard(
      final Transaction from_trans,
      final Transaction to_trans,
      final Account from_acc,
      final Account to_acc,
      final boolean no_date
  );

  /*################## Added for Reg2 #################*/

  public static native Split xaccTransFindSplitByAccount(final Transaction trans, final Account acc);

  /**
   * The xaccTransScrubGains() routine performs a number of cleanup
   * functions on the indicated transaction, with the end-goal of
   * setting up a consistent set of gains/losses for all the splits
   * in the transaction.  This includes making sure that the lot
   * assignments of all the splits are good, and that the lots
   * balance appropriately.
   */
  public static native void xaccTransScrubGains(final Transaction trans, final Account gain_acc);


  /**
   * @warning XXX FIXME
   * gnc_book_count_transactions is a utility function,
   * probably needs to be moved to a utility file somewhere.
   */
  public static native GUInt gnc_book_count_transactions(final QofBook book);

  /* Transaction general getters/setters */

  /**
   * Determine whether this transaction should use commodity trading accounts
   */
  public static native boolean xaccTransUseTradingAccounts(final Transaction trans);

  /**
   * Sorts the splits in a transaction, putting the debits first,
   * followed by the credits.
   */
  public static native void xaccTransSortSplits(final Transaction trans);

  /**
   * Set the  Transaction Type
   * <p>
   * See #TXN_TYPE_NONE, #TXN_TYPE_INVOICE and #TXN_TYPE_PAYMENT
   */
  public static native void xaccTransSetTxnType(final Transaction trans, final char type);

  /**
   * Returns the  Transaction Type
   * <p>
   * See #TXN_TYPE_NONE, #TXN_TYPE_INVOICE and #TXN_TYPE_PAYMENT
   */
  public static native char xaccTransGetTxnType(final Transaction trans);

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
  public static native void xaccTransSetDescription(final Transaction trans, final String desc);

  /**
   * Sets the transaction Document Link
   */
  public static native void xaccTransSetDocLink(final Transaction trans, final String doclink);

  /**
   * Sets the transaction Notes
   * <p>
   * The Notes field is only visible in the register in double-line mode
   */
  public static native void xaccTransSetNotes(final Transaction trans, final String notes);

  /**
   * Gets the transaction Number (or ID) field; rather than use this function
   * directly, see 'gnc_get_num_action' and 'gnc_get_action_num' in
   * engine/engine-helpers.c &amp; .h which takes a user-set book option for
   * selecting the source for the num-cell (the transaction-number or the
   * split-action field) in registers/reports into account automatically
   */
  public static native String xaccTransGetNum(final Transaction trans);

  /**
   * Gets the transaction Description
   */
  public static native String xaccTransGetDescription(final Transaction trans);

  /**
   * Gets the transaction Document Link
   */
  public static native String xaccTransGetDocLink(final Transaction trans);

  /**
   * Gets the transaction Notes
   * <p>
   * The Notes field is only visible in the register in double-line mode
   */
  public static native String xaccTransGetNotes(final Transaction trans);


  /**
   * Sets whether or not this transaction is a "closing transaction"
   */
  public static native void xaccTransSetIsClosingTxn(final Transaction trans, final boolean is_closing);

  /**
   * Returns whether this transaction is a "closing transaction"
   */
  public static native boolean xaccTransGetIsClosingTxn(final Transaction trans);


  /**
   * Add a split to the transaction
   * <p>
   * The xaccTransAppendSplit() method will append the indicated
   * split to the collection of splits in this transaction.
   *
   * @note If the split is already a part of another transaction,
   * it will be removed from that transaction first.
   */
  public static void xaccTransAppendSplit(final Transaction t, final Split s) {
    xaccSplitSetParent(s, t);
  }

  /**
   * Return a pointer to the indexed split in this transaction's split list.
   * Note that the split list is a linked list and that indexed access is
   * O(N). Do not use this method for iteration.
   *
   * @param trans The transaction
   * @param i     The split number.  Valid values for i are zero to
   *              (number_of__splits-1).
   * @return A Split* or NULL if i is out of range.
   */
  public static native Split xaccTransGetSplit(final Transaction trans, final int i);

  /**
   * Inverse of xaccTransGetSplit()
   */
  public static native int xaccTransGetSplitIndex(final Transaction trans, final Split split);

  /**
   * The xaccTransGetSplitList() method returns a GList of the splits
   * in a transaction.
   *
   * @param trans The transaction
   * @return The list of splits. This list must NOT be modified.  Do *NOT* free
   * this list when you are done with it.
   */
  public static native SplitList xaccTransGetSplitList(final Transaction trans);

  /**
   * The xaccTransGetPaymentAcctSplitList() method returns a GList of the splits
   * in a transaction that belong to an account which is considered a
   * valid account for business payments.
   *
   * @param trans The transaction
   * @return The list of splits. This list must be freed when you are done with it.
   */
  public static native SplitList xaccTransGetPaymentAcctSplitList(final Transaction trans);

  /**
   * The xaccTransGetAPARSplitList() method returns a GList of the splits
   * in a transaction that belong to an AR or AP account.
   *
   * @param trans  The transaction
   * @param strict This slightly modifies the test to only consider splits in an AR or AP account and the split is part of a business lot
   * @return The list of splits. This list must be freed when you are done with it.
   */
  public static native SplitList xaccTransGetAPARAcctSplitList(final Transaction trans, final boolean strict);


  public static native boolean xaccTransStillHasSplit(final Transaction trans, final Split s);

  /**
   * The xaccTransGetFirstPaymentAcctSplit() method returns a pointer to the first
   * split in this transaction that belongs to an account which is considered a
   * valid account for business payments.
   *
   * @param trans The transaction
   *              If there is no such split in the transaction NULL will be returned.
   */
  public static native Split xaccTransGetFirstPaymentAcctSplit(final Transaction trans);

  /**
   * The xaccTransGetFirstPaymentAcctSplit() method returns a pointer to the first
   * split in this transaction that belongs to an AR or AP account.
   *
   * @param trans  The transaction
   * @param strict This slightly modifies the test to only consider splits in an AR or AP account and the split is part of a business lot
   *               If there is no such split in the transaction NULL will be returned.
   */
  public static native Split xaccTransGetFirstAPARAcctSplit(final Transaction trans, final boolean strict);

  /**
   * Set the transaction to be ReadOnly by setting a non-NULL value as "reason".
   * <p>
   * FIXME: If "reason" is NULL, this function does nothing, instead of removing the
   * readonly flag; the actual removal is possible only through
   * xaccTransClearReadOnly().
   */
  public static native void xaccTransSetReadOnly(final Transaction trans, final String reason);

  public static native void xaccTransClearReadOnly(final Transaction trans);

  /**
   * Returns a non-NULL value if this Transaction was marked as read-only with
   * some specific "reason" text.
   */
  public static native String xaccTransGetReadOnly(final Transaction trans);

  /**
   * Returns TRUE if this Transaction is read-only because its posted-date is
   * older than the "auto-readonly" threshold of this book. See
   * qof_book_uses_autofreeze() and qof_book_get_autofreeze_gdate().
   */
  public static native boolean xaccTransIsReadonlyByPostedDate(final Transaction trans);

  /*################## Added for Reg2 #################*/

  /**
   * Returns TRUE if this Transaction's posted-date is in the future
   */
  public static native boolean xaccTransInFutureByPostedDate(final Transaction trans);

  /*################## Added for Reg2 #################*/

  /**
   * Returns the number of splits in this transaction.
   */
  public static native int xaccTransCountSplits(final Transaction trans);

  /**
   * FIXME: document me
   */
  public static native boolean xaccTransHasReconciledSplits(final Transaction trans);

  /**
   * FIXME: document me
   */
  public static native boolean xaccTransHasReconciledSplitsByAccount(final Transaction trans, final Account account);

  /**
   * FIXME: document me
   */
  public static native boolean xaccTransHasSplitsInState(final Transaction trans, final char state);

  /**
   * FIXME: document me
   */
  public static native boolean xaccTransHasSplitsInStateByAccount(
      final Transaction trans,
      final char state,
      final Account account
  );


  /**
   * Returns the valuation commodity of this transaction.
   * <p>
   * Each transaction's valuation commodity, or 'currency' is, by definition,
   * the common currency in which all splits in the transaction can be valued.
   * The total value of the transaction must be zero when all splits
   * are valued in this currency.
   *
   * @note What happens if the Currency isn't set?  Ans: bad things.
   */
  public static native gnc_commodity xaccTransGetCurrency(final Transaction trans);

  /**
   * Set the commodity of this transaction.
   */
  public static native void xaccTransSetCurrency(final Transaction trans, final gnc_commodity curr);

  /**
   * The xaccTransGetImbalanceValue() method returns the total value of the
   * transaction.  In a pure double-entry system, this imbalance
   * should be exactly zero, and if it is not, something is broken.
   * However, when double-entry semantics are not enforced, unbalanced
   * transactions can sneak in, and this routine can be used to find
   * out how much things are off by.  The value returned is denominated
   * in the currency that is returned by the xaccTransFindCommonCurrency()
   * method.
   * <p>
   * If the use of currency exchange accounts is enabled then the a
   * a transaction must be balanced in each currency it uses to be considered
   * to be balanced.  The method xaccTransGetImbalance is used by most
   * code to take this into consideration.  This method is only used in a few
   * places that want the transaction value even if currency exchange accounts
   * are enabled.
   */
  public static native gnc_numeric.ByValue xaccTransGetImbalanceValue(final Transaction trans);

  /**
   * The xaccTransGetImbalance method returns a list giving the value of
   * the transaction in each currency for which the balance is not zero.
   * If the use of currency accounts is disabled, then this will be only
   * the common currency for the transaction and xaccTransGetImbalance
   * becomes equivalent to xaccTransGetImbalanceValue.  Otherwise it will
   * return a list containing the imbalance in each currency.
   */
  public static native MonetaryList xaccTransGetImbalance(final Transaction trans);

  /**
   * Returns true if the transaction is balanced according to the rules
   * currently in effect.
   */
  public static native boolean xaccTransIsBalanced(final Transaction trans);

  /**
   * The xaccTransGetAccountValue() method returns the total value applied
   * to a particular account.  In some cases there may be multiple Splits
   * in a single Transaction applied to one account (in particular when
   * trying to balance Lots) -- this function is just a convenience to
   * view everything at once.
   */
  public static native gnc_numeric.ByValue xaccTransGetAccountValue(final Transaction trans, final Account account);

  /**
   * Same as xaccTransGetAccountValue, but uses the Account's commodity.
   */
  public static native gnc_numeric.ByValue xaccTransGetAccountAmount(final Transaction trans, final Account account);

  /*################## Added for Reg2 #################*/
/* Gets the amt/val rate, i.e. rate from the transaction currency to
   the 'split_com' */
  public static native boolean
  xaccTransGetRateForCommodity(
      final Transaction trans,
      final gnc_commodity split_com,
      final Split split_to_exclude,
      final gnc_numeric rate
  );
  /*################## Added for Reg2 #################*/

  /* Compute the conversion rate for the transaction to this account.
   * Any "split value" (which is in the transaction currency),
   * multiplied by this conversion rate, will give you the value you
   * should display for this account.
   *
   * If 'acc' is NULL, return unity.
   */
  public static native gnc_numeric.ByValue xaccTransGetAccountConvRate(final Transaction txn, final Account acc);

  /**
   * Get the account balance for the specified account after the last
   * split in the specified transaction.
   */
  public static native gnc_numeric.ByValue xaccTransGetAccountBalance(final Transaction trans, final Account account);

  /**
   * The xaccTransOrder(ta,tb) method is useful for sorting.
   * Orders ta and tb
   * return &lt;0 if ta sorts before tb
   * return &gt;0 if ta sorts after tb
   * return 0 if they are absolutely equal
   * <p>
   * The comparrison uses the following fields, in order:
   * date posted  (compare as a date)
   * num field (compare as an integer)
   * date entered (compare as a date)
   * description field (comcpare as a string using strcmp())
   * GncGUID (compare as a guid)
   * Finally, it returns zero if all of the above match.
   * Note that it does *NOT* compare its member splits.
   * Note also that it calls xaccTransOrder_num_action with actna and actnb
   * set as NULL.
   */
  public static native int xaccTransOrder(final Transaction ta, final Transaction tb);


  /**
   * The xaccTransOrder_num_action(ta,actna,tb,actnb) method is useful for sorting.
   * Orders ta and tb
   * return &lt;0 if ta sorts before tb
   * return &gt;0 if ta sorts after tb
   * return 0 if they are absolutely equal
   * <p>
   * The comparrison uses the following fields, in order:
   * date posted  (compare as a date)
   * if actna and actnb are NULL,
   * num field (compare as an integer)
   * else actna and actnb  (compare as an integer)
   * date entered (compare as a date)
   * description field (comcpare as a string using strcmp())
   * GncGUID (compare as a guid)
   * Finally, it returns zero if all of the above match.
   * Note that it does *NOT* compare its member splits (except action as
   * specified above).
   */
  public static native int xaccTransOrder_num_action(
      final Transaction ta,
      final String actna,
      final Transaction tb,
      final String actnb
  );

  /* Transaction date setters/getters */

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
   * This method modifies <i>posted</i> date of the transaction,
   * specified by a GDate. The posted date is the date when this
   * transaction was posted at the bank.
   * <p>
   * This is identical to xaccTransSetDate(), but different from
   * xaccTransSetDatePostedSecs which artificially introduces the
   * time-of-day part, which needs to be ignored.
   */
  public static native void xaccTransSetDatePostedGDate(final Transaction trans, final GDate date);

  /**
   * The xaccTransSetDatePostedSecs() method will modify the <i>posted</i>
   * date of the transaction, specified by a time64 (see ctime(3)). The
   * posted date is the date when this transaction was posted at the
   * bank.
   * <p>
   * Please do not use this function, as the extra time-of-day part messes up a
   * lot of places. Rather, please use xaccTransSetDatePostedGDate() or
   * xaccTransSetDatePostedSecsNormalized().
   */
  public static native void xaccTransSetDatePostedSecs(final Transaction trans, final time64 time);

  /**
   * This function sets the <i>posted</i> date of the transaction, specified by
   * a time64 (see ctime(3)). Contrary to xaccTransSetDatePostedSecs(), the time
   * will be normalized to only the date part, and the time-of-day will be
   * ignored. The resulting date is the same as if it had been set as a GDate
   * through xaccTransSetDatePostedGDate().
   * <p>
   * Please prefer this function over xaccTransSetDatePostedSecs().
   * <p>
   * The posted date is the date when this transaction was posted at the bank.
   */
  public static native void xaccTransSetDatePostedSecsNormalized(final Transaction trans, final time64 time);

  /**
   * Modify the date of when the transaction was entered. The entered
   * date is the date when the register entry was made.
   */
  public static native void xaccTransSetDateEnteredSecs(final Transaction trans, final time64 time);

  /**
   * Dates and txn-type for A/R and A/P "invoice" postings
   */
  public static native void xaccTransSetDateDue(final Transaction trans, final time64 time);

  /**
   * Retrieve the posted date of the transaction. The posted date is
   * the date when this transaction was posted at the bank. (Although
   * having different function names, GetDate and GetDatePosted refer
   * to the same single date.)
   */
  public static native time64 xaccTransGetDate(final Transaction trans);

  /**
   * Retrieve the posted date of the transaction. The posted date is
   * the date when this transaction was posted at the bank. (Although
   * having different function names, GetDate and GetDatePosted refer
   * to the same single date.)
   */
  public static native time64 xaccTransRetDatePosted(final Transaction trans);

  /**
   * Retrieve the posted date of the transaction. The posted date is
   * the date when this transaction was posted at the bank.
   */
  public static native GDate xaccTransGetDatePostedGDate(final Transaction trans);

  /*################## Added for Reg2 #################*/

  /**
   * Retrieve the date of when the transaction was entered. The entered
   * date is the date when the register entry was made.
   */
  public static native time64 xaccTransGetDateEntered(final Transaction trans);
  /*################## Added for Reg2 #################*/

  /**
   * Retrieve the date of when the transaction was entered. The entered
   * date is the date when the register entry was made.
   */
  public static native time64 xaccTransRetDateEntered(final Transaction trans);

  /**
   * Dates and txn-type for A/R and A/P "invoice" postings
   */
  public static native time64 xaccTransRetDateDue(final Transaction trans);

  /* Miscellaneous utility routines. */

  /* Transaction voiding */

  /**
   * xaccTransVoid voids a transaction.  A void transaction has no
   * values, is unaffected by reconciliation, and, by default is not
   * included in any queries.  A voided transaction may not be altered.
   *
   * @param transaction The transaction to void.
   * @param reason      The textual reason why this transaction is being
   *                    voided.
   */
  public static native void xaccTransVoid(final Transaction transaction, final String reason);

  /**
   * xaccTransUnvoid restores a voided transaction to its original
   * state.  At some point when gnucash is enhanced to support an audit
   * trail (i.e. write only transactions) this command should be
   * automatically disabled when the audit trail feature is enabled.
   *
   * @param transaction The transaction to restore from voided state.
   */
  public static native void xaccTransUnvoid(final Transaction transaction);

  /**
   * xaccTransReverse creates a Transaction that reverses the given
   * transaction by inverting all the numerical values in the given
   * transaction.  This function cancels out the effect of an earlier
   * transaction.  This will be needed by write only accounts as a way
   * to void a previous transaction (since you can't alter the existing
   * transaction).
   *
   * @param transaction The transaction to create a reverse of.
   * @return a new transaction which reverses the given transaction
   */
  public static native Transaction xaccTransReverse(final Transaction transaction);

  /**
   * Returns the transaction that reversed the given transaction.
   *
   * @param trans a Transaction that has been reversed
   * @return the transaction that reversed the given transaction, or
   * NULL if the given transaction has not been reversed.
   */
  public static native Transaction xaccTransGetReversedBy(final Transaction trans);

  /**
   * Retrieve information on whether or not a transaction has been voided.
   *
   * @param transaction The transaction in question.
   * @return TRUE if the transaction is void, FALSE otherwise. Also
   * returns FALSE upon an error.
   */
  public static native boolean xaccTransGetVoidStatus(final Transaction transaction);

  /**
   * Returns the user supplied textual reason why a transaction was
   * voided.
   *
   * @param transaction The transaction in question.
   * @return A pointer to the user supplied reason for voiding.
   */
  public static native String xaccTransGetVoidReason(final Transaction transaction);

  /**
   * Returns the time that a transaction was voided.
   *
   * @param tr The transaction in question.
   * @return A time64 containing the time that this transaction was
   * voided. Returns a time of zero upon error.
   */
  public static native time64 xaccTransGetVoidTime(final Transaction tr);

  /**
   * The xaccTransRecordPrice() method iterates through the splits and
   * and record the non-currency equivalent prices in the price database.
   *
   * @param trans  The transaction whose price is recorded
   * @param source The price priority level
   */
  public static native void xaccTransRecordPrice(final Transaction trans, final PriceSource source);
}
