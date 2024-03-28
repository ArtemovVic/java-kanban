package ru.artemov.manager.history;

import ru.artemov.tasks.Task;


import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedList<Task> history;
    private final int SIZE = 10;

    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();// history имеет тип интерфейса List, но в конструкторе присваивается конкретная реализация
    }
    // Метод интерфейса List
    /*
    Removes and returns the first element of this collection (optional operation).
Throws:NoSuchElementException – if this collection is empty
UnsupportedOperationException – if this collection implementation does not support this operation
Implementation Requirements:
If this List is not empty, the implementation in this interface returns the result of calling remove(0). Otherwise, it throws NoSuchElementException.
Since:21

    default E removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return this.remove(0);
        }
    }
    */

    @Override
    public void addTaskIdInHistory(Task task) {
        if(history.size() < SIZE){
            history.add(task);
        }
        else {
            history.removeFirst();
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
