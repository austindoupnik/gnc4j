package com.austindoupnik.gnc4j.libgnucash.engine;

import com.austindoupnik.gnc4j.glib.GInt;
import com.austindoupnik.gnc4j.libgnucash.engine.EngineTransaction.Transaction;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.StringArray;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.glib.GLibGList.GList;
import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class EngineGncEngine {
  static {
    nativeRegister(EngineGncEngine.class, "gnc-engine");
  }

  interface TransactionCallback extends Callback {
    GInt invoke(final Transaction t, final Pointer data);
  }

  @NoArgsConstructor
  public static class GNCLot extends PointerType {
    public GNCLot(final Pointer p) {
      super(p);
    }
  }

  public class LotList extends GList {

  }

  public class SplitList extends GList {

  }

  /**
   * gnc_engine_init should be called before gnc engine
   * functions can be used.
   */
  public static native void gnc_engine_init(final int argc, final StringArray argv);

  /**
   * @see #gnc_engine_init(int, StringArray)
   */
  public static void gnc_engine_init(final int argc, final String[] argv) {
    gnc_engine_init(argc, new StringArray(argv));
  }
}
