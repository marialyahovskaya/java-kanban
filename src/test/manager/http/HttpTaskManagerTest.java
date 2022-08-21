package manager.http;

import manager.TaskManagerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static final String KV_SERVER_URL = "http://localhost:8078";

    private KVServer kvserver;

    @BeforeEach
    public void setupManager() throws IOException {
        kvserver = new KVServer();
        kvserver.start();
        manager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
    }

    @AfterEach
    public void shutDownKVServer() {
        kvserver.stop();
    }

    @Test
    public void shouldSaveAndRestoreAllTypesOfTasksAndStatuses() {
        manager.addTask(new Task(1, "t1", "task2", TaskStatus.NEW, 10, LocalDate.of(2022,8,7)));
        manager.addTask(new Task(2, "t2", "task2", TaskStatus.IN_PROGRESS,10, LocalDate.of(2022,8,8)));
        manager.addTask(new Task(3, "t3", "task3", TaskStatus.DONE,10, LocalDate.of(2022,8,9)));
        manager.addTask(new Epic(4, "e", "epic", TaskStatus.NEW, LocalDate.of(2022,8,10)));
        manager.addTask(new Subtask(5, 4, "s", "subtask", TaskStatus.NEW, 10, LocalDate.of(2022,8,11)));

        HttpTaskManager restoredManager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
        assertEquals(3, restoredManager.getTasks().size(), "Загружены все сохраненные задачи");
        assertEquals(1, restoredManager.getEpics().size(), "Загружены все сохраненные эпики");
        assertEquals(1, restoredManager.getSubtasks().size(), "Загружены все сохраненные сабтаски");

        assertEquals(manager.getTask(1), restoredManager.getTask(1), "Сохраненные данные идентичны загруженным");
        assertEquals(manager.getTask(2), restoredManager.getTask(2),"Сохраненные данные идентичны загруженным");
        assertEquals(manager.getTask(3), restoredManager.getTask(3),"Сохраненные данные идентичны загруженным");
        assertEquals(manager.getEpic(4), restoredManager.getEpic(4),"Сохраненные данные идентичны загруженным");
        assertEquals(manager.getSubtask(5), restoredManager.getSubtask(5),"Сохраненные данные идентичны загруженным");
    }

    @Test
    public void shouldCorrectlyStartWithEmptyKVServer() {
        assertNotNull(manager.getTasks(),"Список задач не null");
        assertNotNull(manager.getEpics(),"Список эпиков не null");
        assertNotNull(manager.getSubtasks(),"Список сабтасков не null");
        assertEquals(0, manager.getTasks().size(), "Пустой список задач");
        assertEquals(0, manager.getEpics().size(), "Пустой список эпиков");
        assertEquals(0, manager.getSubtasks().size(), "Пустой список сабтасков");
    }

    @Test
    public void shouldRestoreFromEmptyJsonListsAfterCreatingAndRemovingAllTasksOneByOne() {
        manager.addTask(new Task(1, "t1", "task2", TaskStatus.NEW, 10, LocalDate.of(2022,8,7)));
        manager.addTask(new Task(2, "t2", "task2", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022,8,8)));
        manager.addTask(new Task(3, "t3", "task3", TaskStatus.DONE, 10, LocalDate.of(2022,8,9)));
        manager.addTask(new Epic(4, "e", "epic", TaskStatus.NEW,  LocalDate.of(2022,8,10)));
        manager.addTask(new Subtask(5, 4, "s", "subtask", TaskStatus.NEW, 10, LocalDate.of(2022,8,11)));
        manager.deleteTask(1);
        manager.deleteTask(2);
        manager.deleteTask(3);
        manager.deleteEpic(4);
        manager.deleteSubtask(5);

        HttpTaskManager restoredManager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
        assertNotNull(restoredManager.getTasks(),"Список задач не null");
        assertNotNull(restoredManager.getEpics(),"Список эпиков не null");
        assertNotNull(restoredManager.getSubtasks(),"Список сабтасков не null");
        assertEquals(0, restoredManager.getTasks().size(), "Пустой список задач");
        assertEquals(0, restoredManager.getEpics().size(), "Пустой список эпиков");
        assertEquals(0, restoredManager.getSubtasks().size(), "Пустой список сабтасков");
    }

    @Test
    public void shouldRestoreFromEmptyJsonListsAfterCreatingAndClearingAllData() {
        manager.addTask(new Task(1, "t1", "task2", TaskStatus.NEW, 10, LocalDate.of(2022,8,7)));
        manager.addTask(new Task(2, "t2", "task2", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022,8,8)));
        manager.addTask(new Task(3, "t3", "task3", TaskStatus.DONE, 10, LocalDate.of(2022,8,9)));
        manager.addTask(new Epic(4, "e", "epic", TaskStatus.NEW,  LocalDate.of(2022,8,10)));
        manager.addTask(new Subtask(5, 4, "s", "subtask", TaskStatus.NEW, 10, LocalDate.of(2022,8,11)));
        manager.deleteTasks();
        manager.deleteEpics();

        HttpTaskManager restoredManager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
        assertNotNull(restoredManager.getTasks(),"Список задач не null");
        assertNotNull(restoredManager.getEpics(),"Список эпиков не null");
        assertNotNull(restoredManager.getSubtasks(),"Список сабтасков не null");
        assertEquals(0, restoredManager.getTasks().size(), "Пустой список задач");
        assertEquals(0, restoredManager.getEpics().size(), "Пустой список эпиков");
        assertEquals(0, restoredManager.getSubtasks().size(), "Пустой список сабтасков");
    }


    @Test
    public void shouldRestoreEpicWithNoSubtasks() {
        manager.addTask(new Epic(1, "e", "epic", TaskStatus.NEW, LocalDate.of(2022,8,7)));
        HttpTaskManager restoredManager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
        assertNotNull(restoredManager.getEpics(),"Список эпиков не null");
        assertEquals(manager.getEpic(1), restoredManager.getEpic(1),"Сохраненные данные идентичны загруженным");
    }

    @Test
    public void shouldRestoreEmptyHistory() {
        manager.addTask(new Task(1, "t1", "task2", TaskStatus.NEW, 10, LocalDate.of(2022,8,7)));
        manager.addTask(new Epic(2, "e", "epic", TaskStatus.NEW,  LocalDate.of(2022,8,9)));
        manager.addTask(new Subtask(3, 2, "s", "subtask", TaskStatus.NEW, 10, LocalDate.of(2022,8,9)));
        HttpTaskManager restoredManager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
        assertNotNull(restoredManager.getHistory(), "История не null");
        assertEquals(0, restoredManager.getHistory().size(), "Пустая история");
    }

    @Test
    public void shouldRestoreHistory() {
        manager.addTask(new Task(1, "t1", "task2", TaskStatus.NEW, 10, LocalDate.of(2022,8,7)));
        manager.addTask(new Task(2, "t2", "task2", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022,8,8)));
        manager.addTask(new Task(3, "t3", "task3", TaskStatus.DONE, 10, LocalDate.of(2022,8,9)));
        manager.addTask(new Epic(4, "e", "epic", TaskStatus.NEW,  LocalDate.of(2022,8,10)));
        manager.addTask(new Subtask(5, 4, "s", "subtask", TaskStatus.NEW, 10, LocalDate.of(2022,8,11)));

        manager.getTask(2);
        manager.getTask(3);
        manager.getSubtask(5);
        manager.getEpic(4);

        HttpTaskManager restoredManager = HttpTaskManager.loadFromServer(KV_SERVER_URL);
        assertNotNull(restoredManager.getHistory(), "История не null");
        assertEquals(4, restoredManager.getHistory().size(), "Не пустая история");
        assertEquals(manager.getHistory(), restoredManager.getHistory(), "Загруженная история идентична сохраненной");
    }
}
