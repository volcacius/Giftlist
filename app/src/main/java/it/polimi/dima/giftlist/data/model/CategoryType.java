package it.polimi.dima.giftlist.data.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Elena on 26/08/2016.
 */
public enum CategoryType {

    NERD("nerd"),
    TRAVEL("travel"),
    ART("art"),
    HANDCRAFT("handcraft"),
    TECH("tech"),
    SPORT("sport"),
    BABY("baby"),
    KID("kid"),
    ROMANTIC("anniversary","engagement"),
    WEDDING("wedding"),
    YOUNG("young"),
    OTHER("other");

    private final List<String> text;

    CategoryType(final String ...text) {
        this.text = Arrays.asList(text);
    }

    public List<String> getValues() {
        return text;
    }

    public static CategoryType getCategoryTypeFromString(String name) {
        for (CategoryType c : CategoryType.values()) {
            if (c.getValues().contains(name)) {
                return c;
            }
        }
        return OTHER;
    }

    @Override
    public String toString() {
        return text.get(0);
    }
}
