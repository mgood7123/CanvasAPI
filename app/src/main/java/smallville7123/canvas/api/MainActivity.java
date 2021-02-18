package smallville7123.canvas.api;

import android.app.Activity;
import android.os.Bundle;

import smallville7123.canvas.canvastasks.CanvasApi;
import smallville7123.example.taskbuilder.TaskList;
import smallville7123.example.taskbuilder.TaskListView;

public class MainActivity extends Activity {
    TaskListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskListView = new TaskListView(this);
        setContentView(taskListView);
        TaskList list = new TaskList();
        CanvasApi.addTaskList(list);
        taskListView.setTaskList(list, 40f);
        taskListView.setOnDoneButtonClicked(() -> taskListView.writeKryoToFile(this, "Canvas") );
        taskListView.readKryoFromRelativeFilePath(this, "Canvas", list);
    }

    @Override
    protected void onPause() {
        taskListView.writeKryoToFile(this, "Canvas");
        super.onPause();
    }
}