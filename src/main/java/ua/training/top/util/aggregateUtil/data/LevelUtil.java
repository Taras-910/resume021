package ua.training.top.util.aggregateUtil.data;

import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;

public class LevelUtil {

    public static String getLevel(String site, String level) {
        return switch (level) {
            case trainee -> switch (site) {
                case djinni -> "exp_years=0y";
                case work -> "0";
                case recruit -> "&experience=0";
                default -> "";
            };
            case junior -> switch (site) {
                case djinni -> "exp_years=1y";
                case work -> "1";
                case recruit -> "experience=1";
                default -> "";
            };
            case middle -> switch (site) {
                case djinni -> "exp_years=2y";
                case work -> "164+165+166";
                case recruit -> "experience=1,4";
                default -> "";
            };
            case senior -> switch (site) {
                case djinni -> "exp_years=3y";
                case work -> "165";
                case recruit -> "experience=4";
                default -> "";
            };
            case expert -> switch (site) {
                case djinni -> "exp_years=5y";
                case work -> "166";
                case recruit -> "experience=7";
                default -> "";
            };
            default -> switch (site) {
                case work -> "0+1+164+165+166";
                default -> "";
            };
        };
    }
}
