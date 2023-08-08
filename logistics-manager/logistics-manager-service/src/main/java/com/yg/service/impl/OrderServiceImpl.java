package com.yg.service.impl;

import com.yg.common.Constant;
import com.yg.dto.CustomerDto;
import com.yg.dto.OrderDto;
import com.yg.mapper.OrderDetailMapper;
import com.yg.mapper.OrderMapper;
import com.yg.pojo.BasicData;
import com.yg.pojo.OrderDetail;
import com.yg.pojo.User;
import com.yg.service.IBasicService;
import com.yg.service.ICustomerService;
import com.yg.service.IOrderService;
import com.yg.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper mapper;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBasicService basicService;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Override
    public void queryAddRequiredData(Model model) throws Exception{
        // 1.查询所有的客户
        List<CustomerDto> customers = customerService.query(null);
        // 2.查询出所有的业务员
        List<User> users = userService.queryUserByRoleName(Constant.ROLE_SALEMAN);
        // 3.查询基础数据
        // 3.1 付款方式
        List<BasicData> payments = basicService.queryByParentName(Constant.BASIC_PAYMENT_TYPE);
        // 3.2 货运方式
        List<BasicData> freights = basicService.queryByParentName(Constant.BASIC_FREIGHT_TYPE);
        // 3.3 取件方式
        List<BasicData> fetchs = basicService.queryByParentName(Constant.BASIC_FETCH_TYPE);
        // 3.4 国家/城市
        List<BasicData> countrys = basicService.queryByParentName(Constant.BASIC_COMMON_INTERVAL);
        // 3.5 单位
        List<BasicData> units = basicService.queryByParentName(Constant.BASIC_UNIT);

        model.addAttribute("customers",customers);
        model.addAttribute("users",users);
        model.addAttribute("payments",payments);
        model.addAttribute("freights",freights);
        model.addAttribute("fetchs",fetchs);
        model.addAttribute("countrys",countrys);
        model.addAttribute("units",units);
    }

    @Override
    public void saveOrder(OrderDto dto) {
        // 保存主表数据
        mapper.insert(dto);
        // 保存详情数据
        List<OrderDetail> orderDetails = dto.getOrderDetails();
        if(orderDetails != null && orderDetails.size() > 0){
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setOrderId(dto.getOrderId());
                detailMapper.insertSelective(orderDetail);
            }
        }
    }
}
