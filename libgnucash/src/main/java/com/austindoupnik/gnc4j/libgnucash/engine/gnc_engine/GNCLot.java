package com.austindoupnik.gnc4j.libgnucash.engine.gnc_engine;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GNCLot extends PointerType {
  public GNCLot(final Pointer p) {
    super(p);
  }
}
