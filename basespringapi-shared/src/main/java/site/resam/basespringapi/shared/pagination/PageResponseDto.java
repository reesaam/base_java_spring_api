package site.resam.basespringapi.shared.pagination;

import java.util.List;

/**
 * Strongly typed page wrapper for application / REST layers.
 * gRPC uses concrete list response messages generated per resource.
 */
public record PageResponseDto<T>(
        List<T> items,
        PageMetaDto meta
) {
    public PageResponseDto {
        items = items == null ? List.of() : List.copyOf(items);
    }
}
