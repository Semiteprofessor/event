package com.event.events.model.embeded;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class InstallmentConfig {

    private Integer numberOfInstallments = 1;
    private Double minPerInstallment = 0.0;
    private List<Date> dueDates;
}
