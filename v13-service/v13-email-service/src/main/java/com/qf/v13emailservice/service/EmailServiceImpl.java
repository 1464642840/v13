package com.qf.v13emailservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TException;
import com.qf.v13.entity.TUser;
import com.qf.v13.mapper.TExceptionMapper;
import com.qf.v13.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class EmailServiceImpl extends BaseServiceImpl<TUser> implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.fromAddr}")
    private String from;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private TExceptionMapper exceptionMapper;

    @Override
    public ResultBean sendEmail(TUser user) throws MessagingException {

        Context context = new Context();
        String activeUrl = "http://localhost:9096/email/active?userid=" + user.getId() + "&v_key=" + user.getActiveCode();
        context.setVariable("url", activeUrl);
        context.setVariable("userName", user.getUserName());
        String content = templateEngine.process("email_active_template", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(user.getEmail());
        helper.setSubject("账户激活码");
        helper.setText(content, true);
        mailSender.send(mimeMessage);


        return null;
    }

    @Override
    public ResultBean checkActive(Long id, String v_key) {
        int result = userMapper.checkActive(id, v_key);
        return null;
    }

    @Value("${qcloudsms.appid}")
    private int appid;
    @Value("${qcloudsms.key}")
    private String appkey;
    @Value("${qcloudsms.templated}")
    private int templateId;
    @Value("${qcloudsms.smsSign}")
    private String smsSign;

    @Override
    public void sendMsg(Map data) throws IOException, HTTPException {
        //短信模板的参数
        ArrayList<String> params = new ArrayList();

        params.add((String) data.get("code"));
        params.add("3");
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);

        SmsSingleSenderResult result = ssender.sendWithParam("86", (String) data.get("phone"),
                templateId, params, smsSign, "", "");

    }

    @Override
    public int addOneException(TException exception) {
        return exceptionMapper.insertSelective(exception);
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
