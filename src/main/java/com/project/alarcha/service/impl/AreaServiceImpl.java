package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Area;
import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.repositories.AreaRepository;
import com.project.alarcha.service.AreaService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserService userService;

    @Override
    public AreaModel createArea(AreaModel areaCreateModel) {
        Area area = new Area();

        areaRepository.save(initAndGet(area, areaCreateModel));

        return areaCreateModel;
    }

    @Override
    public Area getById(Long areaId) {
        return areaRepository.getById(areaId);
    }

    public Area initAndGet(Area area, AreaModel areaCreateModel){
        area.setAreaName(areaCreateModel.getAreaName());
        area.setObjectTypes(areaCreateModel.getObjectTypes());
        area.setHotels(areaCreateModel.getHotels());
        area.setUser(userService.getByEmail(areaCreateModel.getEmail()));

        return area;
    }
}