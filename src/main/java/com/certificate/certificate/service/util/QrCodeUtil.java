package com.certificate.certificate.service.util;

import com.certificate.certificate.service.exception.PdfGenerationException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class QrCodeUtil {

    @Value("${certificate.verification-base-url:http://localhost:8083/verify}")
    private String verificationBaseUrl;

    public byte[] generateQrCode(String userId, String status) {
        String payload = String.format("userId=%s;status=%s;verifyUrl=%s/%s", userId, status, verificationBaseUrl, userId);
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BitMatrix matrix = new MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, 180, 180, hints);
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new PdfGenerationException("Failed to generate QR code", ex);
        }
    }
}
