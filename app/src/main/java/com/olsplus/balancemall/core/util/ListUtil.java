package com.olsplus.balancemall.core.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ListUtil {

    public interface GroupBy<T> {
        T groupby(Object obj) ;
    }

    public static final <T extends Comparable<T> ,D> Map<T ,List<D>> group(Collection<D> colls , GroupBy<T> gb){
        if(colls == null || colls.isEmpty()) {
            System.out.println("分組集合不能為空!");
            return null ;
        }
        if(gb == null) {
            System.out.println("分組依據接口不能為Null!");
            return null ;
        }
        Iterator<D> iter = colls.iterator() ;
        Map<T ,List<D>> map = new HashMap<T, List<D>>() ;
        while(iter.hasNext()) {
            D d = iter.next() ;
            T t = gb.groupby(d) ;
            if(map.containsKey(t)) {
                map.get(t).add(d) ;
            } else {
                List<D> list = new ArrayList<D>() ;
                list.add(d) ;
                map.put(t, list) ;
            }
        }

        if(map!=null) {
            Map<T, List<D>> sortMap = new TreeMap<T, List<D>>(
                    new Comparator<T>() {
                        @Override
                        public int compare(T lhs, T rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });
            sortMap.putAll(map);
            return sortMap ;
        }
       return null;
    }

}
