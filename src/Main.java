import manager.Managers;
import manager.TaskManager;
import task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 =
                new Task(0, "Посадить картошку", "Квадратно-гнездовой посев картофеля", TaskStatus.NEW);
        int task1Id = manager.addTask(task1);

        Task task2 = new Task(0, "Вскопать огород", "Копка на полтора штыка лопаты", TaskStatus.NEW);
        int task2Id = manager.addTask(task2);

        Epic epic1 = new Epic(0, "Уборка", "Убраться в квартире", TaskStatus.NEW);
        int epic1Id = manager.addEpic(epic1);

        Subtask subtask1 =
                new Subtask(0, epic1Id, "Помыть полы", "В кухне, в комнате, в ванной", TaskStatus.NEW);
        int subtask1Id = manager.addSubtask(subtask1);

        Subtask subtask2 =
                new Subtask(0, epic1Id, "Пропылесосить", "Ковры, диван, кресло", TaskStatus.NEW);
        int subtask2Id = manager.addSubtask(subtask2);

        Epic epic2 = new Epic(0, "ТО по машине", "Все необходимые мероприятия по ТО", TaskStatus.NEW);
        int epic2Id = manager.addEpic(epic2);

        Subtask subtask3 =
                new Subtask(0, epic2Id, "Заменить масло", "Масло, масляный фильтр", TaskStatus.NEW);
        int subtask3Id = manager.addSubtask(subtask3);

        Task task3 =
                new Task(0, "Помыть кота", "Записаться в салон для котов", TaskStatus.NEW);
        int task3Id = manager.addTask(task3);

        Task task4 = new Task(0, "Постирать", "Загрузить стиральную машинку", TaskStatus.NEW);
        int task4Id = manager.addTask(task4);

        Epic epic3 = new Epic(0, "Ремонт", "Доделать ремонтные работы", TaskStatus.IN_PROGRESS);
        int epic3Id = manager.addEpic(epic3);

        Subtask subtask4 = new Subtask(0, epic3Id, "Антресоль", "Купить материал", TaskStatus.IN_PROGRESS);
        int subtask4Id = manager.addSubtask(subtask4);

        Subtask subtask5 = new Subtask(0, epic3Id, "Пол", "Купить материал", TaskStatus.NEW);
        int subtask5Id = manager.addSubtask(subtask5);

        Subtask subtask6 = new Subtask(0, epic3Id, "Кухня", "Поставить кухню", TaskStatus.IN_PROGRESS);
        int subtask6Id = manager.addSubtask(subtask6);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        task1 = manager.getTask(task1Id);
        task1.setStatus(TaskStatus.DONE);
        manager.updateTask(task1);

        subtask1 = manager.getSubtask(subtask1Id);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask1);

        subtask3 = manager.getSubtask(subtask3Id);
        subtask3.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask3);

        System.out.println("------------------------------");
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        manager.deleteTask(task2Id);
        manager.deleteEpic(epic1Id);

        System.out.println("------------------------------");
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        manager.getTask(task2Id);
        manager.getTask(task3Id);
        manager.getTask(task4Id);
        manager.getEpic(epic1Id);
        manager.getEpic(epic2Id);
        manager.getEpic(epic3Id);
        manager.getSubtask(subtask2Id);
        manager.getSubtask(subtask4Id);
        manager.getSubtask(subtask5Id);
        manager.getSubtask(subtask6Id);

        System.out.println("------------------------------");
        System.out.println(manager.getHistory());
    }
}
