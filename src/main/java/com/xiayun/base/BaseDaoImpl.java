package com.xiayun.base;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiayun
 */
public class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

    protected Class targetClass;

    protected Session session;

    protected int BATCH_MAX_ROW = 100;

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * 是否使用二级缓存--目前好像没有作用
     */
    protected boolean isSetCache = false;


    protected void setSession() {
        // 获取当前线程中的session
        session = sessionFactory.getCurrentSession();
    }

    @Override
    public T insert(T t) {
        setSession();
        Long id = (Long) session.save(t);
        t.setId(id);
        return t;
    }

    @Override
    public void delete(T t) {
        setSession();
        session.delete(t);
    }

    @Override
    public void update(T t) {
        setSession();
        session.merge(t);
    }

    /**
     * 按照id查询
     *
     * @param id
     * @return
     */
    @Override
    public T select(Long id) {
        setSession();
        T t = (T) session.get(targetClass, id);
        return t;
    }

    @Override
    public void delete(Long id) {
        setSession();
        T t = select(id);
        delete(t);
    }

    @Override
    public void trueDelete(T t) {
        setSession();
        session.delete(t);
    }

    @Override
    public <T> int batchSave(final T[] array) {
        Transaction tx = session.beginTransaction();
        for (int i = 0; i < array.length; i++) {
            session.save(array[i]);
            if (i % BATCH_MAX_ROW == 0) {
                session.flush();
                session.clear();
            }
        }
        session.flush();
        session.clear();
        tx.commit();
        return array.length;
    }


    @Override
    public List<T> selectList(Map<String, Object> map) {
        Criteria c = qbcSelect(map);

        c.setCacheable(isSetCache);
        List<T> list = c.list();
        return list;
    }

    /**
     * 查询单条记录
     *
     * @param map
     * @return
     */
    @Override
    public T selectOneObject(Map<String, Object> map) {
        Criteria c = qbcSelect(map);

        c.setCacheable(isSetCache);
        c.setFirstResult(0);
        c.setMaxResults(1);
        List<T> list = c.list();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param map
     * @param page
     * @return
     */
    @Override
    public Page selectWithPage(Map<String, Object> map, Page page) {

        Criteria c = qbcSelect(map);
        c.setCacheable(isSetCache);
        c.setFirstResult(0);
        c.setMaxResults(1);
        if (page == null) {
            page = new Page();
        }
        // 记录数
        Long total = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
        page.setTotal(total);

        // 将原来设置Projection(投影,投影图)的清空
        c.setProjection(null);

        c.setFirstResult((int) ((page.getCurrentPage() - 1) * page.getPageSize()));
        c.setMaxResults(page.getPageSize());
        // 只返回当前我要查询的对象，不需要返回有别名的其他对象
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List l = c.list();
        page.setPageList(l);

        Long size = (page.getTotal() / page.getPageSize());
        Long mod = (page.getTotal() % page.getPageSize());
        if (mod > 0) {
            size += 1;
        }
        page.setTotalPage(size);
        return page;
    }

    /**
     * 统计
     *
     * @param map
     */
    @Override
    public List getProjections(Map<String, Object> map) {
        Criteria c = qbcSelect(map);
        ProjectionList projList = Projections.projectionList();
        for (Map.Entry<String, Object> enty : map.entrySet()) {
            if (enty.getKey().contains(".")) {
                String[] keys = enty.getKey().split("\\.");
                if (keys.length < 2) {
                    continue;
                }
                Projection projection = formatProjections(keys[0], keys[1]);
                if (projection != null) {
                    projList.add(projection);
                }
            }
        }
        c.setProjection(projList);
        List cList = c.list();
        return cList;
    }

    /**
     * 返回所有的查询结果，
     *
     * @param hql    : hql语句
     * @param params : 参数列表
     * @return : List<T>
     */
    @Override
    public List<T> findByHql(String hql, Object... params) {
        List<T> list;
        setSession();
        org.hibernate.query.Query query = session.createQuery(hql);
        for (int i = 0; params != null && i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        list = query.list();
        return list;
    }

    /**
     * 将map中的键值对解析成查询条件
     *
     * @param map
     * @return
     */
    protected Criteria qbcSelect(Map<String, Object> map) {
        setSession();
        Criteria c = session.createCriteria(targetClass);
        Map<String, Criteria> cMap = new HashMap<>(8);
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (e.getValue() != null && !"".equals(e.getValue())) {
                c = formatSQL(e.getKey(), e.getValue(), c, null, false, cMap);
            }
        }
        return c;
    }

    /**
     * @param key         : 要查询的字段
     * @param value       : 值
     * @param c           : Criteria
     * @param disjunction : 或查询Disjunction
     * @param flag        : 是否是或查询
     * @param cMap        : 别名的map，避免重复创建同一对象的别名
     * @return : 返回组合好的Criteria
     */
    private Criteria formatSQL(String key, Object value, Criteria c, Disjunction disjunction, boolean flag, Map<String, Criteria> cMap) {
        // key 示例 eq.goodsDepot.depot.name/eq.name
        String[] keys = key.split("\\.");
        if (keys.length < 2) {
            return c;
        }
        Criterion result = null;
        if (keys.length > 2) {
            // 存在关联查询，为关联表建一个别名
            String j = key.substring(key.indexOf(".") + 1);
            Criteria ali = c;
            String aKey = null;

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < keys.length - 1; i++) {
                aKey = keys[i];
                if (i == 1) {
                    //str = aKey;
                    sb.append(aKey);
                } else {
                    //str = str + "." + aKey;
                    sb.append(".");
                    sb.append(aKey);
                }
                String str = sb.toString();
                if (cMap.get(str) != null) {
                    // 别名已经被创建--
                    ali = cMap.get(str);
                } else {
                    ali = ali.createAlias(str, aKey);
                    cMap.put(str, ali);
                }
            }
            result = format(keys[0], aKey + "." + keys[keys.length - 1], value);
            if (flag) {
                // 或查询
                disjunction.add(result);
            } else {
                ali.add(result);
            }

        } else {
            result = format(keys[0], keys[keys.length - 1], value);
        }


        Order o = null;
        if (result == null && SQLConstants.OR.equals(keys[0])) {
            /*
             * 逻辑或 操作--
             */
            Map<String, Object> m = (Map<String, Object>) value;
            Disjunction dis = Restrictions.disjunction();
            for (Map.Entry<String, Object> e : m.entrySet()) {

                if (e.getValue() != null && !"".equals(e.getValue())) {
                    formatSQL(e.getKey(), e.getValue(), c, dis, true, cMap);
                }
            }
            c.add(dis);
        } else {
            o = formatOrder(keys[0], keys[keys.length - 1], value);
        }

        if (keys.length <= 2) {
            // <=2  表示直接查询本表中字段 >2 表示查询关联表中字段
            if (result != null) {
                if (flag) {
                    // 或查询
                    disjunction.add(result);
                } else {
                    c.add(result);
                }
            } else if (o != null) {
                c.addOrder(o);
            }
        }
        return c;
    }

    /**
     * 排序
     *
     * @param key0
     * @param key1
     * @param value
     * @return
     */
    protected Order formatOrder(String key0, String key1, Object value) {
        if (SQLConstants.DESC.equals(key0)) {
            return Order.desc(key1);
        } else if (SQLConstants.ASC.equals(key0)) {
            return Order.asc(key1);
        }
        return null;
    }

    /**
     * 一些统计函数
     *
     * @param key0
     * @param key1
     * @return
     */
    protected Projection formatProjections(String key0, String key1) {
        if (SQLConstants.COUNT.equals(key0)) {
            // 去重计数
            return Projections.countDistinct(key1);
        } else if (SQLConstants.MAX.equals(key0)) {
            // 求最大值
            return Projections.max(key0);
        }
        return null;
    }


    /**
     * 返回查询条件
     *
     * @param key0
     * @param key1
     * @param value
     * @return
     */
    private Criterion format(String key0, String key1, Object value) {

        switch (key0) {
            case SQLConstants.EQ:
                return Restrictions.eq(key1, value);
            case SQLConstants.ILIKE:
                return Restrictions.ilike(key1, value.toString(), MatchMode.ANYWHERE);
            case SQLConstants.NE:
                return Restrictions.ne(key1, value);
            case SQLConstants.ISNULL:
                return Restrictions.isNull(key1);
            case SQLConstants.ISNOTNULL:
                return Restrictions.isNotNull(key1);
            case SQLConstants.GE:
                return Restrictions.ge(key1, value);
            case SQLConstants.LE:
                return Restrictions.le(key1, value);
            case SQLConstants.BETWEEN:
                if (value.getClass().isArray()) {
                    Object[] result = (Object[]) value;
                    if (result == null || result.length < 2) {
                        return Restrictions.between(key1, result[0], result[1]);
                    }
                }
            case SQLConstants.IN:
                if (value instanceof List) {
                    Object[] result = ((List) value).toArray();
                    return Restrictions.in(key1, result);
                } else if (value.getClass().isArray()) {
                    return Restrictions.not(Restrictions.in(key1, value));
                }
            case SQLConstants.NOTIN:
                if (value instanceof List) {
                    Object[] result = ((List) value).toArray();
                    return Restrictions.not(Restrictions.in(key1, result));
                } else if (value.getClass().isArray()) {
                    return Restrictions.not(Restrictions.in(key1, value));
                }
            default:
                return null;
        }

        /*if (SQLConstants.EQ.equals(key0)) {
            return Restrictions.eq(key1, value);
        } else if (SQLConstants.ILIKE.equals(key0)) {
            return Restrictions.ilike(key1, value.toString(), MatchMode.ANYWHERE);
        } else if (SQLConstants.NE.equals(key0)) {
            return Restrictions.ne(key1, value);
        } else if (SQLConstants.ISNULL.equals(key0)) {
            return Restrictions.isNull(key1);
        } else if (SQLConstants.ISNOTNULL.equals(key0)) {
            return Restrictions.isNotNull(key1);
        } else if (SQLConstants.GE.equals(key0)) {
            return Restrictions.ge(key1, value);
        } else if (SQLConstants.LE.equals(key0)) {
            return Restrictions.le(key1, value);
        } else if (SQLConstants.BETWEEN.equals(key0)) {
            if (value.getClass().isArray()) {
                Object[] result = (Object[]) value;
                if (result == null || result.length < 2) {
                    return Restrictions.between(key1, result[0], result[1]);
                }
            }
        } else if (SQLConstants.IN.equals(key0)) {
            if (value instanceof List) {
                Object[] result = ((List) value).toArray();
                return Restrictions.in(key1, result);
            } else if (value.getClass().isArray()) {
                return Restrictions.not(Restrictions.in(key1, value));
            }
            return Restrictions.in(key1, value);
        } else if (SQLConstants.NOTIN.equals(key0)) {
            if (value instanceof List) {
                Object[] result = ((List) value).toArray();
                return Restrictions.not(Restrictions.in(key1, result));
            } else if (value.getClass().isArray()) {
                return Restrictions.not(Restrictions.in(key1, value));
            }
        }
        return null;*/
    }

//    public void setResultList(List<String> selectList){
//        ProjectionList list = Projections.projectionList();
//        for (String s : selectList) {
//            if(s.trim().startsWith("self.")){
//                list.add(Projections.property(s).as(s.substring(s.indexOf(".") + 1)));
//            } else {
//                list.add(Projections.property(s).as(s.replace(".","_")));
//            }
//        }
//        this.resultList = list;
//    }
}
