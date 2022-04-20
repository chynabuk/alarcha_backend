package com.project.alarcha.service.impl;


import com.project.alarcha.entities.Nature;
import com.project.alarcha.models.NatureModel.NatureModel;
import com.project.alarcha.repositories.NatureRepository;
import com.project.alarcha.service.NatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NatureServiceImpl implements NatureService {
    @Autowired
    NatureRepository natureRepository;


    @Override
    public NatureModel createNature(NatureModel natureModel) {

        Nature nature = new Nature();

        natureRepository.save(initAndGet(nature, natureModel));

        return natureModel;
    }

    private Nature initAndGet(Nature nature, NatureModel natureModel){

        nature.setName(natureModel.getName());

        nature.setDescription(natureModel.getDescription());

        nature.setPhoto(natureModel.getPhoto());

        nature.setNatureType(natureModel.getNatureType());

        return nature;
    }

    @Override
    public Nature getById(Long natureId) {
        return natureRepository.getById(natureId);
    }
}
