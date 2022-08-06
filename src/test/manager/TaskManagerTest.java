package manager;

import org.junit.jupiter.api.Test;
import task.*;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    public void shouldHaveNewEpicStatusWhenNoSubtasks() {
        int epic1Id = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW));
        int epic2Id = manager.addTask(new Epic(null, "title", "description", TaskStatus.IN_PROGRESS));
        int epic3Id = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE));
        assertEquals(TaskStatus.NEW, manager.getEpic(epic1Id).getStatus(), "Статус без подзадач должен быть NEW");
        assertEquals(TaskStatus.NEW, manager.getEpic(epic2Id).getStatus(), "Статус без подзадач должен быть NEW");
        assertEquals(TaskStatus.NEW, manager.getEpic(epic3Id).getStatus(), "Статус без подзадач должен быть NEW");
        Collection<Epic> epics = manager.getEpics();
        for (Epic epic : epics) {
            assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус без подзадач должен быть NEW");
        }
    }

    @Test
    public void shouldHaveNewEpicStatusWhenAllSubtasksHaveNewStatus() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.NEW));
        assertEquals(TaskStatus.NEW, manager.getEpic(epicId).getStatus(), "Если все подзадачи NEW, статус эпика тоже должен быть NEW");
    }

    @Test
    public void shouldHaveDoneEpicStatusWhenAllSubtasksHaveDoneStatus() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.DONE));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE));
        assertEquals(TaskStatus.DONE, manager.getEpic(epicId).getStatus(), "Если все подзадачи DONE, статус эпика тоже должен быть DONE");
    }

    @Test
    public void shouldHaveInProgressEpicStatusWhenSubtasksHaveNewAndDoneStatuses() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статус должен быть IN_PROGRESS");
    }

    @Test
    public void shouldHaveInProgressEpicStatusWhenAllSubtasksHaveInProgressStatus() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.IN_PROGRESS));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.IN_PROGRESS));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статус должен быть IN_PROGRESS");
    }

    @Test
    public void shouldAddAndGetTask() {
        final int taskId = manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        Task task = manager.getTask(taskId);
        assertEquals("помыть посуду", task.getTitle(), "Некорректно сохранен title");
        assertEquals("мыть всё подряд долго", task.getDescription(), "Некорректно сохранен description");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Некорректно сохранен status");
        assertEquals(TaskType.TASK, task.getType(), "Неверно задан type при создании");
    }

    @Test
    public void shouldAddAndGetEpic() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        Epic e = manager.getEpic(epicId);
        assertEquals("погладить", e.getTitle());
        assertEquals("погладить всё подряд долго", e.getDescription());
        assertEquals(TaskStatus.NEW, e.getStatus());
        assertEquals(TaskType.EPIC, e.getType());
    }

    @Test
    public void shouldAddAndGetSubtask() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        int subtaskId = manager.addTask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS));
        Subtask s = manager.getSubtask(subtaskId);
        assertEquals("погладить кота", s.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", s.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, s.getStatus());
        assertEquals(TaskType.SUBTASK, s.getType());
        assertEquals(epicId, s.getEpicId());
        assertNotNull(manager.getEpic(s.getEpicId()));
    }

    @Test
    public void shouldReturnNullWhenTaskIdIsNonexistent() {
        manager.addTask(new Task(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        Task t = manager.getTask(101);
        assertNull(t);
    }

    @Test
    public void shouldReturnNullWhenEpicIdIsNonexistent() {
        manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        Epic e = manager.getEpic(101);
        assertNull(e);
    }

    @Test
    public void shouldReturnNullWhenSubtaskIdIsNonexistent() {
        int epicId = manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Subtask(2, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS));
        Subtask s = manager.getSubtask(102);
        assertNull(s);
    }

    @Test
    public void shouldGetTasks() {
        manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Task(null, "вытереть посуду", "вытирать всё подряд долго", TaskStatus.NEW));
        assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void shouldReturnEmptyTaskCollectionWhenNoTasksAdded() {
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void shouldGetEpics() {
        manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Epic(null, "убрать", "убирать всё подряд долго", TaskStatus.NEW));
        assertEquals(2, manager.getEpics().size());
    }

    @Test
    public void shouldReturnEmptyEpicsCollectionWhenNoEpicsAdded() {
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void shouldGetSubtasks() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS));

        int epicId2 = manager.addTask(new Epic(null, "помыть", "мыть всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS));

        assertEquals(2, manager.getSubtasks().size());
        for (Subtask subtask : manager.getSubtasks()) {
            assertNotNull(manager.getEpic(subtask.getEpicId()));
        }
    }

    @Test
    public void shouldReturnEmptySubtasksCollectionWhenNoSubtasksAdded() {
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    public void shouldDeleteTasks() {
        manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Task(null, "приготовить еду", "проготовить супы котлеты", TaskStatus.NEW));
        manager.deleteTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void shouldNotFallTryingToDeleteTasksWhenNoTasksAdded() {
        manager.deleteTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void shouldDeleteEpics() {
        manager.addTask(new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Epic(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Epic(null, "приготовить еду", "проготовить супы котлеты", TaskStatus.NEW));
        manager.deleteEpics();
        int size = manager.getEpics().size();
        assertEquals(0, size);
    }

    @Test
    public void shouldNotFallTryingToDeleteEpicsWhenNoEpicsAdded() {
        manager.deleteEpics();
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void shouldDeleteSubtasks() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE));
        manager.addTask(new Subtask(null, epicId, "title3", "description3", TaskStatus.DONE));
        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtasks().size());

        Epic e = manager.getEpic(epicId);
        assertEquals(0, e.getSubtasks().size());
    }

    @Test
    public void shouldNotFallTryingToDeleteSubtasksWhenNoSubtasksAdded() {
        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    public void shouldDeleteTask() {
        int taskId = manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        int taskId2 = manager.addTask(new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW));
        manager.deleteTask(taskId2);
        Task t = manager.getTask(taskId);

        assertEquals("помыть посуду", t.getTitle());
        assertEquals("мыть всё подряд долго", t.getDescription());
        assertEquals(TaskStatus.NEW, t.getStatus());
        assertEquals(TaskType.TASK, t.getType());
        assertNull(manager.getTask(taskId2));
    }

    @Test
    public void shouldDeleteEpic() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        int epicId2 = manager.addTask(new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId2, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS));
        manager.addTask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS));

        manager.deleteEpic((epicId2));
        Epic e = manager.getEpic(epicId);

        assertEquals("погладить", e.getTitle());
        assertEquals("погладить всё подряд долго", e.getDescription());
        assertEquals(TaskStatus.NEW, e.getStatus());
        assertEquals(TaskType.EPIC, e.getType());
        assertNull(manager.getEpic(epicId2));
    }

    @Test
    public void shouldDeleteSubtask() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        int subtaskId = manager.addTask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS));
        int epicId2 = manager.addTask(new Epic(null, "помыть", "мыть всё подряд долго", TaskStatus.NEW));
        int subtaskId2 = manager.addTask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS));
        manager.deleteSubtask(subtaskId2);
        Subtask s = manager.getSubtask(subtaskId);

        assertEquals("погладить кота", s.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", s.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, s.getStatus());
        assertEquals(TaskType.SUBTASK, s.getType());
        assertEquals(epicId, s.getEpicId());
        assertNull(manager.getSubtask(subtaskId2));
        assertEquals(0, manager.getEpic(epicId2).getSubtasks().size());
    }

    @Test
    public void shouldNotDeleteTaskWhenTaskIdIsNonexistent() {
        manager.addTask(new Task(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW));
        manager.deleteTask(101);
        assertEquals(1, manager.getTasks().size());
    }

    @Test
    public void shouldNotDeleteEpicWhenEpicIdIsNonexistent() {
        manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        manager.deleteEpic(101);
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    public void shouldNotDeleteSubtaskWhenSubtaskIdIsNonexistent() {
        int epicId = manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW));
        int subtaskId = manager.addTask(new Subtask(2, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS));
        manager.deleteSubtask(102);
        Subtask s = manager.getSubtask(subtaskId);
        assertEquals("погладить кота", s.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", s.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, s.getStatus());
        assertEquals(TaskType.SUBTASK, s.getType());
        assertEquals(epicId, s.getEpicId());
    }

    @Test
    public void shouldUpdateTask() {
        Task task = new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW);
        int taskId = manager.addTask(task);

        task = new Task(taskId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS);
        manager.updateTask(task);

        task = manager.getTask(taskId);
        assertEquals("очень помыть посуду", task.getTitle());
        assertEquals("мыть всё подряд очень долго", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(TaskType.TASK, task.getType());
    }

    @Test
    public void shouldNotUpdateTaskWhenIdIsNonexistent() {
        Task task = new Task(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW);
        manager.addTask(task);

        task = new Task(101, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS);
        manager.updateTask(task);

        task = manager.getTask(101);
        assertNull(task);
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW);
        int epicId = manager.addTask(epic);

        epic = new Epic(epicId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS);
        manager.updateEpic(epic);

        epic = manager.getEpic(epicId);
        assertEquals("очень помыть посуду", epic.getTitle());
        assertEquals("мыть всё подряд очень долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
    }

    @Test
    public void shouldNotUpdateEpicWhenIdIsNonexistent() {
        Epic epic = new Epic(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW);
        manager.addTask(epic);

        epic = new Epic(101, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS);
        manager.updateEpic(epic);

        epic = manager.getEpic(101);
        assertNull(epic);
    }

    @Test
    public void shouldUpdateSubtask() {
        Epic epic1 = new Epic(null, "мойка", "всё что относится к мойке", TaskStatus.NEW);
        Epic epic2 = new Epic(null, "живтоне", "всё что относится к уходу за живтоне", TaskStatus.NEW);

        int epic1Id = manager.addTask(epic1);
        int epic2Id = manager.addTask(epic2);

        Subtask subtask = new Subtask(null, epic1Id, "мойка кота", "помыть как следует с шампунем", TaskStatus.NEW);
        int subtaskId = manager.addTask(subtask);

        subtask = new Subtask(subtaskId, epic2Id, "помывка кота", "помыть кота с шампунем", TaskStatus.DONE);
        manager.updateSubtask(subtask);

        subtask = manager.getSubtask(subtaskId);
        assertEquals("помывка кота", subtask.getTitle());
        assertEquals("помыть кота с шампунем", subtask.getDescription());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(epic1Id, subtask.getEpicId()); // epicId should not be changed
    }

    @Test
    public void shouldNotUpdateSubtaskWhenIdIsNonexistent() {
        Epic epic = new Epic(1, "мойка", "мыть всё подряд долго", TaskStatus.NEW);
        Integer epicId = manager.addTask(epic);
        Subtask subtask = new Subtask(2, epicId, "мойка кота", "помыть как следует с шампунем", TaskStatus.NEW);
        manager.addTask(subtask);

        subtask = new Subtask(101, epicId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask);

        subtask = manager.getSubtask(101);
        assertNull(subtask);
    }

    @Test
    public void shouldGetEpicSubtasks() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.NEW));
        List<Subtask> subtasks = manager.getEpicSubtasks(epicId);
        assertEquals(2, subtasks.size());
    }

    @Test
    public void shouldReturnEmptySubtaskListWhenNoSubtasksAdded() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE));
        List<Subtask> subtasks = manager.getEpicSubtasks(epicId);
        assertEquals(0, subtasks.size());
    }

    @Test
    public void shouldReturnNullAsSubtaskListWhenEpicIdIsNonexistent() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.DONE));
        List<Subtask> subtasks = manager.getEpicSubtasks(101);
        assertNull(subtasks);
    }
}
