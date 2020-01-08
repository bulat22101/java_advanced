package students;

import java.util.*;
import java.util.stream.Collectors;

public class StudentDBEasy implements StudentQuery {
    public final static Comparator<Student> STUDENT_BY_NAME_COMPARATOR = Comparator.comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .thenComparing(Student::getId);
    public final static Comparator<Student> STUDENT_BY_ID_COMPARATOR = Comparator.comparing(Student::getId);


    @Override
    public List<String> getFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toList());
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return students.stream().map(Student::getLastName).collect(Collectors.toList());
    }

    @Override
    public List<String> getGroups(List<Student> students) {
        return students.stream().map(Student::getGroup).collect(Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return students.stream()
                .map(student -> String.format("%s %s", student.getFirstName(), student.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMinStudentFirstName(List<Student> students) {
        return students.stream()
                .min(STUDENT_BY_ID_COMPARATOR)
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudents(students, STUDENT_BY_ID_COMPARATOR);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudents(students, STUDENT_BY_NAME_COMPARATOR);
    }

    public List<Student> sortStudents(Collection<Student> students, Comparator<Student> studentComparator) {
        return students.stream()
                .sorted(studentComparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return students.stream()
                .filter(student -> Objects.equals(student.getFirstName(), name))
                .sorted(STUDENT_BY_NAME_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return students.stream()
                .filter(student -> Objects.equals(student.getLastName(), name))
                .sorted(STUDENT_BY_NAME_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return students.stream()
                .filter(student -> Objects.equals(student.getGroup(), group))
                .sorted(STUDENT_BY_NAME_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
        return students.stream()
                .filter(student -> Objects.equals(student.getGroup(), group))
                .collect(
                        Collectors.toMap(
                                Student::getLastName,
                                Student::getFirstName,
                                (name1, name2) -> Objects.compare(name1, name2, Comparator.comparing(name -> name)) > 0
                                        ? name2
                                        : name1
                        )
                );
    }
}
