package manager;

import org.junit.jupiter.api.Test;
import task.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    public void shouldHaveNewEpicStatusWhenNoSubtasks() {
        int epic1Id = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int epic2Id = manager.addTask(new Epic(null, "title", "description", TaskStatus.IN_PROGRESS, LocalDate.of(2022, 8, 7)));
        int epic3Id = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE, LocalDate.of(2022, 8, 7)));
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
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        assertEquals(TaskStatus.NEW, manager.getEpic(epicId).getStatus(), "Если все подзадачи NEW, статус эпика тоже должен быть NEW");
    }

    @Test
    public void shouldHaveDoneEpicStatusWhenAllSubtasksHaveDoneStatus() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7)));
        assertEquals(TaskStatus.DONE, manager.getEpic(epicId).getStatus(), "Если все подзадачи DONE, статус эпика тоже должен быть DONE");
    }

    @Test
    public void shouldHaveInProgressEpicStatusWhenSubtasksHaveNewAndDoneStatuses() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7)));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статус должен быть IN_PROGRESS");
    }

    @Test
    public void shouldHaveInProgressEpicStatusWhenAllSubtasksHaveInProgressStatus() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epicId).getStatus(), "Статус должен быть IN_PROGRESS");
    }

    @Test
    public void shouldAddAndGetTask() {
        final int taskId = manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        Task task = manager.getTask(taskId);
        assertEquals("помыть посуду", task.getTitle(), "Некорректно сохранен title");
        assertEquals("мыть всё подряд долго", task.getDescription(), "Некорректно сохранен description");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Некорректно сохранен status");
        assertEquals(TaskType.TASK, task.getType(), "Неверно задан type при создании");
        assertEquals(10, task.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), task.getStartTime());
    }

    @Test
    public void shouldAddAndGetEpic() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        Epic epic = manager.getEpic(epicId);
        assertEquals("погладить", epic.getTitle());
        assertEquals("погладить всё подряд долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
        assertEquals(0, epic.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), epic.getStartTime());
    }

    @Test
    public void shouldAddAndGetSubtask() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId = manager.addTask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        Subtask subtask = manager.getSubtask(subtaskId);
        assertEquals("погладить кота", subtask.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", subtask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(epicId, subtask.getEpicId());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertNotNull(manager.getEpic(subtask.getEpicId()));
    }

    @Test
    public void shouldReturnNullWhenTaskIdIsNonexistent() {
        manager.addTask(new Task(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        Task task = manager.getTask(101);
        assertNull(task);
    }

    @Test
    public void shouldReturnNullWhenEpicIdIsNonexistent() {
        manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        Epic epic = manager.getEpic(101);
        assertNull(epic);
    }

    @Test
    public void shouldReturnNullWhenSubtaskIdIsNonexistent() {
        int epicId = manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(2, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        Subtask subtask = manager.getSubtask(102);
        assertNull(subtask);
    }

    @Test
    public void shouldGetTasks() {
        manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Task(null, "вытереть посуду", "вытирать всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void shouldReturnEmptyTaskCollectionWhenNoTasksAdded() {
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void shouldGetEpics() {
        manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Epic(null, "убрать", "убирать всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        assertEquals(2, manager.getEpics().size());
    }

    @Test
    public void shouldReturnEmptyEpicsCollectionWhenNoEpicsAdded() {
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void shouldGetSubtasks() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));

        int epicId2 = manager.addTask(new Epic(null, "помыть", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));

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
        manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Task(null, "приготовить еду", "проготовить супы котлеты", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
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
        manager.addTask(new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Epic(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Epic(null, "приготовить еду", "проготовить супы котлеты", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
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
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title3", "description3", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7)));
        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtasks().size());

        Epic epic = manager.getEpic(epicId);
        assertEquals(0, epic.getSubtasks().size());
    }

    @Test
    public void shouldNotFallTryingToDeleteSubtasksWhenNoSubtasksAdded() {
        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    public void shouldDeleteTask() {
        int taskId = manager.addTask(new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        int taskId2 = manager.addTask(new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.deleteTask(taskId2);
        Task task = manager.getTask(taskId);

        assertEquals("помыть посуду", task.getTitle());
        assertEquals("мыть всё подряд долго", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(TaskType.TASK, task.getType());
        assertEquals(10, task.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), task.getStartTime());
        assertNull(manager.getTask(taskId2));
    }

    @Test
    public void shouldDeleteEpic() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int epicId2 = manager.addTask(new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId2, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));

        manager.deleteEpic((epicId2));
        Epic epic = manager.getEpic(epicId);

        assertEquals("погладить", epic.getTitle());
        assertEquals("погладить всё подряд долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
        assertEquals(0, epic.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), epic.getStartTime());
        assertNull(manager.getEpic(epicId2));
    }

    @Test
    public void shouldDeleteSubtask() {
        int epicId = manager.addTask(new Epic(null, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId = manager.addTask(new Subtask(null, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        int epicId2 = manager.addTask(new Epic(null, "помыть", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId2 = manager.addTask(new Subtask(null, epicId2, "помыть кота",
                "помыть кота специальным шампунем", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        manager.deleteSubtask(subtaskId2);
        Subtask subtask = manager.getSubtask(subtaskId);

        assertEquals("погладить кота", subtask.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", subtask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(epicId, subtask.getEpicId());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertNull(manager.getSubtask(subtaskId2));
        assertEquals(0, manager.getEpic(epicId2).getSubtasks().size());
    }

    @Test
    public void shouldNotDeleteTaskWhenTaskIdIsNonexistent() {
        manager.addTask(new Task(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.deleteTask(101);
        assertEquals(1, manager.getTasks().size());
    }

    @Test
    public void shouldNotDeleteEpicWhenEpicIdIsNonexistent() {
        manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        manager.deleteEpic(101);
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    public void shouldNotDeleteSubtaskWhenSubtaskIdIsNonexistent() {
        int epicId = manager.addTask(new Epic(1, "погладить", "погладить всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7)));
        int subtaskId = manager.addTask(new Subtask(2, epicId, "погладить кота",
                "погладить кота в специальной перчатке для вычесывания", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7)));
        manager.deleteSubtask(102);
        Subtask subtask = manager.getSubtask(subtaskId);
        assertEquals("погладить кота", subtask.getTitle());
        assertEquals("погладить кота в специальной перчатке для вычесывания", subtask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertEquals(epicId, subtask.getEpicId());
    }

    @Test
    public void shouldUpdateTask() {
        Task task = new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7));
        int taskId = manager.addTask(task);

        task = new Task(taskId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7));
        manager.updateTask(task);

        task = manager.getTask(taskId);
        assertEquals("очень помыть посуду", task.getTitle());
        assertEquals("мыть всё подряд очень долго", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(TaskType.TASK, task.getType());
        assertEquals(10, task.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), task.getStartTime());
    }

    @Test
    public void shouldNotUpdateTaskWhenIdIsNonexistent() {
        Task task = new Task(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7));
        manager.addTask(task);

        task = new Task(101, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7));
        manager.updateTask(task);

        task = manager.getTask(101);
        assertNull(task);
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = new Epic(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7));
        int epicId = manager.addTask(epic);

        epic = new Epic(epicId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, LocalDate.of(2022, 8, 7));
        manager.updateEpic(epic);

        epic = manager.getEpic(epicId);
        assertEquals("очень помыть посуду", epic.getTitle());
        assertEquals("мыть всё подряд очень долго", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(TaskType.EPIC, epic.getType());
        assertEquals(0, epic.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), epic.getStartTime());
    }

    @Test
    public void shouldNotUpdateEpicWhenIdIsNonexistent() {
        Epic epic = new Epic(1, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7));
        manager.addTask(epic);

        epic = new Epic(101, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, LocalDate.of(2022, 8, 7));
        manager.updateEpic(epic);

        epic = manager.getEpic(101);
        assertNull(epic);
    }

    @Test
    public void shouldUpdateSubtask() {
        Epic epic1 = new Epic(null, "мойка", "всё что относится к мойке", TaskStatus.NEW, LocalDate.of(2022, 8, 7));
        Epic epic2 = new Epic(null, "живтоне", "всё что относится к уходу за живтоне", TaskStatus.NEW, LocalDate.of(2022, 8, 7));

        int epic1Id = manager.addTask(epic1);
        int epic2Id = manager.addTask(epic2);

        Subtask subtask = new Subtask(null, epic1Id, "мойка кота", "помыть как следует с шампунем", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7));
        int subtaskId = manager.addTask(subtask);

        subtask = new Subtask(subtaskId, epic2Id, "помывка кота", "помыть кота с шампунем", TaskStatus.DONE, 10, LocalDate.of(2022, 8, 7));
        manager.updateSubtask(subtask);

        subtask = manager.getSubtask(subtaskId);
        assertEquals("помывка кота", subtask.getTitle());
        assertEquals("помыть кота с шампунем", subtask.getDescription());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(10, subtask.getDuration());
        assertEquals(LocalDate.of(2022, 8, 7), subtask.getStartTime());
        assertEquals(epic1Id, subtask.getEpicId()); // epicId should not be changed
    }

    @Test
    public void shouldNotUpdateSubtaskWhenIdIsNonexistent() {
        Epic epic = new Epic(1, "мойка", "мыть всё подряд долго", TaskStatus.NEW, LocalDate.of(2022, 8, 7));
        Integer epicId = manager.addTask(epic);
        Subtask subtask = new Subtask(2, epicId, "мойка кота", "помыть как следует с шампунем", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7));
        manager.addTask(subtask);

        subtask = new Subtask(101, epicId, "очень помыть посуду", "мыть всё подряд очень долго", TaskStatus.IN_PROGRESS, 10, LocalDate.of(2022, 8, 7));
        manager.updateSubtask(subtask);

        subtask = manager.getSubtask(101);
        assertNull(subtask);
    }

    @Test
    public void shouldGetEpicSubtasks() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title", "description", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        manager.addTask(new Subtask(null, epicId, "title2", "description2", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 7)));
        List<Subtask> subtasks = manager.getEpicSubtasks(epicId);
        assertEquals(2, subtasks.size());
    }

    @Test
    public void shouldReturnEmptySubtaskListWhenNoSubtasksAdded() {
        int epicId = manager.addTask(new Epic(null, "title", "description", TaskStatus.DONE, LocalDate.of(2022, 8, 7)));
        List<Subtask> subtasks = manager.getEpicSubtasks(epicId);
        assertEquals(0, subtasks.size());
    }

    @Test
    public void shouldReturnNullAsSubtaskListWhenEpicIdIsNonexistent() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.DONE, LocalDate.of(2022, 8, 7)));
        List<Subtask> subtasks = manager.getEpicSubtasks(101);
        assertNull(subtasks);
    }

    @Test
    public void shouldReturnSubtaskStartTimeForEpicWithSingleSubtask() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        manager.addTask(new Subtask(2, 1, "t", "d", TaskStatus.NEW, 180, LocalDate.of(2022, 3, 1)));
        assertEquals(manager.getSubtask(2).getStartTime(), manager.getEpic(1).getStartTime());
    }

    @Test
    public void shouldReturnOriginalStartTimeForEpicWithNoSubtasks() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        assertEquals(LocalDate.of(2022, 8, 22), manager.getEpic(1).getStartTime());
    }


    @Test
    public void shouldReturnEarliestSubtaskStartTimeForEpicWithMultipleSubtasks() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        manager.addTask(new Subtask(2, 1, "t", "d", TaskStatus.NEW, 180, LocalDate.of(2022, 4, 1)));
        manager.addTask(new Subtask(3, 1, "t", "d", TaskStatus.NEW, 180, LocalDate.of(2022, 3, 1)));
        manager.addTask(new Subtask(4, 1, "t", "d", TaskStatus.NEW, 180, LocalDate.of(2022, 5, 1)));
        assertEquals(manager.getSubtask(3).getStartTime(), manager.getEpic(1).getStartTime());
    }

    @Test
    public void shouldReturnSubtaskDurationForEpicWithSingleSubtask() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        manager.addTask(new Subtask(2, 1, "t", "d", TaskStatus.NEW, 180, LocalDate.of(2022, 3, 1)));
        assertEquals(manager.getSubtask(2).getDuration(), manager.getEpic(1).getDuration());
    }


    @Test
    public void shouldReturnZeroDurationForEpicWithNoSubtasks() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        assertEquals(0, manager.getEpic(1).getDuration());
    }

    @Test
    public void shouldReturnSumOfDurationsForEpicWithMultipleSubtasks() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        manager.addTask(new Subtask(2, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 4, 1)));
        manager.addTask(new Subtask(3, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 3, 1)));
        manager.addTask(new Subtask(4, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 5, 1)));
        assertEquals(30, manager.getEpic(1).getDuration());
    }

    @Test
    public void shouldReturnCorrectEndTime() {
        manager.addTask(new Task(1, "title", "description", TaskStatus.NEW, 200, LocalDate.of(2022, 1, 1)));
        assertEquals(LocalDateTime.of(2022, 1, 1, 3, 20), manager.getTask(1).getEndTime());
    }

    @Test
    public void shouldReturnSubtaskEndTimeForEpicWithSingleSubtask() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        manager.addTask(new Subtask(2, 1, "t", "d", TaskStatus.NEW, 180, LocalDate.of(2022, 3, 1)));
        assertEquals(manager.getSubtask(2).getEndTime(), manager.getEpic(1).getEndTime());
    }

    @Test
    public void shouldReturnStartTimeAsEndTimeForEpicWithNoSubtasks() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        assertEquals(LocalDateTime.of(2022, 8, 22, 0, 0), manager.getEpic(1).getEndTime());
    }

    @Test
    public void shouldReturnLatestSubtaskEndTimeForEpicWithMultipleSubtasks() {
        manager.addTask(new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22)));
        manager.addTask(new Subtask(2, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 4, 1)));
        manager.addTask(new Subtask(3, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 3, 1)));
        manager.addTask(new Subtask(4, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 5, 1)));
        assertEquals(manager.getSubtask(4).getEndTime(), manager.getEpic(1).getEndTime());
    }

    @Test
    public void shouldPrioritizeTasksAndSubtasks() {
        Epic epic = new Epic(1, "title", "description", TaskStatus.NEW, LocalDate.of(2022, 8, 22));
        Subtask subtask1 = new Subtask(2, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 4, 1));
        Subtask subtask2 = new Subtask(3, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 3, 1));
        Subtask subtask3 = new Subtask(4, 1, "t", "d", TaskStatus.NEW, 10, LocalDate.of(2022, 8, 1));
        Task task1 = new Task(null, "помыть посуду", "мыть всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 6, 1));
        Task task2 = new Task(null, "погладить", "гладить всё подряд долго", TaskStatus.NEW, 10, LocalDate.of(2022, 7, 1));
        manager.addTask(epic);
        manager.addTask(subtask1);
        manager.addTask(subtask2);
        manager.addTask(subtask3);
        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(5, prioritizedTasks.size(), "Выводятся все задачи и подзадачи");
        assertEquals(subtask2, prioritizedTasks.get(0), "Вывод в порядке приоритета");
        assertEquals(subtask1, prioritizedTasks.get(1), "Вывод в порядке приоритета");
        assertEquals(task1, prioritizedTasks.get(2), "Вывод в порядке приоритета");
        assertEquals(task2, prioritizedTasks.get(3), "Вывод в порядке приоритета");
        assertEquals(subtask3, prioritizedTasks.get(4), "Вывод в порядке приоритета");
    }
}
