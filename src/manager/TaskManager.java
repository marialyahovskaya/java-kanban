package manager;

import task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    int addTask(Task task);

    Task getTask(int id);

    Collection<Task> getTasks();

    void deleteTask(int id);

    void deleteTasks();

    void updateTask(Task task);

    int addEpic(Epic epic);

    Epic getEpic(int id);

    Collection<Epic> getEpics();

    void deleteEpic(int id);

    void deleteEpics();

    void updateEpic(Epic epic);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    int addSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    Collection<Subtask> getSubtasks();

    void deleteSubtasks();

    void deleteSubtask(int id);

    void updateSubtask(Subtask subtask);
}
