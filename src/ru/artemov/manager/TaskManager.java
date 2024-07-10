package ru.artemov.manager;

import ru.artemov.tasks.Epic;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    //--------------------------- Методы для Task---------------------
    List<Task> getAllTask();

    void deleteAllTask() throws ManagerSaveException;

    Task getTaskById(int id) throws ManagerSaveException;

    int createTask(Task task) throws ManagerSaveException;

    void updateTaskById(int id, Task task) throws ManagerSaveException;

    void deleteTaskById(int id) throws ManagerSaveException;

    //--------------------------- Методы для Epic---------------------
    List<Epic> getAllEpic();

    void deleteAllEpic() throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    int createEpic(Epic epic) throws ManagerSaveException;

    void updateEpicById(int id, Epic epic) throws ManagerSaveException;

    void deleteEpicById(int id) throws ManagerSaveException;

    List<SubTask> getAllSubTaskOfEpic(int id);

    //--------------------------- Методы для SubTask---------------------
    List<SubTask> getAllSubTask();

    void deleteAllSubTask() throws ManagerSaveException;

    SubTask getSubTaskById(int id) throws ManagerSaveException;

    int createSubtask(SubTask subTask) throws ManagerSaveException;

    void updateSubtask(int id, SubTask subTask) throws ManagerSaveException;

    void deleteSubtaskById(int id) throws ManagerSaveException;
}
