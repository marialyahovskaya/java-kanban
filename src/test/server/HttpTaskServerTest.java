package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;
import util.LocalDateAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private static final String TASK_SERVER_URL = "http://localhost:8080";

    private HttpTaskServer server;
    private KVServer kvServer;

    private final HttpClient client = HttpClient.newHttpClient();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    public void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    public void stopServers() {
        server.stop();
        kvServer.stop();
    }

    @Test
    public void shouldAddAndGetTask() throws IOException, InterruptedException {
        int taskId = createTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        Task task = loadTask(taskId);

        assertEquals("помыть посуду", task.getTitle(), "Некорректно сохранен title");
        assertEquals("мыть всё подряд долго", task.getDescription(), "Некорректно сохранен description");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Некорректно сохранен status");
        assertEquals(TaskType.TASK, task.getType(), "Неверно задан type при создании");
        assertEquals(10, task.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), task.getStartTime());
    }

    @Test
    public void shouldAddAndGetEpic() throws IOException, InterruptedException {
        int epicId = createEpic(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        Epic epic = loadEpic(epicId);

        assertEquals("погладить", epic.getTitle());
        assertEquals("погладить всё подряд долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
        assertEquals(0, epic.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), epic.getStartTime());
    }

    @Test
    public void shouldAddAndGetSubtask() throws IOException, InterruptedException {
        int epicId = createEpic(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId = createSubtask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        Subtask subtask = loadSubtask(subtaskId);
        assertEquals("погладить кота", subtask.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", subtask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(epicId, subtask.getEpicId());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertNotNull(loadEpic(subtask.getEpicId()));
    }

    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {
        createTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        createTask(new Task(null, "вытереть посуду", "вытирать всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 8)));
        assertEquals(2, loadTasks().size());
    }

    @Test
    public void shouldReturnEmptyTaskCollectionWhenNoTasksAdded() throws IOException, InterruptedException {
        assertEquals(0, loadTasks().size());
    }

    @Test
    public void shouldGetEpics() throws IOException, InterruptedException {
        createEpic(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createEpic(new Epic(null, "убрать", "убирать всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        assertEquals(2, loadEpics().size());
    }

    @Test
    public void shouldReturnEmptyEpicsCollectionWhenNoEpicsAdded() throws IOException, InterruptedException {
        assertEquals(0, loadEpics().size());
    }

    @Test
    public void shouldGetSubtasks() throws IOException, InterruptedException {
        int epicId = createEpic(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createSubtask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));

        int epicId2 = createEpic(new Epic(null, "помыть", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createSubtask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 8)));

        assertEquals(2, loadSubtasks().size());
        for (Subtask subtask : loadSubtasks()) {
            assertNotNull(loadEpic(subtask.getEpicId()));
        }
    }

    @Test
    public void shouldReturnEmptySubtasksCollectionWhenNoSubtasksAdded() throws IOException, InterruptedException {
        assertEquals(0, loadSubtasks().size());
    }

    @Test
    public void shouldDeleteTasks() throws IOException, InterruptedException {
        createTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        createTask(new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 8)));
        createTask(new Task(null, "приготовить еду", "проготовить супы котлеты", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 9)));
        deleteTasks();
        assertEquals(0, loadTasks().size());
    }

    @Test
    public void shouldDeleteEpics() throws IOException, InterruptedException {
        createEpic(new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createEpic(new Epic(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createEpic(new Epic(null, "приготовить еду", "проготовить супы котлеты", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        deleteEpics();
        assertEquals(0, loadEpics().size());
    }


    @Test
    public void shouldDeleteSubtasks() throws IOException, InterruptedException {
        int epicId = createEpic(new Epic(null, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createSubtask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        createSubtask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 8)));
        createSubtask(new Subtask(null, epicId, "title3", "description3", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 9)));
        deleteSubtasks();
        assertEquals(0, loadSubtasks().size());

        Epic epic = loadEpic(epicId);
        assertEquals(0, epic.getSubtasks().size());
    }


    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        int taskId = createTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        int taskId2 = createTask(new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 8)));
        deleteTask(taskId2);
        Task task = loadTask(taskId);

        assertEquals("помыть посуду", task.getTitle());
        assertEquals("мыть всё подряд долго", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(TaskType.TASK, task.getType());
        assertEquals(10, task.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), task.getStartTime());
        assertEquals(1, loadTasks().size());
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        int epicId = createEpic(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int epicId2 = createEpic(new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        createSubtask(new Subtask(null, epicId2, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        createSubtask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 8)));

        deleteEpic(epicId2);
        Epic epic = loadEpic(epicId);

        assertEquals("погладить", epic.getTitle());
        assertEquals("погладить всё подряд долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
        assertEquals(0, epic.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), epic.getStartTime());
        assertEquals(1, loadEpics().size());
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        int epicId = createEpic(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId = createSubtask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        int epicId2 = createEpic(new Epic(null, "помыть", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId2 = createSubtask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 8)));
        deleteSubtask(subtaskId2);
        Subtask subtask = loadSubtask(subtaskId);

        assertEquals("погладить кота", subtask.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", subtask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(epicId, subtask.getEpicId());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertEquals(1, loadSubtasks().size());
        assertEquals(0, loadEpic(epicId2).getSubtasks().size());
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7));
        int taskId = createTask(task);

        task = new Task(taskId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7));
        updateTask(task);

        task = loadTask(taskId);
        assertEquals("очень помыть посуду", task.getTitle());
        assertEquals("мыть всё подряд очень долго", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(TaskType.TASK, task.getType());
        assertEquals(10, task.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), task.getStartTime());
    }

    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7));
        int epicId = createEpic(epic);

        epic = new Epic(epicId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, LocalDate.of(2022, 8, 7));
        updateEpic(epic);

        epic = loadEpic(epicId);
        assertEquals("очень помыть посуду", epic.getTitle());
        assertEquals("мыть всё подряд очень долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
        assertEquals(0, epic.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), epic.getStartTime());
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic(null, "мойка", "всё что относится к мойке", TaskStatus.NEW, LocalDate.of(2022, 8, 7));
        Epic epic2 = new Epic(null, "живтоне", "всё что относится к уходу за живтоне", TaskStatus.NEW, LocalDate.of(2022, 8, 7));

        int epic1Id = createEpic(epic1);
        int epic2Id = createEpic(epic2);

        Subtask subtask = new Subtask(null, epic1Id, "мойка кота", "помыть как следует с шампунем", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7));
        int subtaskId = createSubtask(subtask);

        subtask = new Subtask(subtaskId, epic2Id, "помывка кота", "помыть кота с шампунем", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7));
        updateSubtask(subtask);

        subtask = loadSubtask(subtaskId);
        assertEquals("помывка кота", subtask.getTitle());
        assertEquals("помыть кота с шампунем", subtask.getDescription());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertEquals(epic1Id, subtask.getEpicId()); // epicId should not be changed

        // также проверим, что сабтаск поменялся и в эпике тоже
        Optional<Subtask> subtaskInEpic = loadEpic(epic1Id).getSubtasks()
                .stream()
                .filter(st -> st.getId() == subtaskId)
                .findFirst();

        assertTrue(subtaskInEpic.isPresent());
        subtask = subtaskInEpic.get();
        assertEquals("помывка кота", subtask.getTitle());
        assertEquals("помыть кота с шампунем", subtask.getDescription());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertEquals(epic1Id, subtask.getEpicId()); // epicId should not be changed
    }

    public int createTask(Task task) throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);

        URI uri = URI.create(TASK_SERVER_URL + "/tasks/task");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpTaskServer.CreationResponse creationResponse = gson.fromJson(response.body(), HttpTaskServer.CreationResponse.class);
        return creationResponse.getId();
    }

    public Task loadTask(int id) throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Task.class);
    }

    public List<Task> loadTasks() throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskListType = new TypeToken<List<Task>>() {
        }.getType();
        return gson.fromJson(response.body(), taskListType);
    }

    public void deleteTasks() throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteTask(int id) throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void updateTask(Task task) throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);

        URI uri = URI.create(TASK_SERVER_URL + "/tasks/task/?id=" + task.getId());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public int createEpic(Epic epic) throws IOException, InterruptedException {
        String epicJson = gson.toJson(epic);

        URI uri = URI.create(TASK_SERVER_URL + "/tasks/epic");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(epicJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpTaskServer.CreationResponse creationResponse = gson.fromJson(response.body(), HttpTaskServer.CreationResponse.class);
        return creationResponse.getId();
    }

    public Epic loadEpic(int id) throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Epic.class);
    }

    public List<Epic> loadEpics() throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type epicListType = new TypeToken<List<Epic>>() {
        }.getType();
        return gson.fromJson(response.body(), epicListType);
    }

    public void deleteEpics() throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteEpic(int id) throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void updateEpic(Epic epic) throws IOException, InterruptedException {
        String epicJson = gson.toJson(epic);

        URI uri = URI.create(TASK_SERVER_URL + "/tasks/epic/?id=" + epic.getId());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(epicJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public int createSubtask(Subtask subtask) throws IOException, InterruptedException {
        String subtaskJson = gson.toJson(subtask);

        URI uri = URI.create(TASK_SERVER_URL + "/tasks/subtask");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(subtaskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpTaskServer.CreationResponse creationResponse = gson.fromJson(response.body(), HttpTaskServer.CreationResponse.class);
        return creationResponse.getId();
    }

    public Subtask loadSubtask(int id) throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Subtask.class);
    }

    public List<Subtask> loadSubtasks() throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subtaskListType = new TypeToken<List<Subtask>>() {
        }.getType();
        return gson.fromJson(response.body(), subtaskListType);
    }

    public void deleteSubtasks() throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteSubtask(int id) throws IOException, InterruptedException {
        URI uri = URI.create(TASK_SERVER_URL + "/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void updateSubtask(Subtask subtask) throws IOException, InterruptedException {
        String subtaskJson = gson.toJson(subtask);

        URI uri = URI.create(TASK_SERVER_URL + "/tasks/subtask/?id=" + subtask.getId());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(subtaskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
