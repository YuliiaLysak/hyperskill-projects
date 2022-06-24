package edu.lysak.tracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private final StudentService studentService = new StudentService();

    @Test
    @DisplayName("#getStudentsCount should return 0 if there is no students")
    void getStudentsCount() {
        assertEquals(0, studentService.getStudentsCount());
    }

    @Test
    @DisplayName("#getStudentsCount should return more than 0 if there is students")
    void getStudentsCount_shouldReturnMoreThanZero() {
        studentService.addStudent(List.of("John", "Doe", "email@email.com"));
        assertEquals(1, studentService.getStudentsCount());
    }

    @ParameterizedTest
    @DisplayName("#addStudent(String[]) should add student with valid credentials")
    @MethodSource("getValidStudentCredentials")
    void addStudent(List<String> credentials) {
        assertTrue(studentService.addStudent(credentials));
        assertEquals(1, studentService.getStudentsCount());
        Student student = studentService.getStudents().get(0);
        assertEquals(credentials.get(0), student.getFirstName());
        assertEquals(String.join(" ", credentials.subList(1, credentials.size() - 1)), student.getLastName());
        assertEquals(credentials.get(credentials.size() - 1), student.getEmail());
    }

    @ParameterizedTest
    @DisplayName("#addStudent(String[]) should not add student with invalid credentials")
    @MethodSource("getInvalidStudentCredentials")
    void addStudent_shouldNotAddStudentWithInvalidCredentials(List<String> invalidCredentials) {
        assertFalse(studentService.addStudent(invalidCredentials));
        assertEquals(0, studentService.getStudentsCount());
    }

    static List<Arguments> getValidStudentCredentials() {
        return List.of(
                Arguments.arguments(List.of("John", "Doe", "email@email.com")),
                Arguments.arguments(List.of("Jean-Claude", "Doe", "email@email.com")),
                Arguments.arguments(List.of("O'Neill", "Doe", "email@email.com")),
                Arguments.arguments(List.of("s-u", "Doe", "email@email.com")),
                Arguments.arguments(List.of("na'me", "Doe", "jcda123@google.net")),
                Arguments.arguments(List.of("aa-b'b", "Doe", "anny.md@mail.edu")),
                Arguments.arguments(List.of("Robert", "Jemison", "Van", "de", "Graaff", "email@email.com")),
                Arguments.arguments(List.of("John", "Ronald", "Reuel", "Tolkien", "email@email.com"))
        );
    }

    static List<Arguments> getInvalidStudentCredentials() {
        return List.of(
                Arguments.arguments(List.of("Stanisław", "Doe", "email@email.com")),
                Arguments.arguments(List.of("J---f", "Doe", "email@email.com")),
                Arguments.arguments(List.of("J'-f", "Doe", "email@email.com")),
                Arguments.arguments(List.of("Jean-Claude", "Oğuz", "email@email.com")),
                Arguments.arguments(List.of("J.", "Oğuz", "email@email.com")),
                Arguments.arguments(List.of("Jane", "Doe", "email@e@mail.xyz")),
                Arguments.arguments(List.of("陳", "港", "生")),
                Arguments.arguments(List.of("Stanislaw", "Doe", "email"))

        );
    }
}