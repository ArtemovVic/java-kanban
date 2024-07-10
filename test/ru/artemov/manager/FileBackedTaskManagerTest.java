package ru.artemov.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;


    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTaskManager(Managers.getDefaultHistory());
    }


    @Test
    void saveAndLoadTest() {
        Task task1 = new Task("task1", "Description1", Status.NEW);
        Epic epic1 = new Epic("epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("subTask1", "SubTaskDescription1", Status.IN_PROGRESS, epic1);

        try {
            manager.createTask(task1);
            manager.createEpic(epic1);
            manager.createSubtask(subTask1);


        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }


        try {
            FileBackedTaskManager.loadFromFile();
        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }

        assertEquals(1, manager.getAllTask().size());
        assertEquals(1, manager.getAllEpic().size());
        assertEquals(1, manager.getAllSubTask().size());
    }

    @Test
    void saveEmptyFileTest() {


        try {
            manager.save();

        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }

        try {
            FileBackedTaskManager.loadFromFile();
        } catch (ManagerSaveException e) {
            fail(e.getMessage());
        }

        // Assert that data was loaded correctly
        assertEquals(0, manager.getAllTask().size());
        assertEquals(0, manager.getAllEpic().size());
        assertEquals(0, manager.getAllSubTask().size());
    }
}