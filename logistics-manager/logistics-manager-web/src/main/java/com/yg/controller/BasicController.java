package com.yg.controller;

import com.yg.pojo.BasicData;
import com.yg.service.IBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 基础数据查询
 */
@Controller
@RequestMapping("/basic")
public class BasicController {

    @Autowired
    private IBasicService service;

    /**
     * 跳转并展示所有基础数据
     * @param data
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/query")
    public String query(BasicData data, Model model) throws Exception{
        List<BasicData> list = service.query(data);
        model.addAttribute("list",list);
        return "basic/basic";
    }

    /**
     * 跳转到表单页面，进行添加或者更新操作
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/baseDispatch")
    public String baseDispatch(Integer id, Model model) throws Exception{
        if(id != null){ // 则是更新操作
            // 根据编号查询出数据信息
            BasicData basicData = service.queryById(id);
            model.addAttribute("basicData",basicData);
        }
        //查询出所有的大类数据
        List<BasicData> parents = service.queryAllParentData();
        model.addAttribute("parents",parents);
        return "basic/updateBasic";
    }

    @RequestMapping("/saveOrUpdate")
    public String saveOrUpdate(BasicData data) throws Exception{
        if (data.getBaseId() != null && data.getBaseId() > 0){
            service.updateBasicData(data);
        }else {
            service.addBasicData(data);
        }
        return "redirect:/basic/query";
    }

    @RequestMapping("/deleteById")
    public String deleteById(Integer id) throws Exception{
        service.deleteBasicData(id);
        return "redirect:/basic/query";
    }
}
