package com.certificate.certificate.service.controller;

import com.certificate.certificate.service.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getCertificate(@PathVariable String userId) {
        byte[] pdfBytes = certificateService.generateCertificate(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("vaccination-certificate-" + userId + ".pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
