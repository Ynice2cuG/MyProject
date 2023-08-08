package com.yg.controller;

import com.yg.dto.OrderDto;
import com.yg.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService service;

    @RequestMapping("/orderDispatch")
    public String orderDispatch(Model model) throws Exception {
        service.queryAddRequiredData(model);
        return "order/addOrder";
    }

    @RequestMapping("/saveOrder")
    @ResponseBody
    public String saveOrder(OrderDto dto){
        System.out.println("检查-----"+dto);
        service.saveOrder(dto);
        return "!!!!!!Add Successfully!!!!!!";
    }
}
