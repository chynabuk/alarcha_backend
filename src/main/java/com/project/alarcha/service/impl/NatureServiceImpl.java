package com.project.alarcha.service.impl;


import com.project.alarcha.entities.Nature;
import com.project.alarcha.entities.NatureType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.NatureModel.NatureModel;
import com.project.alarcha.repositories.NatureRepository;
import com.project.alarcha.repositories.NatureTypeRepository;
import com.project.alarcha.service.NatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class NatureServiceImpl implements NatureService {
    @Autowired
    private NatureRepository natureRepository;

    @Autowired
    private NatureTypeRepository natureTypeRepository;


    @Override
    public NatureModel createNature(NatureModel natureModel) {

        Nature nature = new Nature();

        natureRepository.save(initAndGet(nature, natureModel));

        return natureModel;
    }

    @Override
    public NatureModel deleteNature(Long natureId) {
        Nature nature = getNatureById(natureId);

        nature.setIsDeleted(true);

        natureRepository.save(nature);

        return toModel(nature);
    }

    @Override
    public NatureModel updateNature(Long natureId) {
        return null;
    }

    @Override
    public List<NatureModel> getAll() {
        return convertToModels(natureRepository.findAll());
    }

    @Override
    public NatureModel getById(Long natureId) {
        return toModel(getNatureById(natureId));
    }

    private Nature getNatureById(Long natureId) {
        Nature nature = natureRepository.getById(natureId);

        if (nature == null){
            throw new ApiFailException("Nature not found");
        }

        if (nature.getIsDeleted() == null || nature.getIsDeleted()){
            throw new ApiFailException("Nature is not found or deleted");
        }

        return nature;
    }

    @Override
    public List<NatureModel> getFaunas() {
        return null;
    }

    @Override
    public List<NatureModel> getFloras() {
        return null;
    }

    @Override
    public List<NatureModel> convertToModels(List<Nature> natures) {
        List<NatureModel> natureModels = new ArrayList<>();

        for (Nature nature : natures){
            if (!nature.getIsDeleted()){
                natureModels.add(toModel(nature));
            }
        }

        return natureModels;
    }

    private Nature initAndGet(Nature nature, NatureModel natureModel){

        nature.setName(natureModel.getName());

        nature.setDescription(natureModel.getDescription());

        nature.setPhoto(natureModel.getImg().getBytes(StandardCharsets.UTF_8));

        NatureType natureType = natureTypeRepository.getById(natureModel.getNatureTypeId());

        if (natureType == null){
            throw new ApiFailException("Nature type is not found");
        }

        nature.setNatureType(natureType);

        nature.setIsDeleted(false);

        return nature;
    }

    private NatureModel toModel(Nature nature){
        NatureModel natureModel = new NatureModel();
        natureModel.setId(nature.getId());
        natureModel.setNatureTypeId(nature.getNatureType().getId());
        natureModel.setName(nature.getName());
        natureModel.setDescription(nature.getDescription());
        natureModel.setImg(new String(nature.getPhoto(), StandardCharsets.UTF_8));

        return natureModel;
    }
}
