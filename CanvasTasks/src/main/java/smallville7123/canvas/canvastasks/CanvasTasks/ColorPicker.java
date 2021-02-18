package smallville7123.canvas.canvastasks.CanvasTasks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.IdRes;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import UI.SpinnerView;
import smallville7123.canvas.canvastasks.R;
import smallville7123.example.taskbuilder.TaskList;
import smallville7123.example.taskbuilder.TaskParameters;
import smallville7123.inputFilter.HexRangeFilter;
import smallville7123.inputFilter.NumericRangeFilter;

import static smallville7123.example.taskbuilder.TaskList.padLeft;

public class ColorPicker implements TaskList.Builder {
    @Override
    public View generateEditView(Context context, LayoutInflater inflater, TaskList task) {
        return inflater.inflate(R.layout.color_picker, null);
    }

    private SpinnerView mSpinnerView;

    EditText alpha;
    EditText red;
    EditText green;
    EditText blue;
    ImageView picker;

    int color = Color.BLACK;
    int radix;
    String stringAlpha;
    String stringRed;
    String stringGreen;
    String stringBlue;

    EditText setFilter(View view, @IdRes int idRes, InputFilter inputFilter) {
        EditText editText = view.findViewById(idRes);
        editText.setFilters(new InputFilter[] { inputFilter });
        return editText;
    }

    String pad(String input, int radix) {
        return padLeft(input, radix == 10 ? 3 : 2, "0");
    }

    void updateStrings(int color) {
        this.color = color;
        stringAlpha = pad(Integer.toString(Color.alpha(color), radix), radix);
        stringRed = pad(Integer.toString(Color.red(color), radix), radix);
        stringGreen = pad(Integer.toString(Color.green(color), radix), radix);
        stringBlue = pad(Integer.toString(Color.blue(color), radix), radix);
        if (radix == 10) {
            colorInt = "[" + stringAlpha + ", " + stringRed + ", "
                    + stringGreen + ", " + stringBlue + "]";
            colorHex = "#" + colorToHexString(color);
        } else if (radix == 16) {
            colorInt = colorToIntString(color);
            colorHex = "#" + stringAlpha + stringRed + stringGreen + stringBlue;
        }
    }

    String colorToIntString(int color) {
        return "[" + pad(Integer.toString(Color.alpha(color), 10) , 10)+ ", "
                + pad(Integer.toString(Color.red(color), 10), 10) + ", "
                + pad(Integer.toString(Color.green(color), 10), 10) + ", "
                + pad(Integer.toString(Color.blue(color), 10), 10) + "]";
    }

    String colorToHexString(int color) {
        return pad(Integer.toString(Color.alpha(color), 16), 16)
                + pad(Integer.toString(Color.red(color), 16), 16)
                + pad(Integer.toString(Color.green(color), 16), 16)
                + pad(Integer.toString(Color.blue(color), 16), 16);
    }

    String colorInt = colorToIntString(color);
    String colorHex = "#" + colorToHexString(color);

    public int getColor() {
        return color;
    }

    public String getColorInt() {
        return colorInt;
    }

    public String getColorHex() {
        return colorHex;
    }

    void updateText() {
        alpha.setText(stringAlpha);
        red.setText(stringRed);
        green.setText(stringGreen);
        blue.setText(stringBlue);
    }

    void initDialog(Context context) {
        ColorPickerDialog mDialog = new ColorPickerDialog(context, color);
        mDialog.setAlphaSliderVisible(true);
        mDialog.setHexValueEnabled(true);
        mDialog.setOnColorChangedListener(color -> {
            picker.setImageTintList(ColorStateList.valueOf(color));
            updateStrings(color);
            updateText();
        });
        picker.setOnClickListener(v -> mDialog.show());
    }

