package students;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StudentDBAdvanced extends StudentDBHard implements AdvancedStudentGroupQuery {
    @Override
    public String getMostPopularName(Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        student -> String.format("%s %s", student.getFirstName(), student.getLastName())
                        )
                )
                .entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey(),
                        entry.getValue().stream().map(Student::getGroup).distinct().count()
                        )
                )
                .max((o1, o2) -> {
                    if (Objects.equals(o1.getValue(), o2.getValue())) {
                        return o1.getKey().compareTo(o2.getKey());
                    } else {
                        return Long.compare(o1.getValue(), o2.getValue());
                    }
                })
                .map(Map.Entry::getKey)
                .orElse("");
    }
}
