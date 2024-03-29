package ru.artemov.manager.history;

import ru.artemov.tasks.Task;


import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedList<Task> history;
    private final int SIZE = 10;

    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();
    }


    @Override
    public void addTaskIdInHistory(Task task) {
        if(history.size() < SIZE){
            history.add(task);
        }
        else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public void print() {
        for (Task task : history) {
            System.out.println(history.getClass());
            System.out.println(task.getId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
