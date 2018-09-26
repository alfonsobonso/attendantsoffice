package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.attendantsoffice.eventmanager.common.list.ImmutableEntityListOutput;
import org.attendantsoffice.eventmanager.congregation.CongregationApplicationService;
import org.springframework.stereotype.Component;

/**
 * Map between the underlying UserEntity and the UserOutput instance
 *
 */
@Component
public class UserMapper {
    private final CongregationApplicationService congregationApplicationService;

    public UserMapper(CongregationApplicationService congregationApplicationService) {
        this.congregationApplicationService = congregationApplicationService;
    }

    public UserOutput map(UserEntity entity) {
        // the congregation is lazy-loaded. We have cached the lookup of all congs already, so save a join here.
        ImmutableEntityListOutput congregationOutput = ImmutableEntityListOutput.of(
                entity.getCongregation().getCongregationId(),
                congregationApplicationService.findName(entity.getCongregation().getCongregationId()));

        UserOutput output = ImmutableUserOutput.builder()
                .userId(entity.getUserId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .homePhone(Optional.ofNullable(entity.getHomePhone()))
                .mobilePhone(Optional.ofNullable(entity.getMobilePhone()))
                .congregation(congregationOutput)
                .userStatus(entity.getUserStatus())
                .position(entity.getPosition())
                .role(entity.getRole())
                .build();

        return output;
    }

}
