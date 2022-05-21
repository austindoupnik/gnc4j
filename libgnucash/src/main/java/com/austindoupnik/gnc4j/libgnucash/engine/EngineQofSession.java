package com.austindoupnik.gnc4j.libgnucash.engine;

import com.sun.jna.Callback;
import com.sun.jna.PointerType;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.austindoupnik.gnc4j.jna_core.NativeRegister.nativeRegister;
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineQofBook.QofBook;

@UtilityClass
public class EngineQofSession {
  static {
    nativeRegister(EngineQofSession.class, "gnc-engine");
  }

  /**
   * Mode for opening sessions.
   * This replaces three booleans that were passed in order: ignore_lock, create,
   * and force. It's structured so that one can use it as a bit field with the
   * values in the same order, i.e. ignore_lock = 1 &lt;&lt; 2, create = 1 &lt;&lt; 1, and
   * force = 1.
   */
  @AllArgsConstructor
  public enum SessionOpenMode {
    /**
     * Open will fail if the URI doesn't exist or is locked.
     */
    SESSION_NORMAL_OPEN(0),
    /**
     * Create a new store at the URI. It will fail if the store already exists and is found to contain data that would be overwritten.
     */
    SESSION_NEW_STORE(2),
    /**
     * Create a new store at the URI even if a store already exists there.
     */
    SESSION_NEW_OVERWRITE(3),
    /**
     * Open the session read-only, ignoring any existing lock and not creating one if the URI isn't locked.
     */
    SESSION_READ_ONLY(4),
    /**
     * Open the session, taking over any existing lock.
     */
    SESSION_BREAK_LOCK(5);

    private final int value;
  }

  public static class QofSession extends PointerType {

  }

  public static native QofSession qof_session_new(final QofBook book);

  public static native void qof_session_destroy(final QofSession session);

  public interface QofPercentageFunc extends Callback {
    void invoke(final String message, final double percent);
  }

  /**
   * @see #qof_session_begin(QofSession, String, int)
   */
  public static void qof_session_begin(final QofSession session, final String new_uri, final SessionOpenMode mode) {
    qof_session_begin(session, new_uri, mode.value);
  }

  /**
   * Begins a new session.
   * <p>
   * <strong>SessionOpenMode</strong>
   * <p>
   * <code>SESSION_NORMAL_OPEN</code>: Find an existing file or database at the provided uri and
   * open it if it is unlocked. If it is locked post a QOF_BACKEND_LOCKED error.
   * <p>
   * <code>SESSION_NEW_STORE</code>: Check for an existing file or database at the provided
   * uri and if none is found, create it. If the file or database exists post a
   * QOF_BACKED_STORE_EXISTS and return.
   * <p>
   * <code>SESSION_NEW_OVERWRITE</code>: Create a new file or database at the provided uri,
   * deleting any existing file or database.
   * <p>
   * <code>SESSION_READ_ONLY</code>: Find an existing file or database and open it without
   * disturbing the lock if it exists or setting one if not. This will also set a
   * flag on the book that will prevent many elements from being edited and will
   * prevent the backend from saving any edits.
   * <p>
   * <code>SESSION_BREAK_LOCK</code>: Find an existing file or database, lock it, and open
   * it. If there is already a lock replace it with a new one for this session.
   * <p>
   * <strong>Errors</strong>
   * <p>
   * This function signals failure by queuing errors. After it completes use
   * qof_session_get_error() and test that the value is <code>ERROR_BACKEND_NONE</code> to
   * determine that the session began successfully.
   *
   * @param session Newly-allocated with qof_session_new.
   * @param new_uri must be a string in the form of a URI/URL. The access method
   *                specified depends on the loaded backends. Paths may be relative or
   *                absolute.  If the path is relative, that is if the argument is
   *                "file://somefile.xml", then the current working directory is
   *                assumed. Customized backends can choose to search other
   *                application-specific directories or URI schemes as well.
   * @param mode    The SessionOpenMode.
   */
  public static native void qof_session_begin(final QofSession session, final String new_uri, final int mode);

  /**
   * The qof_session_load() method causes the QofBook to be made ready to
   * to use with this URL/datastore.   When the URL points at a file,
   * then this routine would load the data from the file.  With remote
   * backends, e.g. network or SQL, this would load only enough data
   * to make the book actually usable; it would not cause *all* of the
   * data to be loaded.
   * <p>
   * XXX the current design tries to accommodate multiple calls to 'load'
   * for each session, each time wiping out the old books; this seems
   * wrong to me, and should be restricted to allow only one load per
   * session.
   */
  public static native void qof_session_load(final QofSession session, final QofPercentageFunc percentage_func);

  /**
   * The qof_session_end() method will release the session lock. For the
   * file backend, it will *not* save the data to a file. Thus,
   * this method acts as an "abort" or "rollback" primitive.  However,
   * for other backends, such as the sql backend, the data would have
   * been written out before this, and so this routines wouldn't
   * roll-back anything; it would just shut the connection.
   */
  public static native void qof_session_end(final QofSession session);

  /**
   * The qof_session_save() method will commit all changes that have been
   * made to the session. For the file backend, this is nothing
   * more than a write to the file of the current Accounts &amp; etc.
   * For the SQL backend, this is typically a no-op (since all data
   * has already been written out to the database.
   */
  public static native void qof_session_save(final QofSession session, final QofPercentageFunc percentage_func);

  /**
   * Returns the QofBook of this session.
   */
  public static native QofBook qof_session_get_book(final QofSession session);
}
