package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {


    private final ArrayList<Subtask> subtasks;

    public Epic(int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
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
