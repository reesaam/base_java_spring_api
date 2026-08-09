package site.resam.basespringapi.application.identity.dto;

import java.util.List;
import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;
import site.resam.basespringapi.shared.pagination.PageMetaDto;

@ProtoContract(name = "ListUsersResponse")
public record ListUsersResponse(
        @ProtoField(number = 1) List<UserResponse> items,
        @ProtoField(number = 2) PageMetaDto meta
) {
}
