package com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine;

import com.sun.jna.PointerType;

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
public class gnc_commodity extends PointerType {

}
