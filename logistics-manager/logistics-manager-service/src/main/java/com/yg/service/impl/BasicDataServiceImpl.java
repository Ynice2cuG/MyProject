package com.yg.service.impl;

import com.yg.mapper.BasicDataMapper;
import com.yg.pojo.BasicData;
import com.yg.pojo.BasicDataExample;
import com.yg.service.IBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicDataServiceImpl implements IBasicService {

    @Autowired
    private BasicDataMapper mapper;

    @Override
    public List<BasicData> query(BasicData data) throws Exception {
        BasicDataExample example = new BasicDataExample();
        BasicDataExample.Criteria criteria = example.createCriteria();
        if(data!=null){
            if(data.getBaseName() != null && !"".equals(data.getBaseName())){
                criteria.andBaseNameEqualTo(data.getBaseName());
            }
            if (data.getParentId() != null && !"".equals(data.getParentId())){
                criteria.andParentIdEqualTo(data.getParentId());
            }
        }
        criteria.andUIsNull();
        return mapper.selectByExample(example);
    }

    @Override
    public BasicData queryById(Integer id) throws Exception {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer addBasicData(BasicData data) throws Exception {
        if (data.getParentId() != null && data.getParentId() == -1){
            // parentId为null的为大类
            data.setParentId(null);
        }
        return mapper.insertSelective(data);
    }

    @Override
    public Integer updateBasicData(BasicData data) throws Exception {
        if (data.getParentId() != null && data.getParentId() == -1){
            // parentId为null的为大类
            data.setParentId(null);
        }
        return mapper.updateByPrimaryKey(data);
    }

    @Override
    public Integer deleteBasicData(Integer id) throws Exception {
        BasicData basicData = new BasicData();
        basicData.setBaseId(id);
        basicData.setU("1");
        return mapper.updateByPrimaryKeySelective(basicData);
    }

    @Override
    public List<BasicData> queryAllParentData() throws Exception {
        BasicDataExample example = new BasicDataExample();
        example.createCriteria().andParentIdIsNull();
        return mapper.selectByExample(example);
    }

    @Override
    public List<BasicData> queryByParentName(String parentName) throws Exception {
        BasicData data = new BasicData();
        data.setBaseName(parentName);
        List<BasicData> datas = this.query(data);
        if (datas != null && datas.size() > 0){
            BasicData parentData = datas.get(0);
            BasicData data1 = new BasicData();
            data1.setParentId(parentData.getBaseId());
            List<BasicData> list = this.query(data1);
            return list;
        }
        return null;
    }
}
