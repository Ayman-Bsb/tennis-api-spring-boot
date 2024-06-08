package com.ayman.tennis.data;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class PlayerEntityList {

    public static PlayerEntity RAFAEL_NADAL = new PlayerEntity(
            "Nadal",
            "Rafael",
            LocalDate.of(1986, Month.JUNE, 3),
            1,
            5000
    );

    public static PlayerEntity NOVAK_DJOKOVIC = new PlayerEntity(
            "Djokovic",
            "Novak",
            LocalDate.of(1987, Month.MAY, 22),
            2,
            4000
    );

    public static PlayerEntity ROGER_FEDERER = new PlayerEntity(
            "Federer",
            "Roger",
            LocalDate.of(1981, Month.AUGUST, 8),
            3,
            3000
    );

    public static PlayerEntity ANDY_MURRAY = new PlayerEntity(
            "Murray",
            "Andy",
            LocalDate.of(1987, Month.MAY, 15),
            4,
            2000
    );

    public static List<PlayerEntity> ALL = Arrays.asList(ROGER_FEDERER, ANDY_MURRAY, NOVAK_DJOKOVIC, RAFAEL_NADAL);
}