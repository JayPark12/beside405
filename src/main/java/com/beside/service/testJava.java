package com.beside.service;

import com.beside.Entity.testEntity;
import lombok.RequiredArgsConstructor;
import com.beside.DAO.testDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class testJava {

    private final testDao testDao;

    public String serviceTest(testEntity testEntity) {
        testEntity = testDao.aaaaSelect("Jpark");

        return   testEntity.getTestData1();
    }
}
