package github.dmitmel.raketaframework.util;

public class ArrayUtils {
    public static <T> T getAtIndexOrElse(T[] array, int index, T orElse) {
        return (array.length > index) ? array[index] : orElse;
    }
}
