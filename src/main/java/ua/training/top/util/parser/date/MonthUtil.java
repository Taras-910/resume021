package ua.training.top.util.parser.date;

public class MonthUtil {

    public static String getMonth(String month){
        return switch (month) {
            case "Feb", "лютого", "февраля" -> "02";
            case "Mar", "березня", "марта" -> "03";
            case "Apr", "квітня", "апреля" -> "04";
            case "May", "травня", "мая" -> "05";
            case "Jun", "червня", "июня" -> "06";
            case "Jul", "липня", "июля" -> "07";
            case "Aug", "серпня", "августа" -> "08";
            case "Sep", "вересня", "сентября" -> "09";
            case "Oct", "жовтня", "октября" -> "10";
            case "Nov", "листопада", "ноября" -> "11";
            case "Dec", "грудня", "декабря" -> "12";
            default -> "01";
        };
    }
}
