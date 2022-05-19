package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.entities.Object;
import com.project.alarcha.enums.UserRole;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.repositories.AreaRepository;
import com.project.alarcha.service.*;
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

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Override
    public AreaModel createArea(AreaModel areaCreateModel) {
        Area area = new Area();

        areaRepository.save(initAndGet(area, areaCreateModel));

        return areaCreateModel;
    }

    @Override
    public AreaModel updateArea(AreaModel areaModel) {
        Area area = getAreaById(areaModel.getId());
        if (area == null){
            throw new ApiFailException("area not found");
        }

        if (!area.getIsDeleted()){
            setValuesOnUpdateArea(area, areaModel);
            areaRepository.save(area);
        }

        return areaModel;
    }

    @Override
    public AreaModel deleteArea(Long areaId) {
        Area area = getAreaById(areaId);

        deleteHotelDepended(area.getHotels());

        deleteObjectTypeDepended(area.getObjectTypes());

        area.setIsDeleted(true);

        areaRepository.save(area);

        return toModel(area);
    }

    @Override
    public AreaModel getById(Long areaId) {
        Area area = getAreaById(areaId);
        if (area.getIsDeleted()){
            area = null;
        }

        return toModel(area);
    }

    @Override
    public Area getAreaById(Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ApiFailException("Area is not found"));
        if (area.getIsDeleted()){
            throw new ApiFailException("Area is deleted or not found");
        }
        return area;
    }

    @Override
    public List<AreaModel> getAll() {
        List<AreaModel> areaModels = new ArrayList<>();

        for (Area area : areaRepository.getAll()){
            areaModels.add(toModel(area));
        }
        return areaModels;
    }

    @Override
    public List<AreaModel> getForSelectBox() {
        List<AreaModel> areaModels = new ArrayList<>();

        for (Area area : areaRepository.getAll()){
            AreaModel areaModel = new AreaModel();
            areaModel.setId(area.getId());
            areaModel.setAreaName(area.getAreaName());

            areaModels.add(areaModel);
        }
        return areaModels;
    }

    private void deleteHotelDepended(List<Hotel> hotels){
        for (Hotel hotel : hotels){
            for (RoomType roomType : hotel.getRoomTypes()){
                for (Room room : roomType.getRooms()){
                    room.setIsDeleted(true);

                    for (RoomOrder roomOrder : room.getRoomOrders()){
                        roomOrder.setIsDeleted(true);
                    }
                }
                roomType.setIsDeleted(true);
            }

            for (HotelHall hotelHall : hotel.getHotelHalls()){
                hotelHall.setIsDeleted(true);

                for (HotelHallOrder hotelHallOrder : hotelHall.getHotelHallOrders()){
                    hotelHallOrder.setIsDeleted(true);
                }
            }

            hotel.setIsDeleted(true);
        }
    }

    private void deleteObjectTypeDepended(List<ObjectType> objectTypes){
        for (ObjectType objectType : objectTypes){
            objectType.setIsDeleted(true);
            for (Object object : objectType.getObjects()){
                object.setIsDeleted(true);
                for (ObjectOrder objectOrder : object.getObjectOrders()){
                    objectOrder.setIsDeleted(true);
                }
            }

            for (MenuSection menuSection : objectType.getMenuSections()){
                menuSection.setIsDeleted(true);
                for (Menu menu : menuSection.getMenus()){
                    menu.setIsDeleted(true);
                }
            }
        }
    }

    private Area initAndGet(Area area, AreaModel areaCreateModel){
        area.setAreaName(areaCreateModel.getAreaName());
        area.setObjectTypes(null);
        area.setHotels(null);
        area.setUser(userService.getByEmail(areaCreateModel.getEmail()));
        area.setIsDeleted(false);

        return area;
    }

    private void setValuesOnUpdateArea(Area area, AreaModel areaModel){
        String areaName = areaModel.getAreaName();

        if (areaName != null){
            area.setAreaName(areaName);
        }

        if (areaModel.getEmail() != null){
            User user = userService.getByEmail(areaModel.getEmail());
            if (user != null){
                if (!user.getIsDeleted() && user.getUserRole() == UserRole.ADMIN){
                    area.setUser(user);
                }
            }
        }


    }

    private AreaModel toModel(Area area){
        AreaModel areaModel = new AreaModel();
        if (area != null){
            areaModel.setId(area.getId());
            areaModel.setAreaName(area.getAreaName());
            areaModel.setEmail(area.getUser().getEmail());
            areaModel.setHotelModels(hotelService.convertToModel(area.getHotels()));
            areaModel.setObjectTypeModels(objectTypeService.convertToModels(area.getObjectTypes()));
        }

        return areaModel;
    }
}