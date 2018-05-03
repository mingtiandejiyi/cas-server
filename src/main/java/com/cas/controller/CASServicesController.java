package com.cas.controller;


import org.apereo.cas.services.RegexRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ReturnAllAttributeReleasePolicy;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
