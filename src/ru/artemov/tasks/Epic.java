package ru.artemov.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    private final List<SubTask> subTasksList = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW, TypeOfTasks.EPIC);

    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksList=" + subTasksList.size() +
                ", title='" + title + '\'' +

                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public  void checkStatus(Epic epic){
        int counterOfNEW = 0;
        int counterOfDONE = 0;

        for (SubTask subTask : epic.subTasksList) {

            if (subTask.getStatus().equals(Status.NEW)){
                counterOfNEW++;
            } else if (subTask.getStatus().equals(Status.DONE )){
                counterOfDONE++;
            }
        }

        if (subTasksList.isEmpty() || counterOfNEW == epic.subTasksList.size()){
            epic.setStatus(Status.NEW);
        } else if (counterOfDONE == epic.subTasksList.size()){
            epic.setStatus(Status.DONE);
        } else epic.setStatus(Status.IN_PROGRESS);

    }

    public void addSubTask(SubTask subTask){
        subTasksList.add(subTask);
        checkStatus(subTask.getEpic());
    }
    public void removeSubTask(SubTask subTask){
        Epic epic = subTask.getEpic();
        subTasksList.remove(subTask);
        checkStatus(epic);
    }

    public List<SubTask> getSubTasksList() {
        return subTasksList;
    }

}
