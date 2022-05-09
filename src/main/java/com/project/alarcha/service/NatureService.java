package com.project.alarcha.service;


import com.project.alarcha.entities.Nature;
import com.project.alarcha.models.NatureModel.NatureModel;

import java.util.List;

public interface NatureService {
    NatureModel createNature(NatureModel natureModel);
    NatureModel deleteNature(Long natureId);
    NatureModel updateNature(Long natureId);
    NatureModel getById(Long natureId);
    List<NatureModel> getAll();
    List<NatureModel> getFaunas();
    List<NatureModel> getFloras();
    List<NatureModel> convertToModels(List<Nature> natures);
}
