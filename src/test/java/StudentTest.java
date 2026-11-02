import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {

    private Student s1;
    private Student s2;
    private Student s3;
    private Student s4;
    private List<Student> all;

    @BeforeEach
    void setUp() {
        s1 = new Student(1, "Anna", 4.0);
        s2 = new Student(2, "Boris", 7.0);
        s3 = new Student(3, "Clara", 7.0);
        s4 = new Student(4, "Dmitry", 10.0);
        all = List.of(s1, s2, s3, s4);
    }

    @Test
    void createsValidStudent() {
        Student s = new Student(1, "Ivan", 7.5);
        assertEquals(1, s.getId());
        assertEquals("Ivan", s.getName());
        assertEquals(7.5, s.getGrade());
    }

    @Test
    void boundaryGradesAreAllowed() {
        assertEquals(0.0, new Student(1, "A", 0.0).getGrade());
        assertEquals(10.0, new Student(2, "B", 10.0).getGrade());
    }

    @Test
    void rejectsBadId() {
        assertThrows(IllegalArgumentException.class, () -> new Student(0, "Ivan", 5));
        assertThrows(IllegalArgumentException.class, () -> new Student(-1, "Ivan", 5));
    }

    @Test
    void rejectsBadName() {
        assertThrows(IllegalArgumentException.class, () -> new Student(1, null, 5));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "", 5));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "   ", 5));
    }

    @Test
    void rejectsBadGrade() {
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "Ivan", -0.1));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "Ivan", 10.1));
    }

    @Test
    void equalsAndHashCodeWork() {
        Student a = new Student(1, "Ivan", 5.0);
        Student b = new Student(1, "Ivan", 5.0);
        Student c = new Student(2, "Ivan", 5.0);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void hashMapUsesIdAsKey() {
        Map<Integer, Student> map = Student.createHashMap(all);
        assertEquals(4, map.size());
        assertEquals(s2, map.get(2));
    }

    @Test
    void treeMapIsSortedDescending() {
        NavigableMap<Integer, Student> map = Student.createDescendingTreeMap(all);
        assertEquals(4, map.firstKey());
        assertEquals(1, map.lastKey());
    }


    @Test
    void rangeIncludesBothBorders() {
        Map<Integer, Student> map = Student.createHashMap(all);
        List<Student> result = Student.findStudentsByGradeRange(map, 4.0, 7.0);
        assertEquals(Set.of(s1, s2, s3), new HashSet<>(result));
    }
}