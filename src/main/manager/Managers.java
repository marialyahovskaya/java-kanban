package manager;

import manager.file.FileBackedTaskManager;
import manager.file.ManagerLoadException;
import manager.memory.InMemoryHistoryManager;

import java.io.File;

public final class Managers {

    private static final String DB_FILENAME = "data/data.txt";

    public static TaskManager getDefault() {
        try {
            TaskManager manager;
            manager = FileBackedTaskManager.loadFromFile(new File(DB_FILENAME));
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
