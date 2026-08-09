package site.resam.basespringapi.api.grpc.mapper;

import java.util.List;
import site.resam.basespringapi.application.identity.dto.CreateUserRequest;
import site.resam.basespringapi.application.identity.dto.GetUserRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersResponse;
import site.resam.basespringapi.application.identity.dto.UserResponse;
import site.resam.basespringapi.domain.identity.UserStatus;
import site.resam.basespringapi.grpc.v1.PageMeta;
import site.resam.basespringapi.grpc.v1.PageRequest;
import site.resam.basespringapi.shared.pagination.PageMetaDto;
import site.resam.basespringapi.shared.pagination.PageRequestDto;

/**
 * Maps between application DTOs and protobuf-generated messages.
 */
public final class UserGrpcMapper {

    private UserGrpcMapper() {
    }

    public static CreateUserRequest toDto(site.resam.basespringapi.grpc.v1.CreateUserRequest proto) {
        return new CreateUserRequest(proto.getEmail(), proto.getDisplayName());
    }

    public static GetUserRequest toDto(site.resam.basespringapi.grpc.v1.GetUserRequest proto) {
        return new GetUserRequest(proto.getId());
    }

    public static ListUsersRequest toDto(site.resam.basespringapi.grpc.v1.ListUsersRequest proto) {
        PageRequestDto page = proto.hasPage()
                ? new PageRequestDto(proto.getPage().getPage(), proto.getPage().getSize(), proto.getPage().getSort())
                : PageRequestDto.of(0, 20);
        return new ListUsersRequest(page);
    }

    public static site.resam.basespringapi.grpc.v1.UserResponse toProto(UserResponse dto) {
        return site.resam.basespringapi.grpc.v1.UserResponse.newBuilder()
                .setId(nullToEmpty(dto.id()))
                .setEmail(nullToEmpty(dto.email()))
                .setDisplayName(nullToEmpty(dto.displayName()))
                .setStatus(toProtoStatus(dto.status()))
                .setCreatedAt(nullToEmpty(dto.createdAt()))
                .setUpdatedAt(nullToEmpty(dto.updatedAt()))
                .setVersion(dto.version())
                .build();
    }

    public static site.resam.basespringapi.grpc.v1.ListUsersResponse toProto(ListUsersResponse dto) {
        List<site.resam.basespringapi.grpc.v1.UserResponse> items = dto.items().stream()
                .map(UserGrpcMapper::toProto)
                .toList();
        return site.resam.basespringapi.grpc.v1.ListUsersResponse.newBuilder()
                .addAllItems(items)
                .setMeta(toProtoMeta(dto.meta()))
                .build();
    }

    public static PageRequest toProto(PageRequestDto dto) {
        return PageRequest.newBuilder()
                .setPage(dto.page())
                .setSize(dto.size())
                .setSort(nullToEmpty(dto.sort()))
                .build();
    }

    private static PageMeta toProtoMeta(PageMetaDto meta) {
        return PageMeta.newBuilder()
                .setPage(meta.page())
                .setSize(meta.size())
                .setTotalElements(meta.totalElements())
                .setTotalPages(meta.totalPages())
                .build();
    }

    private static site.resam.basespringapi.grpc.v1.UserStatus toProtoStatus(UserStatus status) {
        if (status == null) {
            return site.resam.basespringapi.grpc.v1.UserStatus.USER_STATUS_UNSPECIFIED;
        }
        return switch (status) {
            case ACTIVE -> site.resam.basespringapi.grpc.v1.UserStatus.ACTIVE;
            case INACTIVE -> site.resam.basespringapi.grpc.v1.UserStatus.INACTIVE;
            case LOCKED -> site.resam.basespringapi.grpc.v1.UserStatus.LOCKED;
        };
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
