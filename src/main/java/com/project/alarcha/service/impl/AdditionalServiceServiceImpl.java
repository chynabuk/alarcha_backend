package com.project.alarcha.service.impl;


import com.project.alarcha.entities.AdditionalService;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;
import com.project.alarcha.repositories.AdditionalServiceRepository;
import com.project.alarcha.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public AdditionalServiceModel getById(Long additionalServiceId) {
        AdditionalService additionalService = additionalServiceRepository.getById(additionalServiceId);

        if(additionalService != null){
            if(additionalService.getIsDeleted()){
                throw new ApiFailException("There is no additional service like this!");
            }
        }

        return toModel(additionalService);
    }

    @Override
    public AdditionalService getByAdditionalServiceId(Long additionalServiceId) {
        return additionalServiceRepository.getById(additionalServiceId);
    }

    @Override
    public List<AdditionalServiceModel> getAll() {
        List<AdditionalServiceModel> additionalServiceModels = new ArrayList<>();

        for(AdditionalService additionalService : additionalServiceRepository.findAll()){
            if(!additionalService.getIsDeleted()){
                additionalServiceModels.add(toModel(additionalService));
            }
        }

        return additionalServiceModels;
    }

    @Override
    public AdditionalServiceModel deleteAdditionalService(Long AdditionalServiceId) {
        AdditionalService additionalService = additionalServiceRepository.getById(AdditionalServiceId);

        if(additionalService != null){
            if(additionalService.getIsDeleted()){
                throw new ApiFailException("Additional service is already deleted!");
            }

            additionalService.setIsDeleted(true);
        }

        additionalServiceRepository.save(additionalService);

        return toModel(additionalService);
    }

    @Override
    public AdditionalServiceModel updateAdditionalService(AdditionalServiceModel additionalServiceModel) {
        return null;
    }

    private AdditionalService initAndGet(AdditionalService additionalService, AdditionalServiceModel additionalServiceModel){
        additionalService.setName(additionalServiceModel.getName());
        additionalService.setPrice(additionalServiceModel.getPrice());
        additionalService.setDescription(additionalServiceModel.getDescription());
        additionalService.setInStock(additionalServiceModel.getInStock());
        additionalService.setIsDeleted(false);

        return additionalService;
    }

    private AdditionalServiceModel toModel(AdditionalService additionalService){
        AdditionalServiceModel additionalServiceModel = new AdditionalServiceModel();

        additionalServiceModel.setId(additionalService.getId());
        additionalServiceModel.setName(additionalService.getName());
        additionalServiceModel.setDescription(additionalService.getDescription());
        additionalServiceModel.setInStock(additionalService.getInStock());
        additionalServiceModel.setPrice(additionalService.getPrice());

        return additionalServiceModel;
    }
}
