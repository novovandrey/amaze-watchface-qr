package dev.novov.amazewatchqr.service.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class QrCodeService {

    public BufferedImage generateQrCode(String url) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public String convertToZpkd1Url(String httpUrl) {
        if (httpUrl == null) {
            return null;
        }
        if (httpUrl.startsWith("http://")) {
            return httpUrl.replace("http://", "zpkd1://");
        } else if (httpUrl.startsWith("https://")) {
            return httpUrl.replace("https://", "zpkd1://");
        }
        return httpUrl;
    }
}
