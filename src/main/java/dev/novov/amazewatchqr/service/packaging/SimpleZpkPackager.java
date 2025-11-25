package dev.novov.amazewatchqr.service.packaging;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class SimpleZpkPackager implements ZpkPackager {

    @Override
    public void packageToZpk(InputStream sourceZipInputStream, Path outputZpkPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputZpkPath))) {

            ZipEntry deviceZipEntry = new ZipEntry("device.zip");
            zos.putNextEntry(deviceZipEntry);

            byte[] buffer = new byte[4096];
            int len;
            while ((len = sourceZipInputStream.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            zos.closeEntry();
        }
    }
}
