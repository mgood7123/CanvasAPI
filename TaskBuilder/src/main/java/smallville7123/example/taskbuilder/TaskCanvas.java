package smallville7123.example.taskbuilder;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TaskCanvas extends FrameLayout {
    public TaskCanvas(@NonNull Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public TaskCanvas(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public TaskCanvas(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public TaskCanvas(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    final Object lock = new Object();

    ArrayList<TaskParameters.DataRunnable> list;

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (lock) {
            if (list != null) {
                for (TaskParameters.DataRunnable dataRunnable : list) {
                    dataRunnable.run(canvas);
                }
            }
        }
    }

    public void publish(ArrayList<TaskParameters.DataRunnable> dataRunnableArrayList) {
        synchronized (lock) {
            this.list = dataRunnableArrayList;
        }
    }

    public void requestDraw() {
        invalidate();
    }
}
