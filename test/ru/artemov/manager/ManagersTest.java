package ru.artemov.manager;

import org.junit.jupiter.api.Test;
import ru.artemov.manager.history.HistoryManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultNotNull() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "TaskManager не найден.");
    }

    @Test
    void getDefaultHistoryNotNull() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "historyManager не найден.");
    }
}