package manager.memory;

import manager.HistoryManagerTest;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {
    @BeforeEach
    public void setupManager() {
        historyManager = new InMemoryHistoryManager();
    }
}
