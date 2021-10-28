package ua.training.top.util;

public class MessagesUtil {
    public static final String not_found = "Not found entity with {}";
    public static final String error_request_url = "{} at request {} {}";
    public static final String url_matcher = "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$";
    public static final String url_error = "[url] must be in URL or domain name format";
    public static final String user_exist_error = "User already exist with this e-mail ";
    public static final String invite_sign_in = "?message=You are registered already. Please Sign in&username=";
    public static final String email_matcher ="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public static final String email_error = "[email] must be in the format of an email address";
    public static final String must_not_null = " must not be null ";
    public static final String resume_exist_already = "Your resume already exists, you can update it";
    public static final String not_own_data = "it is not allowed to change other user's data";
    public static final String check_error_data = "Error null data of resume {}";


}
