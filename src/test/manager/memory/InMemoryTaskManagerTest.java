package manager.memory;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {

    TaskManager manager;

    @BeforeEach
    public void setupManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldHaveNewEpicStatusWhenNoSubtasks() {
        int epic1Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.NEW));
        int epic2Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.IN_PROGRESS));
        int epic3Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.DONE));
        assertEquals(TaskStatus.NEW, manager.getEpic(epic1Id).getStatus());
        assertEquals(TaskStatus.NEW, manager.getEpic(epic2Id).getStatus());
        assertEquals(TaskStatus.NEW, manager.getEpic(epic3Id).getStatus());
        Collection<Epic> epics = manager.getEpics();
        for (Epic epic : epics) {
            assertEquals(TaskStatus.NEW, epic.getStatus());
        }
    }

    @Test void shouldHaveNewEpicStatusWhenAllSubtasksHaveNewStatus() {
        int epic1Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.DONE));
        manager.addTask(new Subtask(0, epic1Id, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(0, epic1Id, "title2", "description2", TaskStatus.NEW));
        assertEquals(TaskStatus.NEW, manager.getEpic(epic1Id).getStatus());
    }

    @Test void shouldHaveDoneEpicStatusWhenAllSubtasksHaveDoneStatus() {
        int epic1Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(0, epic1Id, "title", "description", TaskStatus.DONE));
        manager.addTask(new Subtask(0, epic1Id, "title2", "description2", TaskStatus.DONE));
        assertEquals(TaskStatus.DONE, manager.getEpic(epic1Id).getStatus());
    }

    @Test void shouldHaveInProgressEpicStatusWhenSubtasksHaveNewAndDoneStatuses() {
        int epic1Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(0, epic1Id, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(0, epic1Id, "title2", "description2", TaskStatus.DONE));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epic1Id).getStatus());
    }

    @Test void shouldHaveInProgressEpicStatusWhenAllSubtasksHaveInProgressStatus() {
        int epic1Id = manager.addTask(new Epic(0, "title", "description", TaskStatus.NEW));
        manager.addTask(new Subtask(0, epic1Id, "title", "description", TaskStatus.IN_PROGRESS));
        manager.addTask(new Subtask(0, epic1Id, "title2", "description2", TaskStatus.IN_PROGRESS));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(epic1Id).getStatus());
    }


}
