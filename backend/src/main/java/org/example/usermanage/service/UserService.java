package org.example.usermanage.service;

import org.example.usermanage.model.UserInfo;
import org.example.usermanage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 创建或更新用户
    public UserInfo saveOrUpdateUser(UserInfo userInfo) {
        return userRepository.save(userInfo);
    }

    // 返回所有用户
    public Page<UserInfo> queryAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 通过id查询用户
    public UserInfo queryUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 通过username查询用户
    public Page<UserInfo> queryUserByName(String username, Pageable pageable) {
        return userRepository.findByUsername(username, pageable);
    }

    // 通过List id批量删除用户
    public void deleteUsersByIds(List<Long> ids) {
        userRepository.deleteByIds(ids);
    }
}
