package com.certificate.certificate.service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoseDto {

    private Integer doseNumber;
    private String vaccineType;
    private LocalDate vaccinationDate;
}
