package com.yg.service;

import com.github.pagehelper.PageInfo;
import com.yg.dto.UserDto;
import com.yg.pojo.Role;
import com.yg.pojo.User;

import java.util.List;

/**
 * 用户信息
 */
public interface IUserService {

    /**
     * 根据条件查询用户信息
     * @param user
     * @return
     */
    List<User> query(User user) throws Exception;

    /**
     * 添加用户信息
     * @param user
     * @return
     */
    Integer addUser(User user) throws Exception;

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Integer updateUser(User user) throws Exception;


    /**
     * 根据编号删除用户信息
     * @param userId
     * @return
     */
    Integer deleteUser(Integer userId) throws Exception;

    Integer saveOrUpdate(UserDto dto) throws Exception;

    User queryById(Integer userId) throws Exception;

    /**
     * 根据用户编号查询其角色编号
     * @param userId
     * @return
     */
    List<Integer> queryUserRoleIds(Integer userId);

    /**
     *分页查询
     * @return
     */
    PageInfo<User> queryByPage(UserDto dto) throws Exception;

    /**
     * 登录时根据用户名查询用户信息
     * @param userName
     * @return
     */
    User login(String userName);

    List<Role> queryUserHaveRole(User user);

    List<User> queryUserByRoleName(String roleName) throws Exception;
}
