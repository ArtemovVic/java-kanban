package ru.artemov.manager;

import org.junit.jupiter.api.Test;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void addNewTask() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
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
    void updateTask() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Task updateTask = new Task("Test updateTask", "Test updateTask description", Status.IN_PROGRESS, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

        final int taskId = taskManager.createTask(task);
        taskManager.updateTaskById(taskId, updateTask);

        final Task updatedTask = taskManager.getTaskById(taskId);

        assertNotNull(updatedTask, "Задача не найдена.");
        assertEquals(task, updatedTask, "Задачи не совпадают.");

    }

    @Test
    void createTask() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

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
    void addTaskInHistory() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        final int taskId = taskManager.createTask(task);
        Task oldTask = taskManager.getTaskById(taskId);

        Task updateTask = new Task("Test updateTask", "Test updateTask description", Status.IN_PROGRESS, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        taskManager.updateTaskById(taskId, updateTask);

        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(oldTask, history.get(0), "Задачи не совпадают.");
        assertEquals(1, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void deleteDuplicatesFromHistory() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        final int taskId = taskManager.createTask(task);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);

        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(1, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void deleteTaskFromHistory() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        final int taskId = taskManager.createTask(task);
        taskManager.getTaskById(taskId);
        taskManager.deleteTaskById(taskId);
        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(0, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void deleteSubTaskFromHistoryWhenDeleteEpic() throws ManagerSaveException {
        Epic epic1 = new Epic("Epic1", "descrEpic1");
        SubTask subTask11 = new SubTask("SubTask1", "descSub1", Status.NEW, epic1, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask12 = new SubTask("SubTask2", "descSub2", Status.NEW, epic1, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask13 = new SubTask("SubTask3", "descSub3", Status.NEW, epic1, LocalDateTime.of(2023, 1, 3, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        final int epicId = taskManager.createEpic(epic1);
        final int subTaskId1 = taskManager.createSubtask(subTask11);
        final int subTaskId2 = taskManager.createSubtask(subTask12);
        final int subTaskId3 = taskManager.createSubtask(subTask13);

        taskManager.getSubTaskById(subTaskId1);
        taskManager.getSubTaskById(subTaskId2);
        taskManager.getSubTaskById(subTaskId3);
        taskManager.getEpicById(epicId);

        taskManager.deleteEpicById(epicId);
        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не найдена.");
        assertEquals(0, history.size(), "Размер истории не совпадает.");


    }

    @Test
    void checkStatusOfEpicForAllNewTasks() {
        Epic epic1 = new Epic("epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("subTask1", "SubTaskDescription1", Status.NEW, epic1, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask2 = new SubTask("subTask1", "SubTaskDescription1", Status.NEW, epic1, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

        final int epicId = taskManager.createEpic(epic1);
        taskManager.createSubtask(subTask1);
        taskManager.createSubtask(subTask2);

        assertEquals(taskManager.getEpicById(epicId).getStatus(), Status.NEW, "Статус рассчитан неверно.");
    }

    @Test
    void checkStatusOfEpicForAllDoneTasks() {
        Epic epic1 = new Epic("epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("subTask1", "SubTaskDescription1", Status.DONE, epic1, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask2 = new SubTask("subTask1", "SubTaskDescription1", Status.DONE, epic1, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

        final int epicId = taskManager.createEpic(epic1);
        taskManager.createSubtask(subTask1);
        taskManager.createSubtask(subTask2);

        assertEquals(taskManager.getEpicById(epicId).getStatus(), Status.DONE, "Статус рассчитан неверно.");
    }

    @Test
    void checkStatusOfEpicForTasks() {
        Epic epic1 = new Epic("epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("subTask1", "SubTaskDescription1", Status.NEW, epic1, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask2 = new SubTask("subTask1", "SubTaskDescription1", Status.DONE, epic1, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

        final int epicId = taskManager.createEpic(epic1);
        taskManager.createSubtask(subTask1);
        taskManager.createSubtask(subTask2);

        assertEquals(taskManager.getEpicById(epicId).getStatus(), Status.IN_PROGRESS, "Статус рассчитан неверно.");
    }


}