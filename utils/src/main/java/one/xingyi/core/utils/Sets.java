package one.xingyi.core.utils;
import java.util.*;
public class Sets {


    public static <T> Set<T> appendKeepingOrder(Set<T>... lists) {
        Set<T> result = new LinkedHashSet<>();
        for (Set<T> list : lists)
            result.addAll(list);
        return result;
    }

    public static <T>List<T> toList(Set<T> set){
        List<T> result = new ArrayList<>();
        result.addAll(set);
        return result;

    }

    public static String sortedString(Set<String> names, String separator) {
        List<String> result11 = new ArrayList<>();
        result11.addAll(names);
        result11.sort((a, b) -> a.compareTo(b));
        List<String> result = result11;
        return Lists.join(result, separator);
    }
    public static <T> List<T> sortedList(Set<T> ts, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        result.addAll(ts);
        result.sort(comparator);
        return result;
    }
}
