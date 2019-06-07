package com.xiayun.base;

import java.util.List;
import java.util.Map;

public interface BaseService<T extends BaseEntity> {

    T insert(T t);

    void delete(T t);

    void update(T t);

    T select(Long id);

    List<T> selectList(Map<String, Object> map);

    T selectOneObject(Map<String, Object> map);

    Page selectWithPage(Map<String, Object> map, Page page);

}
