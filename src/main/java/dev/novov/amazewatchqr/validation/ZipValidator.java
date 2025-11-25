package dev.novov.amazewatchqr.validation;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ZipValidator {

    public boolean isValidWatchfaceZip(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }

        // Basic check for zip extension
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".zip")) {
            return false;
        }

        // Check for app.json inside
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("app.json")) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }
}
