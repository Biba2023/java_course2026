import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;


public final class Student {

    private final int id;
    private final String name;
    private final double grade;


    public Student(int id, String name, double grade) {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (grade < 0.0 || grade > 10.0) {
            throw new IllegalArgumentException("Оценка должна быть от 0 до 10");
        }
        this.id = id;
        this.name = name;
        this.grade = grade;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public double getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student other = (Student) o;
        return id == other.id
                && Double.compare(grade, other.grade) == 0
                && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, grade);
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name + ", grade=" + grade + "}";
    }


    public static Map<Integer, Student> createHashMap(List<Student> students) {
        Map<Integer, Student> map = new HashMap<>();
        for (Student student : students) {
            map.put(student.getId(), student);
        }
        return map;
    }


    public static NavigableMap<Integer, Student> createDescendingTreeMap(List<Student> students) {
        NavigableMap<Integer, Student> map = new TreeMap<>(Collections.reverseOrder());
        for (Student student : students) {
            map.put(student.getId(), student);
        }
        return map;
    }


    public static List<Student> findStudentsByGradeRange(
            Map<Integer, Student> students,
            double minGrade,
            double maxGrade) {
        if (minGrade > maxGrade) {
            throw new IllegalArgumentException("minGrade больше maxGrade");
        }
        if (minGrade < 0.0 || maxGrade > 10.0) {
            throw new IllegalArgumentException("Границы должны быть в диапазоне от 0 до 10");
        }
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
                result.add(student);
            }
        }
        return result;
    }


    public static List<Student> getTopNStudentsById(
            NavigableMap<Integer, Student> students,
            int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n не может быть отрицательным");
        }
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (result.size() == n) {
                break;
            }
            result.add(student);
        }
        return result;
    }
}