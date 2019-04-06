package com.xiayun.service;

import com.xiayun.entity.PictureEntity;

public interface PictureService {
    PictureEntity findByNameIsLike(String name);

    PictureEntity save(PictureEntity entity);

}
