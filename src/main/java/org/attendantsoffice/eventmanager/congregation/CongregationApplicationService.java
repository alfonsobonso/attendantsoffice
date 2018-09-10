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
