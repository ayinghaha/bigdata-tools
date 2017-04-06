package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Container;

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

}
