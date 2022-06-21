package com.austindoupnik.gnc4j.libgnucash.engine.account;

import com.austindoupnik.gnc4j.glib.*;
import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import com.austindoupnik.gnc4j.jna_core.UnsignedInt;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine.*;
import com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric.gnc_numeric;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineSplit.Split;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.IntByReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.glib.GLibGList.GList;
import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

import com.austindoupnik.gnc4j.libgnucash.engine.gnc_date.time64;

/**
 * Account
 * Splits are grouped into Accounts which are also known
 * as "Ledgers" in accounting practice. Each Account consists of a list of
 * Splits that debit that Account. To ensure consistency, if a Split points
 * to an Account, then the Account must point to the Split, and vice-versa.
 * A Split can belong to at most one Account. Besides merely containing a
 * list of Splits, the Account structure also gives the Account a name, a
 * code number, description and notes fields, a key-value frame, a pointer
 * to the commodity that is used for all splits in this account. The
 * commodity can be the name of anything traded and tradeable: a stock
 * (e.g. "IBM", "McDonald's"), a currency (e.g. "USD", "GBP"), or
 * anything added to the commodity table.
 * Accounts can be arranged in a hierarchical tree.  By accounting
 * convention, the value of an Account is equal to the value of all of its
 * Splits plus the value of all of its sub-Accounts.
 */
@UtilityClass
@SuppressWarnings("unused")
public class EngineAccount {
  static {
    nativeRegister(EngineAccount.class, "gnc-engine");
  }

  /**
   * Constructor
   */
  public static native Account xaccMallocAccount(final QofBook book);

  /**
   * Create a new root level account.
   */
  public static native Account gnc_account_create_root(final QofBook book);

  /**
   * The xaccCloneAccount() routine makes a simple copy of the
   * indicated account, placing it in the indicated book.  It copies
   * the account type, name, description, and the kvp values;
   * it does not copy splits/transactions.  The book should have
   * a commodity table in it that has commodities with the same
   * unique name as the ones being copied in the account (the
   * commodities in the clone will be those from the book).
   */
  public static native Account xaccCloneAccount(final Account source, final QofBook book);

  /**
   * The xaccAccountBeginEdit() subroutine is the first phase of
   * a two-phase-commit wrapper for account updates.
   */
  public static native void xaccAccountBeginEdit(final Account account);

  /**
   * The xaccAccountCommitEdit() subroutine is the second phase of
   * a two-phase-commit wrapper for account updates.
   */
  public static native void xaccAccountCommitEdit(final Account account);

  /**
   * The xaccAccountDestroy() routine can be used to get rid of an
   * account.  The account should have been opened for editing
   * (by calling xaccAccountBeginEdit()) before calling this routine.
   */
  public static native void xaccAccountDestroy(final Account account);

  /**
   * Compare two accounts for equality - this is a deep compare.
   */
  public static native boolean xaccAccountEqual(final Account a, final Account b, final boolean check_guids);

  /**
   * The xaccAccountOrder() subroutine defines a sorting order on
   * accounts.  It takes pointers to two accounts, and returns an int &lt; 0 if
   * the first account is "less than" the second, returns an int &gt; 0 if the
   * first is "greater than" the second, and 0 if they are equal.  To
   * determine the sort order, first the account codes are compared,
   * and if these are equal, then account types, then account
   * names. If still equal, it compares GUID to ensure that there
   * aren't any ties.
   */
  public static native int xaccAccountOrder(final Account account_1, final Account account_2);

  /**
   * Returns the account separation character chosen by the user.
   *
   * @return The character to use.
   */
  public static native String gnc_get_account_separator_string();

  public static native char gnc_get_account_separator();

  public static native void gnc_set_account_separator(final String separator);

  public static native Account gnc_book_get_root_account(final QofBook book);

  public static native void gnc_book_set_root_account(final QofBook book, final Account root);

  /**
   * The xaccAccountLookup() subroutine will return the
   * account associated with the given id, or NULL
   * if there is no such account.
   */
  public static native Account xaccAccountLookup(final GncGUID guid, final QofBook book);

  /**
   * Tests account and descendants -- if all have no splits then return TRUE.
   * Otherwise if any account or its descendants have split return FALSE.
   */
  public static native boolean gnc_account_and_descendants_empty(final Account acc);

  /**
   * Composes a translatable error message showing which account
   * names clash with the current account separator. Can be called
   * after gnc_account_list_name_violations to have a consistent
   * error message in different parts of GnuCash
   *
   * @param separator             The separator character that was verified against
   * @param invalid_account_names A GList of invalid account names.
   * @return An error message that can be displayed to the user or logged.
   * This message string should be freed with g_free when no longer
   * needed.
   */
  public static native String gnc_account_name_violations_errmsg(final String separator, final GList invalid_account_names);

  /**
   * Runs through all the accounts and returns a list of account names
   * that contain the provided separator character. This can be used to
   * check if certain account names are invalid.
   *
   * @param book      Pointer to the book with accounts to verify
   * @param separator The separator character to verify against
   * @return A GList of invalid account names. Should be freed with
   * g_list_free_full (value, g_free) when no longer needed.
   */
  public static native GList gnc_account_list_name_violations(final QofBook book, final String separator);

  public static native QofBook gnc_account_get_book(final Account account);

  /**
   * Set the account's type
   */
  public static native void xaccAccountSetType(final Account account, final GNCAccountType type);

  /**
   * Set the account's name
   */
  public static native void xaccAccountSetName(final Account account, final String name);

  /**
   * Set the account's accounting code
   */
  public static native void xaccAccountSetCode(final Account account, final String code);

  /**
   * Set the account's description
   */
  public static native void xaccAccountSetDescription(final Account account, final String desc);

  /**
   * Set the account's Color
   */
  public static native void xaccAccountSetColor(final Account account, final String color);

  /**
   * Set the account's Filter
   */
  public static native void xaccAccountSetFilter(final Account account, final String filter);

  /**
   * Set the account's Sort Order
   */
  public static native void xaccAccountSetSortOrder(final Account account, final String sortorder);

  /**
   * Set the account's Sort Order direction
   */
  public static native void xaccAccountSetSortReversed(final Account account, final boolean sortreversed);

  /**
   * Set the account's notes
   */
  public static native void xaccAccountSetNotes(final Account account, final String notes);

  /**
   * Set the last num field of an Account
   */
  public static native void xaccAccountSetLastNum(final Account account, final String num);

  /**
   * Set the account's lot order policy
   */
  public static native void gnc_account_set_policy(final Account account, final GNCPolicy policy);

  /**
   * Returns the account's account type.
   * <p>
   * This must not be confused with the \ref GType as returned by
   * gnc_account_get_type(), which is related to glib's type system.
   */
  public static native int xaccAccountGetType(final Account account);

  /**
   * Returns true if the account is a stock, mutual fund or currency,
   * otherwise false.
   */
  public static native boolean xaccAccountIsPriced(final Account acc);

