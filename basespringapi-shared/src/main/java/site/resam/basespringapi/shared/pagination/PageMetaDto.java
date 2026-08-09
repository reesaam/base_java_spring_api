package site.resam.basespringapi.shared.pagination;

import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;

@ProtoContract(name = "PageMeta")
public record PageMetaDto(
        @ProtoField(number = 1) int page,
        @ProtoField(number = 2) int size,
        @ProtoField(number = 3) long totalElements,
        @ProtoField(number = 4) int totalPages
) {
    public static PageMetaDto of(int page, int size, long totalElements) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / (double) size);
        return new PageMetaDto(page, size, totalElements, totalPages);
    }
}
