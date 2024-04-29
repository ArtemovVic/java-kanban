package ru.artemov.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
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
        assertEquals(1, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void deleteDuplicatesFromHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);

        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(1, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void deleteTaskFromHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);
        taskManager.getTaskById(taskId);
        taskManager.deleteTaskById(1);
        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(0, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void deleteSubTaskFromHistoryWhenDeleteEpic() {
        Epic epic1 = new Epic("Epic1", "descrEpic1");
        SubTask subTask11 = new SubTask("SubTask1", "descSub1", Status.NEW, epic1);
        SubTask subTask12 = new SubTask("SubTask2", "descSub2", Status.NEW, epic1);
        SubTask subTask13 = new SubTask("SubTask3", "descSub3", Status.NEW, epic1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subTask11);
        taskManager.createSubtask(subTask12);
        taskManager.createSubtask(subTask13);

        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getEpicById(1);

        taskManager.deleteEpicById(1);
        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(0, history.size(), "Размер истории не совпадает.");


    }

}