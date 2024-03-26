package ru.artemov.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach(){
         taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    void updateTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        Task updateTask = new Task("Test updateTask", "Test updateTask description", Status.IN_PROGRESS);

        final int taskId = taskManager.createTask(task);
        taskManager.updateTaskById(taskId, updateTask);

        final Task updatedTask = taskManager.getTaskById(taskId);

        assertNotNull(updatedTask, "Задача не найдена.");
        assertEquals(task, updatedTask, "Задачи не совпадают.");

    }
    @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);

        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task.getTitle(), savedTask.getTitle(), "Задачи не совпадают.");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Задачи не совпадают.");
        assertEquals(task.getId(), savedTask.getId(), "Задачи не совпадают.");
        assertEquals(task.getType(), savedTask.getType(), "Задачи не совпадают.");
        assertEquals(task.getStatus(), savedTask.getStatus(), "Задачи не совпадают.");

    }

    @Test
    void addTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);
        Task oldTask = taskManager.getTaskById(taskId);

        Task updateTask = new Task("Test updateTask", "Test updateTask description", Status.IN_PROGRESS);
        taskManager.updateTaskById(taskId, updateTask);

        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(oldTask, history.get(0), "Задачи не совпадают.");


    }

}