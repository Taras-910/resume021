package ua.training.top.util.parser.data;

import ua.training.top.model.Rate;

import java.util.Map;

import static java.time.LocalDate.of;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class RateUtil {
    public static final int RATE1_ID = START_SEQ + 10;
    public final static Map<String, Rate> mapRatesTest = Map.of(
            "USDUSD", new Rate(RATE1_ID,  "USDUSD", 1.0, of(2020,10,25)),
            "USDUAH", new Rate(RATE1_ID,  "USDUAH", 36.53, of(2020,10,25)),
            "USDPLN", new Rate(RATE1_ID,  "USDPLN", 4.8544, of(2020,10,25)),
            "USDKZT", new Rate(RATE1_ID,  "USDKZT", 469.5, of(2020,10,25)),
            "USDGBP", new Rate(RATE1_ID,  "USDGBP", 0.87148, of(2020,10,25)),
            "USDEUR", new Rate(RATE1_ID,  "USDEUR", 1.00711, of(2020,10,25)),
            "USDCZK", new Rate(RATE1_ID,  "USDCZK", 24.7275, of(2020,10,25)),
            "USDCAD", new Rate(RATE1_ID,  "USDCAD", 1.35791, of(2020,10,25)),
            "USDBYR", new Rate(RATE1_ID,  "USDBYR", 2.52, of(2020,10,25)),
            "USDBGN", new Rate(RATE1_ID,  "USDBGN", 1.9701, of(2020,10,25))
    );
}
