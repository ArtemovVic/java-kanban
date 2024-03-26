package ru.artemov.manager;

import ru.artemov.tasks.Epic;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();
    void printHistory();
    //--------------------------- Методы для Task---------------------
    List<Task> getAllTask();

    void deleteAllTask();

    Task getTaskById(int id);

    int createTask(Task task);

    void updateTaskById(int id, Task task);

    void deleteTaskById(int id);

    //--------------------------- Методы для Epic---------------------
    List<Epic> getAllEpic();

    void deleteAllEpic();

    Epic getEpicById(int id);

    int createEpic(Epic epic);

    void updateEpicById(int id, Epic epic);

    void deleteEpicById(int id);

    List<SubTask> getAllSubTaskOfEpic(int id);

    //--------------------------- Методы для SubTask---------------------
    List<SubTask> getAllSubTask();

    void deleteAllSubTask();

    SubTask getSubTaskById(int id);

    int createSubtask(SubTask subTask);

    void updateSubtask(int id, SubTask subTask);

    void deleteSubtaskById(int id);
}
