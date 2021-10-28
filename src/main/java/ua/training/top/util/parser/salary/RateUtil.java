package ua.training.top.util.parser.salary;

import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.InstallationUtil.*;

public class RateUtil {

    public static String gbpToUsd(String gbp) throws NumberFormatException{
        return checkValue((int) (Integer.parseInt(gbp) * GBP_TO_USD_RATE) * 100);
    }

    public static String eurToUsd(String eur) throws NumberFormatException{
        return checkValue((int) (Integer.parseInt(eur) * EUR_TO_USD_RATE) * 100);
    }

    public static String hrnToUsd(String hrn) throws NumberFormatException{
        return checkValue((int) (Integer.parseInt(hrn) / HRN_TO_USD_RATE) * 100);
    }

    public static String plnToUsd(String pln) throws NumberFormatException{
        return checkValue((int) ((Float.parseFloat(pln) * 100) / PLN_TO_USD_RATE));
    }

    public static String rubToUsd(String rub) throws NumberFormatException{
        return checkValue((int) ((Float.parseFloat(rub) / RUB_TO_USD_RATE) * 100));
    }

    public static String checkValue(int value){
        return  value > 40000000 ? valueOf((int) (value / 100)) : valueOf(value);
    }
}
