package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TException;
import com.qf.v13.entity.TUser;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.Map;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IEmailService extends IBaseService<TUser> {
    public ResultBean sendEmail(TUser user) throws MessagingException, javax.mail.MessagingException;

    public ResultBean checkActive(Long id, String v_key);

    public void sendMsg(Map data) throws HTTPException, IOException, com.github.qcloudsms.httpclient.HTTPException;
    int addOneException(TException exception);
}
