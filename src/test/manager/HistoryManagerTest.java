package manager;

import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public abstract class HistoryManagerTest<T extends HistoryManager> {
    protected T historyManager;

    @Test
    public void shouldAddTaskToHistory() {
        Task task = new Task(1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022,8,07));
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    public void shouldHaveEmptyHistoryWhenNothingAdded() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не null.");
        assertEquals(0, history.size(), "Пустая история");
    }

    @Test
    public void shouldStoreTasksInHistoryInOrder() {
        historyManager.add(new Task(1, "t1", "d1", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(2, "t2", "d2", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(3, "t3", "d3", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История содержит все задачи");
        assertEquals(1, history.get(0).getId(), "Верный порядок выдачи задач в истории");
        assertEquals(2, history.get(1).getId(), "Верный порядок выдачи задач в истории");
        assertEquals(3, history.get(2).getId(), "Верный порядок выдачи задач в истории");
    }

    @Test
    public void shouldHaveUniqueTaskIds() {
        Task task = new Task(1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022,8,07));
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История содержит задачи без повторов");
    }

    @Test
    public void shouldHaveEmptyHistoryAfterRemovingSingleEntry() {
        Task task = new Task(1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022,8,07));
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не null.");
        assertEquals(0, history.size(), "Пустая история");
    }

    @Test
    public void shouldRemainOrderWhenRemovingFirstHistoryElement() {
        historyManager.add(new Task(1, "t1", "d1", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(2, "t2", "d2", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(3, "t3", "d3", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История содержит все задачи");
        assertEquals(2, history.get(0).getId(), "Верный порядок выдачи задач в истории");
        assertEquals(3, history.get(1).getId(), "Верный порядок выдачи задач в истории");
    }

    @Test
    public void shouldRemainOrderWhenRemovingMiddleHistoryElement() {
        historyManager.add(new Task(1, "t1", "d1", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(2, "t2", "d2", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(3, "t3", "d3", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.remove(2);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История содержит все задачи");
        assertEquals(1, history.get(0).getId(), "Верный порядок выдачи задач в истории");
        assertEquals(3, history.get(1).getId(), "Верный порядок выдачи задач в истории");
    }

    @Test
    public void shouldRemainOrderWhenRemovingLastHistoryElement() {
        historyManager.add(new Task(1, "t1", "d1", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(2, "t2", "d2", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(3, "t3", "d3", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.remove(3);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История содержит все задачи");
        assertEquals(1, history.get(0).getId(), "Верный порядок выдачи задач в истории");
        assertEquals(2, history.get(1).getId(), "Верный порядок выдачи задач в истории");
    }

    @Test
    public void shouldKeepHistoryWhenTryingToRemoveNonexistentElement() {
        historyManager.add(new Task(1, "t1", "d1", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(2, "t2", "d2", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        historyManager.add(new Task(3, "t3", "d3", TaskStatus.NEW, 10, LocalDate.of(2022,8,07)));
        List<Task> history = historyManager.getHistory();
        historyManager.remove(103);

        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История содержит все задачи");
    }
}