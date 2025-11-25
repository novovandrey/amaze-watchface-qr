package dev.novov.amazewatchqr.service.packaging;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Interface for packaging a watchface source ZIP into a Zepp OS compatible .zpk file.
 */
public interface ZpkPackager {

    /**
     * Converts a source ZIP into a .zpk file.
     *
     * @param sourceZipInputStream The input stream of the uploaded watchface ZIP.
     * @param outputZpkPath        The path where the resulting .zpk file should be written.
     * @throws IOException If an I/O error occurs during packaging.
     */
    void packageToZpk(InputStream sourceZipInputStream, Path outputZpkPath) throws IOException;
}
