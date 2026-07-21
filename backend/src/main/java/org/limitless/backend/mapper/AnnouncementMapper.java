package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.Announcement;
import java.util.List;

@Mapper
public interface AnnouncementMapper {
    Announcement selectById(Integer id);
    List<Announcement> selectPage(@Param("title") String title,
                                  @Param("status") String status,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);
    long countPage(@Param("title") String title,
                   @Param("status") String status,
                   @Param("startTime") String startTime,
                   @Param("endTime") String endTime);
    int insert(Announcement announcement);
    int updateById(Announcement announcement);
    int deleteById(Integer id);
}
