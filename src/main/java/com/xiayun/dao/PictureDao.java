package com.xiayun.dao;

import com.xiayun.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PictureDao extends JpaRepository<PictureEntity,Long> {
    PictureEntity findByNameIsLike(String name);
}
