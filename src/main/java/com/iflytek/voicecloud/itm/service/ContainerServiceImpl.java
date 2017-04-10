package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.dao.ContainerDao;
import com.iflytek.voicecloud.itm.entity.Container;
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
}
