package org.attendantsoffice.eventmanager.user;

public final class UserOutputTestDataBuilder {

    public static UserOutput createUser(int userId) {
        UserOutput output = ImmutableUserOutput.builder()
                .userId(userId)
                .firstName("first")
                .lastName("last")
                .email("first.last@example.com")
                .congregation(ImmutableUserCongregationOutput.builder().congregationId(100).name("cong100").build())
                .userStatus(UserStatus.DISABLED)
                .position(UserPosition.BAPTISEDSIS)
                .role(UserRole.USER)
                .build();
        return output;
    }

}
