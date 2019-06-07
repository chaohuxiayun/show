package com.xiayun.base;


import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Date 2019/4/23
 * @Created by xiayun
 */
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    protected BaseDao<T> baseDao;


    @Override
    public T insert(T t) {
        return baseDao.insert(t);
    }

    @Override
    public void delete(T t) {
        baseDao.delete(t);
    }

    @Override
    public void update(T t) {
        baseDao.update(t);
    }

    @Override
    public T select(Long id) {
        return baseDao.select(id);
    }

    @Override
    public List<T> selectList(Map<String, Object> map) {
        return baseDao.selectList(map);
    }

    @Override
    public T selectOneObject(Map<String, Object> map) {
        return baseDao.selectOneObject(map);
    }

    @Override
    public Page selectWithPage(Map<String, Object> map, Page page) {
        return baseDao.selectWithPage(map,page);
    }
}
