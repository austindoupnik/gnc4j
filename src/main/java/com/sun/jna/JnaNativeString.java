package com.sun.jna;

public class JnaNativeString extends NativeString {
  public static Pointer asPointer(final String string) {
    return new JnaNativeString(string).getPointer();
  }

  public JnaNativeString(final String string) {
    super(string);
  }
}
