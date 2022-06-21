package com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine;

import com.austindoupnik.gnc4j.glib.GInt;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface TransactionCallback extends Callback {
  GInt invoke(final Transaction t, final Pointer data);
}
