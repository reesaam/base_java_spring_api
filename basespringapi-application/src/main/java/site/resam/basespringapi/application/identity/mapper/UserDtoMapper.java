package site.resam.basespringapi.application.identity.mapper;

import site.resam.basespringapi.application.identity.dto.UserResponse;
import site.resam.basespringapi.domain.identity.User;

public final class UserDtoMapper {

    private UserDtoMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getEmail(),
                user.getDisplayName(),
                user.getStatus(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString(),
                user.getVersion()
        );
    }
}
