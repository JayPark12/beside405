package com.beside.Controller;
import com.beside.Entity.testEntity;
import lombok.extern.slf4j.Slf4j;
import com.beside.service.testJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequestMapping("/Test01")
public class testController {
    @Autowired
    private testJava testJava;

    @PostMapping("/testRegister")
    public  void testRErere(@RequestBody testEntity testEntity) {

        this.testJava.serviceTest(testEntity);

        System.out.println(testEntity.getTestData1() + "완료");
    }
}
