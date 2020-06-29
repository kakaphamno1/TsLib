package com.tsolution.base;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pageable<T> {
    private List<T> content;
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
    public String getTotalPages(){
        return totalPages == null ? "" : totalPages.toString();
    }
}
