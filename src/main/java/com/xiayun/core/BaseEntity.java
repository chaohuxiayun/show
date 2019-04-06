package com.xiayun.core;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class BaseEntity implements Serializable {


    private static final long serialVersionUID = 6335056279344205457L;
    private Long id;
    private Long deleteTime = 0L;
    private Long addTime = 0L;
    private Long updateTime = 0L;

    /**
     * 这条数据是否已经被删除
     */
    private String isDelete = "0";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="DELETE_TIME")
    public Long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    @Column(name="ADD_TIME")
    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    @Column(name="UPDATE_TIME")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name="IS_DELETE",length = 2)
    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

}
