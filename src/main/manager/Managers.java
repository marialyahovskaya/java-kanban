package manager;

import manager.file.FileBackedTasksManager;
import manager.file.ManagerLoadException;
import manager.memory.InMemoryHistoryManager;
import manager.memory.InMemoryTaskManager;

import java.io.File;

public final class Managers {

    private static final String DB_FILENAME = "data/data.txt";

    public static TaskManager getDefault() {
        try {
            TaskManager manager;
            manager = FileBackedTasksManager.loadFromFile(new File(DB_FILENAME));
            return manager;
        } catch (ManagerLoadException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
