package com.gusparro.friggsys.domain.repositories.pagination;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainPage <T> {

    private List<T> data;

    private long dataAmount;

    private int pagesAmount;

    private int pageNumber;

    private int pageSize;

    private boolean firstPage;

    private boolean lastPage;

}
