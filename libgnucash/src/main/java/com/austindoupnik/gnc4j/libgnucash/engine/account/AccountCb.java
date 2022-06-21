package com.austindoupnik.gnc4j.libgnucash.engine.account;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface AccountCb extends Callback {
  void invoke(final Account a, final Pointer data);
}
