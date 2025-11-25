package dev.novov.amazewatchqr.controller;

import dev.novov.amazewatchqr.model.UploadResponse;
import dev.novov.amazewatchqr.service.WatchfaceService;
import dev.novov.amazewatchqr.service.qr.QrCodeService;
import dev.novov.amazewatchqr.service.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

@RestController
public class WatchfaceController {

    private final WatchfaceService watchfaceService;
    private final StorageService storageService;
    private final QrCodeService qrCodeService;

    public WatchfaceController(WatchfaceService watchfaceService, StorageService storageService,
            QrCodeService qrCodeService) {
        this.watchfaceService = watchfaceService;
        this.storageService = storageService;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/api/watchfaces/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        UploadResponse response = watchfaceService.processUpload(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/zpk/{id}/watchface.zpk")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String id) {
        Resource file = storageService.loadAsResource(id + ".zpk");
        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(value = "/qr/{id}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCode(@PathVariable String id) throws Exception {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String zpkHttpUrl = baseUrl + "/zpk/" + id + "/watchface.zpk";
        String zpkInstallUrl = qrCodeService.convertToZpkd1Url(zpkHttpUrl);

        BufferedImage image = qrCodeService.generateQrCode(zpkInstallUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return ResponseEntity.ok(baos.toByteArray());
    }
}
