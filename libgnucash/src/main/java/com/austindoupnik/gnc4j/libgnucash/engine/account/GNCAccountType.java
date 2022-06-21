package com.austindoupnik.gnc4j.libgnucash.engine.account;

import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The account types are used to determine how the transaction data
 * in the account is displayed.   These values can be safely changed
 * from one release to the next.  Note that if values are added,
 * the file IO translation routines need to be updated. Note
 * also that GUI code depends on these numbers.
 */
@Getter
@RequiredArgsConstructor
public enum GNCAccountType implements JnaEnum<GNCAccountType> {
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
  @Deprecated ACCT_TYPE_CURRENCY(7),
  /**
   * Income accounts are used to denote
   * income
   */
  ACCT_TYPE_INCOME(8),
  /**
   * Expense accounts are used to denote
   * expenses.
   */
  ACCT_TYPE_EXPENSE(9),
  /**
   * Equity account is used to balance the
   * balance sheet.
   */
  ACCT_TYPE_EQUITY(10),
  /**
   * A/R account type
   */
  ACCT_TYPE_RECEIVABLE(11),
  /**
   * A/P account type
   */
  ACCT_TYPE_PAYABLE(12),
  /**
   * The hidden root account of an account tree.
   */
  ACCT_TYPE_ROOT(13),
  /**
   * Account used to record multiple commodity transactions.
   * This is not the same as ACCT_TYPE_CURRENCY above.
   * Multiple commodity transactions have splits in these
   * accounts to make the transaction balance in each
   * commodity as well as in total value.
   */
  ACCT_TYPE_TRADING(14),
  /**
   * stop here; the following types
   * just aren't ready for prime time
   */
  NUM_ACCOUNT_TYPES(15),

  /* bank account types */

  /**
   * bank account type -- don't use this
   * for now, see NUM_ACCOUNT_TYPES
   */
  ACCT_TYPE_CHECKING(15),
  /**
   * bank account type -- don't use this for
   * now, see NUM_ACCOUNT_TYPES
   */
  ACCT_TYPE_SAVINGS(16),
  /**
   * bank account type -- don't use this
   * for now, see NUM_ACCOUNT_TYPES
   */
  ACCT_TYPE_MONEYMRKT(17),
  /**
   * line of credit -- don't use this for
   * now, see NUM_ACCOUNT_TYPES
   */
  ACCT_TYPE_CREDITLINE(18),
  ACCT_TYPE_LAST(19);

  private final int value;

  public static GNCAccountType findByValue(final int v) {
    return JnaEnum.findByValue(GNCAccountType.class, v);
  }

  public static class ByReference extends JnaEnumByReference<GNCAccountType> {
    @Override
    protected GNCAccountType findByValue(int value) {
      return GNCAccountType.findByValue(value);
    }
  }
}
