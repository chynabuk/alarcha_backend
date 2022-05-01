package com.project.alarcha.service.impl;


import com.project.alarcha.entities.AdditionalService;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;
import com.project.alarcha.repositories.AdditionalServiceRepository;
import com.project.alarcha.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {
    @Autowired
    AdditionalServiceRepository additionalServiceRepository;

    @Override
    public AdditionalServiceModel createAdditionalService(AdditionalServiceModel additionalServiceModel) {
        AdditionalService additionalService = new AdditionalService();
        additionalServiceRepository.save(initAndGet(additionalService, additionalServiceModel));

        return additionalServiceModel;
    }
    private AdditionalService initAndGet(AdditionalService additionalService, AdditionalServiceModel additionalServiceModel){
        additionalService.setName(additionalServiceModel.getName());
        additionalService.setPrice(additionalServiceModel.getPrice());
        additionalService.setDescription(additionalServiceModel.getDescription());

        return additionalService;
    }
    @Override
    public AdditionalService getById(Long additionalServiceId) {
        return additionalServiceRepository.getById(additionalServiceId);
    }
}
