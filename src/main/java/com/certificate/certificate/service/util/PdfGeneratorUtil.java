package com.certificate.certificate.service.util;

import com.certificate.certificate.service.dto.DoseDto;
import com.certificate.certificate.service.dto.VaccinationRecordDto;
import com.certificate.certificate.service.exception.PdfGenerationException;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class PdfGeneratorUtil {

    public byte[] generateCertificatePdf(VaccinationRecordDto record, byte[] qrCodeBytes) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Vaccination Certificate", titleFont));
            document.add(new Paragraph("---------------------------------------"));
            document.add(new Paragraph("User ID: " + record.getUserId(), bodyFont));
            document.add(new Paragraph("Vaccination Status: " + record.getStatus(), bodyFont));
            document.add(new Paragraph("Date of Issue: " + LocalDate.now(), bodyFont));
            document.add(new Paragraph(" "));

            List<DoseDto> sortedDoses = record.getDoses().stream()
                    .sorted(Comparator.comparing(DoseDto::getDoseNumber))
                    .toList();

            for (DoseDto dose : sortedDoses) {
                document.add(new Paragraph("Dose " + dose.getDoseNumber() + ":", bodyFont));
                document.add(new Paragraph("  Vaccine Type: " + dose.getVaccineType(), bodyFont));
                document.add(new Paragraph("  Date: " + dose.getVaccinationDate(), bodyFont));
            }

            if (qrCodeBytes != null && qrCodeBytes.length > 0) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Scan QR for verification", bodyFont));
                Image qrImage = Image.getInstance(qrCodeBytes);
                qrImage.scaleToFit(150, 150);
                document.add(qrImage);
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new PdfGenerationException("Failed to generate certificate PDF", ex);
        }
    }
}
