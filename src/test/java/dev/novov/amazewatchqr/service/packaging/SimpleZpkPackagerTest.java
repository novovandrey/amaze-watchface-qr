package dev.novov.amazewatchqr.service.packaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

class SimpleZpkPackagerTest {

    @TempDir
    Path tempDir;

    private SimpleZpkPackager packager;

    @BeforeEach
    void setUp() {
        packager = new SimpleZpkPackager();
    }

    @Test
    void packageToZpk_shouldWrapInDeviceZip() throws IOException {
        Path outputZpk = tempDir.resolve("output.zpk");
        String sourceContent = "SOURCE ZIP CONTENT";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceContent.getBytes());

        packager.packageToZpk(inputStream, outputZpk);

        assertTrue(Files.exists(outputZpk));

        try (ZipFile zpk = new ZipFile(outputZpk.toFile())) {
            ZipEntry deviceZipEntry = zpk.getEntry("device.zip");
            assertNotNull(deviceZipEntry, "ZPK must contain device.zip");

            try (var is = zpk.getInputStream(deviceZipEntry)) {
                String content = new String(is.readAllBytes());
                assertEquals(sourceContent, content);
            }
        }
    }
}
