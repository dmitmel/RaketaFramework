package github.dmitmel.raketaframework.util;

import java.util.ArrayList;
import java.util.List;

public class Arrays {
    private Arrays() {
        throw new RuntimeException("Can\'t create instance of Arrays");
    }
    
    
    public static Boolean[] boxArray(boolean[] array) {
        Boolean[] boxed = new Boolean[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Byte[] boxArray(byte[] array) {
        Byte[] boxed = new Byte[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Character[] boxArray(char[] array) {
        Character[] boxed = new Character[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Short[] boxArray(short[] array) {
        Short[] boxed = new Short[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Integer[] boxArray(int[] array) {
        Integer[] boxed = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Long[] boxArray(long[] array) {
        Long[] boxed = new Long[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Float[] boxArray(float[] array) {
        Float[] boxed = new Float[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    public static Double[] boxArray(double[] array) {
        Double[] boxed = new Double[array.length];
        for (int i = 0; i < array.length; i++)
            boxed[i] = array[i];
        return boxed;
    }
    
    
    public static boolean[] unboxArray(Boolean[] array) {
        boolean[] unboxed = new boolean[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static byte[] unboxArray(Byte[] array) {
        byte[] unboxed = new byte[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static char[] unboxArray(Character[] array) {
        char[] unboxed = new char[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static short[] unboxArray(Short[] array) {
        short[] unboxed = new short[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static int[] unboxArray(Integer[] array) {
        int[] unboxed = new int[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static long[] unboxArray(Long[] array) {
        long[] unboxed = new long[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static float[] unboxArray(Float[] array) {
        float[] unboxed = new float[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    public static double[] unboxArray(Double[] array) {
        double[] unboxed = new double[array.length];
        for (int i = 0; i < array.length; i++)
            unboxed[i] = array[i];
        return unboxed;
    }
    
    
    public static byte[][] splitArray(byte[] array, byte[] delimiter) {
        Byte[] boxedArray = boxArray(array);
        List<Byte> workingPart = java.util.Arrays.asList(boxedArray);
        
        Byte[] boxedDelimiter = boxArray(delimiter);
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
            result.add(unboxArray(boxedHead));
            
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
}
