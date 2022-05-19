package com.project.alarcha.service;


import com.project.alarcha.entities.Nature;
import com.project.alarcha.models.NatureModel.NatureModel;

import java.util.List;

public interface NatureService {
    NatureModel createNature(NatureModel natureModel);
    NatureModel deleteNature(Long natureId);
    NatureModel updateNature(NatureModel natureModel);
    NatureModel getById(Long natureId);
    List<NatureModel> getAll();
    List<NatureModel> getForList();
    List<NatureModel> getFaunas();
    List<NatureModel> getFloras();
    List<NatureModel> convertToModels(List<Nature> natures);
}
