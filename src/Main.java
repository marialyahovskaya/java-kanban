import Task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 =
                new Task(0, "Посадить картошку", "Квадратно-гнездовой посев картофеля", "NEW");
        int task1Id = manager.addTask(task1);

        Task task2 = new Task(0, "Вскопать огород", "Копка на полтора штыка лопаты", "NEW");
        int task2Id = manager.addTask(task2);

        Epic epic1 = new Epic(0, "Уборка", "Убраться в квартире", "NEW");
        int epic1Id = manager.addEpic(epic1);

        Subtask subtask1 =
                new Subtask(0, epic1Id, "Помыть полы", "В кухне, в комнате, в ванной", "NEW");
        int subtask1Id = manager.addSubtask(subtask1);

        Subtask subtask2 =
                new Subtask(0, epic1Id, "Пропылесосить", "Ковры, диван, кресло", "NEW");
        int subtask2Id = manager.addSubtask(subtask2);

        Epic epic2 = new Epic(0, "ТО по машине", "Все необходимые мероприятия по ТО", "NEW");
        int epic2Id = manager.addEpic(epic2);

        Subtask subtask3 =
                new Subtask(0, epic2Id, "Заменить масло", "Масло, масляный фильтр", "NEW");
        int subtask3Id = manager.addSubtask(subtask3);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        task1 = manager.getTask(task1Id);
        task1.setStatus("DONE");
        manager.updateTask(task1);

        subtask1 = manager.getSubtask(subtask1Id);
        subtask1.setStatus("IN_PROGRESS");
        manager.updateSubtask(subtask1);

        subtask3 = manager.getSubtask(subtask3Id);
        subtask3.setStatus("DONE");
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
    }
}
