package com.damao.service.impl;

import com.damao.constant.RabbitMQConstant;
import com.damao.constant.RedisNameConstant;
import com.damao.context.BaseContext;
import com.damao.pojo.dto.*;
import com.damao.result.PageResult;
import com.damao.mapper.UserMapper;
import com.damao.pojo.entity.FollowerRelation;
import com.damao.pojo.entity.User;
import com.damao.pojo.vo.UserVO;
import com.damao.result.exception.*;
import com.damao.result.exception.PasswordErrorException;
import com.damao.service.FollowerRelationService;
import com.damao.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private FollowerRelationService followerRelationService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String password = userLoginDTO.getPassword();
        String username = userLoginDTO.getUsername();
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new AccountNotFoundException("账号或密码错误");
        }
        if (!Objects.equals(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)), user.getPassword())) {
            throw new PasswordErrorException("账号或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new UserStatusErrorException("用户状态不正常");
        }
        return user;
    }

    @Override
    public User registry(UserRegistryDTO userRegistryDTO) {
        String username = userRegistryDTO.getUsername();
        User user = userMapper.getByUsername(username);
        if (user != null) {
            throw new UserAlreadyExistsException("用户名已存在");
        }
        User newUser = new User();
        String email = userRegistryDTO.getEmail();

        SendEmailDTO send = new SendEmailDTO();
        send.setEmail(email);


        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE,"123",send);
        BeanUtils.copyProperties(userRegistryDTO, newUser);
        String password = userRegistryDTO.getPassword();
        String newPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        newUser.setPassword(newPassword);
        userMapper.insert(newUser);
        return newUser;
    }

    @Override
    public void update(User user) {
        String password = user.getPassword();
        if (password != null) {
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        }
        userMapper.update(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    @Override
    public PageResult getFollowersById(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());
        Page<UserVO> myFollowers = userMapper.getFollowersById(userPageQueryDTO);
        if (myFollowers.isEmpty()) return new PageResult();
        List<Long> followedIdsList = followerRelationService.getFollowedById(userPageQueryDTO.getUid());
        HashSet<Long> followedIds = new HashSet<>(followedIdsList);
        for (UserVO myFollow : myFollowers) {
            myFollow.setFollowedTogether(followedIds.contains(myFollow.getUid()));
        }
        return new PageResult(myFollowers.getTotal(), myFollowers.getResult());
    }

    @Override
    public PageResult getFollowedById(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());
        Page<UserVO> myLikes = userMapper.getFollowedById(userPageQueryDTO);
        if (myLikes.isEmpty()) return new PageResult();
        List<Long> followerIdsList = followerRelationService.getFollowersById(userPageQueryDTO.getUid());
        HashSet<Long> followerIds = new HashSet<>(followerIdsList);
        for (UserVO myLike : myLikes) {
            myLike.setFollowedTogether(followerIds.contains(myLike.getUid()));
        }
        return new PageResult(myLikes.getTotal(), myLikes.getResult());
    }

    @Override
    public PageResult getUsers(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());
        Page<UserVO> page = userMapper.getUsers(userPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void followUser(Long id) {
        followerRelationService.add(id, BaseContext.getCurrentId());
    }

    @Override
    public void unFollowUser(Long id) {
        followerRelationService.remove(id,BaseContext.getCurrentId());
    }

    @Override
    public boolean followStatus(Long id) {
        FollowerRelation followerRelation = followerRelationService.getStatus(BaseContext.getCurrentId(),id);
        return followerRelation != null;
    }

    @Override
    public void verifyEmail(VerifyEmailDTO verifyEmailDTO) {
        String email = verifyEmailDTO.getEmail();
        String code = verifyEmailDTO.getCode();
        String code_ = (String) redisTemplate.opsForHash().get(RedisNameConstant.VERIFY_CODE_SET,email);
        if (code_ == null) {
            throw new BaseException("验证码已过期");
        }
        if (!code_.equals(code)){
            throw new BaseException("验证码错误");
        }
        log.info("验证成功");
    }
}


