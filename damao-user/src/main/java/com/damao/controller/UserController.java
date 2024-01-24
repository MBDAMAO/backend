package com.damao.controller;

import com.damao.context.BaseContext;
import com.damao.properties.JwtProperties;
import com.damao.result.PageResult;
import com.damao.result.Result;
import com.damao.utils.JwtUtil;
import com.damao.pojo.dto.UserLoginDTO;
import com.damao.pojo.dto.UserPageQueryDTO;
import com.damao.pojo.dto.UserRegistryDTO;
import com.damao.pojo.dto.VerifyEmailDTO;
import com.damao.pojo.entity.User;
import com.damao.pojo.vo.UserLoginVO;
import com.damao.pojo.vo.UserRegistryVO;
import com.damao.pojo.vo.UserVO;
import com.damao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户登录、注册、信息修改等
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtProperties jwtProperties;

    @Autowired
    public UserController(UserService userService, JwtProperties jwtProperties) {
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        log.info("用户登录(●'◡'●){}", userLoginDTO.getUsername());
        User user = userService.login(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getUid());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .uid(user.getUid())
                .userName(user.getUsername())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @PostMapping("/registry")
    public Result<?> registry(@RequestBody @Validated UserRegistryDTO userRegistryDTO) {
        log.info("用户注册(●'◡'●){}", userRegistryDTO.getEmail());
        User user = userService.registry(userRegistryDTO);



        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getUid());


        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        UserRegistryVO userRegistryVO = UserRegistryVO.builder()
                .token(token)
                .uid(user.getUid())
                .userName(user.getUsername())
                .build();

        return Result.success(userRegistryVO);
    }

    /**
     * 用户信息更新接口
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @GetMapping("/getById/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        UserVO userVO = UserVO.builder()
                .uid(user.getUid())
                .sex(user.getSex())
                .username(user.getUsername())
                .status(user.getStatus())
                .build();

        return Result.success(userVO);
    }

    @GetMapping("/getSelf")
    public Result<UserVO> getSelf(){
        User user = userService.getById(BaseContext.getCurrentId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);

        return Result.success(userVO);
    }

    @GetMapping("/getByName/{username}")
    public Result<UserVO> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        UserVO userVO = UserVO.builder()
                .uid(user.getUid())
                .sex(user.getSex())
                .username(user.getUsername())
                .status(user.getStatus())
                .build();

        return Result.success(userVO);
    }

    /**
     * 获取用户粉丝，分页查询
     */
    @PostMapping("/getFollowers")
    public Result<PageResult> getFollowersById(@RequestBody UserPageQueryDTO userPageQueryDTO) {
        PageResult data = userService.getFollowersById(userPageQueryDTO);
        return Result.success(data);
    }

    /**
     * 获取用户关注者，分页查询
     */
    @PostMapping("/getFollowed")
    public Result<PageResult> getFollowedById(@RequestBody UserPageQueryDTO userPageQueryDTO) {
        PageResult data = userService.getFollowedById(userPageQueryDTO);
        return Result.success(data);
    }

    @PostMapping("/getAllUsers")
    public Result<PageResult> getUsers(@RequestBody UserPageQueryDTO userPageQueryDTO) {
        PageResult data = userService.getUsers(userPageQueryDTO);
        return Result.success(data);
    }

    @PutMapping("/followUser/{id}")
    public Result<?> followUser(@PathVariable Long id){
        userService.followUser(id);
        return Result.success();
    }

    @PutMapping("/unFollowUser/{id}")
    public Result<?> unFollowUser(@PathVariable Long id){
        userService.unFollowUser(id);
        return Result.success();
    }

    @GetMapping("/followStatus/{id}")
    public Result<?> followStatus(@PathVariable Long id){
        Boolean status = userService.followStatus(id);
        return Result.success(status);
    }

    @PostMapping("/verifyEmail")
    public Result<?> verifyEmail(VerifyEmailDTO verifyEmailDTO){
        userService.verifyEmail(verifyEmailDTO);
        return Result.success();
    }
}
