package com.iflytek.voicecloud.itm.dao.config;

import com.iflytek.voicecloud.itm.entity.config.Container;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/6
 */
public interface ContainerDao {

    /**
     * 添加contrainer
     * @param container 添加对象
     * @return  主键id
     */
    int addContainer(Container container);

    /**
     * 获取Container
     * @param condition    条件Map
     * @return  Container对象
     */
    List<Container> getContainerList(Map<String, Object> condition);

    /**
     * 通过主键获取container
     * @param containerId   主键id
     * @return      container对象
     */
    Container getContainerById(String containerId);

    /**
     * 通过主键删除container
     * @param containerId   主键id
     * @return      是否成功
     */
    int deleteContainerById(String containerId);

    /**
     * 根据条件获取Container数量
     * @param condition     条件Map
     * @return  查询条件下的Container数量
     */
    int countContainer(Map<String, Object> condition);

}
