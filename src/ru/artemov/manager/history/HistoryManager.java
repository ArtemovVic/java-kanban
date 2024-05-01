package ru.artemov.manager.history;

import ru.artemov.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void addTaskIdInHistory(Task task);

    void remove(int id);

    List<Task> getHistory();

}
