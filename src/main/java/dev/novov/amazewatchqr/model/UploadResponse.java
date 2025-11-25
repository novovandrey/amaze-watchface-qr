package dev.novov.amazewatchqr.model;

public record UploadResponse(
        String id,
        String zpkHttpUrl,
        String zpkInstallUrl,
        String qrCodeUrl,
        String fileName,
        long size) {
}
