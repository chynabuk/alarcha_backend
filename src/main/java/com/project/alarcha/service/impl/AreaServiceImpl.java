package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Area;
import com.project.alarcha.models.AreaModel.AreaCreateModel;
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
    public AreaCreateModel createArea(AreaCreateModel areaCreateModel) {
        Area area = new Area();

        areaRepository.save(initAndGet(area, areaCreateModel));

        return areaCreateModel;
    }

    public Area initAndGet(Area area, AreaCreateModel areaCreateModel){
        area.setAreaName(areaCreateModel.getAreaName());
        area.setObjects(areaCreateModel.getObjects());
        area.setHotels(areaCreateModel.getHotels());
        area.setUser(userService.getByEmail(areaCreateModel.getEmail()));

        return area;
    }
}