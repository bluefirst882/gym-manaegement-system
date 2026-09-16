package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.Announcement;
import org.limitless.backend.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    public AnnouncementService(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    public PageResult<Announcement> selectPage(String title, String status, String startTime, String endTime,
                                                int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Announcement> list = announcementMapper.selectPage(title, status, startTime, endTime);
        PageInfo<Announcement> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageNumber, pageSize, pageInfo.getTotal());
    }

    public Announcement selectById(Integer id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        return announcement;
    }

    public Integer create(Announcement announcement) {
        if (announcement.getTitle() == null || announcement.getTitle().trim().isEmpty()) {
            throw new BusinessException("公告标题不能为空");
        }
        announcement.setAnnouncementNo(generateAnnouncementNo());
        if (announcement.getStatus() == null) {
            announcement.setStatus("DRAFT");
        }
        if (announcement.getIsTop() == null) {
            announcement.setIsTop(0);
        }
        announcementMapper.insert(announcement);
        return announcement.getId();
    }

    public void update(Integer id, Announcement announcement) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        announcement.setId(id);
        announcementMapper.updateById(announcement);
    }

    public void delete(Integer id) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        announcementMapper.deleteById(id);
    }

    public void publish(Integer id) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        Announcement update = new Announcement();
        update.setId(id);
        update.setStatus("PUBLISHED");
        announcementMapper.updateById(update);
    }

    public void offline(Integer id) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        Announcement update = new Announcement();
        update.setId(id);
        update.setStatus("OFFLINE");
        announcementMapper.updateById(update);
    }

    public void toggleTop(Integer id) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        Announcement update = new Announcement();
        update.setId(id);
        update.setIsTop(existing.getIsTop() != null && existing.getIsTop() == 1 ? 0 : 1);
        announcementMapper.updateById(update);
    }

    private String generateAnnouncementNo() {
        return "N" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
