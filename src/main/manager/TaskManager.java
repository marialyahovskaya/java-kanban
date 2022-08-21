package manager;

import task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    // POST /tasks/task
    int addTask(Task task) throws StartTimeOverlapException;

    // GET /tasks/task/?id={id}
    Task getTask(int id);

    // GET /tasks/task
    Collection<Task> getTasks();

    // DELETE /tasks/task/?id={id}
    void deleteTask(int id);

    // DELETE /tasks/task
    void deleteTasks();

    // POST /tasks/task/?id={id}
    void updateTask(Task task) throws StartTimeOverlapException;

    // GET /tasks/epic/?id={id}
    Epic getEpic(int id);

    // GET /tasks/epic
    Collection<Epic> getEpics();

    // DELETE /tasks/epic/?id={id}
    void deleteEpic(int id);

    // DELETE /tasks/epic
    void deleteEpics();

    // POST /tasks/epic/?id={id}
    void updateEpic(Epic epic);

    //GET /tasks/epic/?id={id}/subtasks
    ArrayList<Subtask> getEpicSubtasks(int epicId);

    // GET /tasks/subtask/?id={id}
    Subtask getSubtask(int id);

    // GET /tasks/subtask
    Collection<Subtask> getSubtasks();

    // DELETE /tasks/subtask
    void deleteSubtasks();

    // DELETE /tasks/subtask/?id=id
    void deleteSubtask(int id);

    // POST /tasks/subtask/?id={id}
    void updateSubtask(Subtask subtask) throws StartTimeOverlapException;

    List<Task> getPrioritizedTasks();
}
