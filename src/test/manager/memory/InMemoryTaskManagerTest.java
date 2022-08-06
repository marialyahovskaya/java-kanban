package manager.memory;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setupManager() {
        manager = new InMemoryTaskManager();
    }

}
