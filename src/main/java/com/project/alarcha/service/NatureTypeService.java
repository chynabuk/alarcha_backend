package com.project.alarcha.service;

import com.project.alarcha.entities.NatureType;
import com.project.alarcha.models.NatureModel.NatureTypeModel;

import java.util.List;

public interface NatureTypeService {
    NatureTypeModel createNatureType(NatureTypeModel natureTypeModel);
    NatureTypeModel getById(Long natureTypeId);
    List<NatureTypeModel> getAll();
    List<NatureTypeModel> getForSelect();
    NatureTypeModel updateNatureType(NatureTypeModel natureTypeModel);
    NatureTypeModel deleteNatureType(Long natureTypeId);
    List<NatureTypeModel> convertToModels(List<NatureType> natureTypes);
    NatureTypeModel createFlora();
    NatureTypeModel createFauna();
    List<NatureTypeModel> createFloraAndFauna();
}
