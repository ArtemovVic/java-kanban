package ru.artemov.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void beforeEach() {
        this.taskManager = Managers.getFileBackedManager();
    }

    @Test
    void saveAndLoadTest() {
        Task task1 = new Task("task1", "Description1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Epic epic1 = new Epic("epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("subTask1", "SubTaskDescription1", Status.IN_PROGRESS, epic1, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

        try {
            taskManager.createTask(task1);
            taskManager.createEpic(epic1);
            taskManager.createSubtask(subTask1);


        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }


        try {
            taskManager.loadFromFile();
        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }

        assertEquals(1, taskManager.getAllTask().size());
        assertEquals(1, taskManager.getAllEpic().size());
        assertEquals(1, taskManager.getAllSubTask().size());
    }

    @Test
    void saveEmptyFileTest() {


        try {
            taskManager.save();

        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }

        try {
            taskManager.loadFromFile();
        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }


        assertEquals(0, taskManager.getAllTask().size());
        assertEquals(0, taskManager.getAllEpic().size());
        assertEquals(0, taskManager.getAllSubTask().size());
    }


}