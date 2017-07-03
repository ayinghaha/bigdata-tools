package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.config.ContainerDao;
import com.iflytek.voicecloud.itm.entity.config.Container;
import com.iflytek.voicecloud.itm.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/6
 */
@Service
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    ContainerDao containerDao;

    public int addContainer(Container container) {
        return containerDao.addContainer(container);
    }

    /**
     * 获取Container
     * @param condition    条件Map
     * @return  Container对象
     */
    public List<Container> getContainerList(Map<String, Object> condition) {
        return containerDao.getContainerList(condition);
    }

    /**
     * 根据条件获取Container数量
     * @param condition     条件Map
     * @return  查询条件下的Container数量
     */
    public int countContainer(Map<String, Object> condition) {
        return containerDao.countContainer(condition);
    }

    /**
     * 通过主键获取container
     * @param containerId   主键id
     * @return      container对象
     */
    public Container getContainerById(String containerId) {
        return containerDao.getContainerById(containerId);
    }

    /**
     * 更新container
     * @param container     更新Container对象
     * @return      是否更新成功
     */
    public int updateContainer(Container container) {
        return containerDao.updateContainer(container);
    }

    /**
     * 通过主键删除container
     * @param containerId   主键id
     * @return      是否成功
     */
    public int deleteContainerById(String containerId) {
        return containerDao.deleteContainerById(containerId);
    }
}
