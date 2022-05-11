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
    public List<NatureTypeModel> createFloraAndFauna() {
        List<NatureType> natureTypes = new ArrayList<>();
        NatureType flora = new NatureType();
        flora.setType("Флора");
        flora.setIsDeleted(false);
        natureTypes.add(flora);

        NatureType fauna = new NatureType();
        fauna.setType("Фауна");
        fauna.setIsDeleted(false);
        natureTypes.add(fauna);

        if (natureTypeRepository.findAll().isEmpty()){
            natureTypeRepository.saveAll(natureTypes);
        }

        return convertToModels(natureTypes);
    }

    @Override
    public NatureTypeModel createFauna() {
        NatureType natureType = new NatureType();
        natureType.setType("Фауна Ала-Арчи");
        natureType.setIsDeleted(false);
        return null;
    }

    @Override
    public NatureTypeModel createFlora() {
        return null;
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
                natureTypeModels.add(toDetailedModel(natureType));
            }
        }

        return natureTypeModels;
    }

    @Override
    public List<NatureTypeModel> getForSelect() {
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
        List<NatureTypeModel> natureTypeModels = new ArrayList<>();
        for (NatureType natureType : natureTypes){
            natureTypeModels.add(toModel(natureType));
        }
        return natureTypeModels;
    }

    private NatureType initAndGet(NatureTypeModel natureTypeModel){
        NatureType natureType = new NatureType();
        natureType.setType(natureTypeModel.getType());
        natureType.setIsDeleted(false);
        return natureType;
    }

    private NatureTypeModel toModel(NatureType natureType){
        NatureTypeModel natureTypeModel = new NatureTypeModel();
        natureTypeModel.setId(natureType.getId());
        natureTypeModel.setType(natureType.getType());
        return natureTypeModel;
    }

    private NatureTypeModel toDetailedModel(NatureType natureType){
        NatureTypeModel natureTypeModel = new NatureTypeModel();
        natureTypeModel.setId(natureType.getId());
        natureTypeModel.setType(natureType.getType());
        if (natureType.getNatures() != null){
            natureTypeModel.setNatureModels(natureService.convertToModels(natureType.getNatures()));
        }

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