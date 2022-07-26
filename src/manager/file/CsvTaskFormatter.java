package manager.file;

import manager.HistoryManager;
import task.*;

import java.util.ArrayList;
import java.util.List;

public class CsvTaskFormatter {

    public static final String CSV_HEADER = "id,type,name,status,description,epic\n";

    public static String toString(Task t) {
        switch (t.getType()) {
            case TASK, EPIC -> {
                return t.getId() + "," + t.getType().toString() + "," + t.getTitle() + "," + t.getStatus() + ","
                        + t.getDescription() + "," + "\n";
            }
            case SUBTASK -> {
                Subtask st = (Subtask) t;
                return st.getId() + "," + st.getType().toString() + "," + st.getTitle()
                        + "," + st.getStatus() + "," + st.getDescription() + "," + st.getEpicId() + "\n";
            }
        }
        throw new IllegalArgumentException("Задача в неизвестном статусе");
    }

    public static Task fromString(String line) throws IllegalArgumentException {
        String[] cols = line.split(",");

        int id = Integer.parseInt(cols[0]);
        String title = cols[2];
        String description = cols[4];
        TaskStatus status = TaskStatus.valueOf(cols[3]);

        TaskType type = TaskType.valueOf(cols[1]);

        switch (type) {
            case TASK -> {
                return new Task(id, title, description, status);
            }
            case EPIC -> {
                return new Epic(id, title, description, status);
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(cols[5]);
                return new Subtask(id, epicId, title, description, status);
            }
            default -> throw new IllegalArgumentException("Непонятный тип задачи " + cols[1]);
        }
    }

    public static String historyToString(HistoryManager history) {
        List<Task> historyList = history.getHistory();
        List<String> idList = historyList.stream().map(task -> task.getId().toString()).toList();
        return String.join(",", idList) + "\n";
    }

    public static List<Integer> historyFromString(String csvIntegers) {
        List<String> stringIds = List.of(csvIntegers.split(","));
        List<Integer> ids = new ArrayList<>();
        for (String s : stringIds) {
            if (!s.isBlank()) {
                ids.add(Integer.parseInt(s));
            }
        }
        return ids;
    }
}
