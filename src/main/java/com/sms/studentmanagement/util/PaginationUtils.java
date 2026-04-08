package com.sms.studentmanagement.util;

import com.sms.studentmanagement.constants.AppConstants;
import com.sms.studentmanagement.dto.PagedResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility helpers for pagination and sorting.
 */
@UtilityClass
public class PaginationUtils {

    public static Pageable buildPageable(int page, int size, String sortBy, String sortDir) {
        int safeSize = Math.min(size, AppConstants.MAX_PAGE_SIZE);
        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(page, safeSize, sort);
    }

    public static Pageable buildPageable(int page, int size) {
        return buildPageable(page, size, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIR);
    }

    public static <E, D> PagedResponse<D> toPagedResponse(Page<E> page, Function<E, D> mapper) {
        List<D> content = page.getContent().stream().map(mapper).collect(Collectors.toList());
        return PagedResponse.<D>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
