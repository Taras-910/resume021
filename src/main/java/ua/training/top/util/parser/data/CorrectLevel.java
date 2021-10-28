package ua.training.top.util.parser.data;

import static ua.training.top.util.parser.data.CommonUtil.*;

public class CorrectLevel {
    public static String getLevel(String site, String level) {
        return switch (level) {
            case "trainee" -> switch (site) {
                case grc -> "&employment=probation";
                case habr -> "&qid=1";
                case linkedin -> "1";
                case djinni -> "&exp_years=0y";
                case rabota -> "%5B%220%22%5D";
                case work -> "0";
                default -> "";
            };
            case "junior" -> switch (site) {
                case grc -> "&experience=noExperience";
                case habr -> "&qid=3";
                case djinni -> "&exp_years=1y";
                case linkedin -> "2";
                case rabota -> "%5B%221%22%5D";
                case work -> "1";
                case yandex -> "&experience=NO_EXPERIENCE";
                default -> "";
            };
            case "middle" -> switch (site) {
                case grc -> "&experience=between1And3";
                case habr -> "&qid=4";
                case djinni -> "&exp_years=2y";
                case linkedin -> "3";
                case rabota -> "%5B%222%22%2C%223%22%2C%224%22%2C%225%22%5D";
                case work -> "164+165+166";
                case yandex -> "&experience=FROM_1_TO_2";
                default -> "";
            };
            case "senior" -> switch (site) {
                case grc -> "&experience=between3And6";
                case habr -> "&qid=5";
                case djinni -> "&exp_years=3y";
                case linkedin -> "4";
                case rabota -> "%5B%223%22%5D";
                case work -> "165";
                case yandex -> "experience=FROM_3_TO_5";
                default -> "";
            };
            case "expert" -> switch (site) {
                case grc -> "&experience=experience=moreThan6";
                case habr -> "&qid=6";
                case djinni -> "&exp_years=5y";
                case linkedin -> "5";
                case rabota -> "%5B%224%22%2C%225%22%5D";
                case work -> "166";
                case yandex -> "&experience=FROM_6";
                default -> "";
            };
            default -> switch (site) {
                case grc -> "";
                case habr -> "";
                case djinni -> "";
                case linkedin -> "3";
                case rabota -> "";
                case work -> "0+1+164+165+166";
                case yandex -> "&experience=FROM_1_TO_2";
                default -> "";
            };
        };
    }
}

