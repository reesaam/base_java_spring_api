package site.resam.basespringapi.domain.identity;

import site.resam.basespringapi.proto.annotation.ProtoEnum;

@ProtoEnum(name = "UserStatus")
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    LOCKED
}
