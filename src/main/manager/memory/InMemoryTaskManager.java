package manager.memory;

import manager.HistoryManager;
import manager.Managers;
import manager.StartTimeOverlapException;
import manager.TaskManager;
import task.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    protected final TreeSet<Task> priorityIndex = new TreeSet<>();

    protected final HistoryManager history = Managers.getDefaultHistory();

    protected int nextId = 0;

    protected Predicate<Task> timeOverlappingValidator = task -> {
        for (Task t : getPrioritizedTasks()) {
            // не должно быть пересечений длительности выполнения кроме случая сверки задачи с самой собой
            if (LocalDateTime.of(t.getStartTime(), LocalTime.of(0, 0)).isBefore(task.getEndTime()) &&
                    LocalDateTime.of(task.getStartTime(), LocalTime.of(0, 0)).isBefore(t.getEndTime()) &&
                    !t.getId().equals(task.getId()))
            {
                return false;
            }
        }
        return true;
    };

    private int generateId() {
        return ++nextId;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public int addTask(Task task) throws StartTimeOverlapException {
        int taskId;
        if (task.getId() == null) {
            taskId = generateId();
            task.setId(taskId);
        } else {
            taskId = task.getId();
        }

        if (task.getType() == TaskType.TASK || task.getType() == TaskType.SUBTASK) {
            if (!timeOverlappingValidator.test(task))
                throw new StartTimeOverlapException("Временной слот для создаваемой задачи уже занят");
            priorityIndex.add(task);
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
                epic.recalculateData();
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
        tasks.values().stream().forEach(priorityIndex::remove);
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.values().stream().forEach(priorityIndex::remove);
        subtasks.values().stream()
                .map(Subtask::getEpicId)
                .distinct()
                .forEach(id -> epics.get(id).getSubtasks().clear());
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        if (task == null)
            return;

        priorityIndex.remove(task);
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null)
            return;
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        for (Subtask subtask : epicSubtasks) {
            priorityIndex.remove(subtask);
            subtasks.remove(subtask.getId());
            history.remove(subtask.getId());
        }
        epics.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null)
            return;

        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.remove(subtask);
        epic.recalculateData();

        priorityIndex.remove(subtask);
        subtasks.remove(id);
        history.remove(id);
    }

    @Override
    public void updateTask(Task task) throws StartTimeOverlapException {
        if (!tasks.containsKey(task.getId())) {
            return;
        }

        if (!timeOverlappingValidator.test(task)) {
            throw new StartTimeOverlapException("Указанный новый временной слот задачи уже занят");
        }

        priorityIndex.add(task);
        Task oldTask = tasks.get(task.getId());
        priorityIndex.remove(oldTask);
        tasks.put(task.getId(), task);
        priorityIndex.add(task);
    }

    @Override
    public void updateSubtask(Subtask st) throws StartTimeOverlapException {
        if (!subtasks.containsKey(st.getId())) {
            return;
        }

        if (!timeOverlappingValidator.test(st)) {
            throw new StartTimeOverlapException("Указанный новый временной слот подзадачи уже занят");
        }

        Subtask oldSubtask = subtasks.get(st.getId());

        // epicId should not be changed/updated
        if (st.getEpicId() != oldSubtask.getEpicId()) {
            st = new Subtask(st.getId(), oldSubtask.getEpicId(), st.getTitle(), st.getDescription(), st.getStatus(), st.getDuration(), st.getStartTime());
        }

        priorityIndex.remove(oldSubtask);
        priorityIndex.add(st);
        subtasks.put(st.getId(), st);

        Epic epic = epics.get(st.getEpicId());
        epic.recalculateData();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return epic.getSubtasks();
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priorityIndex);
    }
}
