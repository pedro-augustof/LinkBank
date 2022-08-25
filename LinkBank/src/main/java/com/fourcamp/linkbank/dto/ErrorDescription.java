package com.fourcamp.linkbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDescription {

    private Integer errorCode;

    private String errorDescription;

    private Date hour;
}
