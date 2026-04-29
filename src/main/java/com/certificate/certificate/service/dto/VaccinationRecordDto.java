package com.certificate.certificate.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordDto {

    private String userId;
    private List<DoseDto> doses;
    private String status;
}
