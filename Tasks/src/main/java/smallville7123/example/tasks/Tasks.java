package smallville7123.example.tasks;

import smallville7123.example.taskbuilder.TaskList;
import smallville7123.example.tasks.tasks.Toast;

public class Tasks {
    public static void addTaskList(TaskList taskList) {
        TaskList prebuilt = taskList.add("prebuilt");
        Toast.get(prebuilt);
    }
}
