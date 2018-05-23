package com.cas.auth.handler;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.FailedLoginException;

import com.cas.auth.UsernamePasswordSysCredential;
import com.cas.auth.util.CustomPasswordEncoder;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.InvalidLoginLocationException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义密码验证(MD5)
 *
 * @author bjf
 * 创建日期:2018/05/02
 */
public class CustomerHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    public CustomerHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory,
                           Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    /**
     * 用于判断用户的Credential(换而言之，就是登录信息)，是否是俺能处理的
     * 就是有可能是，子站点的登录信息中不止有用户名密码等信息，还有部门信息的情况
     */
    @Override
    public boolean supports(Credential credential) {
        //判断传递过来的Credential 是否是自己能处理的类型
        return credential instanceof UsernamePasswordCredential;
    }

    /**
     * 用于授权处理
     */
    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        //UsernamePasswordCredential transformedCredential = (UsernamePasswordCredential) credential;
        UsernamePasswordSysCredential transformedCredential = (UsernamePasswordSysCredential) credential;
        //获取传递过来的用户名和密码
        String username = transformedCredential.getUsername();
        String password = transformedCredential.getPassword();
        String appKey = transformedCredential.getSystem();
        DriverManagerDataSource d = new DriverManagerDataSource();
        d.setDriverClassName("com.mysql.jdbc.Driver");
        d.setUrl("jdbc:mysql://127.0.0.1:3306/cas_test");
        d.setUsername("root");
        d.setPassword("2018");
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(d);

        //查询数据库加密的的密码
        List<Map<String, Object>> userMaps = template.queryForList("SELECT * FROM cas_user WHERE username = ?", username);

        if (userMaps == null || userMaps.size() != 1) {
            //账号错误
            throw new AccountLockedException();
        }
        //查询对应的用户名
        List<Map<String, Object>> appUserMaps = template.queryForList("SELECT * FROM cas_user_realtion WHERE casUsername = ? AND appkey = ?", username, appKey);
        if (appUserMaps == null || appUserMaps.size() != 1) {
            //账号错误
            return  null;
        }
        Map<String, Object> appUser = appUserMaps.get(0);
        String principalName = (String) appUser.get("username");
        Map<String, Object> user = userMaps.get(0);
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        if (encoder.matches(password, user.get("password").toString())) {
            //登录成功通过this.principalFactory.createPrincipal来返回用户属性
            return createHandlerResult(transformedCredential, principalFactory.createPrincipal(principalName, Collections.emptyMap()), null);
        }
        return null;
    }

}
