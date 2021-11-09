package ua.training.top.util.parser.data;

import java.util.List;

import static ua.training.top.util.parser.data.CommonDataUtil.*;

public class AddressUtil {

    public static String getToAddressWork(String address) {
        return address.contains("·") ? address.substring(address.lastIndexOf("·") + 1).trim() : address;
    }

    public static String getToAddress(String text) {
        if(isEmpty(text)) {
            return link;
        }
        List<String> list = getMatcherGroups(text, extract_address);
        return list.size() > 0 ? list.get(0).trim() : link;
    }
}
