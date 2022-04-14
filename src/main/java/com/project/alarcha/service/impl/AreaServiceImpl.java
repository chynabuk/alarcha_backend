package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Area;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.UserRole;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.repositories.AreaRepository;
import com.project.alarcha.service.AreaService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public AreaModel updateArea(AreaModel areaModel) {
        Area area = getById(areaModel.getId());
        if (area != null){
            if (!area.getIsDeleted()){
                setValuesOnUpdateArea(area, areaModel);
            }
        }
        areaRepository.save(area);

        return areaModel;
    }

    @Override
    public AreaModel deleteArea(Long areaId) {
        Area area = getById(areaId);

        if (area != null){
            if (area.getIsDeleted()){
                throw new ApiFailException("Area is already deleted");
            }
            area.setIsDeleted(true);
        }
        areaRepository.save(area);

        return toModel(area);
    }

    @Override
    public Area getById(Long areaId) {
        return areaRepository.getById(areaId);
    }

    @Override
    public List<AreaModel> getAll() {
        List<AreaModel> areaModels = new ArrayList<>();

        for (Area area : areaRepository.findAll()){
            if (!area.getIsDeleted()){
                areaModels.add(toModel(area));
            }
        }
        return areaModels;
    }

    private Area initAndGet(Area area, AreaModel areaCreateModel){
        area.setAreaName(areaCreateModel.getAreaName());
//        area.setObjects(areaCreateModel.getObjects());
//        area.setHotels(areaCreateModel.getHotels());
        area.setIsDeleted(false);
        User user = userService.getByEmail(areaCreateModel.getEmail());

        area.setUser(user);

        return area;
    }

    private void setValuesOnUpdateArea(Area area, AreaModel areaModel){
        String areaName = areaModel.getAreaName();
        User user = userService.getByEmail(areaModel.getEmail());
//        List<Hotel> hotels = areaModel.getHotels();
//        List<Object> objects = areaModel.getObjects();

        if (areaName != null){
            area.setAreaName(areaName);
        }

        if (user != null){
            if (!user.getIsDeleted() && user.getUserRole() == UserRole.ADMIN){
                area.setUser(user);
            }
        }
    }

    private AreaModel toModel(Area area){
        AreaModel areaModel = new AreaModel();
        areaModel.setId(area.getId());
        areaModel.setAreaName(area.getAreaName());
        areaModel.setEmail(area.getUser().getEmail());

        return areaModel;
    }
}