  /**
   * This function will set the starting commodity balance for this
   * account.  This routine is intended for use with backends that do
   * not return the complete list of splits for an account, but rather
   * return a partial list.  In such a case, the backend will typically
   * return all of the splits after some certain date, and the
   * 'starting balance' will represent the summation of the splits up
   * to that date.
   */
  public static native void gnc_account_set_start_balance(final Account acc, final gnc_numeric.ByValue start_baln);

  /**
   * This function will set the starting cleared commodity balance for
   * this account.  This routine is intended for use with backends that
   * do not return the complete list of splits for an account, but
   * rather return a partial list.  In such a case, the backend will
   * typically return all of the splits after some certain date, and
   * the 'starting balance' will represent the summation of the splits
   * up to that date.
   */
  public static native void gnc_account_set_start_cleared_balance(final Account acc, final gnc_numeric start_baln);

  /**
   * This function will set the starting reconciled commodity balance
   * for this account.  This routine is intended for use with backends
   * that do not return the complete list of splits for an account, but
   * rather return a partial list.  In such a case, the backend will
   * typically return all of the splits after some certain date, and
   * the 'starting balance' will represent the summation of the splits
   * up to that date.
   */
  public static native void gnc_account_set_start_reconciled_balance(final Account acc, final gnc_numeric.ByValue start_baln);

  /**
   * Tell the account that the running balances may be incorrect and
   * need to be recomputed.
   *
   * @param acc Set the flag on this account.
   */
  public static native void gnc_account_set_balance_dirty(final Account acc);

  /**
   * Tell the account believes that the splits may be incorrectly
   * sorted and need to be resorted.
   *
   * @param acc Set the flag on this account.
   */
  public static native void gnc_account_set_sort_dirty(final Account acc);

  /**
   * Set the defer balance flag. If defer is true, the account balance
   * is not automatically computed, which can save a lot of time if
   * multiple operations have to be done on the same account. If
   * defer is false, further operations on account will cause the
   * balance to be recomputed as normal.
   *
   * @param acc   Set the flag on this account.
   * @param defer New value for the flag.
   */
  public static native void gnc_account_set_defer_bal_computation(final Account acc, final boolean defer);

  /**
   * Insert the given split from an account.
   *
   * @param acc The account to which the split should be added.
   * @param s   The split to be added.
   * @return TRUE is the split is successfully added to the set of
   * splits in the account.  FALSE if the addition fails for any reason
   * (including that the split is already in the account).
   */
  public static native boolean gnc_account_insert_split(final Account acc, final Split s);

  /**
   * Remove the given split from an account.
   *
   * @param acc The account from which the split should be removed.
   * @param s   The split to be removed.
   * @return TRUE is the split is successfully removed from the set of
   * splits in the account.  FALSE if the removal fails for any
   * reason.
   */
  public static native boolean gnc_account_remove_split(final Account acc, final Split s);

  /**
   * Get the account's name
   */
  public static native String xaccAccountGetName(final Account account);

  /**
   * Get the account's accounting code
   */
  public static native String xaccAccountGetCode(final Account account);

  /**
   * Get the account's description
   */
  public static native String xaccAccountGetDescription(final Account account);

  /**
   * Get the account's color
   */
  public static native String xaccAccountGetColor(final Account account);

  /**
   * Get the account's filter
   */
  public static native String xaccAccountGetFilter(final Account account);

  /**
   * Get the account's Sort Order
   */
  public static native String xaccAccountGetSortOrder(final Account account);

  /**
   * Get the account's Sort Order direction
   */
  public static native boolean xaccAccountGetSortReversed(final Account account);

  /**
   * Get the account's notes
   */
  public static native String xaccAccountGetNotes(final Account account);

  /**
   * Get the last num field of an Account
   */
  public static native String xaccAccountGetLastNum(final Account account);

  /**
   * Get the account's lot order policy
   */
  public static native GNCPolicy gnc_account_get_policy(final Account account);

  /**
   * Get the account's flag for deferred balance computation
   */
  public static native boolean gnc_account_get_defer_bal_computation(final Account acc);

  /**
   * The following recompute the partial balances (stored with the
   * transaction) and the total balance, for this account
   */
  public static native void xaccAccountRecomputeBalance(final Account account);

  /**
   * The xaccAccountSortSplits() routine will resort the account's
   * splits if the sort is dirty. If 'force' is true, the account
   * is sorted even if the editlevel is not zero.
   */
  public static native void xaccAccountSortSplits(final Account acc, final boolean force);

  /**
   * The gnc_account_get_full_name routine returns the fully qualified name
   * of the account using the given separator char. The name must be
   * g_free'd after use. The fully qualified name of an account is the
   * concatenation of the names of the account and all its ancestor
   * accounts starting with the topmost account and ending with the
   * given account. Each name is separated by the given character.
   *
   * @note <strong>WAKE UP!</strong>
   * Unlike all other gets, the string returned by gnc_account_get_full_name()
   * must be freed by you the user !!!
   * hack alert -- since it breaks the rule of string allocation, maybe this
   * routine should not be in this library, but some utility library?
   */
  public static native String gnc_account_get_full_name(final Account account);

  /**
   * Retrieve the gains account used by this account for the indicated
   * currency, creating and recording a new one if necessary.
   * <p>
   * FIXME: There is at present no interface to designate an existing
   * account, and the new account name is hard coded to
   * "Orphaned Gains -- CUR"
   * <p>
   * FIXME: There is no provision for creating separate accounts for
   * anything other than currency, e.g. holding period of a security.
   */
  public static native Account xaccAccountGainsAccount(final Account acc, final gnc_commodity curr);

  /**
   * Set a string that identifies the Finance::Quote backend that
   * should be used to retrieve online prices.  See price-quotes.scm
   * for more information
   *
   * @deprecated Price quote information is now stored on the
   * commodity, not the account.
   */
  @Deprecated
  public static native void dxaccAccountSetPriceSrc(final Account account, final String src);

  /**
   * Get a string that identifies the Finance::Quote backend that
   * should be used to retrieve online prices.  See price-quotes.scm
   * for more information. This function uses a static char*.
   *
   * @deprecated Price quote information is now stored on the
   * commodity, not the account.
   */
  @Deprecated
  public static native String dxaccAccountGetPriceSrc(final Account account);

  /*
   * Account Commodity setters/getters
   *   Accounts are used to store an amount of 'something', that 'something'
   *   is called the 'commodity'.  An account can only hold one kind of
   *   commodity.  The following are used to get and set the commodity,
   *   and also to set the SCU, the 'Smallest Commodity Unit'.
   *
   * Note that when we say that a 'split' holds an 'amount', that amount
   *   is denominated in the account commodity.  Do not confuse 'amount'
   *   and 'value'.  The 'value' of a split is the value of the amount
   *   expressed in the currency of the transaction.  Thus, for example,
   *   the 'amount' may be 12 apples, where the account commodity is
   *   'apples'.  The value of these 12 apples may be 12 dollars, where
   *   the transaction currency is 'dollars'.
   *
   * The SCU is the 'Smallest Commodity Unit', signifying the smallest
   *   non-zero amount that can be stored in the account.  It is
   *   represented as the integer denominator of a fraction.  Thus,
   *   for example, a SCU of 12 means that 1/12 of something is the
   *   smallest amount that can be stored in the account.  SCU's can
   *   be any value; they do not need to be decimal.  This allows
   *   the use of accounts with unusual, non-decimal commodities and
   *   currencies.
   *
   *   Normally, the SCU is determined by the commodity of the account.
   *   However, this default SCU can be over-ridden and set to an
   *   account-specific value.  This is account-specific value is
   *   called the 'non-standard' value in the documentation below.
   */

