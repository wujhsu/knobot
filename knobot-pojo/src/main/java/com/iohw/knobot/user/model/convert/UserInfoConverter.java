package com.iohw.knobot.user.model.convert;

import com.iohw.knobot.user.model.UserInfoDO;
import com.iohw.knobot.user.model.dto.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author: iohw
 * @date: 2025/5/5 10:26
 * @description:
 */
@Mapper
public interface UserInfoConverter {
    UserInfoConverter INSTANCE = Mappers.getMapper(UserInfoConverter.class);

    UserInfoDto toDto(UserInfoDO userInfoDO);
}
