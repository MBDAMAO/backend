package com.damao.service;

import com.damao.result.PageResult;
import com.damao.pojo.dto.UserLoginDTO;
import com.damao.pojo.dto.UserPageQueryDTO;
import com.damao.pojo.dto.UserRegistryDTO;
import com.damao.pojo.dto.VerifyEmailDTO;
import com.damao.pojo.entity.User;


public interface UserService {
    User login(UserLoginDTO userLoginDTO);

    User registry(UserRegistryDTO userRegistryDTO);

    void update(User user);

    User getById(Long id);

    User getByUsername(String username);

    PageResult getFollowersById(UserPageQueryDTO userPageQueryDTO);

    PageResult getFollowedById(UserPageQueryDTO userPageQueryDTO);

    PageResult getUsers(UserPageQueryDTO userPageQueryDTO);

    void followUser(Long id);

    void unFollowUser(Long id);

    void verifyEmail(VerifyEmailDTO verifyEmailDTO);

    boolean followStatus(Long id);
}
