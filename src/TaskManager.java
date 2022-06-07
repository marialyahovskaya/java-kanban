import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    final private HashMap<Integer, Task> tasks = new HashMap<>();
    final private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    final private HashMap<Integer, Epic> epics = new HashMap<>();

    int nextId = 0;

    public int generateId() {
        return ++nextId;
    }

    public int addTask(Task task) {
        int taskId = generateId();
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public int addEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);
        epics.put(epicId, epic);
        return epicId;
    }

    public String calculateEpicStatus(Epic epic) {
        String status = "";
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            status = "NEW";
        } else {
            int newSubtaskCount = 0;
            int doneSubtaskCount = 0;
            int inProgressSubtaskCount = 0;
            for (Integer subtaskId : subtaskIds) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask.getStatus().equals("NEW")) {
                    status = "NEW";
                    newSubtaskCount++;
                } else if (subtask.getStatus().equals("DONE")) {
                    status = "DONE";
                    doneSubtaskCount++;
                } else {
                    status = "IN_PROGRESS";
                    inProgressSubtaskCount++;
                }
            }
            if (inProgressSubtaskCount > 0 || (newSubtaskCount > 0 && doneSubtaskCount > 0)) {
                status = "IN_PROGRESS";
            }
        }
        return status;
    }

    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        String epicStatus = calculateEpicStatus(epic);
        epic.setStatus(epicStatus);
        return epic;
    }

    public int addSubtask(Subtask subtask) {
        int subtaskId = generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);

        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Integer> epicSubtasks = epic.getSubtaskIds();

        epicSubtasks.add(subtaskId);

        return subtaskId;
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }

    public Collection<Epic> getEpics() {
        Collection<Epic> epicCollection = epics.values();
        for (Epic epic : epicCollection) {
            String epicStatus = calculateEpicStatus(epic);
            epic.setStatus(epicStatus);
        }
        return epicCollection;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        int index = subtaskIds.indexOf(id);
        subtaskIds.remove(index);
        subtasks.remove(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        ArrayList<Subtask> subtasksList = new ArrayList<>();

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasksList.add(subtask);
        }
        return subtasksList;
    }
}
