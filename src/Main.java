import manager.Managers;
import manager.TaskManager;
import task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        if (manager == null) {
            System.out.println("Менеджер не работает");
            System.exit(1);
        }

        Task task1 =
                new Task(null, "Посадить картошку", "Квадратно-гнездовой посев картофеля", TaskStatus.NEW);
        int task1Id = manager.addTask(task1);

        Task task2 = new Task(null, "Вскопать огород", "Копка на полтора штыка лопаты", TaskStatus.NEW);
        int task2Id = manager.addTask(task2);

        Epic epic1 = new Epic(null, "Уборка", "Убраться в квартире", TaskStatus.NEW);
        int epic1Id = manager.addTask(epic1);

        Subtask subtask1 =
                new Subtask(null, epic1Id, "Помыть полы", "В кухне", TaskStatus.NEW);
        int subtask1Id = manager.addTask(subtask1);

        Subtask subtask2 =
                new Subtask(null, epic1Id, "Пропылесосить", "Ковры", TaskStatus.NEW);
        int subtask2Id = manager.addTask(subtask2);

          Epic epic2 = new Epic(null, "ТО по машине", "Все необходимые мероприятия по ТО", TaskStatus.NEW);
        int epic2Id = manager.addTask(epic2);

        Subtask subtask3 =
                new Subtask(null, epic2Id, "Заменить масло", "Масло и масляный фильтр", TaskStatus.NEW);
        int subtask3Id = manager.addTask(subtask3);

        Task task3 =
                new Task(null, "Помыть кота", "Записаться в салон для котов", TaskStatus.NEW);
        int task3Id = manager.addTask(task3);

        Task task4 = new Task(null, "Постирать", "Загрузить стиральную машинку", TaskStatus.NEW);
        int task4Id = manager.addTask(task4);

        Epic epic3 = new Epic(null, "Ремонт", "Доделать ремонтные работы", TaskStatus.IN_PROGRESS);
        int epic3Id = manager.addTask(epic3);

        Subtask subtask4 = new Subtask(null, epic3Id, "Антресоль", "Купить материал", TaskStatus.IN_PROGRESS);
        int subtask4Id = manager.addTask(subtask4);

        Subtask subtask5 = new Subtask(null, epic3Id, "Пол", "Купить материал", TaskStatus.NEW);
        int subtask5Id = manager.addTask(subtask5);

        Subtask subtask6 = new Subtask(null, epic3Id, "Кухня", "Поставить кухню", TaskStatus.IN_PROGRESS);
        int subtask6Id = manager.addTask(subtask6);

        manager.getTask(task2Id);
        manager.getEpic(epic1Id);
        manager.getEpic(epic3Id);
        manager.getSubtask(subtask2Id);
        manager.getSubtask(subtask5Id);
        manager.getTask(task4Id);
        manager.getSubtask(subtask2Id);

    }
}
