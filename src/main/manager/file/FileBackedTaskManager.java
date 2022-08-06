package manager.file;

import manager.TaskManager;
import manager.memory.InMemoryTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void restoreHistory(List<Integer> history) {
        for (int id : history) {
            if (tasks.containsKey(id)) getTask(id);
            else if (epics.containsKey(id)) getEpic(id);
            else if (subtasks.containsKey(id)) getSubtask(id);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadException {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                List<Integer> history;

                int maxId = 0;

                for (int i = 1; i < lines.size(); i++) {
                    if (lines.get(i).isBlank()) {
                        history = CsvTaskFormatter.historyFromString(lines.get(i + 1));
                        taskManager.restoreHistory(history);
                        taskManager.nextId = maxId;
                        break;
                    }
                    Task task = CsvTaskFormatter.fromString(lines.get(i));
                    if (task.getId() > maxId) maxId = task.getId();
                    taskManager.addTask(task);
                }
            } catch (IOException e) {
                throw new ManagerLoadException("Ошибка при чтении файла - " + e.getMessage());
            }
        } else {
            // если файл не существует, то попробуем записать в него пустые данные.
            // Если ошибок нет, то значит сможем в него писать при сохранении и дальше
            // и не будет ошибок при следуюшем чтении
            try {
                // здесь могут быть исключения и мы их корректно поймаем и выдадим ошибки именно
                // создания файла, а не сохранения
                Files.createFile(file.toPath());
            } catch (InvalidPathException e) {
                throw new ManagerLoadException("Файл не существует и его невозможно создать (некорректное имя файла) - " + e.getMessage());
            } catch (IOException e) {
                throw new ManagerLoadException("Файл не существует и его невозможно создать");
            }
            try {
                taskManager.save();
            } catch (ManagerSaveException e) {
                throw new ManagerLoadException("Невозможно записать данные во вновь созданный файл");
            }

        }
        return taskManager;
    }

    private void save() throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(CsvTaskFormatter.CSV_HEADER);
            for (Task t : tasks.values()) {
                fw.write(CsvTaskFormatter.toString(t));
            }
            for (Epic e : epics.values()) {
                fw.write(CsvTaskFormatter.toString(e));
            }
            for (Subtask s : subtasks.values()) {
                fw.write(CsvTaskFormatter.toString(s));
            }
            fw.write("\n");
            fw.write(CsvTaskFormatter.historyToString(this.history));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла - " + e.getMessage());
        }
    }

    @Override
    public int addTask(Task task) {
        int taskId = super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return taskId;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask st) {
        super.updateSubtask(st);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return subtask;
    }
}
