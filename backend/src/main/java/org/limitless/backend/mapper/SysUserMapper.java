package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.SysUser;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectById(Integer id);

    SysUser selectByUsername(String username);

    List<SysUser> selectPage(@Param("username") String username,
                             @Param("realName") String realName,
                             @Param("phone") String phone,
                             @Param("roleId") Integer roleId);

    long countPage(@Param("username") String username,
                   @Param("realName") String realName,
                   @Param("phone") String phone,
                   @Param("roleId") Integer roleId);

    int insert(SysUser user);

    int updateById(SysUser user);

    int deleteById(Integer id);

    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    int updateLastLogin(@Param("id") Integer id);
}
