package dev.novov.amazewatchqr.service;

import dev.novov.amazewatchqr.model.UploadResponse;
import dev.novov.amazewatchqr.service.packaging.ZpkPackager;
import dev.novov.amazewatchqr.service.qr.QrCodeService;
import dev.novov.amazewatchqr.service.storage.StorageService;
import dev.novov.amazewatchqr.validation.ZipValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class WatchfaceService {

    private final StorageService storageService;
    private final ZpkPackager zpkPackager;
    private final QrCodeService qrCodeService;
    private final ZipValidator zipValidator;

    public WatchfaceService(StorageService storageService, ZpkPackager zpkPackager, QrCodeService qrCodeService,
            ZipValidator zipValidator) {
        this.storageService = storageService;
        this.zpkPackager = zpkPackager;
        this.qrCodeService = qrCodeService;
        this.zipValidator = zipValidator;
    }

    public UploadResponse processUpload(MultipartFile file) throws IOException {
        if (!zipValidator.isValidWatchfaceZip(file)) {
            throw new IllegalArgumentException("Invalid watchface ZIP. Must contain app.json.");
        }

        String id = UUID.randomUUID().toString();

        String zpkFilename = id + ".zpk";
        Path zpkPath = storageService.getRootLocation().resolve(zpkFilename);

        zpkPackager.packageToZpk(file.getInputStream(), zpkPath);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String zpkHttpUrl = baseUrl + "/zpk/" + id + "/watchface.zpk";
        String zpkInstallUrl = qrCodeService.convertToZpkd1Url(zpkHttpUrl);
        String qrCodeUrl = baseUrl + "/qr/" + id + ".png";

        return new UploadResponse(
                id,
                zpkHttpUrl,
                zpkInstallUrl,
                qrCodeUrl,
                file.getOriginalFilename(),
                file.getSize());
    }
}
