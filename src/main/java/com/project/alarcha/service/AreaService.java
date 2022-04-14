package com.project.alarcha.service;

import com.project.alarcha.entities.Area;
import com.project.alarcha.models.AreaModel.AreaModel;

import java.util.List;

public interface AreaService{
    AreaModel createArea(AreaModel areaCreateModel);
    List<AreaModel> getAll();
    Area getById(Long areaId);
    AreaModel updateArea(AreaModel areaModel);
    AreaModel deleteArea(Long areaId);
}
