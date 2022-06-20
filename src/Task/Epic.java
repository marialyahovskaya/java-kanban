package Task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {


    private final ArrayList<Integer> subtaskIds;

    public Epic(int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                "} " + super.toString();
    }
}
