package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface Clock {

   LocalDate currentDate();

   LocalDateTime currentDateTime();

   LocalTime currentTime();
}
