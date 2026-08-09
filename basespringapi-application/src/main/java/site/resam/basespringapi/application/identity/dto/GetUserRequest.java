package site.resam.basespringapi.application.identity.dto;

import jakarta.validation.constraints.NotBlank;
import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;

@ProtoContract(name = "GetUserRequest")
public record GetUserRequest(
        @ProtoField(number = 1)
        @NotBlank
        String id
) {
}
