package ru.artemov.manager;

import ru.artemov.tasks.Epic;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaskManager {
    private static int id = 1;

    private final HashMap<Integer, Task> tasksById = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskById = new HashMap<>();
    private final HashMap<Integer, Epic> epicById = new HashMap<>();

//--------------------------- Методы для Task---------------------


    public List<Task> getAllTask() {
        return new ArrayList<>(tasksById.values());
    }

    public void deleteAllTask() {
        tasksById.clear();
    }

    public Task getTaskById(int id) {
        return tasksById.get(id);
    }

    public void createTask(Task task) {
        task.setId(id);
        tasksById.put(task.getId(), task);
        id++;
    }

    public void updateTaskById(int id, Task task) {
        task.setId(id);
        tasksById.put(id, task);
    }

    public void deleteTaskById(int id) {
        tasksById.remove(id);
    }

    //--------------------------- Методы для Epic---------------------
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epicById.values());
    }

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

    public Epic getEpicById(int id) {
        return epicById.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(id);
        epicById.put(id, epic);
        id++;
    }

    public void updateEpicById(int id, Epic epic) {
        Epic oldEpic = epicById.get(id);
        for (SubTask subTask : oldEpic.getSubTasksList()) {
            subTask.setEpic(epic);
            epic.addSubTask(subTask);
        }
        epicById.put(id, epic);
    }

    public void deleteEpicById(int id) {
        for (SubTask subTask : List.copyOf(epicById.get(id).getSubTasksList())) {
            deleteSubtaskById(subTask.getId());
        }
        epicById.remove(id);
    }

    public List<SubTask> getAllSubTaskOfEpic(int id) {
        return epicById.get(id).getSubTasksList();
    }

//--------------------------- Методы для SubTask---------------------

    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskById.values());
    }

    public void deleteAllSubTask() {
        for (SubTask subTask : subTaskById.values()) {
            subTask.getEpic().removeSubTask(subTask);
        }
        subTaskById.clear();
    }

    public SubTask getSubTaskById(int id) {
        return subTaskById.get(id);
    }

    public void createSubtask(SubTask subTask) {
        subTask.setId(id);
        subTaskById.put(subTask.getId(), subTask);
        subTask.getEpic().addSubTask(subTask);
        id++;
    }

    public void updateSubtask(int id, SubTask subTask) {
        deleteSubtaskById(id);
        subTask.setId(id);
        subTaskById.put(subTask.getId(), subTask);
        subTask.getEpic().addSubTask(subTask);

    }

    public void deleteSubtaskById(int id) {
        subTaskById.get(id).getEpic().removeSubTask(subTaskById.get(id));
        subTaskById.remove(id);


    }

}
