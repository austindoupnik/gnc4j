package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.glib.GInt;
import com.austindoupnik.gnc4j.glib.GUInt;
import com.austindoupnik.gnc4j.jna_core.JnaEnum;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineGncEngine.gnc_commodity;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineGncEngine.gnc_commodity_table;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.glib.GLibGList.GList;
import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineAccount.GncGUID;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncEngine.gnc_commodity_namespace;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncEngine.gnc_quote_source;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncNumeric.gnc_numeric;

/**
 * Commodity Commodities
 * A commodity is something of value that is easily tradeable or
 * sellable; for example, currencies, stocks, bonds, grain,
 * copper, and oil are all commodities.  This file provides
 * an API for defining a commodities, and for working with
 * collections of commodities.  All GnuCash financial transactions
 * must identify the commodity that is being traded.
 *
 * @warning The system used here does not follow the object
 * handling and identification system (GncGUID's, Entities, etc.)
 * that the other parts of GnuCash use.  The API really should be
 * ported over.  This would allow us to get rid of the
 * commodity table routines defined below.
 */
@UtilityClass
@SuppressWarnings("unused")
public class EngineGncCommodity {
  static {
    nativeRegister(EngineGncCommodity.class, "gnc-engine");
  }

  public static class CommodityList extends GList {

  }

  /* Commodity Quote Source functions */

  /**
   * The quote source type enum account types are used to determine how
   * the transaction data in the account is displayed.  These values
   * can be safely changed from one release to the next.
   */
  @Getter
  @RequiredArgsConstructor
  public enum QuoteSourceType implements JnaEnum<QuoteSourceType> {
    /**
     * This quote source pulls from a single
     * specific web site.  For example, the
     * yahoo_australia source only pulls from
     * the yahoo web site.
     */
    SOURCE_SINGLE(0),
    /**
     * This quote source may pull from multiple
     * web sites.  For example, the australia
     * source may pull from ASX, yahoo, etc.
     */
    SOURCE_MULTI(1),
    /**
     * This is a locally installed quote source
     * that gnucash knows nothing about. May
     * pull from single or multiple
     * locations.
     */
    SOURCE_UNKNOWN(2),
    SOURCE_MAX(3),
    /**
     * The special currency quote source.
     */
    SOURCE_CURRENCY(SOURCE_MAX.getValue());

    private final int value;
  }

  /**
   * This function indicates whether or not the Finance::Quote module
   * is installed on a user's computer.  This includes any other related
   * modules that gnucash need to process F::Q information.
   *
   * @return TRUE is F::Q is installed properly.
   */
  public static native boolean gnc_quote_source_fq_installed();

  /**
   * This function returns the version of the Finance::Quote module
   * installed on a user's computer. If no proper installation is found
   * it will return NULL.
   *
   * @return a version string or NULL
   */
  public static native String gnc_quote_source_fq_version();

  /**
   * Update gnucash internal tables based on what Finance::Quote
   * sources are installed.  Sources that have been explicitly coded
   * into gnucash are marked sensitive/insensitive based upon whether
   * they are present. New sources that gnucash doesn't know about are
   * added to its internal tables.
   *
   * @param sources_list A list of strings containing the source names
   *                     as they are known to F::Q.
   */
  public static native void gnc_quote_source_set_fq_installed(final String version_string, final GList sources_list);

  /**
   * Return the number of entries for a given type of quote source.
   *
   * @param type The quote source type whose count should be returned.
   * @return The number of entries for this type of quote source.
   */
  public static native GInt gnc_quote_source_num_entries(final QuoteSourceType type);

  /**
   * Create a new quote source. This is called by the F::Q startup code
   * or the XML parsing code to add new entries to the list of
   * available quote sources.
   *
   * @param name      The internal name for this new quote source.
   * @param supported TRUE if this quote source is supported by F::Q.
   *                  Should only be set by the F::Q startup routine.
   * @return A pointer to the newly created quote source.
   */
  public static native gnc_quote_source gnc_quote_source_add_new(final String name, final boolean supported);

