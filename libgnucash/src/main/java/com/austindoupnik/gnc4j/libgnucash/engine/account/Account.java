package com.austindoupnik.gnc4j.libgnucash.engine.account;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Account extends PointerType {
  public Account(final Pointer p) {
    super(p);
  }
}
