package org.attendantsoffice.eventmanager.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Map between the underlying UserEntity and the UserOutput instance
 *
 */
@Component
public class UserMapper {

    public UserOutput map(UserEntity entity) {
        // fake this data for now
        UserCongregationOutput congregationOutput = ImmutableUserCongregationOutput.builder()
                .congregationId(entity.getCongregationId())
                .name("Cong#" + entity.getCongregationId())
                .build();

        UserOutput output = ImmutableUserOutput.builder()
                .userId(entity.getUserId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .homePhone(Optional.ofNullable(entity.getHomePhone()))
                .mobilePhone(Optional.ofNullable(entity.getMobilePhone()))
                .congregation(congregationOutput)
                .build();
        return output;
    }

}
