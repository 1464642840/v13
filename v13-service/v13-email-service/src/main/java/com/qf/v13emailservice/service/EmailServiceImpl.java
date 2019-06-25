package com.qf.v13emailservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import com.qf.v13.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class EmailServiceImpl extends BaseServiceImpl<TUser> implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromAddr}")
    private String from;

    @Autowired
    private TUserMapper userMapper;

    @Override
    public ResultBean sendEmail(TUser user) {
        String content = user.getUserName() + ":"
                + "<a href='http://localhost:9096/email/" +
                "active?userid=" + user.getId() + "&v_key=" + user.getActiveCode() + "'>点击激活</a>";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("账户激活码");
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResultBean checkActive(Long id, String v_key) {
       int result =  userMapper.checkActive(id, v_key);
        return  null;
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
