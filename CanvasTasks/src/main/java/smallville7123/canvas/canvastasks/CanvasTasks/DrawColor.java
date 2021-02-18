package smallville7123.canvas.canvastasks.CanvasTasks;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import UI.SpinnerView;
import smallville7123.canvas.canvastasks.R;
import smallville7123.example.taskbuilder.TaskList;
import smallville7123.example.taskbuilder.TaskParameters;

public class DrawColor {
    private static final String TAG = "DrawColor";

    public static void get(TaskList prebuilt) {
        prebuilt.add(
                "DrawColor",
                new TaskList.Builder() {
                    final ColorPicker colorPicker = new ColorPicker();
                    View colorPickerView;
                    TaskParameters colorPickerParameters;

                    final PorterDuffMode porterDuffMode = new PorterDuffMode();
                    View porterDuffModeView;
                    TaskParameters porterDuffModeParameters;

                    final BlendMode blendMode = new BlendMode();
                    View blendModeView;
                    TaskParameters blendModeParameters;

                    @Override
                    public View generateEditView(Context context, LayoutInflater inflater, TaskList task) {
                        colorPickerView = colorPicker.generateEditView(context, inflater, task);
                        porterDuffModeView = porterDuffMode.generateEditView(context, inflater, task);
                        blendModeView = blendMode.generateEditView(context, inflater, task);
                        return inflater.inflate(R.layout.draw_color, null);
                    }

                    @Override
                    public TaskParameters generateParameters() {
                        colorPickerParameters = colorPicker.generateParameters();
                        blendModeParameters = blendMode.generateParameters();
                        porterDuffModeParameters = porterDuffMode.generateParameters();
                        return new TaskParameters() {
                            SpinnerView mSpinnerView;

                            @IdRes int viewIdRes = 0;
                            FrameLayout oldContainer;
                            FrameLayout currentContainer;

                            FrameLayout oldPortContainer;
                            FrameLayout currentPortContainer;

                            FrameLayout oldBlendContainer;
                            FrameLayout currentBlendContainer;
                            boolean isPorterDuffMode = false;
                            boolean isBlendMode = false;

                            int position = -1;

                            @Override
                            public void acquireViewIDsInEditView(View view) {
                                mSpinnerView = view.findViewById(R.id.SpinnerView);
                                if (position != -1) mSpinnerView.setPosition(position);

                                mSpinnerView.setOnViewSelected((pos, oldView, oldViewResId, newView, newViewResId) -> {
                                    position = pos;
                                    viewIdRes = newViewResId;

                                    if (newView != null) {
                                        if (newViewResId == 0) {
                                            throw new RuntimeException("newView is not null but newViewResId is 0");
                                        }

                                        oldContainer = currentContainer;
                                        if (oldContainer != null) {
                                            if (oldContainer.getChildCount() == 1) {
                                                oldContainer.removeViewAt(0);
                                            }
                                        }

                                        oldPortContainer = currentPortContainer;
                                        if (oldPortContainer != null) {
                                            if (oldPortContainer.getChildCount() == 1) {
                                                oldPortContainer.removeViewAt(0);
                                            }
                                        }

                                        oldBlendContainer = currentBlendContainer;
                                        if (oldBlendContainer != null) {
                                            if (oldBlendContainer.getChildCount() == 1) {
                                                oldBlendContainer.removeViewAt(0);
                                            }
                                        }

                                        currentContainer = null;
                                        currentPortContainer = null;
                                        currentBlendContainer = null;

                                        currentContainer = newView.findViewById(R.id.colorContainer);
                                        currentContainer.addView(colorPickerView);
                                        colorPickerParameters.acquireViewIDsInEditView(colorPickerView);
                                        isPorterDuffMode = false;
                                        isBlendMode = false;

                                        if (newViewResId == R.layout.draw_color_int_porterduff_mode) {
                                            isPorterDuffMode = true;
                                            currentPortContainer = newView.findViewById(R.id.PorterDuffModeContainer);
                                            currentPortContainer.addView(porterDuffModeView);
                                            porterDuffModeParameters.acquireViewIDsInEditView(porterDuffModeView);
                                        } else if (newViewResId == R.layout.draw_color_int_blendmode || newViewResId == R.layout.draw_color_long_blendmode) {
                                            isBlendMode = true;
                                            currentBlendContainer = newView.findViewById(R.id.BlendModeContainer);
                                            currentBlendContainer.addView(blendModeView);
                                            blendModeParameters.acquireViewIDsInEditView(blendModeView);
                                        }
                                    }
                                });
                            }

                            @Override
                            public boolean checkParametersAreValid(Context context, View view) {
                                return blendModeParameters.checkParametersAreValid(context, blendModeView) && porterDuffModeParameters.checkParametersAreValid(context, porterDuffModeView);
                            }

                            @Override
                            public void restoreParametersInEditView(Context context, View view) {
                                if (position != -1) mSpinnerView.setPosition(position);
                                blendModeParameters.restoreParametersInEditView(context, blendModeView);
                                porterDuffModeParameters.restoreParametersInEditView(context, porterDuffModeView);
                            }

                            @Override
                            public SpannableStringBuilder getParameterDescription() {
                                DescriptionBuilder descriptionBuilder = new DescriptionBuilder()
                                        .append(colorPickerParameters.getParameterDescription());
                                if (isBlendMode) {
                                    descriptionBuilder
                                            .append(", ")
                                            .append(blendModeParameters.getParameterDescription());
                                }
                                if (isPorterDuffMode) {
                                    descriptionBuilder
                                            .append(", ")
                                            .append(porterDuffModeParameters.getParameterDescription());
                                }
                                return descriptionBuilder.getBuilder();
                            }

                            @Override
                            public DataRunnable generateAction(Context context) {
                                return data -> {
                                    Canvas canvas = (Canvas) data;
                                    if (viewIdRes == R.layout.draw_color_int) {
                                        canvas.drawColor(colorPicker.color);
                                    } else if (viewIdRes == R.layout.draw_color_long) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            // TODO
//                                            canvas.drawColor((long) colorPicker.color);
                                            canvas.drawColor(colorPicker.color);
                                        } else {
                                            canvas.drawColor(colorPicker.color);
                                        }
                                    } else if (viewIdRes == R.layout.draw_color_int_porterduff_mode) {
                                        canvas.drawColor(colorPicker.color, porterDuffMode.mode);
                                    } else if (viewIdRes == R.layout.draw_color_int_blendmode) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            canvas.drawColor(colorPicker.color, blendMode.mode);
                                        } else {
                                            canvas.drawColor(colorPicker.color);
                                        }
                                    } else if (viewIdRes == R.layout.draw_color_long_blendmode) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            // TODO
//                                            canvas.drawColor((long) colorPicker.color, blendMode.mode);
                                            canvas.drawColor(colorPicker.color, blendMode.mode);
                                        } else {
                                            canvas.drawColor(colorPicker.color);
                                        }
                                    }
                                };
                            }

                            /**
                             * Writes the bytes for the object to the output.
                             *
                             * @param kryo
                             * @param output
                             */
                            @Override
                            public void write(Kryo kryo, Output output) {
                                writeClass(DrawColor.class, output);
                                output.writeInt(position);
                                colorPickerParameters.write(kryo, output);
                                output.writeBoolean(isBlendMode);
                                if (isBlendMode) {
                                    blendModeParameters.write(kryo, output);
                                }
                                output.writeBoolean(isPorterDuffMode);
                                if (isPorterDuffMode) {
                                    porterDuffModeParameters.write(kryo, output);
                                }
                            }

                            /**
                             * Reads bytes and returns a new object of the specified concrete type.
                             *
                             * @param kryo
                             * @param input
                             */
                            @Override
                            public void read(Kryo kryo, Input input) {
                                if (readClass(DrawColor.class, input)) {
                                    position = input.readInt();
                                    colorPickerParameters.read(kryo, input);
                                    isBlendMode = input.readBoolean();
                                    if (isBlendMode) {
                                        blendModeParameters.read(kryo, input);
                                    }
                                    isPorterDuffMode = input.readBoolean();
                                    if (isPorterDuffMode) {
                                        porterDuffModeParameters.read(kryo, input);
                                    }
                                }
                            }
                        };
                    }
                }
        );
    }
}
