package com.austindoupnik.gnc4j.glib;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class GLibGDate {
  static {
    nativeRegister(GLibGDate.class, "glib-2.0.0", "glib-2.0");
  }

  @FieldOrder(
      value = {
          GDate.Fields.value,
      }
  )
  @NoArgsConstructor
  @FieldNameConstants
  public static class GDate extends Structure {
    public long value;

    protected GDate(final Pointer p) {
      super(p);
    }

    @NoArgsConstructor
    public static class ByValue extends GDate implements Structure.ByValue {
      private ByValue(final Pointer p) {
        super(p);
        read();
      }
    }

    public ByValue byValue() {
      write();
      return new ByValue(getPointer());
    }
  }
}
