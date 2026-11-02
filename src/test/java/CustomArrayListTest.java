import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {

    private CustomArrayList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomArrayList<>();
    }

    private void fill(int count) {
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
    }

    @Test
    void newListIsEmpty() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void addOneElement() {
        list.add(5);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(5, list.get(0));
    }

    @Test
    void addSeveralElementsKeepsOrder() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void addMoreThanTenElementsKeepsData() {
        fill(100);
        assertEquals(100, list.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void growWorksWithCapacityOne() {
        CustomArrayList<Integer> small = new CustomArrayList<>(1);
        for (int i = 0; i < 20; i++) {
            small.add(i);
        }
        assertEquals(20, small.size());
        assertEquals(19, small.get(19));
    }

    @Test
    void constructorRejectsBadCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CustomArrayList<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new CustomArrayList<Integer>(-5));
    }

    @Test
    void getFirstMiddleLast() {
        fill(5);
        assertEquals(0, list.get(0));
        assertEquals(2, list.get(2));
        assertEquals(4, list.get(4));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, 3, 4, 100})
    void getRejectsBadIndex(int index) {
        fill(3);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, 3, 4, 100})
    void removeRejectsBadIndex(int index) {
        fill(3);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
        assertEquals(3, list.size());
    }

    @Test
    void getAndRemoveOnEmptyListThrow() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }


    @ParameterizedTest
    @CsvSource({"0, 0", "2, 2", "4, 4"})
    void removeReturnsCorrectValueAndShiftsTail(int index, int expected) {
        fill(5);
        int removed = list.remove(index);
        assertEquals(expected, removed);
        assertEquals(4, list.size());

        int value = 0;
        for (int i = 0; i < list.size(); i++) {
            if (value == expected) {
                value++;
            }
            assertEquals(value, list.get(i));
            value++;
        }
    }

    @Test
    void removeAllElementsMakesListEmpty() {
        fill(5);
        for (int i = 0; i < 5; i++) {
            list.remove(0);
        }
        assertTrue(list.isEmpty());
        list.add(42);
        assertEquals(42, list.get(0));
    }

    @Test
    void addNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> list.add(null));
        assertEquals(0, list.size());
    }

    @Test
    void iteratorGoesInOrder() {
        fill(15);
        int expected = 0;
        for (int value : list) {
            assertEquals(expected, value);
            expected++;
        }
        assertEquals(15, expected);
    }

    @Test
    void iteratorOnEmptyListHasNoElements() {
        assertFalse(list.iterator().hasNext());
    }

    @Test
    void nextOnFinishedIteratorThrows() {
        fill(2);
        Iterator<Integer> it = list.iterator();
        it.next();
        it.next();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
}
