package task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks;

    public Epic(Integer id, String title, String description, TaskStatus status, LocalDate startTime) {
        super(id, title, description, status, 0, startTime);
        subtasks = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public int getDuration() {
        return subtasks.stream()
                .mapToInt(Task::getDuration)
                .sum();
    }

    @Override
    public LocalDate getStartTime() {
        if (subtasks.size() == 0) return startTime;
        return subtasks.stream()
                .map(Task::getStartTime)
                .reduce(
                        subtasks.get(0).getStartTime(),
                        (acc, cv) -> cv.isBefore(acc) ? cv : acc
                );
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.size() == 0) return super.getEndTime();
        return subtasks.stream()
                .map(Task::getEndTime)
                .reduce(
                        subtasks.get(0).getEndTime(),
                        (acc, cv) -> cv.isAfter(acc) ? cv : acc
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtasks +
                "} " + super.toString();
    }
}
