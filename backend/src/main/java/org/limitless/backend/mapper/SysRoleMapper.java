package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.limitless.backend.entity.SysRole;

@Mapper
public interface SysRoleMapper {
    SysRole selectById(Integer id);
}
