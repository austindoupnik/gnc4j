package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EngineGncNumeric {
  @FieldOrder({
      gnc_numeric.Fields.num,
      gnc_numeric.Fields.denom,
  })
  @NoArgsConstructor
  @AllArgsConstructor
  @FieldNameConstants
  public static class gnc_numeric extends Structure {
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

  /**
   * Read a gnc_numeric from str, skipping any leading whitespace.
   * Return TRUE on success and store the resulting value in "n".
   * Return NULL on error.
   */
  public static native boolean string_to_gnc_numeric(final String str, final gnc_numeric n);

  /**
   * Convert numeric to floating-point value.
   */
  public static native double gnc_numeric_to_double(final gnc_numeric.ByValue n);

  /**
   * Convert to string. The returned buffer is to be g_free'd by the
   * caller (it was allocated through g_strdup)
   */
  public static native String gnc_numeric_to_string(final gnc_numeric.ByValue n);
}
