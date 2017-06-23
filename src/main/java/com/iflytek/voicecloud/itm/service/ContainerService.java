package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.config.Container;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/6
 */
public interface ContainerService {

    int addContainer(Container container);

    List<Container> getContainerList(Map<String, Object> condition);

    int countContainer(Map<String, Object> condition);

    Container getContainerById(String containerId);

    int deleteContainerById(String containerId);

}
