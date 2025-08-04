package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapperForAdmin {
    AdminUserInfoResponseDto entityToDto(User user);
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "cards", ignore = true)
    User dtoToEntity(UserInfoRequestDto dto);
}
