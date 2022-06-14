package com.austindoupnik.gnc4j.jna_core;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;

import java.util.Objects;

public interface JnaEnum<E extends Enum<E> & JnaEnum<E>> extends NativeMapped {
  static <E extends Enum<E> & JnaEnum<E>> E findByValue(final Class<E> type, final int value) {
    final E[] elements = type.getEnumConstants();
    for (final E element : elements) {
      if (Objects.equals(value, element.getValue())) {
        return element;
      }
    }
    throw new RuntimeException();
  }

  abstract class JnaEnumByReference<E extends Enum<E> & JnaEnum<E>> extends com.sun.jna.ptr.ByReference {
    public JnaEnumByReference() {
      super(NativeInt.SIZE);
    }

    public void setValue(final E value) {
      getPointer().setInt(0, value.getValue());
    }

    public E getValue() {
      return findByValue(getPointer().getInt(0));
    }

    protected abstract E findByValue(final int value);
  }

  int getValue();

  @Override
  @SuppressWarnings("unchecked")
  default Object fromNative(final Object nativeValue, final FromNativeContext context) {
    return JnaEnum.findByValue((Class<E>) getClass(), (Integer) nativeValue);
  }

  @Override
  default Object toNative() {
    return getValue();
  }

  @Override
  default Class<?> nativeType() {
    return Integer.TYPE;
  }
}
