package org.willthisfly.raketaframework.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Arrays {
    private Arrays() {
        throw new UnsupportedOperationException("Can\'t create instance of Arrays");
    }
    
    
    public static Boolean[] box(boolean[] array) {
        Boolean[] boxed = new Boolean[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Byte[] box(byte[] array) {
        Byte[] boxed = new Byte[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Character[] box(char[] array) {
        Character[] boxed = new Character[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Short[] box(short[] array) {
        Short[] boxed = new Short[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Integer[] box(int[] array) {
        Integer[] boxed = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Long[] box(long[] array) {
        Long[] boxed = new Long[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Float[] box(float[] array) {
        Float[] boxed = new Float[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Double[] box(double[] array) {
        Double[] boxed = new Double[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    
    public static boolean[] unbox(Boolean[] array) {
        boolean[] unboxed = new boolean[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static byte[] unbox(Byte[] array) {
        byte[] unboxed = new byte[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static char[] unbox(Character[] array) {
        char[] unboxed = new char[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static short[] unbox(Short[] array) {
        short[] unboxed = new short[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static int[] unbox(Integer[] array) {
        int[] unboxed = new int[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static long[] unbox(Long[] array) {
        long[] unboxed = new long[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static float[] unbox(Float[] array) {
        float[] unboxed = new float[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static double[] unbox(Double[] array) {
        double[] unboxed = new double[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    
    public static byte[][] splitArray(byte[] array, byte[] delimiter) {
        Byte[] boxedArray = box(array);
        List<Byte> workingPart = java.util.Arrays.asList(boxedArray);
        
        Byte[] boxedDelimiter = box(delimiter);
        List<Byte> delimiterList = java.util.Arrays.asList(boxedDelimiter);
        
        ArrayList<byte[]> result = new ArrayList<>();
    
        int delimiterStartIndex;
        do {
            delimiterStartIndex = Lists.indexOf(workingPart, delimiterList);
            
            List<Byte> head = delimiterStartIndex >= 0 ?
                    workingPart.subList(0, delimiterStartIndex) :
                    // If there's no delimiter - using working part as
                    // last element of result. This will be last iteration
                    workingPart;
            Byte[] boxedHead = head.toArray(new Byte[0]);
            result.add(unbox(boxedHead));
            
            int delimiterEndIndex = delimiterStartIndex + delimiter.length;
            int workingPartSize = workingPart.size();
            
            // Do something if there's at least one-element list after head.
            // Can't be done it the last element is delimiter
            if (delimiterEndIndex < workingPartSize)
                workingPart = workingPart.subList(delimiterEndIndex, workingPart.size());
            else
                // If delimiter is the last element - it isn't be added, so no
                // 'else' condition
                break;
            
        } while (delimiterStartIndex >= 0);
    
        return result.toArray(new byte[result.size()][]);
    }
    
    
    public static <T> Iterator<T> toIterator(T[] array) {
        return new Iterator<T>() {
            private int cursor = 0;
            
            @Override
            public boolean hasNext() {
                return cursor < array.length;
            }
    
            @Override
            public T next() {
                if (hasNext()) {
                    T item = array[cursor];
                    cursor++;
                    return item;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
