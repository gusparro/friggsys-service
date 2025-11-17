package com.gusparro.friggsys.domain.repositories.pagination;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageParameters {

    private int page;

    private int size;

    private String orderBy;

    private PageOrder direction;

}
