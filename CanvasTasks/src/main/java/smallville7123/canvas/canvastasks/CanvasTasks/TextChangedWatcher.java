package smallville7123.canvas.canvastasks.CanvasTasks;

import android.text.Editable;
import android.text.TextWatcher;

public interface TextChangedWatcher extends TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    void afterTextChanged(Editable s);
}
