package org.example.usermanage.controller;

import org.example.usermanage.model.UserInfo;
import org.example.usermanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Page<UserInfo> findAll(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.queryAllUsers(pageable);
    }

    @GetMapping("/getById")
    public UserInfo findById(@RequestParam Long id) {
        return userService.queryUserById(id);
    }

    @GetMapping("/getByName")
    public Page<UserInfo> findByName(@RequestParam String name,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.queryUserByName(name, pageable);
    }

    @PostMapping("/saveOrUpdate")
    public UserInfo saveOrUpdate(@RequestBody UserInfo userInfo) {
        return userService.saveOrUpdateUser(userInfo);
    }

    @DeleteMapping("/deleteByIds")
    public void deleteByIds(@RequestBody List<Long> ids) {
        userService.deleteUsersByIds(ids);
    }
}
