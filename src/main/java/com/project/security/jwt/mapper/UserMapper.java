package com.project.security.jwt.mapper;


import com.project.security.jwt.data.entity.CustomUser;
import com.project.security.jwt.model.UserRegisterModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    CustomUser map (UserRegisterModel model);
    UserRegisterModel map (CustomUser user);

}
