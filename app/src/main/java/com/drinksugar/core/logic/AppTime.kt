package com.drinksugar.core.logic

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AppTime {
    private val FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun format(dt: LocalDateTime): String = dt.format(FMT)
    fun now(): String = format(LocalDateTime.now())
    fun dayStart(day: LocalDate): String = format(day.atStartOfDay())
    fun dayEnd(day: LocalDate): String = format(day.plusDays(1).atStartOfDay())
}
