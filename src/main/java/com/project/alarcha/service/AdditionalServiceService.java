package com.project.alarcha.service;

import com.project.alarcha.entities.AdditionalService;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;

public interface AdditionalServiceService {
    AdditionalServiceModel createAdditionalService(AdditionalServiceModel additionalServiceModel);
    AdditionalService getById(Long additionalServiceId);
}
