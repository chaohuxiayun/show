package com.xiayun.base;


import java.util.List;
import java.util.Map;

public interface BaseDao<T extends BaseEntity>  {

    T insert(T t);

    void delete(T t);

    void update(T t);

    T select(Long id);

    void delete(Long id);

    T selectOneObject(Map<String, Object> map);

    void trueDelete(T t);

    List<T> selectList(Map<String, Object> map);

    Page selectWithPage(Map<String, Object> map, Page page);

    List getProjections(Map<String, Object> map);

    <T> int batchSave(final T[] array);

    List<T> findByHql(String hql, Object... params);
}
