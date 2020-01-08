package students;

import java.util.*;
import java.util.stream.Collectors;

public class StudentDBHard extends StudentDBEasy implements StudentGroupQuery {
    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroups(students, STUDENT_BY_NAME_COMPARATOR);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroups(students, STUDENT_BY_ID_COMPARATOR);
    }

    @Override
    public String getLargestGroup(Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().stream().map(Student::getFirstName).count()))
                .max((o1, o2) -> {
                    if (Objects.equals(o1.getValue(), o2.getValue())) {
                        return o2.getKey().compareTo(o1.getKey());
                    } else {
                        return Long.compare(o1.getValue(), o2.getValue());
                    }
                })
                .map(Map.Entry::getKey)
                .orElse("");
    }

    @Override
    public String getLargestGroupFirstName(Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().stream().map(Student::getFirstName).distinct().count()))
                .max((o1, o2) -> {
                    if (Objects.equals(o1.getValue(), o2.getValue())) {
                        return o2.getKey().compareTo(o1.getKey());
                    } else {
                        return Long.compare(o1.getValue(), o2.getValue());
                    }
                })
                .map(Map.Entry::getKey)
                .orElse("");
    }

    public List<Group> getGroups(Collection<Student> students, Comparator<Student> studentsComparator) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet().stream()
                .map(entry -> new Group(entry.getKey(), sortStudents(entry.getValue(), studentsComparator)))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }
}
