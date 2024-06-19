package com.beside.reservation.service;

import com.beside.common.util.CommomUtil;
import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.dto.MntiReserListOutput;
import com.beside.reservation.repository.ReserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiReserListService {

    private final ReserRepository reserRepository;
    private final CommomUtil commomUtil;

    public Page<MntiReserListOutput> execute(Pageable pageable) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MntiReserListOutput> mntiReserListOutput = new ArrayList<>();
        List<MntiReserEntity> mntiReserListOutputList = reserRepository.findByMntiCntAndUserIdAndScheduleId(id , "0");
        List<MntiReserEntity> mntiReserListPage = mntiReserListOutputList.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        for (MntiReserEntity mntiReserEntity : mntiReserListPage) {
            List<String> potoFileSelect = commomUtil.potoFile(mntiReserEntity.getMntiListNo(), mntiReserEntity.getMntiName());
            MntiReserListOutput mntiReserOutput = new MntiReserListOutput();
            mntiReserOutput.setMntiName(mntiReserEntity.getMntiName());
            mntiReserOutput.setMntiListNo(mntiReserEntity.getMntiListNo());
            mntiReserOutput.setMntiLevel(mntiReserEntity.getMntiLevel());

            if (!potoFileSelect.isEmpty()) {
                mntiReserOutput.setPotoFile(potoFileSelect.get(0));
            }

            mntiReserListOutput.add(mntiReserOutput);
        }
        return new PageImpl<>(mntiReserListOutput, pageable, mntiReserListOutput.size());
    }
}
