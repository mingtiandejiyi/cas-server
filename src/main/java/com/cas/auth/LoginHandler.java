package com.cas.auth;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountLockedException;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 自定义密码验证(MD5)
 * @author bjf
 * 创建日期:2018/05/02
 *
 */
public class LoginHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

	public LoginHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory,
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
		UsernamePasswordCredential transformedCredential = (UsernamePasswordCredential) credential;
		
		//获取传递过来的用户名和密码
		String username = transformedCredential.getUsername();
        String password = transformedCredential.getPassword();
    	try {
			DriverManagerDataSource d=new DriverManagerDataSource();
			d.setDriverClassName("com.mysql.jdbc.Driver");
			d.setUrl("jdbc:mysql://192.168.10.67:3306/cas_dev");
			d.setUsername("msyy");
			d.setPassword("msyy2018");
			JdbcTemplate template=new JdbcTemplate();
			template.setDataSource(d);
			//查询数据库加密的的密码
			List<Map<String,Object>> userMaps = template.queryForList("SELECT * FROM cas_user WHERE username = ?", username);
			if(userMaps==null || userMaps.size()!=1){
				//账号错误
				throw new AccountLockedException();
			}

			Map<String,Object> user= userMaps.get(0);
			//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			CustomPasswordEncoder encoder = new CustomPasswordEncoder();
			if (encoder.matches(password, user.get("password").toString())) {
				/*////返回多属性
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("username", user.get("username"));
				map.put("password", user.get("password"));
				map.put("email", user.get("email"));
				map.put("addr", user.get("addr"));
				map.put("phone", user.get("phone"));
				map.put("age", user.get("age"));*/
				//查询对应的用户名
				List<Map<String, Object>> appUserMaps = template.queryForList("SELECT appkey,username FROM cas_user_realtion WHERE casUsername = ? ", username);
				if (appUserMaps.size() < 1) {	return  null;}
				StringBuilder sbName=new StringBuilder();
				for(Map<String, Object> map : appUserMaps){
					sbName.append(",");
					sbName.append((String)map.get("appkey"));
					sbName.append("|");
					sbName.append((String)map.get("username"));
				}
				if(sbName.length()>0){
					sbName.replace(0,1,"");
				}
				//登录成功通过this.principalFactory.createPrincipal来返回用户属性
				return createHandlerResult(transformedCredential, principalFactory.createPrincipal(sbName.toString(), Collections.emptyMap()), null);
			}
			//return createHandlerResult(credential, this.principalFactory.createPrincipal(username, result), null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			/*if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
		}
    	/*//当是admin用户的情况，直接就登录了，谁叫他是admin用户呢
    	if(username.startsWith("admin")) {
    		//直接返回去了
    		return createHandlerResult(credential, this.principalFactory.createPrincipal(username, Collections.emptyMap()), null);
    	}else if (username.startsWith("lock")) {
            //用户锁定
            throw new AccountLockedException();
        } else if (username.startsWith("disable")) {
            //用户禁用
            throw new AccountDisabledException();
        } else if (username.startsWith("invali")) {
            //禁止登录该工作站登录
            throw new InvalidLoginLocationException();
        } else if (username.startsWith("passorwd")) {
            //密码错误
            throw new FailedLoginException();
        } else if (username.startsWith("account")) {
            //账号错误
            throw new AccountLockedException();
        }*/
		return null;
	}

}
