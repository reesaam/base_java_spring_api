package site.resam.basespringapi.application.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;

@ProtoContract(name = "CreateUserRequest")
public record CreateUserRequest(
        @ProtoField(number = 1)
        @NotBlank
        @Email
        @Size(max = 320)
        String email,

        @ProtoField(number = 2)
        @NotBlank
        @Size(max = 200)
        String displayName
) {
}
