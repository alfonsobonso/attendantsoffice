/*
 * Copyright (c) 2018 by Hotspring Ventures Limited
 * 16 Charles Ii Street (c/o Calder & Co), London SW1Y 4NW
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Hotspring Ventures Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Hotspring Ventures Limited.
 */
package org.attendantsoffice.eventmanager.congregation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fetch/update the underlying congregation information.
 * We expect few entries here, so we cache this as a complete list
 */
@Service
@Transactional
public class CongregationApplicationService {
    private final CongregationRepository congregationRepository;

    public CongregationApplicationService(CongregationRepository congregationRepository) {
        this.congregationRepository = congregationRepository;
    }

    @Transactional(readOnly = true)
    public String findName(Integer congregationId) {
        List<CongregationEntity> congregations = congregationRepository.findAllCongregations();

        CongregationEntity entity = congregations.stream()
                .filter(c -> c.getCongregationId().equals(congregationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Congregation#" + congregationId));
        return entity.getName();
    }


}
