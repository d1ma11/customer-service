package ru.mts.customerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionData {
    private String code;
    private String message;
}
