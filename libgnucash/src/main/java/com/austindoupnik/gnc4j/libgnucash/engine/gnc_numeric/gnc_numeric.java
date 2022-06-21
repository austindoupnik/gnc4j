package com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import static com.sun.jna.Structure.FieldOrder;

@FieldOrder({
    gnc_numeric.Fields.num,
    gnc_numeric.Fields.denom,
})
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class gnc_numeric extends Structure {
  @NoArgsConstructor
  public static class ByValue extends gnc_numeric implements Structure.ByValue {
    private ByValue(final Pointer p) {
      super(p);
      read();
    }
  }

  public NativeLong num;
  public NativeLong denom;

  public gnc_numeric(final long num, final long denom) {
    this(new NativeLong(num), new NativeLong(denom));
  }

  protected gnc_numeric(final Pointer p) {
    super(p);
  }

  public ByValue byValue() {
    write();
    return new ByValue(getPointer());
  }
}