  /**
   * Given the internal (gnucash or F::Q) name of a quote source, find
   * the data structure identified by this name.
   *
   * @param internal_name The name of this quote source.
   * @return A pointer to the price quote source that has the specified
   * internal name.
   */
  public static native gnc_quote_source gnc_quote_source_lookup_by_internal(final String internal_name);

  /**
   * Given the type/index of a quote source, find the data structure
   * identified by this pair.
   *
   * @param type  The type of this quote source.
   * @param index The index of this quote source within its type.
   * @return A pointer to the price quote source that has the specified
   * type/index.
   */
  public static native gnc_quote_source gnc_quote_source_lookup_by_ti(final QuoteSourceType type, final GInt index);

  /**
   * Given a gnc_quote_source data structure, return the flag that
   * indicates whether this particular quote source is supported by
   * the user's F::Q installation.
   *
   * @param source The quote source in question.
   * @return TRUE if the user's computer supports this quote source.
   */
  public static native boolean gnc_quote_source_get_supported(final gnc_quote_source source);

  /**
   * Given a gnc_quote_source data structure, return the type of this
   * particular quote source. (SINGLE, MULTI, UNKNOWN)
   *
   * @param source The quote source in question.
   * @return The type of this quote source.
   */
  public static native QuoteSourceType gnc_quote_source_get_type(final gnc_quote_source source);

  /**
   * Given a gnc_quote_source data structure, return the index of this
   * particular quote source within its type.
   *
   * @param source The quote source in question.
   * @return The index of this quote source in its type.
   */
  public static native GInt gnc_quote_source_get_index(final gnc_quote_source source);

  /**
   * Given a gnc_quote_source data structure, return the user friendly
   * name of this quote source.  E.G. "Yahoo Australia" or "Australia
   * (Yahoo, ASX, ...)"
   *
   * @param source The quote source in question.
   * @return The user friendly name.
   */
  public static native String gnc_quote_source_get_user_name(final gnc_quote_source source);

  /**
   * Given a gnc_quote_source data structure, return the internal name
   * of this quote source.  This is the name used by both gnucash and
   * by Finance::Quote.  E.G. "yahoo_australia" or "australia"
   *
   * @param source The quote source in question.
   * @return The internal name.
   */
  public static native String gnc_quote_source_get_internal_name(final gnc_quote_source source);

  /* Commodity Creation */

  /**
   * Create a new commodity. This function allocates a new commodity
   * data structure, populates it with the data provided, and then
   * generates the dynamic names that exist as part of a commodity.
   *
   * @param book                The book that the new commodity will belong to.
   * @param fullname            The complete name of this commodity. E.G. "Acme
   *                            Systems, Inc."
   * @param commodity_namespace An aggregation of commodities. E.G. ISO4217,
   *                            Nasdaq, Downbelow, etc.
   * @param mnemonic            An abbreviation for this stock.  For publicly
   *                            traced stocks, this field should contain the stock ticker
   *                            symbol. This field is used to get online price quotes, so it must
   *                            match the stock ticker symbol used by the exchange where you want
   *                            to get automatic stock quote updates.  E.G. ACME, ACME.US, etc.
   * @param cusip               A string containing the CUSIP code or similar
   *                            UNIQUE code for this commodity like the ISIN. The stock ticker is
   *                            NOT appropriate as that goes in the mnemonic field.
   * @param fraction            The smallest division of this commodity
   *                            allowed. I.E. If this is 1, then the commodity must be traded in
   *                            whole units; if 100 then the commodity may be traded in 0.01
   *                            units, etc.
   * @return A pointer to the new commodity.
   * @note This function does not check to see if the commodity exists
   * before adding a new commodity.
   */
  public static native gnc_commodity gnc_commodity_new(
      final QofBook book,
      final String fullname,
      final String commodity_namespace,
      final String mnemonic,
      final String cusip,
      final int fraction
  );

