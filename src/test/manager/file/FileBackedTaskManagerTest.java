package manager.file;

import manager.TaskManagerTest;
import manager.memory.InMemoryTaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static final String DB_FILENAME = "data/test.txt";

    @BeforeEach
    public void setupManager() {
        manager = FileBackedTaskManager.loadFromFile(new File(DB_FILENAME));
    }

    @AfterEach
    public void removeTestDatabaseFile() {
        new File(DB_FILENAME).delete();
    }
}
