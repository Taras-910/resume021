package ua.training.top.util;

public class InformUtil {
    public static final String
            all = "all",
            email_error = "[email] must be in the format of an email address",
            update_error_and_redirect = "update error {} and redirect to save",
            url_error = "[url] must be in URL or domain name format",
            check_error_data = "Error null data of resume {}",
            delay = "There is set delay within {} minutes",
            setting_delay = "-".repeat(8)+" delay={} min {} sec"+"-".repeat(8),
            not_own_data = "It's not allowed to change other user's data",
            user_exist = "User already exist with this e-mail ",
            no_authorized_user_found = "No authorized user found",
            not_find_driver = "Could not find DB driver",
            error_request_url = "{} at request {} {}",
            not_found = "Not found entity with {}",
            number_inform = "\n{} list.size={}\n",
            must_not_null = " must not be null ",
            must_has_id = "Entity must has id",
            email_matcher = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            resume_exist_already = "Your resume already exists, you could update it",
            invite_sign_in = "?message=You are registered already. Please Sign in&username=",
            exist_end_replace = "Resume {} already existed in the database but was replaced by {}",
            url_matcher = "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]" +
                    "\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|" +
                    "(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\" +
                    "u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$";
}