  /**
   * Destroy a commodity.  Release all memory attached to this data structure.
   *
   * @param cm The commodity to destroy.
   * @note This function does not (can not) check to see if the
   * commodity is referenced anywhere.
   */
  public static native void gnc_commodity_destroy(final gnc_commodity cm);

  /**
   * Copy src into dest
   */
  public static native void gnc_commodity_copy(final gnc_commodity dest, final gnc_commodity src);

  /**
   * allocate and copy
   */
  public static native gnc_commodity gnc_commodity_clone(final gnc_commodity src, final QofBook dest_book);

  /* Commodity Accessor Routines - Get */

  /**
   * Retrieve the mnemonic for the specified commodity.  This will be a
   * pointer to a null terminated string of the form "ACME", "QWER",
   * etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the mnemonic for this commodity.  This string
   * is owned by the engine and should not be freed by the caller.
   */
  public static native String gnc_commodity_get_mnemonic(final gnc_commodity cm);

  /**
   * Retrieve the namespace for the specified commodity.  This will be
   * a pointer to a null terminated string of the form "AMEX",
   * "NASDAQ", etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the namespace for this commodity.  This string
   * is owned by the engine and should not be freed by the caller.
   */
  public static native String gnc_commodity_get_namespace(final gnc_commodity cm);

  /**
   * Retrieve the namespace data structure for the specified commodity.
   * This will be a pointer to another data structure.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the namespace data structure for this
   * commodity.
   */
  public static native gnc_commodity_namespace gnc_commodity_get_namespace_ds(final gnc_commodity cm);

  /**
   * Retrieve the full name for the specified commodity.  This will be
   * a pointer to a null terminated string of the form "Acme Systems,
   * Inc.", etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the full name for this commodity.  This string
   * is owned by the engine and should not be freed by the caller.
   */
  public static native String gnc_commodity_get_fullname(final gnc_commodity cm);

  /**
   * Retrieve the 'print' name for the specified commodity.  This will
   * be a pointer to a null terminated string of the form "Acme
   * Systems, Inc. (ACME)", etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the print name for this commodity.  This
   * string is owned by the engine and should not be freed by the
   * caller.
   */
  public static native String gnc_commodity_get_printname(final gnc_commodity cm);

  /**
   * Retrieve the 'exchange code' for the specified commodity.  This
   * will be a pointer to a null terminated string of the form
   * "AXQ14728", etc.  This field is often used when presenting
   * information to the user.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the exchange code for this commodity.  This
   * string is owned by the engine and should not be freed by the
   * caller.
   * @note This is a unique code that specifies a particular item or
   * set of shares of a commodity, not a code that specifies a stock
   * exchange.  That is the namespace field.
   */
  public static native String gnc_commodity_get_cusip(final gnc_commodity cm);

  /**
   * Retrieve the 'unique' name for the specified commodity.  This will
   * be a pointer to a null terminated string of the form "AMEX::ACME",
   * etc.  This field is often used when performing comparisons or
   * other functions invisible to the user.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the 'unique' name for this commodity.  This
   * string is owned by the engine and should not be freed by the
   * caller.
   */
  public static native String gnc_commodity_get_unique_name(final gnc_commodity cm);

  /**
   * Retrieve the fraction for the specified commodity.  This will be
   * an integer value specifying the number of fractional units that
   * one of these commodities can be divided into.  Should always be a
   * power of 10.
   *
   * @param cm A pointer to a commodity data structure.
   * @return The number of fractional units that one of these
   * commodities can be divided into.
   */
  public static native int gnc_commodity_get_fraction(final gnc_commodity cm);

  /**
   * Retrieve the automatic price quote flag for the specified
   * commodity.  This flag indicates whether stock quotes should be
   * retrieved for the specified stock.
   *
   * @param cm A pointer to a commodity data structure.
   * @return TRUE if quotes should be pulled for this commodity, FALSE
   * otherwise.
   */
  public static native boolean gnc_commodity_get_quote_flag(final gnc_commodity cm);