    @Override
    public TaskParameters generateParameters() {
        return new TaskParameters() {
            int position = 0;
            @Override
            public void acquireViewIDsInEditView(View view) {
                picker = view.findViewById(R.id.picker);
                mSpinnerView = view.findViewById(R.id.SpinnerView2);
                if (position != -1) mSpinnerView.setPosition(position);


                mSpinnerView.setOnViewSelected((pos, oldView1, oldViewResId1, newView1, newViewResId1) -> {
                    position = pos;
                    if (newView1 != null) {
                        if (newViewResId1 == 0) {
                            throw new RuntimeException("newView is not null but newViewResId is 0");
                        }

                        if (newViewResId1 == R.layout.color_integer) {
                            radix = 10;
                            alpha = setFilter(newView1, R.id.alpha, new NumericRangeFilter(255));
                            red = setFilter(newView1, R.id.red, new NumericRangeFilter(255));
                            green = setFilter(newView1, R.id.green, new NumericRangeFilter(255));
                            blue = setFilter(newView1, R.id.blue, new NumericRangeFilter(255));
                        } else if (newViewResId1 == R.layout.color_hexadecimal) {
                            radix = 16;
                            alpha = setFilter(newView1, R.id.alpha, new HexRangeFilter(0xFF));
                            red = setFilter(newView1, R.id.red, new HexRangeFilter(0xFF));
                            green = setFilter(newView1, R.id.green, new HexRangeFilter(0xFF));
                            blue = setFilter(newView1, R.id.blue, new HexRangeFilter(0xFF));
                        } else {
                            throw new RuntimeException("invalid layout id");
                        }

                        alpha.addTextChangedListener((TextChangedWatcher) s -> {
                            updateStrings(Color.argb(
                                    s.length() == 0 ? 0 : Integer.parseInt(s.toString(), radix),
                                    Color.red(color),
                                    Color.green(color),
                                    Color.blue(color)
                            ));
                            picker.setImageTintList(ColorStateList.valueOf(color));
                            initDialog(view.getContext());
                        });

                        red.addTextChangedListener((TextChangedWatcher) s -> {
                            updateStrings(Color.argb(
                                    Color.alpha(color),
                                    s.length() == 0 ? 0 : Integer.parseInt(s.toString(), radix),
                                    Color.green(color),
                                    Color.blue(color)
                            ));
                            picker.setImageTintList(ColorStateList.valueOf(color));
                            initDialog(view.getContext());
                        });

                        green.addTextChangedListener((TextChangedWatcher) s -> {
                            updateStrings(Color.argb(
                                    Color.alpha(color),
                                    Color.red(color),
                                    s.length() == 0 ? 0 : Integer.parseInt(s.toString(), radix),
                                    Color.blue(color)
                            ));
                            picker.setImageTintList(ColorStateList.valueOf(color));
                            initDialog(view.getContext());
                        });

                        blue.addTextChangedListener((TextChangedWatcher) s -> {
                            updateStrings(Color.argb(
                                    Color.alpha(color),
                                    Color.red(color),
                                    Color.green(color),
                                    s.length() == 0 ? 0 : Integer.parseInt(s.toString(), radix)
                            ));
                            picker.setImageTintList(ColorStateList.valueOf(color));
                            initDialog(view.getContext());
                        });

                        picker.setImageTintList(ColorStateList.valueOf(color));
                        initDialog(view.getContext());
                        updateStrings(color);
                        updateText();
                    }
                });
                picker.setImageTintList(ColorStateList.valueOf(color));
                initDialog(view.getContext());
            }

            @Override
            public boolean checkParametersAreValid(Context context, View view) {
                return false;
            }

            @Override
            public void restoreParametersInEditView(Context context, View view) {
                if (position != -1) mSpinnerView.setPosition(position);
            }

            @Override
            public SpannableStringBuilder getParameterDescription() {
                return new DescriptionBuilder()
                        .append("color: ")
                        .appendBold(getColorInt())
                        .append(" (")
                        .appendBold(getColorHex())
                        .append(")")
                        .getBuilder();
            }

            @Override
            public DataRunnable generateAction(Context context) {
                return null;
            }

            @Override
            public void write(Kryo kryo, Output output) {
                writeClass(ColorPicker.class, output);
                output.writeInt(position);
                output.writeInt(color);
            }

            @Override
            public void read(Kryo kryo, Input input) {
                if (readClass(ColorPicker.class, input)) {
                    position = input.readInt();
                    updateStrings(input.readInt());
                }
            }
        };
    }
}
