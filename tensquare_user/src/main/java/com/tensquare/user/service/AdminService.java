package com.tensquare.user.service;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class AdminService {

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    AdminDao adminDao;

    @Autowired
    IdWorker idWorker;

    public void add(Admin admin) {
        admin.setId(idWorker.nextId() + ""); //主键值
        //密码加密
        String newpassword = encoder.encode(admin.getPassword());//加密后的密码
        adminDao.save(admin);
    }

    /**
     * 根据登录名和密码查询
     * @return
     */
    public Admin findByLoginnameAndPassword(String loginname, String password) {
        Admin admin = adminDao.findByLoginname(loginname);
        //如果传入的密码和数据库密码匹配
        if (admin != null && encoder.matches(password, admin.getPassword())) {
            return admin;
        } else {
            return null;
        }
    }
}