  /**
   * Retrieve the automatic price quote source for the specified
   * commodity.  This will be a pointer to a null terminated string of
   * the form "Yahoo (Asia)", etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the price quote source for this commodity.
   */
  public static native gnc_quote_source gnc_commodity_get_quote_source(final gnc_commodity cm);

  public static native gnc_quote_source gnc_commodity_get_default_quote_source(final gnc_commodity cm);

  /**
   * Retrieve the automatic price quote timezone for the specified
   * commodity.  This will be a pointer to a null terminated string of
   * the form "America/New_York", etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the price quote timezone for this commodity.
   * This string is owned by the engine and should not be freed by the
   * caller.
   */
  public static native String gnc_commodity_get_quote_tz(final gnc_commodity cm);

  /**
   * Retrieve the user-defined symbol for the specified commodity. This
   * will be a pointer to a nul terminated string like "£", "US$", etc.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the user-defined symbol for this commodity.
   * NULL means that the user didn't define any symbol, and that fallback to
   * e.g. the mnemonic is in order. This string is owned by the engine and
   * should not be freed by the caller.
   */
  public static native String gnc_commodity_get_user_symbol(final gnc_commodity cm);

  /**
   * Retrieve the default symbol for the specified commodity. This will
   * be a pointer to a nul terminated string like "£", "US$", etc. Note
   * that for the locale currency, you probably want to look at the
   * system-provided symbol first. See gnc_commodity_get_nice_symbol.
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the default symbol for this commodity.
   */
  public static native String gnc_commodity_get_default_symbol(final gnc_commodity cm);

  /**
   * Retrieve a symbol for the specified commodity, suitable for
   * display to the user. This will be a pointer to a nul terminated
   * string like "£", "US$", etc. That function is locale-aware and
   * will base its choice of symbol on the user-configured symbol,
   * the locale a
   *
   * @param cm A pointer to a commodity data structure.
   * @return A pointer to the symbol for this commodity.
   */
  public static native String gnc_commodity_get_nice_symbol(final gnc_commodity cm);

  /* Commodity Accessor Routines - Set */

  /**
   * Set the mnemonic for the specified commodity.  This should be a
   * pointer to a null terminated string of the form "ACME", "QWER",
   * etc.
   *
   * @param cm       A pointer to a commodity data structure.
   * @param mnemonic A pointer to the mnemonic for this commodity.
   *                 This string belongs to the caller and will be duplicated by the
   *                 engine.
   */
  public static native void gnc_commodity_set_mnemonic(final gnc_commodity cm, final String mnemonic);

  /**
   * Set the namespace for the specified commodity.  This should be a
   * pointer to a null terminated string of the form "AMEX", "NASDAQ",
   * etc.
   *
   * @param cm            A pointer to a commodity data structure.
   * @param new_namespace A pointer to the namespace for this commodity.
   *                      This string belongs to the caller and will be duplicated by the
   *                      engine.
   */
  public static native void gnc_commodity_set_namespace(final gnc_commodity cm, final String new_namespace);

  /**
   * Set the full name for the specified commodity.  This should be
   * a pointer to a null terminated string of the form "Acme Systems,
   * Inc.", etc.
   *
   * @param cm       A pointer to a commodity data structure.
   * @param fullname A pointer to the full name for this commodity.
   *                 This string belongs to the caller and will be duplicated by the
   *                 engine.
   */
  public static native void gnc_commodity_set_fullname(final gnc_commodity cm, final String fullname);

  /**
   * Set the 'exchange code' for the specified commodity.  This should
   * be a pointer to a null terminated string of the form "AXQ14728",
   * etc.
   *
   * @param cm    A pointer to a commodity data structure.
   * @param cusip A pointer to the cusip or other exchange specific
   *              data for this commodity.  This string belongs to the caller and
   *              will be duplicated by the engine.
   * @note This is a unique code that specifies a particular item or
   * set of shares of a commodity, not a code that specifies a stock
   * exchange.  That is the namespace field.
   */
  public static native void gnc_commodity_set_cusip(final gnc_commodity cm, final String cusip);

