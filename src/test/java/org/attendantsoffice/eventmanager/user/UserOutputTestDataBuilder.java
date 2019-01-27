package org.attendantsoffice.eventmanager.user;

import org.attendantsoffice.eventmanager.common.list.ImmutableEntityListOutput;

public final class UserOutputTestDataBuilder {

    public static UserOutput createUser(int userId) {
        UserOutput output = ImmutableUserOutput.builder()
                .userId(userId)
                .firstName("first")
                .lastName("last")
                .email("first.last@example.com")
                .congregation(ImmutableEntityListOutput.builder().id(100).name("cong100").build())
                .userStatus(UserStatus.UNAVAILABLE)
                .position(UserPosition.BAPTISEDSIS)
                .role(UserRole.USER)
                .build();
        return output;
    }

}
