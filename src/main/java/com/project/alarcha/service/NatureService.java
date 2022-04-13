package com.project.alarcha.service;


import com.project.alarcha.entities.Nature;
import com.project.alarcha.models.NatureModel.NatureModel;



public interface NatureService {
    NatureModel createNature(NatureModel natureModel);
    Nature getById(Long natureId);
}
