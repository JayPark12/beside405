package com.beside.complete.service;

import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.dto.MntiReserDetailInput;
import com.beside.reservation.repository.ReserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiCompleteService {

        private final ReserRepository reserRepository;
        public void execute(MntiReserDetailInput mntiReserDetailInput) throws Exception {
            String id = SecurityContextHolder.getContext().getAuthentication().getName();
            mntiComplete(id, mntiReserDetailInput);

        }

        private void mntiComplete (String id , MntiReserDetailInput mntiReserDetailInput) throws Exception {
            MntiReserEntity mntiReserEntity = reserRepository.findByMntiReserDeleteSearch(id , mntiReserDetailInput.getMntiListNo(),mntiReserDetailInput.getMntiStrDate()); //등산 횟수 체크 (같은 산)

            mntiReserEntity.setMntiCnt(mntiReserEntity.getMntiCnt());
            mntiReserEntity.setId(id);
            mntiReserEntity.setMntiListNo(mntiReserDetailInput.getMntiListNo());
            mntiReserEntity.setMntiSts("2"); //0 : 등산 계획,  1 :등산 중 , 2 : 등산완료 ,3 : 등산실패 , 4: 등산 취소

            reserRepository.save(mntiReserEntity);
        }
}