  /**
   * Set the account's commodity
   */
  public static native void xaccAccountSetCommodity(final Account account, final gnc_commodity comm);

  /**
   * Get the account's commodity
   */
  public static native gnc_commodity xaccAccountGetCommodity(final Account account);

  /**
   * Returns a gnc_commodity that is a currency, suitable for being a
   * Transaction's currency. The gnc_commodity is taken either from the current
   * account, or from the next parent account that has a gnc_commodity that is a
   * currency. If neither this account nor any of its parent has such a commodity
   * that is a currency, NULL is returned. In that case, you can use
   * gnc_default_currency() but you might want to show a warning dialog first.
   */
  public static native gnc_commodity gnc_account_get_currency_or_parent(final Account account);

  /**
   * Return the SCU for the account.  If a non-standard SCU has been
   * set for the account, that is returned; else the default SCU for
   * the account commodity is returned.
   */
  public static native int xaccAccountGetCommoditySCU(final Account account);

  /**
   * Return the 'internal' SCU setting.  This returns the over-ride
   * SCU for the account (which might not be set, and might be zero).
   */
  public static native int xaccAccountGetCommoditySCUi(final Account account);

  /**
   * Set the SCU for the account. Normally, this routine is not
   * required, as the default SCU for an account is given by its
   * commodity.
   */
  public static native void xaccAccountSetCommoditySCU(final Account account, final int frac);

  /**
   * Set the flag indicating that this account uses a non-standard SCU.
   */
  public static native void xaccAccountSetNonStdSCU(final Account account, final boolean flag);

  /**
   * Return boolean, indicating whether this account uses a
   * non-standard SCU.
   */
  public static native boolean xaccAccountGetNonStdSCU(final Account account);

  /* Account Balance */

  /**
   * Get the current balance of the account, which may include future
   * splits
   */
  public static native gnc_numeric xaccAccountGetBalance(final Account account);

  /**
   * Get the current balance of the account, only including cleared
   * transactions
   */
  public static native gnc_numeric xaccAccountGetClearedBalance(final Account account);

  /**
   * Get the current balance of the account, only including reconciled
   * transactions
   */
  public static native gnc_numeric xaccAccountGetReconciledBalance(final Account account);

  public static native gnc_numeric xaccAccountGetPresentBalance(final Account account);

  public static native gnc_numeric xaccAccountGetProjectedMinimumBalance(final Account account);

  /**
   * Get the balance of the account at the end of the day before the date specified.
   */
  public static native gnc_numeric xaccAccountGetBalanceAsOfDate(final Account account, final time64 date);

  /**
   * Get the reconciled balance of the account at the end of the day of the date specified.
   */
  public static native gnc_numeric xaccAccountGetReconciledBalanceAsOfDate(final Account account, final time64 date);

  /* These two functions convert a given balance from one commodity to
     another.  The account argument is only used to get the Book, and
     may have nothing to do with the supplied balance.  Likewise, the
     date argument is only used for commodity conversion and may have
     nothing to do with supplied balance.
     Since they really have nothing to do with Accounts, there's
     probably some better place for them, but where?  gnc-commodity.h?
  */
  public static native gnc_numeric xaccAccountConvertBalanceToCurrency(
      final Account account, /* for book */
      final gnc_numeric balance,
      final gnc_commodity balance_currency,
      final gnc_commodity new_currency
  );

  public static native gnc_numeric xaccAccountConvertBalanceToCurrencyAsOfDate(
      final Account account, /* for book */
      final gnc_numeric balance,
      final gnc_commodity balance_currency,
      final gnc_commodity new_currency,
      final time64 date
  );

