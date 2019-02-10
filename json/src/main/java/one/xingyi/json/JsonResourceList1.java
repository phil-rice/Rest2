package one.xingyi.json;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Resource;
import one.xingyi.core.client.IResourceList;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@RequiredArgsConstructor @EqualsAndHashCode @ToString
public class JsonResourceList1 implements IResourceList<Object> {
    final JSONArray array;
    @Override public int size() {
        return array.length();
    }
    @Override public Object get(int n) {
        return array.get(n);
    }

    JSONArray copy() {return new JSONArray(array.myArrayList);} //it is copied by the constructor

    @Override public IResourceList<Object> withItem(int n, Object o) {
        return new JsonResourceList1(copy().put(n, o));
    }
    @Override public IResourceList<Object> append(Object o) {
        JSONArray copy = copy();
        copy.myArrayList.add(o);
        return new JsonResourceList1(copy);
    }
    @Override public Iterator<Object> iterator() {
        return array.iterator();
    }
}
