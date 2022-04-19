package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.glib.GLibGList;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncCommodity.*;

@UtilityClass
public class EngineAccount {
  static {
    Native.register("gnc-engine");
  }

  /**
   * The account types are used to determine how the transaction data
   * in the account is displayed.   These values can be safely changed
   * from one release to the next.  Note that if values are added,
   * the file IO translation routines need to be updated. Note
   * also that GUI code depends on these numbers.
   */
  @AllArgsConstructor
  public enum GNCAccountType {
    /**
     * Not a type
     */
    ACCT_TYPE_INVALID(-1),
    /**
     * Not a type
     */
    ACCT_TYPE_NONE(-1),
    /**
     * The bank account type denotes a savings
     * or checking account held at a bank.
     * Often interest bearing.
     */
    ACCT_TYPE_BANK(0),
    /**
     * The cash account type is used to denote a
     * shoe-box or pillowcase stuffed with *
     * cash.
     */
    ACCT_TYPE_CASH(1),
    /**
     * The Credit card account is used to denote
     * credit (e.g. amex) and debit (e.g. visa,
     * mastercard) card accounts
     */
    ACCT_TYPE_CREDIT(3),
    /**
     * asset (and liability) accounts indicate
     * generic, generalized accounts that are
     * none of the above.
     */
    ACCT_TYPE_ASSET(2),
    /**
     * liability (and asset) accounts indicate
     * generic, generalized accounts that are
     * none of the above.
     */
    ACCT_TYPE_LIABILITY(4),
    /**
     * Stock accounts will typically be shown in
     * registers which show three columns:
     * price, number of shares, and value.
     */
    ACCT_TYPE_STOCK(5),
    /**
     * Mutual Fund accounts will typically be
     * shown in registers which show three
     * columns: price, number of shares, and
     * value.
     */
    ACCT_TYPE_MUTUAL(6),
    /**
     * The currency account type indicates that
     * the account is a currency trading
     * account.  In many ways, a currency
     * trading account is like a stock *
     * trading account. It is shown in the
     * register with three columns: price,
     * number of shares, and value. Note:
     * Since version 1.7.0, this account is *
     * no longer needed to exchange currencies
     * between accounts, so this type is
     * DEPRECATED.
     */
    @Deprecated ACCT_TYPE_CURRENCY(7);

    private final int value;
  }

  public static class GncGUID extends PointerType {

  }

  public static class Account extends PointerType {

  }

  public static class GNCPolicy extends PointerType {

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
   * the first account is "less than" the second, returns an int > 0 if the
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
  public static native String gnc_account_name_violations_errmsg(final String separator, final GLibGList.GList invalid_account_names);

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
  public static native GLibGList.GList gnc_account_list_name_violations(final QofBook book, final String separator);

  public static native QofBook gnc_account_get_book(final Account account);

  /**
   * @see #xaccAccountSetType(Account, int)
   */
  public static void xaccAccountSetType(final Account account, final GNCAccountType type) {
    xaccAccountSetType(account, type.value);
  }

  /**
   * Set the account's type
   */
  public static native void xaccAccountSetType(final Account account, final int type);

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
  public static native void gnc_account_set_start_balance(final Account acc, final EngineGncNumeric.gnc_numeric.ByValue start_baln);

  /**
   * This function will set the starting reconciled commodity balance
   * for this account.  This routine is intended for use with backends
   * that do not return the complete list of splits for an account, but
   * rather return a partial list.  In such a case, the backend will
   * typically return all of the splits after some certain date, and
   * the 'starting balance' will represent the summation of the splits
   * up to that date.
   */
  public static native void gnc_account_set_start_reconciled_balance(final Account acc, final EngineGncNumeric.gnc_numeric.ByValue start_baln);

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

  public static class Split extends PointerType {

  }

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
   * Set the account's commodity
   */
  public static native void xaccAccountSetCommodity(final Account account, final gnc_commodity comm);

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
   * The gnc_account_lookup_full_name() subroutine works like
   * gnc_account_lookup_by_name, but uses fully-qualified names using the
   * given separator.
   */
  public static native Account gnc_account_lookup_by_full_name(final Account any_account, final String name);
}
