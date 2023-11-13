package monologue.util;

public class TypeCoercion {
    public static long[] toLongArray(int[] arr) {
        long[] newArr = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = (long) arr[i];
        }
        return newArr;
    }
}
