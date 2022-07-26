import manager.Managers;
import manager.TaskManager;
import task.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        if (manager == null) {
            System.out.println("Менеджер не работает");
            System.exit(1);
        }

//        Task task1 =
//                new Task(null, "Посадить картошку", "Квадратно-гнездовой посев картофеля", TaskStatus.NEW);
//        int task1Id = manager.addTask(task1);
//
//        Task task2 = new Task(null, "Вскопать огород", "Копка на полтора штыка лопаты", TaskStatus.NEW);
//        int task2Id = manager.addTask(task2);
//
//        Epic epic1 = new Epic(null, "Уборка", "Убраться в квартире", TaskStatus.NEW);
//        int epic1Id = manager.addTask(epic1);
//
//        Subtask subtask1 =
//                new Subtask(null, epic1Id, "Помыть полы", "В кухне", TaskStatus.NEW);
//        int subtask1Id = manager.addTask(subtask1);
//
//        Subtask subtask2 =
//                new Subtask(null, epic1Id, "Пропылесосить", "Ковры", TaskStatus.NEW);
//        int subtask2Id = manager.addTask(subtask2);
//
//          Epic epic2 = new Epic(null, "ТО по машине", "Все необходимые мероприятия по ТО", TaskStatus.NEW);
//        int epic2Id = manager.addTask(epic2);
//
//        Subtask subtask3 =
//                new Subtask(null, epic2Id, "Заменить масло", "Масло и масляный фильтр", TaskStatus.NEW);
//        int subtask3Id = manager.addTask(subtask3);
//
//        Task task3 =
//                new Task(null, "Помыть кота", "Записаться в салон для котов", TaskStatus.NEW);
//        int task3Id = manager.addTask(task3);
//
//        Task task4 = new Task(null, "Постирать", "Загрузить стиральную машинку", TaskStatus.NEW);
//        int task4Id = manager.addTask(task4);
//
//        Epic epic3 = new Epic(null, "Ремонт", "Доделать ремонтные работы", TaskStatus.IN_PROGRESS);
//        int epic3Id = manager.addTask(epic3);
//
//        Subtask subtask4 = new Subtask(null, epic3Id, "Антресоль", "Купить материал", TaskStatus.IN_PROGRESS);
//        int subtask4Id = manager.addTask(subtask4);
//
//        Subtask subtask5 = new Subtask(null, epic3Id, "Пол", "Купить материал", TaskStatus.NEW);
//        int subtask5Id = manager.addTask(subtask5);
//
//        Subtask subtask6 = new Subtask(null, epic3Id, "Кухня", "Поставить кухню", TaskStatus.IN_PROGRESS);
//        int subtask6Id = manager.addTask(subtask6);
//
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getSubtasks());
//
//        task1 = manager.getTask(task1Id);
//
//        task1.setStatus(TaskStatus.DONE);
//        manager.updateTask(task1);
//
//        subtask1 = manager.getSubtask(subtask1Id);
//        subtask1.setStatus(TaskStatus.IN_PROGRESS);
//        manager.updateSubtask(subtask1);
//
//        subtask3 = manager.getSubtask(subtask3Id);
//        subtask3.setStatus(TaskStatus.DONE);
//        manager.updateSubtask(subtask3);
//
//        System.out.println("------------------------------");
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getSubtasks());
//
//        manager.deleteTask(task2Id);
//        manager.getEpic(2);
//
//        System.out.println("------------------------------");
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getSubtasks());
//
//        manager.getTask(task2Id);
//        manager.getTask(task3Id);
//        manager.getTask(task4Id);
//        manager.getEpic(epic1Id);
//        manager.getTask(task3Id);
//        manager.getTask(task4Id);
//        manager.getEpic(epic2Id);
//        manager.getEpic(epic3Id);
//        manager.getSubtask(subtask2Id);
//        manager.getSubtask(subtask4Id);
//        manager.getEpic(epic3Id);
//        manager.getSubtask(subtask5Id);
//        manager.getSubtask(subtask6Id);
//        manager.getTask(task4Id);
//        manager.getSubtask(subtask2Id);
//
//////        // Ожидается, что в истории не будет повторов
//        System.out.println("\n----- 1 ------------------------------");
//        List<Task> history = manager.getHistory();
//        for(Task t : history) {
//            System.out.println(t.getId() + " - " + t.getClass() + " - " + t.getTitle());
//        }
//
//        manager.deleteTask(task4Id);
////        // Ожидается, что в истории не будет задачи "Постирать"
//        System.out.println("\n----- 2 ------------------------------");
//        history = manager.getHistory();
//        for(Task t : history) {
//            System.out.println(t.getId() + " - " + t.getClass() + " - " + t.getTitle());
//        }
//
//        manager.deleteEpic(epic3Id);
////        // Ожидается, что в истории не будет эпика "Ремонт" и его подзадач
//        System.out.println("\n----- 3 ------------------------------");
//        history = manager.getHistory();
//        for(Task t : history) {
//            System.out.println(t.getId() + " - " + t.getClass() + " - " + t.getTitle());
//        }
    }
}
