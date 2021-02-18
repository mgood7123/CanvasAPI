package smallville7123.canvas.canvastasks;

import smallville7123.example.taskbuilder.TaskList;

public class CanvasApi {
    public static void addTaskList(TaskList taskList) {
        Canvas.addTaskList(taskList);
        Paint.addTaskList(taskList);
    }
}
