package ru.artemov.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<SubTask> subTasksList = new ArrayList<>();

    private LocalDateTime endEpicTime;


    public Epic(String title, String description) {
        super(title, description, Status.NEW, TypeOfTasks.EPIC);

    }

    public Epic(String title, String description, int id) {
        super(title, description, Status.NEW, TypeOfTasks.EPIC);
        this.id = id;

    }

    public Epic(Epic epic) {
        super(epic);
        this.subTasksList = epic.getSubTasksList();
        this.endEpicTime = epic.getEndEpicTime();

    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksList=" + subTasksList.size() +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    public void checkStatus(Epic epic) {
        int counterOfNEW = 0;
        int counterOfDONE = 0;

        for (SubTask subTask : epic.subTasksList) {

            if (subTask.getStatus().equals(Status.NEW)) {
                counterOfNEW++;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                counterOfDONE++;
            }
        }

        if (subTasksList.isEmpty() || counterOfNEW == epic.subTasksList.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterOfDONE == epic.subTasksList.size()) {
            epic.setStatus(Status.DONE);
        } else epic.setStatus(Status.IN_PROGRESS);

    }

    public void addSubTask(SubTask subTask) {
        subTasksList.add(subTask);
        checkStatus(subTask.getEpic());
        this.startTime = setStartEpicTime(subTask.getEpic());
        this.duration = setDurationEpic(subTask.getEpic());
        this.endEpicTime = setEndEpicTime(subTask.getEpic());
    }

    public void removeSubTask(SubTask subTask) {
        Epic epic = subTask.getEpic();
        subTasksList.remove(subTask);
        checkStatus(epic);
        this.startTime = setStartEpicTime(subTask.getEpic());
    }

    public List<SubTask> getSubTasksList() {
        return List.copyOf(subTasksList);
    }

    public LocalDateTime setEndEpicTime(Epic epic) {
        return epic.subTasksList.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(Task::getStartTime))
                .map(Task::getEndTime)
                .orElse(null);
    }


    public LocalDateTime setStartEpicTime(Epic epic) {
        return epic.subTasksList.stream()
                .filter(Objects::nonNull)
                .min(Comparator.comparing(Task::getStartTime))
                .map(Task::getStartTime)
                .orElse(null);
    }

    public Duration setDurationEpic(Epic epic) {
        return epic.subTasksList.stream()
                .filter(Objects::nonNull)
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime getEndEpicTime() {
        return endEpicTime;
    }
}
