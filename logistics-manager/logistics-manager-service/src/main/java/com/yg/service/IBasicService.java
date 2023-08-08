package com.yg.service;

import com.yg.pojo.BasicData;

import java.util.List;

/**
 * 基础数据
 */
public interface IBasicService {

    /**
     * 根据条件查询基础数据
     * @param data
     * @return
     */
    List<BasicData> query(BasicData data) throws Exception;

    /**
     * 根据id查询基础数据
     * @param id
     * @return
     * @throws Exception
     */
    BasicData queryById(Integer id) throws Exception;

    /**
     * 添加基础数据
     * @param data
     * @return
     */
    Integer addBasicData(BasicData data) throws Exception;

    /**
     * 更新基础数据
     * @param data
     * @return
     */
    Integer updateBasicData(BasicData data) throws Exception;


    /**
     * 根据编号删除基础数据
     * @param id
     * @return
     */
    Integer deleteBasicData(Integer id) throws Exception;

    /**
     * 查询出所有大类基础数据
     * @return
     * @throws Exception
     */
    List<BasicData> queryAllParentData() throws Exception;

    /**
     * 根据父类基础数据名称，获取所有子类基础数据
     * @param parentName
     * @return
     */
    List<BasicData> queryByParentName(String parentName) throws Exception;
}
