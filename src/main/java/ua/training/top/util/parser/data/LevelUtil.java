package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.ConstantsUtil.*;

public class LevelUtil {

    public static String getLevel(String site, String level) {
        return switch (level) {
            case trainee -> switch (site) {
                case djinni -> "exp_years=0y";
                case rabota -> "%5B%220%22%5D";
                case work -> "0";
                case recruit -> "&experience=0";
                default -> "";
            };
            case junior -> switch (site) {
                case djinni -> "exp_years=1y";
                case rabota -> "%5B%221%22%5D";
                case work -> "1";
                case recruit -> "experience=1";
                default -> "";
            };
            case middle -> switch (site) {
                case djinni -> "exp_years=2y";
                case rabota -> "%5B%222%22%2C%223%22%2C%224%22%2C%225%22%5D";
                case work -> "164+165+166";
                case recruit -> "experience=1,4";
                default -> "";
            };
            case senior -> switch (site) {
                case djinni -> "exp_years=3y";
                case rabota -> "%5B%223%22%5D";
                case work -> "165";
                case recruit -> "experience=4";
                default -> "";
            };
            case expert -> switch (site) {
                case djinni -> "exp_years=5y";
                case rabota -> "%5B%224%22%2C%225%22%5D";
                case work -> "166";
                case recruit -> "experience=7";
                default -> "";
            };
            default -> switch (site) {
                case rabota -> "%5B%222%22%2C%223%22%2C%224%22%2C%225%22%5D";
                case work -> "0+1+164+165+166";
                default -> "";
            };
        };
    }
}
