package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.ActivityRegistration;
import java.util.List;

@Mapper
public interface ActivityRegistrationMapper {
    ActivityRegistration selectById(Long id);
    List<ActivityRegistration> selectPage(@Param("activityId") Integer activityId,
                                          @Param("activityTitle") String activityTitle,
                                          @Param("userId") Integer userId,
                                          @Param("auditStatus") String auditStatus,
                                          @Param("regStartTime") String regStartTime,
                                          @Param("regEndTime") String regEndTime);
    int insert(ActivityRegistration registration);
    int updateById(ActivityRegistration registration);
    int deleteById(Long id);
}
