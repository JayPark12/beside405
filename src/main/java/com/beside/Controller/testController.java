package com.beside.Controller;
import com.beside.Entity.testEntity;
import lombok.extern.slf4j.Slf4j;
import com.beside.service.testJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequestMapping("/login")
public class testController {
    @Autowired
    private testJava testJava;

    @GetMapping("/")
    public  String testRErere(@RequestBody testEntity testEntity) {


        return "index";
    }
}
