package smallville7123.canvas.canvastasks;

import smallville7123.canvas.canvastasks.CanvasTasks.DrawColor;
import smallville7123.example.taskbuilder.TaskList;

public class Canvas {
    public static void addTaskList(TaskList taskList) {
        TaskList canvas = taskList.add("Canvas");
        DrawColor.get(canvas);
    }
}
