package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Nature;
import com.project.alarcha.entities.NatureType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.NatureModel.NatureTypeModel;
import com.project.alarcha.repositories.NatureTypeRepository;
import com.project.alarcha.service.NatureService;
import com.project.alarcha.service.NatureTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NatureTypeServiceImpl implements NatureTypeService {

    @Autowired
    private NatureTypeRepository natureTypeRepository;

    @Autowired
    private NatureService natureService;

    @Override
    public NatureTypeModel createNatureType(NatureTypeModel natureTypeModel) {
//        if (natureTypeRepository.findAll().stream().map(natureType -> !natureType.getIsDeleted()).count() <= 2){
//
//        }
        natureTypeRepository.save(initAndGet(natureTypeModel));
        return natureTypeModel;
    }

    @Override
    public NatureTypeModel getById(Long natureTypeId) {
        NatureType natureType = getNatureType(natureTypeId);
        return toDetailedModel(natureType);
    }

    @Override
    public List<NatureTypeModel> getAll() {
        List<NatureTypeModel> natureTypeModels = new ArrayList<>();

        for (NatureType natureType : natureTypeRepository.findAll()){
            if (!natureType.getIsDeleted()){
                natureTypeModels.add(toModel(natureType));
            }
        }

        return natureTypeModels;
    }

    @Override
    public NatureTypeModel updateNatureType(NatureTypeModel natureTypeModel) {
        return null;
    }

    @Override
    public NatureTypeModel deleteNatureType(Long natureTypeId) {
        NatureType natureType = getNatureType(natureTypeId);

        natureType.setIsDeleted(true);

        List<Nature> natures = natureType.getNatures();
        if (natures != null){
            natures.forEach(nature -> nature.setIsDeleted(true));
        }

        natureTypeRepository.save(natureType);

        return toDetailedModel(natureType);
    }

    @Override
    public List<NatureTypeModel> convertToModels(List<NatureType> natureTypes) {
        return null;
    }

    private NatureType initAndGet(NatureTypeModel natureTypeModel){
        NatureType natureType = new NatureType();
        natureType.setType(natureTypeModel.getType());
        natureType.setIsDeleted(false);
        return natureType;
    }

    private NatureTypeModel toModel(NatureType natureType){
        NatureTypeModel natureTypeModel = new NatureTypeModel();
        natureTypeModel.setType(natureType.getType());
        return natureTypeModel;
    }

    private NatureTypeModel toDetailedModel(NatureType natureType){
        NatureTypeModel natureTypeModel = new NatureTypeModel();
        natureTypeModel.setType(natureType.getType());
        natureTypeModel.setNatureModels(natureService.convertToModels(natureType.getNatures()));

        return natureTypeModel;
    }

    private NatureType getNatureType(Long id){
        NatureType natureType = natureTypeRepository.getById(id);

        if (natureType == null){
            throw new ApiFailException("NatureType is not found");
        }

        if (natureType.getIsDeleted()){
            throw new ApiFailException("NatureType is not found or deleted");
        }

        return natureType;
    }
}