  /**
   * Set the fraction for the specified commodity.  This should be
   * an integer value specifying the number of fractional units that
   * one of these commodities can be divided into.  Should always be a
   * power of 10.
   *
   * @param cm                A pointer to a commodity data structure.
   * @param smallest_fraction The number of fractional units that one of
   *                          these commodities can be divided into.
   */
  public static native void gnc_commodity_set_fraction(final gnc_commodity cm, int smallest_fraction);

  /**
   * Set the automatic price quote flag for the specified commodity,
   * based on user input. This flag indicates whether stock quotes
   * should be retrieved for the specified stock.
   * <p>
   * It is necessary to have a separate function to distinguish when
   * this setting is being modified by a user so that the
   * auto-enabling/auto-disabling of currencies can be handled
   * properly.
   *
   * @param cm   A pointer to a commodity data structure.
   * @param flag TRUE if quotes should be pulled for this commodity, FALSE
   *             otherwise.
   */
  public static native void gnc_commodity_user_set_quote_flag(final gnc_commodity cm, final boolean flag);

  /**
   * Set the automatic price quote flag for the specified commodity.
   * This flag indicates whether stock quotes should be retrieved for
   * the specified stock.
   *
   * @param cm   A pointer to a commodity data structure.
   * @param flag TRUE if quotes should be pulled for this commodity, FALSE
   *             otherwise.
   */
  public static native void gnc_commodity_set_quote_flag(final gnc_commodity cm, final boolean flag);

  /**
   * Set the automatic price quote source for the specified commodity.
   * This should be a pointer to a null terminated string of the form
   * "Yahoo (Asia)", etc.  Legal values can be found in the
   * quote_sources array in the file gnc-ui-util.c.
   *
   * @param cm  A pointer to a commodity data structure.
   * @param src A pointer to the price quote source for this commodity.
   */
  public static native void gnc_commodity_set_quote_source(final gnc_commodity cm, final gnc_quote_source src);

  /**
   * Set the automatic price quote timezone for the specified
   * commodity.  This should be a pointer to a null terminated string
   * of the form "America/New_York", etc.  Legal values can be found in
   * the known_timezones array in the file src/gnome-utils/dialog-commodity.c.
   *
   * @param cm A pointer to a commodity data structure.
   * @param tz A pointer to the price quote timezone for this commodity.
   *           This string belongs to the caller and will be duplicated by the
   *           engine.
   */
  public static native void gnc_commodity_set_quote_tz(final gnc_commodity cm, final String tz);

  /**
   * Set a user-defined symbol for the specified commodity. This should
   * be a pointer to a nul terminated string like "£", "US$", etc.
   *
   * @param cm          A pointer to a commodity data structure.
   * @param user_symbol A pointer to the symbol for this commodity. This string
   *                    belongs to the caller and will be duplicated by the engine.
   */
  public static native void gnc_commodity_set_user_symbol(final gnc_commodity cm, final String user_symbol);

  /* Commodity Comparison */

  /**
   * This routine returns TRUE if the two commodities are equivalent.
   * Commodities are equivalent if they have the same namespace and
   * mnemonic.  Equivalent commodities may belong to different
   * exchanges, may have different fullnames, and may have different
   * fractions.
   */
  public static native boolean gnc_commodity_equiv(final gnc_commodity a, final gnc_commodity b);

  /**
   * This routine returns TRUE if the two commodities are equal.
   * Commodities are equal if they have the same namespace, mnemonic,
   * fullname, exchange private code and fraction.
   */
  public static native boolean gnc_commodity_equal(final gnc_commodity a, final gnc_commodity b);

