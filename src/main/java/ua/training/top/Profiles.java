package ua.training.top;

import static ua.training.top.util.InformUtil.not_find_driver;

public class Profiles {
    public static final String
            POSTGRES_DB = "postgres",
            HEROKU = "heroku";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        if (isClassExists("org.postgresql.Driver")) {
            return POSTGRES_DB;
        } else {
            throw new IllegalStateException(not_find_driver);
        }
    }

    private static boolean isClassExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
