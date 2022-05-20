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
import java.util.Date;
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
            throw new ApiFailException("Арея не найдена.");
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
        area.setDeletedDate(new Date());

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
                .orElseThrow(() -> new ApiFailException("Арея не найдена"));
        if (area.getIsDeleted()){
            throw new ApiFailException("Арея не найдена или удалена");
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
        Date deletedDate = new Date();
        if (hotels != null){
            if (!hotels.isEmpty()){
                for (Hotel hotel : hotels){
                    hotel.setIsDeleted(true);
                    hotel.setDeletedDate(deletedDate);

                    List<RoomType> roomTypes = hotel.getRoomTypes();
                    if (roomTypes != null){
                        if (!roomTypes.isEmpty()){
                            for (RoomType roomType : roomTypes){
                                roomType.setIsDeleted(true);
                                roomType.setDeletedDate(deletedDate);

                                List<Room> rooms = roomType.getRooms();
                                if (rooms != null){
                                    if (!rooms.isEmpty()){
                                        for (Room room : rooms){
                                            room.setIsDeleted(true);
                                            room.setDeletedDate(deletedDate);

                                            List<RoomOrder> roomOrders = room.getRoomOrders();
                                            if (roomOrders != null){
                                                if (!roomOrders.isEmpty()){
                                                    for (RoomOrder roomOrder : roomOrders){
                                                        roomOrder.setIsDeleted(true);
                                                        roomOrder.setDeletedDate(deletedDate);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    List<HotelHall> hotelHalls = hotel.getHotelHalls();
                    if (hotelHalls != null){
                        if (!hotelHalls.isEmpty()){
                            for (HotelHall hotelHall : hotelHalls){
                                hotelHall.setIsDeleted(true);
                                hotelHall.setDeletedDate(deletedDate);
                                List<HotelHallOrder> hotelHallOrders = hotelHall.getHotelHallOrders();
                                if (hotelHallOrders != null){
                                    if (!hotelHallOrders.isEmpty()){
                                        for (HotelHallOrder hotelHallOrder : hotelHallOrders){
                                            hotelHallOrder.setIsDeleted(true);
                                            hotelHallOrder.setDeletedDate(deletedDate);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void deleteObjectTypeDepended(List<ObjectType> objectTypes){
        Date deletedDate = new Date();
        if (objectTypes != null){
            for (ObjectType objectType : objectTypes){
                objectType.setIsDeleted(true);
                objectType.setDeletedDate(deletedDate);
                List<Object> objects = objectType.getObjects();
                if (objects != null){
                    if (!objects.isEmpty()){
                        for (Object object : objects){
                            object.setIsDeleted(true);
                            object.setDeletedDate(deletedDate);
                            for (ObjectOrder objectOrder : object.getObjectOrders()){
                                objectOrder.setIsDeleted(true);
                                objectOrder.setDeletedDate(deletedDate);
                            }
                        }
                    }
                }

                List<MenuSection> menuSections = objectType.getMenuSections();
                if (menuSections != null){
                    if (!menuSections.isEmpty()){
                        for (MenuSection menuSection : menuSections){
                            menuSection.setIsDeleted(true);
                            menuSection.setDeletedDate(deletedDate);
                            for (Menu menu : menuSection.getMenus()){
                                menu.setIsDeleted(true);
                                menu.setDeletedDate(deletedDate);
                            }
                        }
                    }
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

            List<Hotel> hotels = area.getHotels();
            if (hotels != null){
                if (!hotels.isEmpty()){
                    areaModel.setHotelModels(hotelService.convertToModel(area.getHotels()));
                }
            }

            List<ObjectType> objectTypes = area.getObjectTypes();
            if (objectTypes != null){
                if (!objectTypes.isEmpty()){
                    areaModel.setObjectTypeModels(objectTypeService.convertToModels(objectTypes));
                }
            }
        }

        return areaModel;
    }
}