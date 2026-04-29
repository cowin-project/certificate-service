package com.certificate.certificate.service.service;

import com.certificate.certificate.service.client.VaccinationClient;
import com.certificate.certificate.service.dto.VaccinationRecordDto;
import com.certificate.certificate.service.util.PdfGeneratorUtil;
import com.certificate.certificate.service.util.QrCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {

    private final VaccinationClient vaccinationClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final QrCodeUtil qrCodeUtil;

    @Cacheable(value = "certificatePdfByUser", key = "#userId")
    public byte[] generateCertificate(String userId) {
        log.info("Generating certificate for userId={}", userId);
        VaccinationRecordDto vaccinationRecord = vaccinationClient.getVaccinationRecord(userId);
        byte[] qrCodeBytes = qrCodeUtil.generateQrCode(vaccinationRecord.getUserId(), vaccinationRecord.getStatus());
        return pdfGeneratorUtil.generateCertificatePdf(vaccinationRecord, qrCodeBytes);
    }
}
