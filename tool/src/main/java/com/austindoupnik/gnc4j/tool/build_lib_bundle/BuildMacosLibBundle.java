package com.austindoupnik.gnc4j.tool.build_lib_bundle;

import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.austindoupnik.gnc4j.tool.build_lib_bundle.ExecUtil.checkCall;
import static com.austindoupnik.gnc4j.tool.build_lib_bundle.ExecUtil.checkOutput;
import static picocli.CommandLine.Option;

public class BuildMacosLibBundle implements Callable<Integer> {
  public static void main(final String[] args) {
    final int exitCode = new CommandLine(new BuildMacosLibBundle()).execute(args);
    System.exit(exitCode);
  }

  @Option(names = "--gnucash-app", required = true)
  private Path gnucashApp;

  @Option(names = "--destination", defaultValue = "lib", required = true)
  private Path destination;

  @Override
  public Integer call() {
    final Path source = gnucashApp.resolve("Contents/Resources/lib");
    final List<Path> dylibs = Arrays.asList(
        Paths.get("libgnc-engine.dylib"),
        Paths.get("libgnc-core-utils.dylib"),
        Paths.get("libglib-2.0.0.dylib"),
        Paths.get("gnucash/libgncmod-backend-xml.dylib"),
        Paths.get("gnucash/libgncmod-backend-dbi.dylib")
    );
    final Path executablePath = gnucashApp.resolve("Contents/MacOS/Gnucash");
    process(source, executablePath, destination, dylibs);

    return 0;
  }

  private void process(final Path source, final Path executablePath, final Path destination, final List<Path> dylibs) {
    final Set<Path> allDylibs = new HashSet<>();
    for (final Path dylib : dylibs) {
      gatherAllDylib(allDylibs, executablePath, source.resolve(dylib));
    }

    final Set<Path> copiedDylibs = new HashSet<>();
    for (final Path dylib : allDylibs) {
      final Path copiedDylib = destination.resolve(source.relativize(dylib));
      try {
        Files.createDirectories(copiedDylib.getParent());
      } catch (final IOException ex) {
        throw new UncheckedIOException(ex);
      }
      try {
        Files.copy(dylib, copiedDylib);
      } catch (final IOException ex) {
        throw new UncheckedIOException(ex);
      }
      copiedDylibs.add(copiedDylib);
    }

    makeRelative(copiedDylibs);
    signAll(copiedDylibs);
  }

  private static void signAll(final Set<Path> dylibs) {
    for (final Path dylib : dylibs) {
      checkCall(BuildMacosLibBundle.class, "codesign", "--force", "--timestamp", "--sign", "-", dylib.toString());
    }
  }

  private void makeRelative(final Set<Path> dylibs) {
    final Path foo = Paths.get("@executable_path/../Resources/lib/");
    for (final Path dylib : dylibs) {
      final String output = checkOutput(BuildMacosLibBundle.class, "otool", "-L", dylib.toString());
      for (final String line : output.split(System.lineSeparator())) {
        final Matcher m = Pattern.compile("\\t(@executable_path/.*?\\.dylib) ").matcher(line);
        if (m.find()) {
          final Path p = Paths.get(m.group(1));
          final Path x = goUp(destination, dylib.getParent()).resolve(foo.relativize(p));
          checkCall(BuildMacosLibBundle.class, "install_name_tool", "-change", m.group(1), String.format("@loader_path/%s", x), dylib.toString());
        }
      }
    }
  }

  private static Path goUp(final Path parent, Path child) {
    final StringBuilder x = new StringBuilder();
    while (!Objects.equals(child, parent)) {
      child = child.getParent();
      x.append("../");
    }
    return Paths.get(x.toString());
  }

  private static void gatherAllDylib(final Set<Path> dylibs, final Path executablePath, final Path dylib) {
    dylibs.add(dylib);
    final String output = checkOutput(BuildMacosLibBundle.class, "otool", "-L", dylib.toString());
    for (final String line : output.split(System.lineSeparator())) {
      final Matcher m = Pattern.compile("\\t@executable_path/(.*?\\.dylib) ").matcher(line);
      if (m.find()) {
        final Path p = executablePath.getParent().resolve(m.group(1)).normalize();
        if (!dylibs.contains(p)) {
          gatherAllDylib(dylibs, executablePath, p);
        }
      }
    }
  }
}
