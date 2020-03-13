package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    UserDao dao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        User u = dao.findByUsername(user.getUsername());
        if (u == null) {
            user.setCode(UuidUtil.getUuid());
            user.setStatus("N");
            dao.save(user);
            String content = "<a href='http://localhost:8080/travel/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
            MailUtils.sendMail(user.getEmail(), content, "激活邮件");
            return true;
        }
        return false;
    }

    @Override
    public boolean active(String code) {
        User user = dao.findByCode(code);
        if (user == null) {
            return false;
        }
        dao.updateStatus(user);
        return true;
    }

    @Override
    public User loign(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
