package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class StudentSetTest {

    private StudentSet studentSet;
    private StudentSet studentSet_usingComparator;
    private Comparator<Student> byAge = Comparator
            .comparing(Student::getDateOfBirth);
    private Set<Student> treeSet;
    private Set<Student> treeSet_usingComparator;

    private static final Student student0 = new Student("student", LocalDate.parse("2000-10-05"), "none");
    private static final Student student1 = new Student("student1", LocalDate.parse("2003-04-01"), "none");
    private static final Student student2 = new Student("student2", LocalDate.parse("1999-05-11"), "none");
    private static final Student student3 = new Student("student3", LocalDate.parse("2444-05-07"), "none");
    private static final Student student4 = new Student("student4", LocalDate.parse("1997-05-10"), "none");

    @BeforeEach
    void setUp() {
        studentSet = new StudentSet();
        studentSet_usingComparator = new StudentSet(byAge);
        treeSet = new TreeSet<>();
        treeSet_usingComparator = new TreeSet<>(byAge);
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    public void contains(Student student) {
        studentSet.add(student);
        assertTrue(studentSet.contains(student));
        assertFalse(studentSet.contains(student4));
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    void add(Student student) {
        studentSet.add(student);
        assertAll(
                () -> assertThat(studentSet).contains(student),
                () -> assertThat(studentSet.iterator().next()).isEqualTo(student),
                () -> assertThat(studentSet.add(student)).isFalse(),
                () -> assertThat(studentSet.add(student3)).isTrue()
        );
    }
    @ParameterizedTest
    @MethodSource("studentProvider")
    void addUsingComparator(Student student){
        studentSet_usingComparator.add(student);
        assertAll(
                () -> assertThat( studentSet_usingComparator).contains(student),
                () -> assertThat( studentSet_usingComparator.iterator().next()).isEqualTo(student),
                () -> assertThat( studentSet_usingComparator.add(student)).isFalse(),
                () -> assertThat( studentSet_usingComparator.add(student3)).isTrue()
        );
    }


    @Test
    void iterator() {
        studentSet.add(student2);
        Iterator<Student> iterator1 = studentSet.iterator();

        assertAll(
                () -> assertThat(iterator1.next()).isEqualTo(student2),
                () -> assertThat(iterator1.hasNext()).isFalse()
        );
    }


    @Test
    void iteratorWithNoElements() {
        assertAll(
                () -> assertThat(studentSet.iterator().hasNext()).isFalse(),
                () -> assertThatThrownBy(() -> assertThat(studentSet.iterator().next()).isInstanceOf(IndexOutOfBoundsException.class))
        );
    }

    @Test
    void addTestUsingTreeSet() {
        treeSet.add(student2);
        treeSet.add(student1);
        treeSet.add(student0);
        studentSet.add(student2);
        studentSet.add(student1);
        studentSet.add(student0);

        assertAll(
                () -> assertThat(studentSet).containsExactlyElementsOf(treeSet),
                () -> assertThat(studentSet.add(student4)).isEqualTo(treeSet.add(student4)),
                () -> assertThat(studentSet.add(student0)).isEqualTo(treeSet.add(student0))
        );
    }

    @Test
    void addTestUsingTreeSet_usingComparator() {
        studentSet_usingComparator.add(student2);
        studentSet_usingComparator.add(student1);
        studentSet_usingComparator.add(student0);
        treeSet_usingComparator.add(student2);
        treeSet_usingComparator.add(student1);
        treeSet_usingComparator.add(student0);

        assertAll(
                () -> assertThat(studentSet_usingComparator).containsExactlyElementsOf(treeSet_usingComparator),
                () -> assertThat(studentSet_usingComparator.add(student4)).isEqualTo(treeSet_usingComparator.add(student4)),
                () -> assertThat(studentSet_usingComparator.add(student0)).isEqualTo(treeSet_usingComparator.add(student0))
        );
    }


    @Test
    void addAll() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentSet.addAll(studentList);
        assertAll(
                () -> assertThat(studentSet).containsExactlyElementsOf(studentList),
                () -> assertThat(studentSet).hasSize(studentList.size())
        );
    }


    @Test
    void remove() {
        studentSet.add(student2);
        studentSet.add(student1);
        studentSet.add(student4);
        studentSet.remove(student2);

        assertAll(
                () -> assertThat(studentSet).doesNotContain(student2),
                () -> assertThat(studentSet.remove(student2)).isFalse(),
                () -> assertThat(studentSet.remove(student4)).isTrue()
        );
    }
    @Test
    void remove_using_Comparator(){
        studentSet_usingComparator.add(student2);
        studentSet_usingComparator.add(student1);
        studentSet_usingComparator.add(student0);
        studentSet_usingComparator.add(student4);
        studentSet_usingComparator.remove(student1);
        assertAll(
                () -> assertThat(studentSet_usingComparator).doesNotContain(student1),
                () -> assertThat(studentSet_usingComparator.remove(student1)).isFalse(),
                () -> assertThat(studentSet_usingComparator.remove(student4)).isTrue()

        );
    }

    @Test
    void toArray() {
        studentSet.add(student1);
        studentSet.add(student2);
        studentSet.add(student3);
        Object[] students = studentSet.toArray();
        assertThat(students).containsExactlyElementsOf(studentSet);
    }

    @Test
    void clear() {
        studentSet.add(student1);
        studentSet.clear();
        assertThat(studentSet).hasSize(0);
    }

    @Test
    void isEmpty() {
        studentSet.add(student1);
        assertThat(studentSet.isEmpty()).isFalse();
        studentSet.clear();
        assertThat(studentSet.isEmpty()).isTrue();

    }

    @Test
    void size() {
        studentSet.add(student1);
        studentSet.add(student2);
        assertThat(studentSet.size()).isEqualTo(2);
        studentSet.remove(student1);
        assertThat(studentSet.size()).isEqualTo(1);
    }

    @Test
    void toArrayGeneric(){
        studentSet.add(student4);
        studentSet.add(student1);
        Student[] students = new Student[studentSet.size()];
        students = studentSet.toArray(students);
        assertThat(students).containsExactlyElementsOf(studentSet);
    }

    @Test
    void toStringTest(){
        studentSet.add(student1);
        studentSet.add(student2);
        treeSet.add(student1);
        treeSet.add(student2);
        assertThat(studentSet.toString())
                .isEqualTo(treeSet.toString());
    }

    static Stream<Arguments> studentProvider() {
        Student student0 = new Student("student", LocalDate.parse("2000-10-05"), "none");
        Student student1 = new Student("student1", LocalDate.parse("2003-04-01"), "none");
        Student student2 = new Student("student2", LocalDate.parse("1999-05-11"), "none");
        return Stream.of(Arguments.of(student0),
                Arguments.of(student1),
                Arguments.of(student2));
    }

}

