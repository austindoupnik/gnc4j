package com.austindoupnik.gnc4j.glib;

import com.sun.jna.Callback;
import com.sun.jna.IntegerType;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;

@UtilityClass
public class GLibGList {
  static {
    nativeRegister(GLibGList.class, "glib-2.0.0", "glib-2.0");
  }

  public static <T> GList fromList(final List<T> elements, final Function<T, Pointer> converter) {
    GList result = null;
    for (final T e : elements) {
      result = g_list_append(result, converter.apply(e));
    }
    return result;
  }

  public static <T> List<T> toList(final GList elements, final Function<Pointer, T> converter) {
    final List<T> result = new ArrayList<>();
    g_list_foreach(elements, (data0, userData) -> result.add(converter.apply(data0)), null);
    return result;
  }

  @FieldOrder({
      GList.Fields.data,
      GList.Fields.next,
      GList.Fields.prev,
  })
  @NoArgsConstructor
  @AllArgsConstructor
  @FieldNameConstants
  public static class GList extends Structure {
    public static class ByReference extends GList implements Structure.ByReference {

    }

    public Pointer data;

    public GList.ByReference next;
    public GList.ByReference prev;
  }

  public static class GUInt extends IntegerType {
    public GUInt() {
      this(0);
    }

    public GUInt(final long value) {
      super(4, value, true);
    }
  }

  public interface GFunc extends Callback {
    void invoke(final Pointer data, final Pointer userData);
  }

  /**
   * g_list_free:
   * <p>
   * Frees all of the memory used by a #GList.
   * The freed elements are returned to the slice allocator.
   * <p>
   * If list elements contain dynamically-allocated memory, you should
   * either use g_list_free_full() or free them manually first.
   * <p>
   * It can be combined with g_steal_pointer() to ensure the list head pointer
   * is not left dangling:
   * <code>
   * GList *list_of_borrowed_things = …;  &#47;* (transfer container) *&#47;
   * g_list_free (g_steal_pointer (&amp;list_of_borrowed_things));
   * </code>
   *
   * @param list the first link of a #GList
   */
  public static native void g_list_free(final GList list);

  /**
   * g_list_append:
   * Adds a new element on to the end of the list.
   * <p>
   * Note that the return value is the new start of the list,
   * if @list was empty; make sure you store the new value.
   * <p>
   * g_list_append() has to traverse the entire list to find the end,
   * which is inefficient when adding multiple elements. A common idiom
   * to avoid the inefficiency is to use g_list_prepend() and reverse
   * the list with g_list_reverse() when all elements have been added.
   * <pre>
   * // Notice that these are initialized to the empty list.
   * GList *string_list = NULL, *number_list = NULL;
   *
   * // This is a list of strings.
   * string_list = g_list_append (string_list, "first");
   * string_list = g_list_append (string_list, "second");
   *
   * // This is a list of integers.
   * number_list = g_list_append (number_list, GINT_TO_POINTER (27));
   * number_list = g_list_append (number_list, GINT_TO_POINTER (14));
   * </pre>
   *
   * @param list a pointer to a #GList
   * @param data the data for the new element
   * @return either @list or the new start of the #GList if @list was %NULL
   */
  public static native GList g_list_append(final GList list, final Pointer data);

  /**
   * g_list_length:
   * Gets the number of elements in a #GList.
   * <p>
   * This function iterates over the whole list to count its elements.
   * Use a #GQueue instead of a GList if you regularly need the number
   * of items. To check whether the list is non-empty, it is faster to check @list against %NULL.
   *
   * @param list: a #GList, this must point to the top of the list
   * @return the number of elements in the #GList
   */
  public static native GUInt g_list_length(final GList list);

  /**
   * g_list_foreach:
   * Calls a function for each element of a #GList.
   * <p>
   * It is safe for @func to remove the element from @list, but it must
   * not modify any part of the list after that element.
   *
   * @param list      a #GList, this must point to the top of the list
   * @param func      the function to call with each element's data
   * @param user_data user data to pass to the function
   */
  public static native void g_list_foreach(final GList list, final GFunc func, final Pointer user_data);

  /**
   * g_list_nth_data:
   * Gets the data of the element at the given position.
   * <p>
   * This iterates over the list until it reaches the @n-th position. If you
   * intend to iterate over every element, it is better to use a for-loop as
   * described in the #GList introduction.
   *
   * @param list a #GList, this must point to the top of the list
   * @param n    the position of the element
   * @return the element's data, or %NULL if the position
   * is off the end of the #GList
   */
  public static native Pointer g_list_nth_data(final GList list, final GUInt n);
}
