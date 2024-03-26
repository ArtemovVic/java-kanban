package ru.artemov.manager;

import ru.artemov.manager.history.HistoryManager;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;

    private final HashMap<Integer, Task> tasksById;
    private final HashMap<Integer, SubTask> subTaskById;
    private final HashMap<Integer, Epic> epicById;


    protected HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasksById = new HashMap<>();
        this.subTaskById = new HashMap<>();
        this.epicById = new HashMap<>();
        this.historyManager = historyManager;
    }

    //--------------------------- Методы для Task---------------------


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    @Override
    public void printHistory(){
        historyManager.print();
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasksById.values());
    }

    @Override
    public void deleteAllTask() {
        tasksById.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addTaskIdInHistory(new Task(tasksById.get(id)));
        return tasksById.get(id);
    }

    @Override
    public int createTask(Task task) {
        id++;
        task.setId(id);
        tasksById.put(task.getId(), task);
        return id;
    }

    @Override
    public void updateTaskById(int id, Task task) {
        task.setId(id);
        tasksById.put(id, task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasksById.remove(id);
    }

    //--------------------------- Методы для Epic---------------------
    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epicById.values());
    }

    @Override
    public void deleteAllEpic() {
        for (Epic epic : epicById.values()) {
            for (SubTask subTask : epic.getSubTasksList()) {
                if (subTaskById.containsValue(subTask)) {
                    subTaskById.remove(subTask.getId());
                }
            }
        }
        epicById.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addTaskIdInHistory(new Epic(epicById.get(id)));
        return epicById.get(id);
    }

    @Override
    public int createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epicById.put(id, epic);
        return id;
    }

    @Override
    public void updateEpicById(int id, Epic epic) {
        Epic oldEpic = epicById.get(id);
        for (SubTask subTask : oldEpic.getSubTasksList()) {
            subTask.setEpic(epic);
            epic.addSubTask(subTask);
        }
        epicById.put(id, epic);
    }

    @Override
    public void deleteEpicById(int id) {
        for (SubTask subTask : List.copyOf(epicById.get(id).getSubTasksList())) {
            deleteSubtaskById(subTask.getId());
        }
        epicById.remove(id);
    }

    @Override
    public List<SubTask> getAllSubTaskOfEpic(int id) {
        return epicById.get(id).getSubTasksList();
    }

//--------------------------- Методы для SubTask---------------------

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskById.values());
    }

    @Override
    public void deleteAllSubTask() {
        for (SubTask subTask : subTaskById.values()) {
            subTask.getEpic().removeSubTask(subTask);
        }
        subTaskById.clear();
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.addTaskIdInHistory(new SubTask(subTaskById.get(id)));
        return subTaskById.get(id);
    }

    @Override
    public int createSubtask(SubTask subTask) {
        id++;
        subTask.setId(id);
        subTaskById.put(subTask.getId(), subTask);
        subTask.getEpic().addSubTask(subTask);
        return id;
    }

    @Override
    public void updateSubtask(int id, SubTask subTask) {
        deleteSubtaskById(id);
        subTask.setId(id);
        subTaskById.put(subTask.getId(), subTask);
        subTask.getEpic().addSubTask(subTask);

    }

    @Override
    public void deleteSubtaskById(int id) {
        subTaskById.get(id).getEpic().removeSubTask(subTaskById.get(id));
        subTaskById.remove(id);


    }


}
