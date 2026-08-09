package site.resam.basespringapi.application.identity.dto;

import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;
import site.resam.basespringapi.shared.pagination.PageRequestDto;

@ProtoContract(name = "ListUsersRequest")
public record ListUsersRequest(
        @ProtoField(number = 1)
        PageRequestDto page
) {
}