  /**
   * This routine returns 0 if the two commodities are equal, 1 otherwise.
   * Commodities are equal if they have the same namespace, mnemonic,
   * fullname, exchange private code and fraction.
   * This function is useful for list-traversal comparison purposes where
   * The semantics are 0, &lt;0, or &gt;0 (equal, greater than, less than) rather
   * than "true or false"
   */
  public static native int gnc_commodity_compare(final gnc_commodity a, final gnc_commodity b);

  /**
   * A wrapper around gnc_commodity_compare() which offers the function
   * declaration that is needed for g_list_find_custom(), which needs
   * void pointers instead of gnc_commodity ones.
   */
  public static native int gnc_commodity_compare_void(final Pointer a, final Pointer b);

  /* Currency Checks */

  /**
   * Checks to see if the specified commodity namespace is the
   * namespace for ISO 4217 currencies.
   *
   * @param commodity_namespace The string to check.
   * @return TRUE if the string indicates an ISO currency, FALSE otherwise.
   */
  public static native boolean gnc_commodity_namespace_is_iso(final String commodity_namespace);

  /**
   * Checks to see if the specified commodity is an ISO 4217 recognized currency.
   *
   * @param cm The commodity to check.
   * @return TRUE if the commodity represents a currency, FALSE otherwise.
   */
  public static native boolean gnc_commodity_is_iso(final gnc_commodity cm);

  /**
   * Checks to see if the specified commodity is an ISO 4217 recognized
   * currency or a legacy currency.
   *
   * @param cm The commodity to check.
   * @return TRUE if the commodity represents a currency, FALSE otherwise.
   */
  public static native boolean gnc_commodity_is_currency(final gnc_commodity cm);

  /* Commodity Table */

  /**
   * Returns the commodity table associated with a book.
   */
  public static native gnc_commodity_table gnc_commodity_table_get_table(final QofBook book);

  /* Commodity Table Lookup functions */

  public static native gnc_commodity gnc_commodity_table_lookup(
      final gnc_commodity_table table,
      final String commodity_namespace,
      final String mnemonic
  );

  public static native gnc_commodity gnc_commodity_table_lookup_unique(
      final gnc_commodity_table table,
      final String unique_name
  );

  public static native gnc_commodity gnc_commodity_table_find_full(
      final gnc_commodity_table t,
      final String commodity_namespace,
      final String fullname
  );

  public static native gnc_commodity gnc_commodity_find_commodity_by_guid(final GncGUID guid, final QofBook book);

  /* Commodity Table Maintenance functions */

  /**
   * Add a new commodity to the commodity table.  This routine handles
   * the cases where the commodity already exists in the database (does
   * nothing), or another entries has the same namespace and mnemonic
   * (updates the existing entry).
   *
   * @param table A pointer to the commodity table
   * @param comm  A pointer to the commodity to add.
   * @return The added commodity. Null on error.
   * @note The commodity pointer passed to this function should not be
   * used after its return, as it may have been destroyed.  Use the
   * return value which is guaranteed to be valid.
   */
  public static native gnc_commodity gnc_commodity_table_insert(final gnc_commodity_table table, final gnc_commodity comm);

  /**
   * Remove a commodity from the commodity table. If the commodity to
   * remove doesn't exist, nothing happens.
   *
   * @param table A pointer to the commodity table
   * @param comm  A pointer to the commodity to remove.
   */
  public static native void gnc_commodity_table_remove(final gnc_commodity_table table, final gnc_commodity comm);

  /**
   * Add all the standard namespaces and currencies to the commodity
   * table.  This routine creates the namespaces for the NYSE, NASDAQ,
   * etc.  It also adds all of the ISO 4217 currencies to the commodity
   * table.
   *
   * @param table A pointer to the commodity table.
   * @param book  Unused.
   */
  public static native boolean gnc_commodity_table_add_default_data(final gnc_commodity_table table, final QofBook book);

  /* Commodity Table Namespace functions */

