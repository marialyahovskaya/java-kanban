package manager.memory;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    protected final HistoryManager history = Managers.getDefaultHistory();

    protected int nextId = 0;

    private int generateId() {
        return ++nextId;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public int addTask(Task task) {
        int taskId;
        if (task.getId() == null) {
            taskId = generateId();
            task.setId(taskId);
        } else {
            taskId = task.getId();
        }

        switch (task.getType()) {
            case TASK -> tasks.put(taskId, task);
            case EPIC -> epics.put(taskId, (Epic) task);
            case SUBTASK -> {
                Subtask subtask = (Subtask) task;
                subtasks.put(taskId, subtask);
                int epicId = subtask.getEpicId();
                Epic epic = epics.get(epicId);
                ArrayList<Subtask> epicSubtasks = epic.getSubtasks();

                epicSubtasks.add(subtask);
            }
        }
        return taskId;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    private TaskStatus calculateEpicStatus(Epic epic) {
        TaskStatus status = null;
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        if (epicSubtasks.isEmpty()) {
            status = TaskStatus.NEW;
        } else {
            int newSubtaskCount = 0;
            int doneSubtaskCount = 0;
            int inProgressSubtaskCount = 0;
            for (Subtask subtask : epicSubtasks) {
                if (subtask.getStatus().equals(TaskStatus.NEW)) {
                    status = TaskStatus.NEW;
                    newSubtaskCount++;
                } else if (subtask.getStatus().equals(TaskStatus.DONE)) {
                    status = TaskStatus.DONE;
                    doneSubtaskCount++;
                } else {
                    status = TaskStatus.IN_PROGRESS;
                    inProgressSubtaskCount++;
                }
            }
            if (inProgressSubtaskCount > 0 || (newSubtaskCount > 0 && doneSubtaskCount > 0)) {
                status = TaskStatus.IN_PROGRESS;
            }
        }
        return status;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        history.add(epics.get(id));

        TaskStatus epicStatus = calculateEpicStatus(epic);
        epic.setStatus(epicStatus);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return null;
        }
        history.add(subtasks.get(id));

        return subtasks.get(id);
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    @Override
    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }

    @Override
    public Collection<Epic> getEpics() {
        Collection<Epic> epicCollection = epics.values();
        for (Epic epic : epicCollection) {
            TaskStatus epicStatus = calculateEpicStatus(epic);
            epic.setStatus(epicStatus);
        }
        return epicCollection;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
            history.remove(subtask.getId());
        }
        epics.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        int index = epicSubtasks.indexOf(subtask);
        epicSubtasks.remove(index);
        subtasks.remove(id);
        history.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        return epic.getSubtasks();
    }
}
