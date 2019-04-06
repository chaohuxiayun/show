package com.xiayun.service.impl;

import com.xiayun.dao.PictureDao;
import com.xiayun.entity.PictureEntity;
import com.xiayun.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureDao pictureDao;

    @Override
    public PictureEntity findByNameIsLike(String name) {
        return pictureDao.findByNameIsLike("123");
    }

    @Override
    public PictureEntity save(PictureEntity entity) {
        return pictureDao.save(entity);
    }
}
