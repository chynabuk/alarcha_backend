package com.project.alarcha.service;

import com.project.alarcha.entities.AdditionalService;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;

import java.util.List;

public interface AdditionalServiceService {
    AdditionalServiceModel createAdditionalService(AdditionalServiceModel additionalServiceModel);
    AdditionalServiceModel getById(Long additionalServiceId);
    AdditionalService getByAdditionalServiceId(Long additionalServiceId);
    List<AdditionalServiceModel> getAll();
    AdditionalServiceModel deleteAdditionalService(Long AdditionalServiceId);
    AdditionalServiceModel updateAdditionalService(AdditionalServiceModel additionalServiceModel);
}
