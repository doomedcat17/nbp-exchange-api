package com.doomedcat17.nbpexchangeapi.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageDto<T> {

    private long page;
    private long totalPages;
    private List<T> results;
}
