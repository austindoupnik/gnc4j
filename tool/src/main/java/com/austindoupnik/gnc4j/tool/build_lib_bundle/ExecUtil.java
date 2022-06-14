package com.austindoupnik.gnc4j.tool.build_lib_bundle;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecUtil {
  public static void checkCall(final Class<?> type, final String... command) {
    final ProcessExecutor executor = new ProcessExecutor()
        .command(command)
        .redirectOutput(Slf4jStream.of(type).asInfo())
        .redirectError(Slf4jStream.of(type).asError())
        .exitValue(0)
        .destroyOnExit()
        .timeout(2, TimeUnit.MINUTES);

    try {
      executor.execute();
    } catch (final IOException ex) {
      throw new UncheckedIOException(ex);
    } catch (final InterruptedException | TimeoutException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static String checkOutput(final Class<?> type, final String... command) {
    final ProcessExecutor executor = new ProcessExecutor()
        .command(command)
        .redirectError(Slf4jStream.of(type).asError())
        .exitValue(0)
        .destroyOnExit()
        .timeout(2, TimeUnit.MINUTES)
        .readOutput(true);

    final ProcessResult result;
    try {
      result = executor.execute();
    } catch (final IOException ex) {
      throw new UncheckedIOException(ex);
    } catch (final InterruptedException | TimeoutException ex) {
      throw new RuntimeException(ex);
    }

    return result.outputUTF8();
  }
}