  /**
   * Return the textual name of a namespace data structure.
   *
   * @param ns A pointer to the namespace data structure.
   * @return A pointer to the name of the namespace.  This string is
   * owned by the engine and should not be freed by the caller.
   */
  public static native String gnc_commodity_namespace_get_name(final gnc_commodity_namespace ns);

  /**
   * Return the textual name of a namespace data structure in a form suitable to
   * present to the user.
   *
   * @param ns A pointer to the namespace data structure.
   * @return A pointer to the gui friendly name of the namespace.  This string is
   * owned by the engine and should not be freed by the caller.
   * @notes The returned string is marked for translation, but not translated yet.
   * If you want it translated pass the return value on to gettext.
   */
  public static native String gnc_commodity_namespace_get_gui_name(final gnc_commodity_namespace ns);


  /**
   * Return a list of all commodity data structures in the specified namespace.
   *
   * @return A pointer to the list of structures.  NULL if an invalid
   * argument was supplied.
   * @note This list is owned by the engine.  The caller must not free the list.
   */
  public static native GList gnc_commodity_namespace_get_commodity_list(final gnc_commodity_namespace ns);


  /**
   * Test to see if the indicated namespace exits in the commodity table.
   *
   * @param table               A pointer to the commodity table
   * @param commodity_namespace The new namespace to check.
   * @return 1 if the namespace exists. 0 if it doesn't exist, or the
   * routine was passed a bad argument.
   */
  public static native int gnc_commodity_table_has_namespace(final gnc_commodity_table table, final String commodity_namespace);

  /**
   * Return a list of all namespaces in the commodity table.  This
   * returns both system and user defined namespaces.
   *
   * @return A pointer to the list of names.  NULL if an invalid
   * argument was supplied.
   * @note It is the callers responsibility to free the list.
   */
  public static native GList gnc_commodity_table_get_namespaces(final gnc_commodity_table t);

  /**
   * Return a list of all namespace data structures in the commodity table.  This
   * returns both system and user defined namespace structures.
   *
   * @return A pointer to the list of structures.  NULL if an invalid
   * argument was supplied.
   * @note This list is owned by the engine.  The caller must not free the list.
   */
  public static native GList gnc_commodity_table_get_namespaces_list(final gnc_commodity_table t);

  /**
   * This function adds a new string to the list of commodity namespaces.
   * If the new namespace already exists, nothing happens.
   *
   * @param table               A pointer to the commodity table
   * @param commodity_namespace The new namespace to be added.
   * @param book                The book that the new namespace will belong to.
   * @return A pointer to the newly created namespace.
   */
  public static native gnc_commodity_namespace gnc_commodity_table_add_namespace(
      final gnc_commodity_table table,
      final String commodity_namespace,
      final QofBook book
  );

  /**
   * This function finds a commodity namespace in the set of existing commodity namespaces.
   *
   * @param table               A pointer to the commodity table
   * @param commodity_namespace The new namespace to be added.
   * @return The a pointer to the namespace found, or NULL if the
   * namespace doesn't exist.
   */
  public static native gnc_commodity_namespace gnc_commodity_table_find_namespace(
      final gnc_commodity_table table,
      final String commodity_namespace
  );

  /**
   * This function deletes a string from the list of commodity namespaces.
   * If the namespace does not exist, nothing happens.
   *
   * @param table               A pointer to the commodity table
   * @param commodity_namespace The namespace to be deleted.
   * @note This routine will destroy any commodities that exist as part
   * of this namespace.  Use it carefully.
   */
  public static native void gnc_commodity_table_delete_namespace(
      final gnc_commodity_table table,
      final String commodity_namespace
  );

  /* Commodity Table Accessor functions */

  /**
   * Returns the number of commodities in the commodity table.
   *
   * @param tbl A pointer to the commodity table
   * @return The number of commodities in the table. 0 if there are no
   * commodities, or the routine was passed a bad argument.
   */
  public static native GUInt gnc_commodity_table_get_size(final gnc_commodity_table tbl);

