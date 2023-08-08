package com.yg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yg.dto.UserDto;
import com.yg.mapper.RoleMapper;
import com.yg.mapper.UserMapper;
import com.yg.mapper.UserRoleMapper;
import com.yg.pojo.*;
import com.yg.service.IUserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用户管理
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper mapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<User> query(User user) throws Exception {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if(user!=null){
            if(user.getUserName()!=null && !"".equals(user.getUserName())){
                criteria.andUserNameEqualTo(user.getUserName());
            }
        }
        //查询的是U2为null的记录
        criteria.andU2IsNull();
        return mapper.selectByExample(example);
    }

    @Override
    public Integer addUser(User user) throws Exception {
        return mapper.insertSelective(user);
    }

    @Override
    public Integer updateUser(User user) throws Exception {
        return mapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer deleteUser(Integer userId) throws Exception {
        User user = new User();
        user.setUserId(userId);
        user.setU2("1");
        return mapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer saveOrUpdate(UserDto dto) throws Exception {
        // 1.对密码进行加密，再添加用户信息至t_user表
        User user = dto.getUser();
        if(user.getUserId() != null){ //表示是进行用户的更新操作,不用再对密码加密
            this.updateUser(user);
        }else { //表示是进行用户的添加操作,对密码加密
            // 对密码加密，MD5加密＋salt（盐值）
            String salt = UUID.randomUUID().toString();
            Md5Hash passwordHash = new Md5Hash(user.getPassword(),salt,1024);
            user.setPassword(passwordHash.toString());
            user.setU1(salt);
            //添加用户信息至t_user表，设置了主键自增回填，添加成功后user.getUserId()可获取Id
            this.addUser(user);
        }

        // 根据用户编号删除对应的角色信息
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(user.getUserId());
        userRoleMapper.deleteByExample(example);

        // 2.若该用户有与之关联的角色信息，则将关联关系保存到t_user_role表中
        // 获取分配给当前用户的角色信息
        List<Integer> roleIds = dto.getRoleIds();
        if(roleIds != null && roleIds.size() > 0){// 表示该用户有与之关联的角色信息
            for (Integer roleId : roleIds) {
                // 将用户和角色的关联关系保存到t_user_role中
                UserRoleKey userRole = new UserRoleKey();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(roleId);
                userRoleMapper.insertSelective(userRole);
            }
        }
        return 1;
    }

    @Override
    public User queryById(Integer userId) throws Exception {
        return mapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<Integer> queryUserRoleIds(Integer userId) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(example);
        ArrayList<Integer> ids = new ArrayList<>();
        for (UserRoleKey userRoleKey : userRoleKeys) {
            ids.add(userRoleKey.getRoleId());
        }
        return ids;
    }

    @Override
    public PageInfo<User> queryByPage(UserDto dto) throws Exception{
        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
        List<User> list = this.query(dto.getUser());
        PageInfo<User> pageInfo = new PageInfo<>(list);
        for (User user : list) {
            System.out.println(user);
        }
        return pageInfo;
    }


    /**
     * 完成登录认证的方法
     * @param userName
     * @return
     */
    @Override
    public User login(String userName) {
        UserExample example = new UserExample();
        example.createCriteria().andUserNameEqualTo(userName);
        List<User> list = mapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Role> queryUserHaveRole(User user) {
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(user.getUserId());
        // 查询出用户具有的角色的所有的编号
        List<UserRoleKey> roleIds = userRoleMapper.selectByExample(example);
        if (roleIds != null && roleIds.size() > 0){
            List<Role> roles = new ArrayList<>();
            for (UserRoleKey userRoleKey : roleIds) {
                Role role = roleMapper.selectByPrimaryKey(userRoleKey.getRoleId());
                roles.add(role);
            }
            return roles;
        }
        return null;
    }

    /**
     * 根据角色名称 查询所有该角色的用户信息
     * @param roleName
     * @return
     */
    @Override
    public List<User> queryUserByRoleName(String roleName) throws Exception {
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria().andRoleNameEqualTo(roleName);
        List<Role> roles = roleMapper.selectByExample(roleExample);
        if (roles != null && roles.size() > 0){
            Role role = roles.get(0);
            UserRoleExample userRoleExample = new UserRoleExample();
            userRoleExample.createCriteria().andRoleIdEqualTo(role.getRoleId());
            List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
            if (userRoleKeys != null && userRoleKeys.size() > 0 ){
                List<User> list = new ArrayList<>();
                for (UserRoleKey userRoleKey : userRoleKeys) {
                    User user = this.queryById(userRoleKey.getUserId());
                    list.add(user);
                }
                return list;
            }
        }
        return null;
    }
}
