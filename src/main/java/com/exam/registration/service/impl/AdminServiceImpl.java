package com.exam.registration.service.impl;

import com.exam.registration.mapper.AdminMapper;
import com.exam.registration.model.Admin;
import com.exam.registration.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.util.DateUtils;

import java.time.Instant;
import java.util.*;

/**
 * @author yhf
 * @classname AdminServiceImpl
 **/
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public long countAdmins(String keyword) {
        return adminMapper.countAdmins(keyword);
    }

    @Override
    public int deleteAdminByPrimaryKey(Long id) {
        return adminMapper.deleteAdminByPrimaryKey(id);
    }

    @Override
    public int deleteAdminByPrimaryKeys(String ids) {
        return adminMapper.deleteAdminByPrimaryKeys(ids);
    }

    @Override
    public int insertAdmin(Admin admin) {
        String salt = UUID.randomUUID().toString().substring(0, 5);
        admin.setSalt(salt);
        String password = DigestUtils.md5DigestAsHex((admin.getPassword() + salt).getBytes());
        admin.setPassword(password);
        if (Objects.isNull(admin.getIsDeleted())) {
            admin.setIsDeleted(false);
        }
        Date now = new Date();
        admin.setCreateTime(now);
        admin.setUpdateTime(now);
        return adminMapper.insertAdmin(admin);
    }

    @Override
    public int insertAdminSelective(Admin record) {
        return 0;
    }

    @Override
    public List<Admin> listAdmins() {
        return adminMapper.listAdmins();
    }

    @Override
    public List<Admin> listAdminsByPage(Map<String, Object> map) {
        return adminMapper.listAdminsByPage(map);
    }

    @Override
    public Admin getAdminByPrimaryKey(Long id) {
        return adminMapper.getAdminByPrimaryKey(id);
    }

    @Override
    public Admin getAdminByName(String name) {
        return adminMapper.getAdminByName(name);
    }

    @Override
    public int updateAdminByPrimaryKeySelective(Admin admin) {
        Admin queryAdmin = getAdminByPrimaryKey(admin.getId());
        if (Objects.isNull(queryAdmin)) {
            return 0;
        }

        if (!StringUtils.isEmpty(queryAdmin.getPassword())) {
            String newPass = DigestUtils.md5DigestAsHex((admin.getPassword() + queryAdmin.getSalt())
                    .getBytes());
            admin.setPassword(newPass);
        }

        admin.setUpdateTime(new Date());

        return adminMapper.updateAdminByPrimaryKeySelective(admin);
    }

    @Override
    public int updateAdminByPrimaryKey(Admin record) {
        return 0;
    }

    @Override
    public int login(Admin admin) {
        Admin queryAdmin = getAdminByName(admin.getName());
        if (Objects.isNull(queryAdmin)) {
            return 0;
        }

        String md5Pass = DigestUtils.md5DigestAsHex((admin.getPassword() + queryAdmin.getSalt()).getBytes());
        if (md5Pass.equals(queryAdmin.getPassword())) {
            return 1;
        }
        return 0;
    }
}