  /**
   * Return a list of all commodities in the commodity table that are
   * in the given namespace.
   *
   * @param table               A pointer to the commodity table
   * @param commodity_namespace A string indicating which commodities should be
   *                            returned. It is a required argument.
   * @return A pointer to the list of commodities.  NULL if an invalid
   * argument was supplied, or the namespace could not be found.
   * @note It is the callers responsibility to free the list.
   */
  public static native CommodityList gnc_commodity_table_get_commodities(final gnc_commodity_table table, final String commodity_namespace);

  /**
   * This function returns a list of commodities for which price quotes
   * should be retrieved.  It will scan the entire commodity table (or
   * a subset) and check each commodity to see if the price_quote_flag
   * field has been set.  All matching commodities are queued onto a
   * list, and the head of that list is returned.  Use the command-line
   * given expression as a filter on the commodities to be returned. If
   * non-null, only commodities in namespace that match the specified
   * regular expression are checked.  If none was given, all
   * commodities are checked.
   *
   * @param table A pointer to the commodity table
   * @return A pointer to a list of commodities.  NULL if invalid
   * arguments were supplied or if there no commodities are flagged for
   * quote retrieval.
   * @note It is the callers responsibility to free the list.
   */
  public static native CommodityList gnc_commodity_table_get_quotable_commodities(final gnc_commodity_table table);

  public interface CommodityTableForEachCb extends Callback {
    boolean invoke(final gnc_commodity cm, final Pointer user_data);
  }

  /**
   * Call a function once for each commodity in the commodity table.
   * This table walk returns whenever the end of the table is reached,
   * or the function returns FALSE.
   *
   * @param table     A pointer to the commodity table
   * @param f         The function to call for each commodity.
   * @param user_data A pointer that is passed into the function
   *                  unchanged by the table walk routine.
   */
  public static native boolean gnc_commodity_table_foreach_commodity(
      final gnc_commodity_table table,
      final CommodityTableForEachCb f,
      final Pointer user_data
  );

  /*  Commodity Table Private/Internal-Use Only Routines */

  /**
   * You probably shouldn't be using gnc_commodity_table_new() directly,
   * it's for internal use only. You should probably be using
   * gnc_commodity_table_get_table()
   */
  public static native gnc_commodity_table gnc_commodity_table_new();

  public static native void gnc_commodity_table_destroy(final gnc_commodity_table table);

  /**
   * Given the commodity 'findlike', this routine will find and return the
   * equivalent commodity (commodity with the same 'unique name') in
   * the indicated book.  This routine is primarily useful for setting
   * up clones of things across multiple books.
   */
  public static native gnc_commodity gnc_commodity_obtain_twin(final gnc_commodity findlike, final QofBook book);

  /**
   * You should probably not be using gnc_commodity_table_register()
   * It is an internal routine for registering the gncObject for the
   * commodity table.
   */
  public static native boolean gnc_commodity_table_register();

  public static native void gnc_commodity_begin_edit(final gnc_commodity cm);

  public static native void gnc_commodity_commit_edit(final gnc_commodity cm);

  /* Monetary value, commodity identity and numeric value */

  public static class gnc_monetary extends Structure {
    gnc_commodity commodity;
    gnc_numeric.ByValue value;
  }

  /**
   * A list of monetary values.  This could be a hash table, but as currently
   * used it rarely contains more than one or two different commodities so
   * it doesn't seem worth the trouble.
   */
  public static class MonetaryList extends GList {

  }

  /* Manipulate MonetaryList lists */

  /**
   * Add a gnc_monetary to the list
   */
  public static native MonetaryList gnc_monetary_list_add_monetary(final MonetaryList list, final gnc_monetary mon);

  /**
   * Delete all the zero-value entries from a list
   */
  public static native MonetaryList gnc_monetary_list_delete_zeros(final MonetaryList list);

  /**
   * Free a monetary list and all the items it points to
   */
  public static native void gnc_monetary_list_free(final MonetaryList list);
}
