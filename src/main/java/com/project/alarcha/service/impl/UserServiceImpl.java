package com.project.alarcha.service.impl;

import com.project.alarcha.config.security.JwtUtils;
import com.project.alarcha.entities.*;
import com.project.alarcha.entities.Object;
import com.project.alarcha.enums.TimeType;
import com.project.alarcha.enums.UserRole;
import com.project.alarcha.enums.UserStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallOrderBasketModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderBasketModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderBasketModel;
import com.project.alarcha.models.SecurityModel.UserSecurityModel;
import com.project.alarcha.models.TokenModel.UserTokenModel;
import com.project.alarcha.models.UserModel.*;
import com.project.alarcha.repositories.UserRepository;
import com.project.alarcha.service.RefreshTokenService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final JwtUtils jwtUtils;

    public UserServiceImpl(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserTokenModel authorization(UserAuthModel userAuthModel) {
        String email = userAuthModel.getEmail();
        String password = userAuthModel.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserSecurityModel userDetails = (UserSecurityModel) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        UserTokenModel userTokenModel = initUserTokenModel(userDetails, jwtToken, refreshToken);
        return userTokenModel;
    }

    @Override
    public UserToSendModel createUser(UserRegistrationModel userRegistrationModel) {
        checkEmailForUnique(userRegistrationModel.getEmail());
        User user = initAndSaveUser(userRegistrationModel);
        return initUserToSendModel(user);
    }

    @Override
    public void createSuperAdmin() {
        User user = new User();
        user.setFirstName("Kuba");
        user.setLastName("Kushtarbekov");
        user.setEmail("kuba@gmail.com");
        user.setPassword(encoder.encode("kuba12345"));
        user.setPhone("+996 777777777");
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserRole(UserRole.SUPER_ADMIN);
        user.setIsDeleted(false);
        userRepository.save(user);
    }

    @Override
    public List<UserToSendModel> getAllUsersToSendModel() {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> !user.getIsDeleted())
                .collect(Collectors.toList());

        List<UserToSendModel> userToSendModels = new ArrayList<>();

        for (User user : users){
            userToSendModels.add(initUserToSendModel(user));
        }

        return userToSendModels;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public List<UserToSendModel> getAdmins() {
        List<UserToSendModel> userToSendModels = new ArrayList<>();

        userRepository.findAll()
                .stream()
                .filter(user ->  !user.getIsDeleted() && user.getUserRole() == UserRole.ADMIN)
                .collect(Collectors.toList())
                    .forEach(user -> userToSendModels.add(initUserToSendModel(user)));


        return userToSendModels;
    }

    @Override
    public UserToSendModel deleteUser(Long userId) {
        User user = getById(userId);

        if (user.getIsDeleted()){
            throw new ApiFailException("User is already deleted");
        }

        user.setIsDeleted(true);

        userRepository.save(user);

        return initUserToSendModel(user);
    }

    @Override
    public UserToSendModel updateUser(UserUpdateModel userUpdateModel) {
        User user = getById(userUpdateModel.getId());

        setValuesOnUpdate(user, userUpdateModel);

        userRepository.save(user);

        return initUserToSendModel(user);
    }

    @Override
    public UserOrdersModel getUserOrdersModel(Long id) {
        User user = getById(id);

        UserOrdersModel userOrdersModel = new UserOrdersModel();
        userOrdersModel.setId(id);

        List<RoomOrder> roomOrders = user.getRoomOrders();
        List<ObjectOrder> objectOrders = user.getObjectOrders();
        List<HotelHallOrder> hotelHallOrders = user.getHotelHallOrders();
        if (!roomOrders.isEmpty() || roomOrders != null){
            userOrdersModel.setRoomOrderBasketModels(convertToRoomOrderBasket(roomOrders));
        }
        if (!objectOrders.isEmpty() || objectOrders != null){
            userOrdersModel.setOrderBasketModels(convertToObjectOrderBasket(objectOrders));
        }
        if (!hotelHallOrders.isEmpty() || hotelHallOrders != null){
            userOrdersModel.setHotelHallOrderBasketModels(convertToHotelHallOrderBasket(hotelHallOrders));
        }

        return userOrdersModel;
    }

    private void checkEmailForUnique(String email) {
        User dataUserByEmail = getByEmail(email);
        if (dataUserByEmail != null)
            throw new ApiFailException(email + " is already existed");
    }

    private void setValuesOnUpdate(User user, UserUpdateModel userUpdateModel){
        String fName = userUpdateModel.getFirstName();
        String lName = userUpdateModel.getLastName();
        String email = userUpdateModel.getEmail();
        String password = userUpdateModel.getPassword();
        String phone = userUpdateModel.getPhone();

        if (fName != null){
            user.setFirstName(fName);
        }
        if (lName != null){
            user.setLastName(lName);
        }
        if (email != null){
            checkEmailForUnique(email);

            user.setEmail(email);
        }
        if (password != null){
            user.setPassword(encoder.encode(password));
        }
        if (phone != null){
            user.setPhone(phone);
        }

    }

    private User initAndSaveUser(UserRegistrationModel userRegistrationModel) {
        User user = new User();
        user.setIsDeleted(false);
        user.setFirstName(userRegistrationModel.getFirstName());
        user.setLastName(userRegistrationModel.getLastName());
        String encodingPassword = encoder.encode(userRegistrationModel.getPassword());
        user.setPassword(encodingPassword);
        user.setEmail(userRegistrationModel.getEmail());
        user.setPhone(userRegistrationModel.getPhone());
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserRole(UserRole.CLIENT);
        userRepository.save(user);

        return user;
    }

    private UserToSendModel initUserToSendModel(User user) {
        UserToSendModel userToSendModel = new UserToSendModel();
        userToSendModel.setId(user.getId());
        userToSendModel.setFirstName(user.getFirstName());
        userToSendModel.setLastName(user.getLastName());
        userToSendModel.setEmail(user.getEmail());
        userToSendModel.setPhone(user.getPhone());
        userToSendModel.setRole(user.getUserRole().name());
        return userToSendModel;
    }

    private UserTokenModel initUserTokenModel(UserSecurityModel userDetails, String accessToken, RefreshToken refreshToken) {
        UserTokenModel userTokenModel = new UserTokenModel();
        userTokenModel.setUserId(userDetails.getId());
        userTokenModel.setFirstName(userDetails.getFirstName());
        userTokenModel.setLastName(userDetails.getLastName());
        userTokenModel.setEmail(userDetails.getUsername());
        userTokenModel.setPhone(userTokenModel.getPhone());
        userTokenModel.setRole(userTokenModel.getRole());
        userTokenModel.setToken(accessToken);
        userTokenModel.setRefreshtoken(refreshToken.getToken());
        return userTokenModel;
    }


    private List<RoomOrderBasketModel> convertToRoomOrderBasket(List<RoomOrder> roomOrders){
        List<RoomOrderBasketModel> roomOrderBasketModels = new ArrayList<>();

        for (RoomOrder roomOrder : roomOrders){
            RoomOrderBasketModel roomOrderBasketModel = new RoomOrderBasketModel();
            roomOrderBasketModel.setId(roomOrder.getId());
            Room room = roomOrder.getRoom();
            RoomType roomType = room.getRoomType();
            roomOrderBasketModel.setRoomType(roomType.getType());
            roomOrderBasketModel.setRoomNumber(room.getRoomNumber());
            roomOrderBasketModel.setDays(roomOrder.getEndDate().getDate() - roomOrder.getStartDate().getDate());
            roomOrderBasketModel.setHotelName(roomType.getHotel().getHotelName());
            roomOrderBasketModel.setTotalPrice(roomOrder.getTotalPrice());
            roomOrderBasketModel.setPrice(roomType.getPrice());
            roomOrderBasketModel.setOrderStatus(roomOrder.getOrderStatus());
            roomOrderBasketModel.setCreatedDate(roomOrder.getCreateDate());

            roomOrderBasketModel.setImg(new String(roomType.getRoomTypeImages().get(0).getImg(), StandardCharsets.UTF_8));

            roomOrderBasketModels.add(roomOrderBasketModel);
        }

        return roomOrderBasketModels;
    }

    private List<ObjectOrderBasketModel> convertToObjectOrderBasket(List<ObjectOrder> objectOrders){
        List<ObjectOrderBasketModel> objectOrderBasketModels = new ArrayList<>();

        for (ObjectOrder objectOrder : objectOrders){
            ObjectOrderBasketModel orderBasketModel = new ObjectOrderBasketModel();
            orderBasketModel.setId(objectOrder.getId());
            Object object = objectOrder.getObject();
            ObjectType objectType = object.getObjectType();
            orderBasketModel.setObjectType(objectType.getName());
            orderBasketModel.setPrice(objectType.getPrice());
            orderBasketModel.setPriceForNextHours(objectType.getPricePerHour());
            orderBasketModel.setTotalPrice(objectOrder.getTotalPrice());
            orderBasketModel.setName(object.getName());
            if (objectType.getTimeType() == TimeType.DATE){
                orderBasketModel.setDays(objectOrder.getEndDate().getDate() - objectOrder.getStartDate().getDate());
            }
            else if (objectType.getTimeType() == TimeType.TIME){
                orderBasketModel.setHours(objectOrder.getEndTime().getHours() - objectOrder.getStartTime().getHours());
            }
            
            orderBasketModel.setOrderStatus(objectOrder.getOrderStatus());

            orderBasketModel.setCreatedDate(objectOrder.getCreateDate());
            
            orderBasketModel.setImg(new String(objectType.getObjectTypeImages().get(0).getImg(), StandardCharsets.UTF_8));

            objectOrderBasketModels.add(orderBasketModel);
        }
        
        return objectOrderBasketModels;
    }

    private List<HotelHallOrderBasketModel> convertToHotelHallOrderBasket(List<HotelHallOrder> hotelHallOrders){
        List<HotelHallOrderBasketModel> hotelHallOrderBasketModels = new ArrayList<>();

        for (HotelHallOrder hotelHallOrder : hotelHallOrders){
            HotelHallOrderBasketModel orderBasketModel = new HotelHallOrderBasketModel();
            orderBasketModel.setId(hotelHallOrder.getId());
            HotelHall hotelHall = hotelHallOrder.getHotelHall();
            orderBasketModel.setHotelName(hotelHall.getHotel().getHotelName());
            orderBasketModel.setPrice(hotelHall.getPrice());
            orderBasketModel.setPriceForNextHours(hotelHall.getPriceForNextHours());
            orderBasketModel.setTotalPrice(hotelHallOrder.getTotalPrice());
            orderBasketModel.setName(hotelHall.getName());
            orderBasketModel.setHours(hotelHallOrder.getEndTime().getHours() - hotelHallOrder.getStartTime().getHours());
            orderBasketModel.setOrderStatus(hotelHallOrder.getOrderStatus());
            orderBasketModel.setCreatedDate(hotelHallOrder.getCreateDate());

            orderBasketModel.setImg(new String(hotelHall.getHotelHallImages().get(0).getImg(), StandardCharsets.UTF_8));

            hotelHallOrderBasketModels.add(orderBasketModel);
        }

        return hotelHallOrderBasketModels;
    }

}