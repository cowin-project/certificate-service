package com.certificate.certificate.service.client;

import com.certificate.certificate.service.dto.VaccinationRecordDto;
import com.certificate.certificate.service.exception.RemoteServiceException;
import com.certificate.certificate.service.exception.VaccinationRecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class VaccinationClient {

    private final RestTemplate restTemplate;

    @Value("${vaccination.service.base-url:http://localhost:8082}")
    private String vaccinationServiceBaseUrl;

    public VaccinationRecordDto getVaccinationRecord(String userId) {
        String url = vaccinationServiceBaseUrl + "/vaccinations/" + userId;
        try {
            log.info("Calling Vaccination Service for userId={}", userId);
            VaccinationRecordDto response = restTemplate.getForObject(url, VaccinationRecordDto.class);
            if (response == null || response.getDoses() == null || response.getDoses().isEmpty()) {
                throw new VaccinationRecordNotFoundException("No vaccination record found for userId: " + userId);
            }
            return response;
        } catch (RestClientResponseException ex) {
            HttpStatusCode status = ex.getStatusCode();
            if (status.value() == 404) {
                throw new VaccinationRecordNotFoundException("No vaccination record found for userId: " + userId);
            }
            log.error("Vaccination service returned error status={} for userId={}", status.value(), userId);
            throw new RemoteServiceException("Vaccination service returned an error", ex);
        } catch (VaccinationRecordNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to call Vaccination service for userId={}", userId, ex);
            throw new RemoteServiceException("Unable to contact vaccination service", ex);
        }
    }
}
