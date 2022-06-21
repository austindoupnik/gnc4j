package com.austindoupnik.gnc4j.libgnucash.engine.account;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface AccountCb2 extends Callback {
  Pointer invoke(final Account a, final Pointer data);
}
