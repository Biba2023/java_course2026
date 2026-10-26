public class ArrayUtils {

    public static <T> int findFirst(T[] array, T element) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            final T current = array[i];
            if (element == null) {
                if (current == null) {
                    return i;
                }
            } else if (element.equals(current)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        final String[] names = {"Alice", "Bob", "Charlie"};
        final int index = ArrayUtils.findFirst(names, "Bob"); // Ожидаем: 1

    }
}
