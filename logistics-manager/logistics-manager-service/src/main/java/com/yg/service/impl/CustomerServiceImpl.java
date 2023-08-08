package com.yg.service.impl;

import com.yg.common.Constant;
import com.yg.dto.CustomerDto;
import com.yg.mapper.CustomerMapper;
import com.yg.mapper.OrderMapper;
import com.yg.pojo.*;
import com.yg.service.IBasicService;
import com.yg.service.ICustomerService;
import com.yg.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerMapper mapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBasicService basicService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void getUpdateInfo(Integer id, Model model) throws Exception {
        if(id != null && id > 0){
            // 表示是更新数据 根据当前的客户编号查询出对应的客户信息
            Customer customer = mapper.selectByPrimaryKey(id);
            model.addAttribute("customer",customer);

        }
        // 查询角色是业务员 的所有用户信息
        List<User> users = userService.queryUserByRoleName(Constant.ROLE_SALEMAN);
        // 查询属于常用区间 的所有基础数据
        List<BasicData> intervals = basicService.queryByParentName(Constant.BASIC_COMMON_INTERVAL);

        model.addAttribute("users",users);
        model.addAttribute("intervals",intervals);
    }

    @Override
    public Integer save(Customer customer) {
        return mapper.insertSelective(customer);
    }

    @Override
    public List<CustomerDto> query(Customer customer) throws Exception {
        /*获取当前登录用户的角色信息,角色不同查询客户结果不同*/
        //1.通过shiro获取当前登录用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //2.查询当前用户具有的角色信息
        List<Role> roles = userService.queryUserHaveRole(user);
        //3.判断当前用户是否具有操作员或者管理员的角色
        boolean flag = false; // 默认:无操作员或者管理员的角色
        for (Role role : roles) {
            if (Constant.ROLE_OPERATOR.equals(role.getRoleName()) ||
                Constant.ROLE_ADMIN.equals(role.getRoleName())){
                //说明具有操作员或者管理员的角色
                flag = true;
            }
        }
        CustomerExample example = new CustomerExample();
        if (!flag){// 说明只有业务员这一角色，无操作员或者管理员的角色
            example.createCriteria().andUserIdEqualTo(user.getUserId());
        }
        List<Customer> list = mapper.selectByExample(example);

        // return结果应为List<CustomerDto>，现进行转换List<Customer> --> List<CustomerDto>
        if (list != null && list.size() > 0){
            List<CustomerDto> dtos = new ArrayList<>();
            for (Customer c : list) {
                CustomerDto dto = new CustomerDto();
                BeanUtils.copyProperties(c,dto); // 实现对象属性的拷贝
                dto.setUserName(userService.queryById(c.getUserId()).getRealName());
                dto.setIntervalName(basicService.queryById(c.getBaseId()).getBaseName());
                dtos.add(dto);
            }
            return dtos;
        }
        return null;
    }

    @Override
    public void update(Customer customer) {
        mapper.updateByPrimaryKeySelective(customer);
    }

    @Override
    public String checkCustomerOrder(Integer id) {
        OrderExample example = new OrderExample();
        example.createCriteria().andCustomerIdEqualTo(id);
        int count = orderMapper.countByExample(example);
        return count>0 ? "1":"0";
    }

    @Override
    public Integer deleteById(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
