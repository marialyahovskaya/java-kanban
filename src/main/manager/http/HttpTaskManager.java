package manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.file.FileBackedTaskManager;
import manager.file.ManagerLoadException;
import manager.file.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {
    private static KVClient client;
    private static final Gson gson  = Managers.getGson();

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        client = new KVClient(url);
    }

    public static HttpTaskManager loadFromServer(String url) throws ManagerLoadException {
        try {
            final HttpTaskManager taskManager = new HttpTaskManager(url);

            String tasksJson = client.load("tasks");
            Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> loadedTasks = gson.fromJson(tasksJson, taskListType);

            String epicsJson = client.load("epics");
            Type epicListType = new TypeToken<ArrayList<Epic>>(){}.getType();
            List<Epic> loadedEpics = gson.fromJson(epicsJson, epicListType);

            String subtasksJson = client.load("subtasks");
            Type subtaskListType = new TypeToken<ArrayList<Subtask>>(){}.getType();
            List<Subtask> loadedSubtasks = gson.fromJson(subtasksJson, subtaskListType);

            String historyJson = client.load("history");
            Type historyType = new TypeToken<ArrayList<Integer>>(){}.getType();
            List<Integer> loadedHistory = gson.fromJson(historyJson, historyType);

            loadedTasks.forEach(taskManager::addTask);
            loadedEpics.forEach(taskManager::addTask);

            // кладем сабтаски напрямую в map, чтобы не создавались клоны сабтасков в списке сабтасков эпика
            loadedSubtasks.forEach(st -> taskManager.subtasks.put(st.getId(), st));

            int maxId = loadedTasks.stream().map(Task::getId).reduce(0, Integer::max);
            maxId = loadedEpics.stream().map(Epic::getId).reduce(maxId, Integer::max);
            maxId = loadedSubtasks.stream().map(Subtask::getId).reduce(maxId, Integer::max);
            taskManager.nextId = maxId;

            taskManager.restoreHistory(loadedHistory);

            return taskManager;
        } catch (IOException | InterruptedException e ) {
            System.out.println("Ошибка при создании HttpTaskManager - " + e.getMessage());
            throw new ManagerLoadException(e.getMessage());
        }
    }

    @Override
    protected void save() throws ManagerSaveException {
        try {
            client.put("tasks", gson.toJson(tasks.values()));
            client.put("epics", gson.toJson(epics.values()));
            client.put("subtasks", gson.toJson(subtasks.values()));
            List<Integer> ids = history.getHistory().stream().map(Task::getId).toList();
            client.put("history", gson.toJson(ids));
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при сохранении данных на сервер - " + e.getMessage());
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
