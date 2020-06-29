package com.tsolution.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelRespone<T> {
    private List<T> lstContent;
    private Object pageable;
    private Boolean last;
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Object sort;
    private Boolean first;
    private Boolean empty;
    private Integer numberOfElements;
}
