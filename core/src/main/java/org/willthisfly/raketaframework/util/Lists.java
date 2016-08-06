package org.willthisfly.raketaframework.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    private Lists() {
        throw new RuntimeException("Can\'t create instance of Lists");
    }
    
    
    public static <T> int indexOf(List<T> list, List<T> target) {
        if (target.size() == 0) {
            return 0;
        }
        
        outer:
        for (int startIndex = 0; startIndex < list.size() - target.size() + 1; startIndex++) {
            for (int i = 0; i < target.size(); i++) {
                if (list.get(startIndex + i) != target.get(i)) {
                    continue outer;
                }
            }
            
            return startIndex;
        }
        
        return -1;
    }
    
    public static <T> List<T> fromIterable(Iterable<T> iterable) {
        ArrayList<T> list = new ArrayList<>(0);
        iterable.forEach(list::add);
        return list;
    }
}
