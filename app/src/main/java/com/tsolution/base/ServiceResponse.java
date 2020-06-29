package com.tsolution.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse<T> {
    @SerializedName("content")
    private List<T> arrData;

    @SerializedName("totalPages")
    private Integer totalPages;
}
