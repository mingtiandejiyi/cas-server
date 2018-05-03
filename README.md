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

CAS Properties
https://apereo.github.io/cas/5.2.x/installation/Configuration-Properties.html#service-registry

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

动态加载、管理services

        <dependency>
            <groupId>org.apereo.cas</groupId>
            <artifactId>cas-server-support-jpa-service-registry</artifactId>
            <version>${cas.version}</version>
        </dependency>
        

动态加载、管理services配置

        
        #开启识别json文件，默认false
        cas.serviceRegistry.initFromJson=true
        #自动扫描服务配置，默认开启
        cas.serviceRegistry.watcherEnabled=true
        #120秒扫描一遍
        cas.serviceRegistry.repeatInterval=120000
        #延迟15秒开启
        cas.serviceRegistry.startDelay=15000
        #资源加载路径
        #cas.serviceRegistry.config.location=classpath:/services
        
        cas.serviceRegistry.schedule.repeatInterval=120000
        cas.serviceRegistry.schedule.startDelay=15000
        #-----------动态添加services----------------
        cas.serviceRegistry.jpa.healthQuery=
        cas.serviceRegistry.jpa.isolateInternalQueries=false
        cas.serviceRegistry.jpa.url=jdbc:mysql://127.0.0.1:3306/cas_test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
        cas.serviceRegistry.jpa.failFastTimeout=1
        cas.serviceRegistry.jpa.dialect=org.hibernate.dialect.MySQL5Dialect
        cas.serviceRegistry.jpa.leakThreshold=10
        cas.serviceRegistry.jpa.batchSize=1
        cas.serviceRegistry.jpa.user=root
        cas.serviceRegistry.jpa.ddlAuto=create-drop
        cas.serviceRegistry.jpa.password=2018
        cas.serviceRegistry.jpa.autocommit=false
        cas.serviceRegistry.jpa.driverClass=com.mysql.jdbc.Driver
        cas.serviceRegistry.jpa.idleTimeout=5000
        # cas.serviceRegistry.jpa.dataSourceName=
        cas.serviceRegistry.jpa.dataSourceProxy=false
        # Hibernate-specific properties (i.e. `hibernate.globally_quoted_identifiers`)
        # cas.serviceRegistry.jpa.properties.propertyName=propertyValue
        
        cas.serviceRegistry.jpa.pool.suspension=false
        cas.serviceRegistry.jpa.pool.minSize=6
        # cas.serviceRegistry.jpa.pool.maxSize=18
        # cas.serviceRegistry.jpa.pool.maxWait=2000
        


  