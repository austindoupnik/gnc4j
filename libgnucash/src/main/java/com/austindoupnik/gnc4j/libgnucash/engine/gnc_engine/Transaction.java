package com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine;

import com.sun.jna.PointerType;

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
public class Transaction extends PointerType {

}
