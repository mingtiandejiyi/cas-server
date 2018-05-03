CAS Overlay Template
============================

Generic CAS WAR overlay to exercise the latest versions of CAS. This overlay could be freely used as a starting template for local CAS war overlays. The CAS services management overlay is available [here](https://github.com/apereo/cas-services-management-overlay).

# Versions

```xml
<cas.version>5.2.4</cas.version>
```

# Requirements

* Tomcat 8.0+
* JDK 1.8+

# Configuration

The `etc` directory contains the configuration files and directories that need to be copied to `/etc/cas/config`.

创建cas_test数据库，在com.yellowcong.auth.handler里的doAuthentication找到以下代码，修改数据库链接参数并执行init.sql里的sql语句
    
    d.setDriverClassName("com.mysql.jdbc.Driver");
	d.setUrl("jdbc:mysql://127.0.0.1:3306/cas_test");			
	d.setUsername("root");
	d.setPassword("2018");
	
	

# Deployment

相关参考资料网址

CAS之5.2x版本单点登录服务安装-yellowcong
https://blog.csdn.net/yelllowcong/article/details/78805420 

Cas之5.2.x版本之设定SSL证书的两种方式-yellowcong
https://blog.csdn.net/yelllowcong/article/details/79229655

- Create a keystore file `thekeystore` under `/etc/cas`. Use the password `changeit` for both the keystore and the key/certificate entries.
- Ensure the keystore is loaded up with keys and certificates of the server.

On a successful deployment via the following methods, CAS will be available at:

* `http://cas.server.name:8080/cas`
* `https://cas.server.name:8443/cas`
  