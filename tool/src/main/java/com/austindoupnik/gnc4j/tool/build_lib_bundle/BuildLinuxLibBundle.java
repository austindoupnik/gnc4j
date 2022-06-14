package com.austindoupnik.gnc4j.tool.build_lib_bundle;

import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.austindoupnik.gnc4j.tool.build_lib_bundle.ExecUtil.checkOutput;
import static picocli.CommandLine.Option;

public class BuildLinuxLibBundle implements Callable<Integer> {
  public static void main(final String[] args) {
    final int exitCode = new CommandLine(new BuildLinuxLibBundle()).execute(args);
    System.exit(exitCode);
  }

  @Option(names = "--prefix", required = true)
  private Path prefix;

  @Option(names = "--destination", defaultValue = "lib", required = true)
  private Path destination;

  @Override
  public Integer call() {
    final Set<Path> result = new HashSet<>();
    search(prefix.resolve(Paths.get("libgnc-engine.so")), result, this::getSharedLibraries);
    search(prefix.resolve(Paths.get("libgnc-core-utils.so")), result, this::getSharedLibraries);
    search(prefix.resolve(Paths.get("gnucash/libgncmod-backend-xml.so")), result, this::getSharedLibraries);
    search(prefix.resolve(Paths.get("gnucash/libgncmod-backend-dbi.so")), result, this::getSharedLibraries);

    result.forEach(p -> {
      final Path target;
      if (p.startsWith(prefix)) {
        final Path x = prefix.relativize(p);
        target = destination.resolve(x);
      } else {
        target = destination.resolve(p.getFileName());
      }
      try {
        Files.createDirectories(target.getParent());
        Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
      } catch (final IOException ex) {
        throw new UncheckedIOException(ex);
      }
    });
    return 0;
  }

  public static <T> void search(final T current, final Set<T> visited, final Function<T, Iterable<T>> getChildren) {
    if (visited.contains(current)) {
      return;
    }
    visited.add(current);
    final Iterable<T> children = getChildren.apply(current);
    for (final T child : children) {
      search(child, visited, getChildren);
    }
  }

  private List<Path> getSharedLibraries(final Path sharedLibrary) {
    final Pattern pattern = Pattern.compile(".* => (.*) \\(.*\\)");
    final String output = checkOutput(getClass(), "ldd", sharedLibrary.toString());
    return Stream.of(output.split(System.lineSeparator()))
        .map(pattern::matcher)
        .filter(Matcher::find)
        .map(m -> m.group(1))
        .map(Paths::get)
        .collect(Collectors.toList());
  }
}
