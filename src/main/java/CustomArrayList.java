import java.util.Iterator;
import java.util.NoSuchElementException;


public class CustomArrayList<E> implements CustomList<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;


    public CustomArrayList() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public CustomArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Ёмкость должна быть больше 0");
        }
        data = new Object[capacity];
        size = 0;
    }

    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("Нельзя добавлять null");
        }
        if (size == data.length) {
            grow();
        }
        data[size] = element;
        size++;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) data[index];
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E removed = (E) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public E next() {
                if (current >= size) {
                    throw new NoSuchElementException("Элементов больше нет");
                }
                E result = (E) data[current];
                current++;
                return result;
            }
        };
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void grow() {
        int newCapacity = data.length + data.length / 2;
        if (newCapacity == data.length) {
            newCapacity = data.length + 1;
        }
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }
}
