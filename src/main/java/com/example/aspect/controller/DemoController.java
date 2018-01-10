package com.example.aspect.controller;


import com.example.aspect.common.annotation.RequestLog;
import com.example.aspect.dto.SimplePersonInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/demo")
public class DemoController {

    @RequestLog
    @GetMapping(value = "/get")
    public Map getTest(@RequestParam(name = "name") String name,@RequestParam(name = "age") Integer age){
        log.error("error log get test");
        Map returnMap = new HashMap();
        returnMap.put("name",name);
        returnMap.put("age",age);
        return returnMap;
    }


    @RequestLog
    @PostMapping(value = "/post")
    public ResponseEntity postTest(@RequestBody SimplePersonInfo personInfo){
        log.error("error log post test");
        return ResponseEntity.ok(personInfo);
    }

}
