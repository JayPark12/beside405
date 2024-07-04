package com.beside.reservation.service;

import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.dto.MntiReserDetailInput;
import com.beside.reservation.dto.MntiReserInput;
import com.beside.reservation.dto.MntiReserOutput;
import com.beside.reservation.repository.ReserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiReserDeleteService {

    private final ReserRepository reserRepository;

    public void execute(MntiReserDetailInput mntiReserDetailInput) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        reserInsert(id, mntiReserDetailInput);


    }

    private void reserInsert (String id , MntiReserDetailInput mntiReserDetailInput) throws InterruptedException {
        MntiReserEntity mntiReserEntity = new MntiReserEntity(); //새로운 정보 입력
        Integer mntiCnt = reserRepository.findByMntiReserDeleteSearch(id , mntiReserDetailInput.getMntiListNo(),mntiReserDetailInput.getMntiStrDate()); //등산 횟수 체크 (같은 산)
        mntiReserEntity.setMntiCnt(mntiCnt);
        mntiReserEntity.setId(id);
        mntiReserEntity.setMntiListNo(mntiReserDetailInput.getMntiListNo());
        mntiReserEntity.setMntiSts("4"); //0 : 등산 계획,  1 :등산 중 , 2 : 등산완료 ,3 : 등산실패 , 4: 등산 취소

        reserRepository.save(mntiReserEntity);
    }

}
