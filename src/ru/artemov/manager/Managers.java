package ru.artemov.manager;

import ru.artemov.manager.history.HistoryManager;
import ru.artemov.manager.history.InMemoryHistoryManager;

public class Managers {

    public static InMemoryTaskManager getDefault() {
        HistoryManager historyManager = getDefaultHistory();
        return new InMemoryTaskManager(historyManager);
    }

    public static FileBackedTaskManager getFileBackedManager() {
        HistoryManager historyManager = getDefaultHistory();
        return new FileBackedTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
