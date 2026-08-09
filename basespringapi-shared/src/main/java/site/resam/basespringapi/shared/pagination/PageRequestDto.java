package site.resam.basespringapi.shared.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;

/**
 * Generic page request shared by REST and gRPC.
 */
@ProtoContract(name = "PageRequest")
public record PageRequestDto(
        @ProtoField(number = 1) @Min(0) int page,
        @ProtoField(number = 2) @Min(1) @Max(200) int size,
        @ProtoField(number = 3) String sort
) {
    public PageRequestDto {
        if (size <= 0) {
            size = 20;
        }
        if (page < 0) {
            page = 0;
        }
    }

    public static PageRequestDto of(int page, int size) {
        return new PageRequestDto(page, size, null);
    }
}