  /* These functions get some type of balance in the desired commodity.
     'report_commodity' may be NULL to use the account's commodity. */
  public static native gnc_numeric xaccAccountGetBalanceInCurrency(
      final Account account,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  public static native gnc_numeric xaccAccountGetClearedBalanceInCurrency(
      final Account account,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  public static native gnc_numeric xaccAccountGetReconciledBalanceInCurrency(
      final Account account,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  public static native gnc_numeric xaccAccountGetPresentBalanceInCurrency(
      final Account account,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  public static native gnc_numeric xaccAccountGetProjectedMinimumBalanceInCurrency(
      final Account account,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  /**
   * This function gets the balance at the end of the given date, ignoring
   * closing entries, in the desired commodity.
   */
  public static native gnc_numeric xaccAccountGetNoclosingBalanceAsOfDateInCurrency(
      final Account acc,
      final time64 date,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  /**
   * This function gets the balance at the end of the given date in the desired
   * commodity.
   */
  public static native gnc_numeric xaccAccountGetBalanceAsOfDateInCurrency(
      final Account account,
      final time64 date,
      final gnc_commodity report_commodity,
      final boolean include_children
  );

  public static native gnc_numeric xaccAccountGetNoclosingBalanceChangeForPeriod(
      final Account acc,
      final time64 date1,
      final time64 date2,
      final boolean recurse
  );

  public static native gnc_numeric xaccAccountGetBalanceChangeForPeriod(
      final Account acc,
      final time64 date1,
      final time64 date2,
      final boolean recurse
  );

  /*
   * Account Children and Parents.
   * The set of accounts is represented as a doubly-linked tree, so that given
   * any account, both its parent and its children can be easily found.
   * At the top of the tree hierarchy lies a single root node, the root account.
   *
   * The account tree hierarchy is unique, in that a given account can
   * have only one parent account.
   */

  /**
   * This function will remove from the child account any pre-existing
   * parent relationship, and will then add the account as a child of
   * the new parent.  The exception to this is when the old and new
   * parent accounts are the same, in which case this function does
   * nothing.
   * <p>
   * If the child account belongs to a different book than the
   * specified new parent account, the child will be removed from the
   * other book (and thus, the other book's entity tables, generating a
   * destroy event), and will be added to the new book (generating a
   * create event).
   *
   * @param new_parent The new parent account to which the child should
   *                   be attached.
   * @param child      The account to attach.
   */
  public static native void gnc_account_append_child(final Account new_parent, final Account child);

  /**
   * This function will remove the specified child account from the
   * specified parent account. It will NOT free the associated memory
   * or otherwise alter the account: the account can now be reparented
   * to a new location.  Note, however, that it will mark the old
   * parents as having been modified.
   *
   * @param parent The parent account from which the child should be
   *               removed.
   * @param child  The child account to remove.
   */
  public static native void gnc_account_remove_child(final Account parent, final Account child);

  /**
   * This routine returns a pointer to the parent of the specified
   * account.  If the account has no parent, i.e it is either the root
   * node or is a disconnected account, then its parent will be NULL.
   *
   * @param account A pointer to any exiting account.
   * @return A pointer to the parent account node, or NULL if there is
   * no parent account.
   */
  public static native Account gnc_account_get_parent(final Account account);

  /**
   * This routine returns the root account of the account tree that the
   * specified account belongs to.  It is the equivalent of repeatedly
   * calling the gnc_account_get_parent() routine until that routine
   * returns NULL.
   *
   * @param account A pointer to any existing account.
   * @return The root node of the account tree to which this account
   * belongs.  NULL if the account is not part of any account tree.
   */
  public static native Account gnc_account_get_root(final Account account);

  /**
   * This routine indicates whether the specified account is the root
   * node of an account tree.
   *
   * @param account A pointer to any account.
   * @return TRUE if this account is of type ROOT.  FALSE otherwise.
   */
  public static native boolean gnc_account_is_root(final Account account);

  /**
   * This routine returns a GList of all children accounts of the specified
   * account.  This function only returns the immediate children of the
   * specified account.  For a list of all descendant accounts, use the
   * gnc_account_get_descendants() function.
   * <p>
   * If you are looking for the splits of this account, use
   * xaccAccountGetSplitList() instead. This function here deals with
   * children accounts inside the account tree.
   *
   * @param account The account whose children should be returned.
   * @return A GList of account pointers, or NULL if there are no
   * children accounts. It is the callers responsibility to free any returned
   * list with the g_list_free() function.
   */
  public static native GList gnc_account_get_children(final Account account);

  /**
   * This routine returns a GList of all children accounts of the specified
   * account, ordered by xaccAccountOrder().  \sa gnc_account_get_children()
   */
  public static native GList gnc_account_get_children_sorted(final Account account);

  /**
   * Return the number of children of the specified account.  The
   * returned number does not include the account itself.
   *
   * @param account The account to query.
   * @return The number of children of the specified account.
   */
  public static native int gnc_account_n_children(final Account account);

  /**
   * Return the index of the specified child within the list of the
   * parent's children.  The first child index is 0.  This function
   * returns -1 if the parent account is NULL of if the specified child
   * does not belong to the parent account.
   *
   * @param parent The parent account to check.
   * @param child  The child account to find.
   * @return The index of the child account within the specified
   * parent, or -1.
   */
  public static native int gnc_account_child_index(final Account parent, final Account child);

  /**
   * Return the n'th child account of the specified parent account.  If
   * the parent account is not specified or the child index number is
   * invalid, this function returns NULL.
   *
   * @param parent The parent account to check.
   * @param num    The index number of the child account that should be
   *               returned.
   * @return A pointer to the specified child account, or NULL
   */
  public static native Account gnc_account_nth_child(final Account parent, final int num);

  /**
   * This routine returns a flat list of all of the accounts that are
   * descendants of the specified account.  This includes not only the
   * the children, but the children of the children, etc. For a list of
   * only the immediate child accounts, use the
   * gnc_account_get_children() function.  Within each set of child
   * accounts, the accounts returned by this function are unordered.
   * For a list of descendants where each set of children is sorted via
   * the standard account sort function, use the
   * gnc_account_get_descendants_sorted() function.
   *
   * @param account The account whose descendants should be returned.
   * @return A GList of account pointers, or NULL if there are no
   * descendants. It is the callers responsibility to free any returned
   * list with the g_list_free() function.
   */
  public static native GList gnc_account_get_descendants(final Account account);

  /**
   * This function returns a GList containing all the descendants of
   * the specified account, sorted at each level.  This includes not
   * only the the children, but the children of the children, etc.
   * Within each set of child accounts, the accounts returned by this
   * function are ordered via the standard account sort function.  For
   * a list of descendants where each set of children is unordered, use
   * the gnc_account_get_descendants() function.
   * <p>
   * Note: Use this function where the results are intended for display
   * to the user.  If the results are internal to GnuCash or will be
   * resorted at some later point in time you should use the
   * gnc_account_get_descendants() function.
   *
   * @param account The account whose descendants should be returned.
   * @return A GList of account pointers, or NULL if there are no
   * descendants. It is the callers responsibility to free any returned
   * list with the g_list_free() function.
   */
  public static native GList gnc_account_get_descendants_sorted(final Account account);

  /**
   * Return the number of descendants of the specified account.  The
   * returned number does not include the account itself.
   *
   * @param account The account to query.
   * @return The number of descendants of the specified account.
   */
  public static native int gnc_account_n_descendants(final Account account);

  /**
   * Return the number of levels of this account below the root
   * account.
   *
   * @param account The account to query.
   * @return The number of levels below the root.
   */
  public static native int gnc_account_get_current_depth(final Account account);

  /**
   * Return the number of levels of descendants accounts below the
   * specified account.  The returned number does not include the
   * specified account itself.
   *
   * @param account The account to query.
   * @return The number of levels of descendants.
   */
  public static native int gnc_account_get_tree_depth(final Account account);

  /* ForEach */

  /**
   * This method will traverse the immediate children of this accounts,
   * calling 'func' on each account.  This function traverses all
   * children nodes.  To traverse only a subset of the child nodes use
   * the gnc_account_foreach_child_until() function.
   *
   * @param account   A pointer to the account on whose children the
   *                  function should be called.
   * @param func      A function taking two arguments, an Account and a
   *                  gpointer.
   * @param user_data This data will be passed to each call of func.
   */
  public static native void gnc_account_foreach_child(
      final Account account,
      final AccountCb func,
      /*@ null @*/ final Pointer user_data
  );

  /**
   * This method will traverse all children of this accounts and their
   * descendants, calling 'func' on each account.  This function
   * traverses all descendant nodes.  To traverse only a subset of the
   * descendant nodes use the gnc_account_foreach_descendant_until()
   * function.
   *
   * @param account   A pointer to the account on whose descendants the
   *                  function should be called.
   * @param func      A function taking two arguments, an Account and a
   *                  gpointer.
   * @param user_data This data will be passed to each call of func.
   */
  public static native void gnc_account_foreach_descendant(
      final Account account,
      final AccountCb func,
      /*@ null @*/ Pointer user_data
  );

  /**
   * This method will traverse all children of this accounts and their
   * descendants, calling 'func' on each account.  Traversal will stop
   * when func returns a non-null value, and the routine will return
   * with that value.  Therefore, this function will return null if
   * func returns null for every account.  For a simpler function that
   * always traverses all children nodes, use the
   * gnc_account_foreach_descendant() function.
   *
   * @param account   A pointer to the account on whose descendants the
   *                  function should be called.
   * @param func      A function taking two arguments, an Account and a
   *                  gpointer.
   * @param user_data This data will be passed to each call of func.
   */
  public static native Pointer gnc_account_foreach_descendant_until(
      final Account account,
      final AccountCb2 func,
      /*@ null @*/ final Pointer user_data
  );

  /* Concatenation, Merging */

  /**
   * The gnc_account_join_children() subroutine will move (reparent)
   * all child accounts from the from_parent account to the to_parent
   * account, preserving the account hierarchy.  It will also take care
   * that the moved accounts will have the to_parent's book parent
   * as well.
   */
  public static native void gnc_account_join_children(final Account to_parent, final Account from_parent);

  /**
   * The gnc_account_merge_children() subroutine will go through an
   * account, merging all child accounts that have the same name and
   * description.  This function is useful when importing Quicken(TM)
   * files.
   */
  public static native void gnc_account_merge_children(final Account parent);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetReconcileChildrenStatus(final Account account, final boolean status);

  /**
   * DOCUMENT ME!
   */
  public static native boolean xaccAccountGetReconcileChildrenStatus(final Account account);

  /**
   * Returns true if the account is 'ancestor' or has 'ancestor' as an
   * ancestor.  An ancestor account may be the accounts parent, its
   * parent's parent, its parent's parent's parent, etc.  Returns false
   * if either one is NULL.
   */
  public static native boolean xaccAccountHasAncestor(final Account acc, final Account ancestor);

  /* Lookup Accounts and Subaccounts by name or code */

  /**
   * The gnc_account_lookup_by_name() subroutine fetches the account by
   * name from the descendants of the specified account.  The immediate
   * children are searched first.  If there is no match,, then a
   * recursive search of all descendants is performed looking for a
   * match.
   *
   * @return A pointer to the account with the specified name, or NULL
   * if the account was not found.
   */
  public static native Account gnc_account_lookup_by_name(final Account parent, final String name);

  /**
   * The gnc_account_lookup_full_name() subroutine works like
   * gnc_account_lookup_by_name, but uses fully-qualified names using the
   * given separator.
   */
  public static native Account gnc_account_lookup_by_full_name(final Account any_account, final String name);

  /**
   * The gnc_account_lookup_by_code() subroutine works like
   * gnc_account_lookup_by_name, but uses the account code.
   */
  public static native Account gnc_account_lookup_by_code(final Account parent, final String code);

  /**
   * Find the opening balance account for the currency.
   *
   * @param account   The account of which the sought-for account is a descendant.
   * @param commodity The commodity in which the account should be denominated
   * @return The descendant account of EQUITY_TYPE_OPENING_BALANCE or NULL if one doesn't exist.
   */
  public static native Account gnc_account_lookup_by_opening_balance(final Account account, final gnc_commodity commodity);

  /**
   * Find a direct child account matching name, GNCAccountType, and/or commodity.
   * <p>
   * Name and commodity may be nullptr in which case the accounts in the
   * list may have any value for those properties.  Note that commodity
   * matching is by equivalence: If the mnemonic/symbol and namespace
   * are the same, it matches.
   *
   * @param root      The account among whose children one expects to find
   *                  the account.
   * @param name      The name of the account to look for or nullptr.
   * @param acctype   The GNCAccountType to match.
   * @param commodity The commodity in which the account should be denominated or nullptr.
   * @return A GList of children matching the supplied parameters.
   */
  public static native GList gnc_account_lookup_by_type_and_commodity(
      final Account root,
      final String name,
      final GNCAccountType acctype,
      final gnc_commodity commodity
  );

  /* GNCAccountType conversion/checking */

  /**
   * Conversion routines for the account types to/from strings
   * that are used in persistent storage, communications.  These
   * strings should *not* be translated to the local language.
   * Typical conversion is ACCT_TYPE_INCOME -&gt; "INCOME".
   */
  public static native String xaccAccountTypeEnumAsString(final GNCAccountType type);

  /**
   * Conversion routines for the account types to/from strings
   * that are used in persistent storage, communications.  These
   * strings should *not* be translated to the local language.
   * Typical conversion is "INCOME" -&gt; ACCT_TYPE_INCOME.
   */
  public static native boolean xaccAccountStringToType(final String str, final GNCAccountType.ByReference type);

  /**
   * Conversion routines for the account types to/from strings
   * that are used in persistent storage, communications.  These
   * strings should *not* be translated to the local language.
   * Typical conversion is "INCOME" -&gt; ACCT_TYPE_INCOME.
   */
  public static native GNCAccountType xaccAccountStringToEnum(final String str);

  /**
   * The xaccAccountGetTypeStr() routine returns a string suitable for
   * use in the GUI/Interface.  These strings should be translated
   * to the local language.
   */
  public static native String xaccAccountGetTypeStr(final GNCAccountType type);

  /**
   * Return the bitmask of account types compatible with a given type.
   * That is, you could switch to any of the account types in the compatible
   * list without unwanted side-effects.
   */
  public static native GUInt32 xaccAccountTypesCompatibleWith(final GNCAccountType type);

  /**
   * Return the bitmask of parent account types compatible with a given type.
   */
  public static native GUInt32 xaccParentAccountTypesCompatibleWith(final GNCAccountType type);

  /**
   * Return TRUE if accounts of type parent_type can have accounts
   * of type child_type as children.
   */
  public static native boolean xaccAccountTypesCompatible(final GNCAccountType parent_type, final GNCAccountType child_type);

  /**
   * Returns the bitmask of the account type enums that are valid.  Deprecated and
   * root account types are stripped.
   */
  public static native GUInt32 xaccAccountTypesValid();

  /**
   * Convenience function to check if the account is a valid
   * Asset or Liability type, but not a business account type
   * (meaning not an Accounts Payable/Accounts Receivable).
   */
  public static native boolean xaccAccountIsAssetLiabType(final GNCAccountType t);

  /**
   * Convenience function to return the fundamental type
   * asset/liability/income/expense/equity given an account type.
   */
  public static native GNCAccountType xaccAccountTypeGetFundamental(final GNCAccountType t);

  /**
   * Convenience function to check if the account is a valid
   * business account type
   * (meaning an Accounts Payable/Accounts Receivable).
   */
  public static native boolean xaccAccountIsAPARType(final GNCAccountType t);

  /**
   * Convenience function to check if the account is a valid
   * Equity type.
   */
  public static native boolean xaccAccountIsEquityType(final GNCAccountType t);

  /* Account split/transaction list management */

  /**
   * The xaccAccountGetSplitList() routine returns a pointer to a GList of
   * the splits in the account.
   *
   * @note This GList is the account's internal
   * data structure: do not delete it when done; treat it as a read-only
   * structure.  Note that some routines (such as xaccAccountRemoveSplit())
   * modify this list directly, and could leave you with a corrupted
   * pointer.
   * @note This should be changed so that the returned value is a copy
   * of the list. No other part of the code should have access to the
   * internal data structure used by this object.
   */
  public static native GList xaccAccountGetSplitList(final Account account);

  /**
   * The xaccAccountCountSplits() routine returns the number of all
   * the splits in the account. xaccAccountCountSplits is O(N). if
   * testing for emptiness, use xaccAccountGetSplitList != NULL.
   *
   * @param acc              the account for which to count the splits
   * @param include_children also count splits in descendants (TRUE) or
   *                         for this account only (FALSE).
   */
  public static native GUInt64 xaccAccountCountSplits(final Account acc, final boolean include_children);

  /**
   * The xaccAccountMoveAllSplits() routine reassigns each of the splits
   * in accfrom to accto.
   */
  public static native void xaccAccountMoveAllSplits(final Account accfrom, final Account accto);

  /**
   * The xaccAccountForEachTransaction() routine will traverse all of
   * the transactions in <i>account</i> and call the callback
   * function <i>proc</i> on each transaction.  Processing will continue
   * if-and-only-if <i>proc</i> returns 0. The user data pointer
   *
   * <i>data</i> will be passed on to the callback function @a proc.
   * <p>
   * This function does not descend recursively to traverse transactions
   * in child accounts.
   * <i>proc</i> will be called exactly once for each transaction that is
   * pointed to by at least one split in the given account.
   * <p>
   * The result of this function will be 0 <em>if and only if</em>
   * every relevant transaction was traversed exactly once.
   * Else the return value is the last non-zero value returned by proc.
   * <p>
   * \warning For performance reasons, the transaction callback @a proc
   * must never destroy any of the transaction's splits, nor assign any
   * of them to a different account. <b>To do so risks a crash.</b>
   * <p>
   * \warning The traversal occurs only over the transactions that
   * are locally cached in the local gnucash engine.  If the gnucash
   * engine is attached to a remote database, the database may contain
   * (many) transactions that are not mirrored in the local cache.
   * This routine will not cause an SQL database query to be performed;
   * it will not traverse transactions present only in the remote
   * database.
   */
  public static native GInt xaccAccountForEachTransaction(final Account account, final TransactionCallback proc, final Pointer data);

  /**
   * Returns a pointer to the transaction, not a copy.
   */
  public static native Transaction xaccAccountFindTransByDesc(final Account account, final String description);

  /**
   * Returns a pointer to the split, not a copy.
   */
  public static native Split xaccAccountFindSplitByDesc(final Account account, final String description);

  /* Account lots */

  /**
   * The xaccAccountInsertLot() method will register the indicated lot
   * with this account.   Any splits later inserted into this lot must
   * belong to this account.  If the lot is already in another account,
   * the lot, and all of the splits in it, will be moved from that
   * account to this account.
   */
  public static native void xaccAccountInsertLot(final Account account, final GNCLot lot);

  public static native void xaccAccountRemoveLot(final Account account, final GNCLot lot);

  /**
   * The xaccAccountGetLotList() routine returns a list of all lots in
   * this account.
   *
   * @param account The account whose lots should be returned.
   * @return A GList of lot pointers, or NULL if there are no lots in
   * this account children. It is the callers responsibility to free
   * any returned list with the g_list_free() function.
   */
  public static native LotList xaccAccountGetLotList(final Account account);

  public interface LotForEachCb extends Callback {
    GPointer invoke(final GNCLot lot, final GPointer user_data);
  }

  /**
   * The xaccAccountForEachLot() method will apply the function 'proc'
   * to each lot in the account.  If 'proc' returns a non-NULL value,
   * further application will be stopped, and the resulting value
   * will be returned.  There is no guaranteed order over which
   * the Lots will be traversed.
   */
  public static native GPointer xaccAccountForEachLot(
      final Account acc,
      final LotForEachCb proc,
      /*@ null @*/ final GPointer user_data
  );

  public interface LotCb extends Callback {
    GPointer invoke(final GNCLot lot, final GPointer user_data);
  }

  public interface LotFindCb extends Callback {
    boolean invoke(final GNCLot lot, final GPointer user_data);
  }

  /**
   * Find a list of open lots that match the match_func.  Sort according
   * to sort_func.  If match_func is NULL, then all open lots are returned.
   * If sort_func is NULL, then the returned list has no particular order.
   * The caller must free to returned list.
   */
  public static native LotList xaccAccountFindOpenLots(
      final Account acc,
      final LotFindCb match_func,
      /*@ null @*/ final GPointer user_data,
      final GCompareFunc sort_func
  );

  /* Account Reconciliation information getters/setters */

  /**
   * DOCUMENT ME!
   */
  public static native boolean xaccAccountGetReconcileLastDate(final Account account, final time64.ByReference last_date);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetReconcileLastDate(final Account account, final time64 last_date);

  /**
   * DOCUMENT ME!
   */
  public static native boolean xaccAccountGetReconcileLastInterval(final Account account, final IntByReference months, final IntByReference days);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetReconcileLastInterval(final Account account, final int months, final int days);

  /**
   * DOCUMENT ME!
   */
  public static native boolean xaccAccountGetReconcilePostponeDate(final Account account, final time64.ByReference postpone_date);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetReconcilePostponeDate(final Account account, final time64 postpone_date);

  /**
   * DOCUMENT ME!
   */
  public static native boolean xaccAccountGetReconcilePostponeBalance(final Account account, final gnc_numeric balance);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetReconcilePostponeBalance(final Account account, final gnc_numeric.ByValue balance);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountClearReconcilePostpone(final Account account);

  /**
   * DOCUMENT ME!
   */
  @Getter
  @RequiredArgsConstructor
  public enum GNCPlaceholderType implements JnaEnum<GNCPlaceholderType> {
    PLACEHOLDER_NONE(0),
    PLACEHOLDER_THIS(1),
    PLACEHOLDER_CHILD(2);

    private final int value;
  }

  /* Account Placeholder flag */

  /**
   * Get the "placeholder" flag for an account.  If this flag is set
   * then the account may not be modified by the user.
   *
   * @param account The account whose flag should be retrieved.
   * @return The current state of the account's "placeholder" flag.
   */
  public static native boolean xaccAccountGetPlaceholder(final Account account);

  /**
   * Set the "placeholder" flag for an account.  If this flag is set
   * then the account may not be modified by the user.
   *
   * @param account The account whose flag should be retrieved.
   * @param val     The new state for the account's "placeholder" flag.
   */
  public static native void xaccAccountSetPlaceholder(final Account account, final boolean val);

  /* Account Append Text flag */

  /**
   * Get the "import-append-text" flag for an account.  This is the saved
   * state of the Append checkbox in the "Generic import transaction matcher"
   * used to set the initial state of the Append checkbox next time this
   * account is imported.
   *
   * @param account The account whose flag should be retrieved.
   * @return The current state of the account's "import-append-text" flag.
   */
  public static native boolean xaccAccountGetAppendText(final Account account);

  /**
   * Set the "import-append-text" flag for an account.  This is the saved
   * state of the Append checkbox in the "Generic import transaction matcher"
   * used to set the initial state of the Append checkbox next time this
   * account is imported.
   *
   * @param account The account whose flag should be retrieved.
   * @param val     The new state for the account's "import-append-text" flag.
   */
  public static native void xaccAccountSetAppendText(final Account account, final boolean val);

  /**
   * Get the "opening-balance" flag for an account.  If this flag is set
   * then the account is used for opening balance transactions.
   *
   * @param account The account whose flag should be retrieved.
   * @return The current state of the account's "opening-balance" flag.
   */
  public static native boolean xaccAccountGetIsOpeningBalance(final Account account);

  /**
   * Set the "opening-balance" flag for an account. If this flag is set
   * then the account is used for opening balance transactions.
   *
   * @param account The account whose flag should be set.
   * @param val     The new state for the account's "opening-balance" flag.
   */
  public static native void xaccAccountSetIsOpeningBalance(final Account account, final boolean val);

  /**
   * Returns PLACEHOLDER_NONE if account is NULL or neither account nor
   * any descendant of account is a placeholder.  If account is a
   * placeholder, returns PLACEHOLDER_THIS.  Otherwise, if any
   * descendant of account is a placeholder, return PLACEHOLDER_CHILD.
   */
  public static native GNCPlaceholderType xaccAccountGetDescendantPlaceholder(final Account account);

  /* Account Hidden flag */

  /**
   * Get the "hidden" flag for an account.  If this flag is set then
   * the account (and any children) will be hidden from the user unless
   * they explicitly ask to see them.
   *
   * @param acc The account whose flag should be retrieved.
   * @return The current state of the account's "hidden" flag.
   */
  public static native boolean xaccAccountGetHidden(final Account acc);

  /**
   * Set the "hidden" flag for an account.  If this flag is set then
   * the account (and any children) will be hidden from the user unless
   * they explicitly ask to see them.
   *
   * @param acc The account whose flag should be retrieved.
   * @param val The new state for the account's "hidden" flag.
   */
  public static native void xaccAccountSetHidden(final Account acc, final boolean val);

  /**
   * Should this account be "hidden".  If this flag is set for this
   * account (or any parent account) then the account should be hidden
   * from the user unless they explicitly ask to see it.  This function
   * is different from the xaccAccountGetHidden() function because it
   * checks the flag in parent accounts in addition to this account.
   *
   * @param acc The account whose flag should be retrieved.
   * @return Whether or not this account should be "hidden".
   */
  public static native boolean xaccAccountIsHidden(final Account acc);

  /* Account Auto Interest flag */

  /**
   * Get the "auto interest" flag for an account.  If this flag is set then
   * the account (and any children) will trigger an interest transfer after reconciling.
   *
   * @param acc The account whose flag should be retrieved.
   * @return The current state of the account's "auto interest" flag.
   */
  public static native boolean xaccAccountGetAutoInterest(final Account acc);

  /**
   * Set the "auto interest" flag for an account.  If this flag is set then
   * the account (and any children) will trigger an interest transfer after reconciling.
   *
   * @param acc The account whose flag should be retrieved.
   * @param val The new state for the account's "auto interest" flag.
   */
  public static native void xaccAccountSetAutoInterest(final Account acc, final boolean val);

  /* Account Tax related getters/setters */

  /**
   * DOCUMENT ME!
   */
  public static native boolean xaccAccountGetTaxRelated(final Account account);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetTaxRelated(final Account account, boolean tax_related);

  /**
   * DOCUMENT ME!
   */
  public static native String xaccAccountGetTaxUSCode(final Account account);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetTaxUSCode(final Account account, final String code);

  /**
   * DOCUMENT ME!
   */
  public static native String xaccAccountGetTaxUSPayerNameSource(final Account account);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetTaxUSPayerNameSource(final Account account, final String source);

  /**
   * DOCUMENT ME!
   */
  public static native GInt64 xaccAccountGetTaxUSCopyNumber(final Account account);

  /**
   * DOCUMENT ME!
   */
  public static native void xaccAccountSetTaxUSCopyNumber(final Account account, final GInt64 copy_number);

  /* Account type debit/credit string getters */

  /**
   * Get the debit string associated with this account type
   */
  public static native String gnc_account_get_debit_string(final GNCAccountType acct_type);

  /**
   * Get the credit string associated with this account type
   */
  public static native String gnc_account_get_credit_string(final GNCAccountType acct_type);

  /* Account marking */

  /**
   * Set a mark on the account.  The meaning of this mark is
   * completely undefined. Its presented here as a utility for the
   * programmer, to use as desired.  Handy for performing customer traversals
   * over the account tree.  The mark is *not* stored in the database/file
   * format.  When accounts are newly created, the mark is set to zero.
   */
  public static native void xaccAccountSetMark(final Account account, final short mark);

  /*
   * Get the mark set by xaccAccountSetMark
   * short xaccAccountGetMark (const final Account account);
   */

  /**
   * The xaccClearMark will find the root account, and clear the mark in
   * the entire account tree.
   */
  public static native void xaccClearMark(final Account account, final short val);

  /**
   * The xaccClearMarkDown will clear the mark only in this and in
   * sub-accounts.
   */
  public static native void xaccClearMarkDown(final Account account, final short val);

  /* Staged Traversal */

  /*
   * The following functions provide support for "staged traversals"
   * over all of the transactions in an account or group.  The idea
   * is to be able to perform a sequence of traversals ("stages"),
   * and perform an operation on each transaction exactly once
   * for that stage.
   *
   * Only transactions whose current "stage" is less than the
   * stage of the current traversal will be affected, and they will
   * be "brought up" to the current stage when they are processed.
   *
   * For example, you could perform a stage 1 traversal of all the
   * transactions in an account, and then perform a stage 1 traversal of
   * the transactions in a second account.  Presuming the traversal of
   * the first account didn't abort prematurely, any transactions shared
   * by both accounts would be ignored during the traversal of the
   * second account since they had been processed while traversing the
   * first account.
   *
   * However, if you had traversed the second account using a stage
   * of 2, then all the transactions in the second account would have
   * been processed.
   *
   * Traversal can be aborted by having the callback function return
   * a non-zero value.  The traversal is aborted immediately, and the
   * non-zero value is returned.  Note that an aborted traversal can
   * be restarted; no information is lost due to an abort.
   *
   * The initial impetus for this particular approach came from
   * generalizing a mark/sweep practice that was already being
   * used in FileIO.c.
   *
   * Note that currently, there is a hard limit of 256 stages, which
   * can be changed by enlarging "marker" in the transaction struct
   */

  /**
   * gnc_account_tree_begin_staged_transaction_traversals()
   * resets the traversal marker inside every transactions of every
   * account in the account tree originating with the specified node.
   * This is done so that a new sequence of staged traversals can
   * begin.
   */
  public static native void gnc_account_tree_begin_staged_transaction_traversals(final Account acc);

  /**
   * xaccSplitsBeginStagedTransactionTraversals() resets the traversal
   * marker for each transaction which is a parent of one of the
   * splits in the list.
   */
  public static native void xaccSplitsBeginStagedTransactionTraversals(final SplitList splits);

  /**
   * xaccAccountBeginStagedTransactionTraversals() resets the traversal
   * marker for each transaction which is a parent of one of the
   * splits in the account.
   */
  public static native void xaccAccountBeginStagedTransactionTraversals(final Account account);

  /**
   * xaccTransactionTraverse() checks the stage of the given transaction.
   * If the transaction hasn't reached the given stage, the transaction
   * is updated to that stage and the function returns TRUE. Otherwise
   * no change is made and the function returns FALSE.
   */
  public static native boolean xaccTransactionTraverse(final Transaction trans, final int stage);

  /**
   * xaccAccountStagedTransactionTraversal() calls <i>thunk</i> on each
   * transaction in account <i>a</i> whose current marker is less than the
   * given <i>stage</i> and updates each transaction's marker to be @a stage.
   * The traversal will stop if <i>thunk</i> returns a non-zero value.
   * xaccAccountStagedTransactionTraversal() function will return zero
   * or the non-zero value returned by @a thunk.
   * This API does not handle handle recursive traversals.
   * <p>
   * \warning For performance reasons, the transaction callback @a thunk
   * must never destroy any of the transaction's splits, nor assign any
   * of them to a different account. <b>To do so risks a crash.</b>
   */
  public static native int xaccAccountStagedTransactionTraversal(
      final Account a,
      final UnsignedInt stage,
      final TransactionCallback thunk,
      final Pointer data
  );

  /**
   * gnc_account_tree_staged_transaction_traversal() calls <i>thunk</i> on each
   * transaction in the group whose current marker is less than the
   * given <i>stage</i> and updates each transaction's marker to be @a stage.
   * The traversal will stop if <i>thunk</i> returns a non-zero value.
   * gnc_account_tree_staged_transaction_traversal() function will return zero
   * or the non-zero value returned by <i>thunk.</i>  This
   * API does not handle handle recursive traversals.
   * <p>
   * \warning For performance reasons, the transaction callback @a thunk
   * must never destroy any of the transaction's splits, nor assign any
   * of them to a different account. <b>To do so risks a crash.</b>
   */
  public static native int gnc_account_tree_staged_transaction_traversal(
      final Account account,
      final UnsignedInt stage,
      final TransactionCallback thunk,
      final Pointer data
  );

  /**
   * Traverse all of the transactions in the given account group.
   * Continue processing IF <i>proc</i> returns 0. This function
   * will descend recursively to traverse transactions in the
   * children of the accounts in the group.
   *
   * <i>Proc</i> will be called exactly once for each transaction that is
   * pointed to by at least one split in any account in the hierarchy
   * topped by the root Account @a acc.
   * <p>
   * The result of this function will be 0 IF every relevant
   * transaction was traversed exactly once; otherwise, the return
   * value is the last non-zero value returned by the callback.
   * <p>
   * \warning For performance reasons, the transaction callback @a proc
   * must never destroy any of the transaction's splits, nor assign any
   * of them to a different account. <b>To do so risks a crash.</b>
   * <p>
   * \warning The traversal occurs only over the transactions that
   * are locally cached in the local gnucash engine.  If the gnucash
   * engine is attached to a remote database, the database may contain
   * (many) transactions that are not mirrored in the local cache.
   * This routine will not cause an SQL database query to be performed;
   * it will not traverse transactions present only in the remote
   * database.
   * <p>
   * Note that this routine is just a trivial wrapper for
   * <p>
   * gnc_account_tree_begin_staged_transaction_traversals(g);
   * gnc_account_tree_staged_transaction_traversal(g, 42, proc, data);
   */
  public static native int xaccAccountTreeForEachTransaction(
      final Account acc,
      final TransactionCallback proc,
      final Pointer data
  );

  public static class GncImportMatchMap extends PointerType {

  }

  /**
   * Obtain an ImportMatchMap object from an Account or a Book
   */
  public static native GncImportMatchMap gnc_account_imap_create_imap(final Account acc);

  /**
   * Look up an Account in the map non-Baysian
   */
  public static native Account gnc_account_imap_find_account(
      final GncImportMatchMap imap,
      final String category,
      final String key
  );

  /**
   * Store an Account in the map non Baysian
   */
  public static native void gnc_account_imap_add_account(
      final GncImportMatchMap imap,
      final String category,
      final String key,
      final Account acc
  );

  /**
   * Remove a reference to an Account in the map non Baysian
   */
  public static native void gnc_account_imap_delete_account(
      final GncImportMatchMap imap,
      final String category,
      final String key
  );

  /**
   * Look up an Account in the map using Baysian
   */
  public static native Account gnc_account_imap_find_account_bayes(final GncImportMatchMap imap, final GList tokens);

  /**
   * Updates the imap for a given account using a list of tokens
   */
  public static native void gnc_account_imap_add_account_bayes(
      final GncImportMatchMap imap,
      final GList tokens,
      final Account acc
  );

  /**
   * Returns a GList of structure imap_info of all Bayesian mappings for
   * required Account
   */
  public static native GList gnc_account_imap_get_info_bayes(final Account acc);

  /**
   * Returns a GList of structure imap_info of all Non Bayesian mappings for
   * required Account
   */
  public static native GList gnc_account_imap_get_info(final Account acc, final String category);

  /**
   * Returns the text string pointed to by head and category for the Account, free
   * the returned text
   */
  public static native String gnc_account_get_map_entry(final Account acc, final String head, final String category);

  /**
   * Delete the entry for Account pointed to by head,category and match_string,
   * if empty is TRUE then use delete if empty
   */
  public static native void gnc_account_delete_map_entry(
      final Account acc,
      final String head,
      final String category,
      final String match_string,
      final boolean empty
  );

  /**
   * Delete all bayes entries for Account
   */
  public static native void gnc_account_delete_all_bayes_maps(final Account acc);

  /**
   * Reset the flag that indicates the function imap_convert_bayes_to_flat
   * has been run
   */
  public static native void gnc_account_reset_convert_bayes_to_flat();

  /* Deprecated Routines. */

  /**
   * @deprecated The current API associates only one thing with an
   * account: the 'commodity'. Use xaccAccountGetCommodity() to fetch
   * it.
   * <p>
   * These two funcs take control of their gnc_commodity args. Don't free
   */
  @Deprecated
  public static native void DxaccAccountSetCurrency(final Account account, final gnc_commodity currency);

  /**
   * @deprecated The current API associates only one thing with an
   * account: the 'commodity'. Use xaccAccountGetCommodity() to fetch
   * it.
   */
  @Deprecated
  public static native gnc_commodity DxaccAccountGetCurrency(final Account account);

  /**
   * Set the timezone to be used when interpreting the results from a
   * given Finance::Quote backend.  Unfortunately, the upstream sources
   * don't label their output, so the user has to specify this bit.
   *
   * @deprecated Price quote information is now stored on the
   * commodity, not the account.
   */
  @Deprecated
  public static native void dxaccAccountSetQuoteTZ(final Account account, final String tz);

  /**
   * Get the timezone to be used when interpreting the results from a
   * given Finance::Quote backend.  Unfortunately, the upstream sources
   * don't label their output, so the user has to specify this
   * bit. This function uses a static char*.
   *
   * @deprecated Price quote information is now stored on the
   * commodity, not the account.
   */
  @Deprecated
  public static native String dxaccAccountGetQuoteTZ(final Account account);
}
