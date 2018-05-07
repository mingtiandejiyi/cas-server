package com.cas.controller;


import org.apereo.cas.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;

@RestController
public class CASServicesController {

    @Autowired
    @Qualifier("servicesManager")
    //这个是管理我们所有服务的类
    private ServicesManager servicesManager;

    @GetMapping("/addClient")
    public String addClient(@RequestParam String serviceId, @RequestParam int id) throws IOException {
        String a="^(https|imaps|http)://"+serviceId+".*";
        RegexRegisteredService service=new RegexRegisteredService();
        RegisteredService regTemp=servicesManager.findServiceBy(serviceId);
        if(regTemp!=null){
            return "fail";
        }
        ReturnAllAttributeReleasePolicy re=new ReturnAllAttributeReleasePolicy();
        service.setServiceId(a);
        service.setId(id);
        service.setAttributeReleasePolicy(re);
        service.setName(serviceId);
        service.setLogoutUrl(new URL("http://"+serviceId));//这个是为了单点登出而作用的
        servicesManager.save(service);
        servicesManager.load();
        return "success";
    }

    @RequestMapping(value = "/addClient/{protocol}/{serviceId}/{id}",method = RequestMethod.GET)
    public String addClient(@PathVariable("serviceId") String serviceId,@PathVariable("protocol") String protocol
            ,@PathVariable("id") int id) throws IOException {
        String url=protocol+"://"+serviceId;
        RegisteredService svc = servicesManager.findServiceBy(url);
        if(svc!=null){
            return "0";//0代表着已存在这个服务，服务是通过正则去匹配的，所以这边建议使用ip或者域名+端口号
        }
        String a="^"+url+".*";//匹配以这个url开始的url
        RegexRegisteredService service=new RegexRegisteredService();
        ReturnAllAttributeReleasePolicy re=new ReturnAllAttributeReleasePolicy();
        service.setServiceId(a);
        service.setId(id);
        service.setAttributeReleasePolicy(re);
        //将name统一设置为servicesId
        service.setName(serviceId);
        service.setLogoutType(LogoutType.BACK_CHANNEL);
        service.setLogoutUrl(new URL(url));//这个是为了单点登出而作用的
        servicesManager.save(service);
        servicesManager.load();
        return "1";//添加服务成功
    }
}
