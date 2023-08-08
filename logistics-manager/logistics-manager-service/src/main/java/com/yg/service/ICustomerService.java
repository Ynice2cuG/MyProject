package com.yg.service;

import com.yg.dto.CustomerDto;
import com.yg.pojo.Customer;
import org.springframework.ui.Model;

import java.util.List;

public interface ICustomerService {
    /**
     *获取添加或更新客户需要准备的数据
     * @param id
     * @param model
     */
    void getUpdateInfo(Integer id, Model model) throws Exception;

    /**
     * 添加客户
     * @param customer
     * @return
     */
    Integer save(Customer customer);

    /**
     * 客户管理只能是 业务员 操作员 管理员,这三者才能看到查询后的页面
     * 其中，操作员和管理员可看到所有的客户信息
     * 业务员只能看到属于自己管理的客户信息
     * @param customer
     * @return
     * @throws Exception
     */
    List<CustomerDto> query(Customer customer) throws Exception;

    /**
     * 更新客户信息
     * @param customer
     */
    void update(Customer customer);

    /**
     * 通过客户id查询其一共有多少订单
     * @param id
     * @return
     */
    String checkCustomerOrder(Integer id);

    /**
     * 通过客户id删除客户
     * @param id
     * @return
     */
    Integer deleteById(Integer id);
}
