package com.damao.mapper;


import com.damao.annotation.AutoFill;
import com.damao.enumeration.OperationType;
import com.damao.pojo.dto.UserPageQueryDTO;
import com.damao.pojo.entity.User;
import com.damao.pojo.vo.UserVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from video_platform.users where uid = #{uid}")
    User getById(Long uid);

    @Select("select * from video_platform.users where username = #{username}")
    User getByUsername(String username);

    @AutoFill(OperationType.INSERT)
    void insert(User newUser);

    @AutoFill(OperationType.UPDATE)
    void update(User user);

    Page<UserVO> getFollowersById(UserPageQueryDTO userPageQueryDTO);

    Page<UserVO> getFollowedById(UserPageQueryDTO userPageQueryDTO);

    Page<UserVO> getUsers(UserPageQueryDTO userPageQueryDTO);

    @Select("SELECT like_vector from video_platform.user_model where uid=#{uid}")
    String getModel(Long uid);
}
