package io.deephaven.engine.v2.utils.freezeby;

import io.deephaven.compilertools.ReplicateUtilities;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.deephaven.compilertools.ReplicatePrimitiveCode.*;

public class ReplicateFreezeBy {
    public static void main(String[] args) throws IOException {
        final List<String> results =
                charToAllButBoolean("DB/src/main/java/io/deephaven/engine/v2/utils/freezeby/CharFreezeByHelper.java");

        final Optional<String> longResult = results.stream().filter(s -> s.contains("Long")).findFirst();
        // noinspection OptionalGetWithoutIsPresent
        fixupLong(longResult.get());
        final String objectResult =
                charToObject("DB/src/main/java/io/deephaven/engine/v2/utils/freezeby/CharFreezeByHelper.java");
        fixupObject(objectResult);

        final String booleanResult =
                charToBoolean("DB/src/main/java/io/deephaven/engine/v2/utils/freezeby/CharFreezeByHelper.java");
        fixupBoolean(booleanResult);
    }

    private static void fixupObject(String objectResult) throws IOException {
        final File objectFile = new File(objectResult);
        final List<String> lines = FileUtils.readLines(objectFile, Charset.defaultCharset());
        final List<String> newLines = ReplicateUtilities.replaceRegion(lines, "clearIndex",
                Collections.singletonList("        removed.forAllLongs(idx -> resultSource.set(idx, null));"));
        FileUtils.writeLines(objectFile, newLines);
    }

    private static void fixupBoolean(String booleanResult) throws IOException {
        final File booleanFile = new File(booleanResult);
        final List<String> lines = FileUtils.readLines(booleanFile, Charset.defaultCharset());
        final List<String> newLines =
                ReplicateUtilities.globalReplacements(lines, "final BooleanChunk asBoolean = values.asBooleanChunk",
                        "final ObjectChunk<Boolean, ?> asBoolean = values.asObjectChunk");
        FileUtils.writeLines(booleanFile, newLines);
    }

    private static void fixupLong(String longResult) throws IOException {
        final File longFile = new File(longResult);
        final List<String> lines = FileUtils.readLines(longFile, Charset.defaultCharset());
        final List<String> newLines =
                ReplicateUtilities.globalReplacements(0, lines, "LongArraySource", "AbstractLongArraySource");
        FileUtils.writeLines(longFile, newLines);
    }
}
