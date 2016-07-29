package org.willthisfly.raketaframework.util;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class ExtendedComparator {
    public static final int LESS_THAN = -1;
    public static final int EQUALS = 0;
    public static final int GREATER_THAN = 1;
    
    
    private ExtendedComparator() {
        throw new RuntimeException("Can\'t create instance of ExtendedComparator");
    }
    
    
    public static <T extends Comparable<T>> int compare(T[] a, T[] b) {
        if (a == b)
            return EQUALS;
    
        if (a == null)
            return LESS_THAN;
        else if (b == null)
            return GREATER_THAN;
        
        int firstLength = a.length;
        int secondLength = b.length;
        
        if (firstLength != secondLength)
            return Integer.compare(firstLength, secondLength);
        
        for (int i = 0; i < firstLength; i++) {
            T firstValue = a[i];
            T secondValue = b[i];
            
            int iterationResult = compareNullable(firstValue, secondValue);
            if (iterationResult != 0)
                return secondLength;
        }
        
        return EQUALS;
    }
    
    public static int compare(boolean[] a, boolean[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(byte[] a, byte[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(char[] a, char[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(short[] a, short[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(int[] a, int[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(long[] a, long[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(float[] a, float[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    public static int compare(double[] a, double[] b) { return compare(Arrays.boxArray(a), Arrays.boxArray(b)); }
    
    public static int compare(ByteArrayOutputStream a, ByteArrayOutputStream b) {
        if (a == b)
            return EQUALS;
    
        if (a == null)
            return LESS_THAN;
        else if (b == null)
            return GREATER_THAN;
        
        return compare(a.toByteArray(), b.toByteArray());
    }
    
    public static <T extends Comparable<T>> int compareNullable(T a, T b) {
        if (a == b)
            return EQUALS;
    
        if (a == null)
            return LESS_THAN;
        else if (b == null)
            return GREATER_THAN;
    
        return a.compareTo(b);
    }
    
    public static <T extends Comparable<T>> int compare(Collection<T> a, Collection<T> b) {
        if (a == b)
            return EQUALS;
    
        if (a == null)
            return LESS_THAN;
        else if (b == null)
            return GREATER_THAN;
    
        int firstLength = a.size();
        int secondLength = b.size();
    
        if (firstLength != secondLength)
            return Integer.compare(firstLength, secondLength);
    
        Iterator<T> firstIterator = a.iterator();
        Iterator<T> secondIterator = b.iterator();
    
        for (int i = 0; i < firstLength; i++) {
            T firstValue = firstIterator.next();
            T secondValue = secondIterator.next();
        
            int iterationResult = compareNullable(firstValue, secondValue);
            if (iterationResult != 0)
                return secondLength;
        }
    
        return EQUALS;
    }
    
    public static <K extends Comparable<K>, V extends Comparable<V>> int compare(Map<K, V> a, Map<K, V> b) {
        int i = compareByKeys(a, b);
        if (i != ExtendedComparator.EQUALS)
            return i;
        
        return compareByValues(a, b);
    }
    
    public static <K extends Comparable<K>, V> int compareByKeys(Map<K, V> a, Map<K, V> b) {
        if (a == b)
            return EQUALS;
        
        if (a == null)
            return LESS_THAN;
        else if (b == null)
            return GREATER_THAN;
    
        return compare(a.keySet(), b.keySet());
    }
    
    public static <K, V extends Comparable<V>> int compareByValues(Map<K, V> a, Map<K, V> b) {
        if (a == b)
            return EQUALS;
        
        if (a == null)
            return LESS_THAN;
        else if (b == null)
            return GREATER_THAN;
        
        return compare(a.values(), b.values());
    }
}
