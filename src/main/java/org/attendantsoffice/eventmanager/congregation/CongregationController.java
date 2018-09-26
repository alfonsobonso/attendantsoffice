package org.attendantsoffice.eventmanager.congregation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.attendantsoffice.eventmanager.common.list.EntityListOutput;
import org.attendantsoffice.eventmanager.common.list.ImmutableEntityListOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CRUD controls around the {@code CongregationEntity}.
 */
@RestController
public class CongregationController {
    private final CongregationApplicationService congregationApplicationService;

    public CongregationController(CongregationApplicationService congregationApplicationService) {
        this.congregationApplicationService = congregationApplicationService;
    }

    /**
     * Fetch the basic name value pair for the congregations, used in typeaheads
     */
    @GetMapping(path = "/congregations/list")
    public List<EntityListOutput> fetchList() {
        List<EntityListOutput> entityList = congregationApplicationService.findAll().stream()
                .map(c -> ImmutableEntityListOutput.of(c.getCongregationId(), c.getName()))
                .sorted(Comparator.comparing(EntityListOutput::getName))
                .collect(Collectors.toList());
        return entityList;
    }

}
