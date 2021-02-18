package smallville7123.canvas.canvastasks.CanvasTasks;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import UI.SpinnerView;
import smallville7123.canvas.canvastasks.R;
import smallville7123.example.taskbuilder.TaskList;
import smallville7123.example.taskbuilder.TaskParameters;

public class PorterDuffMode implements TaskList.Builder {
    @Override
    public View generateEditView(Context context, LayoutInflater inflater, TaskList task) {
        mSpinnerView = (SpinnerView) inflater.inflate(R.layout.porterduff_mode, null);
        return mSpinnerView;
    }

    private SpinnerView mSpinnerView;

    PorterDuff.Mode mode = PorterDuff.Mode.SRC_OVER;

    static final String[] descriptions = {
            "Destination pixels covered by the source are cleared to 0",
            "The source pixels replace the destination pixels.",
            "The source pixels are discarded, leaving the destination intact.",
            "The source pixels are drawn over the destination pixels.",
            "The source pixels are drawn behind the destination pixels.",
            "Keeps the source pixels that cover the destination pixels, " +
                    "discards the remaining source and destination pixels.",
            "Keeps the destination pixels that cover source pixels, " +
                    "discards the remaining source and destination pixels.",
            "Keeps the source pixels that do not cover destination pixels.\n\n" +
                    "Discards source pixels that cover destination pixels.\n\n" +
                    "Discards all destination pixels.",
            "Keeps the destination pixels that are not covered by source pixels.\n\n" +
                    "Discards destination pixels that are covered by source pixels.\n\n" +
                    "Discards all source pixels.",
            "Discards the source pixels that do not cover destination pixels.\n\n" +
                    "Draws remaining source pixels over destination pixels.",
            "Discards the destination pixels that are not covered by source pixels.\n\n" +
                    "Draws remaining destination pixels over source pixels.",
            "Discards the source and destination pixels where source pixels cover destination pixels.\n\n" +
                    "Draws remaining source pixels.",
            "Retains the smallest component of the source and destination pixels.",
            "Retains the largest component of the source and destination pixel.",
            "Multiplies the source and destination pixels.",
            "Adds the source and destination pixels, then subtracts the source pixels multiplied by the destination.",
            "Adds the source pixels to the destination pixels and saturates the result.",
            "Multiplies or screens the source and destination depending on the destination color."
    };

    TextView mTextView;

    public static PorterDuff.Mode intToMode(int val) {
        switch (val) {
            default:
            case 0:
                return PorterDuff.Mode.CLEAR;
            case 1:
                return PorterDuff.Mode.SRC;
            case 2:
                return PorterDuff.Mode.DST;
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 4:
                return PorterDuff.Mode.DST_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 6:
                return PorterDuff.Mode.DST_IN;
            case 7:
                return PorterDuff.Mode.SRC_OUT;
            case 8:
                return PorterDuff.Mode.DST_OUT;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 10:
                return PorterDuff.Mode.DST_ATOP;
            case 11:
                return PorterDuff.Mode.XOR;
            case 12:
                return PorterDuff.Mode.DARKEN;
            case 13:
                return PorterDuff.Mode.LIGHTEN;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            case 17:
                return PorterDuff.Mode.OVERLAY;
        }
    }

    @Override
    public TaskParameters generateParameters() {
        return new TaskParameters() {
            int position = 0;
            @Override
            public void acquireViewIDsInEditView(View view) {
                if (position != -1) mSpinnerView.setPosition(position);
                mSpinnerView.setOnViewSelected((pos, oldView1, oldViewResId1, newView1, newViewResId1) -> {
                    position = pos;
                    if (newView1 != null) {
                        if (newViewResId1 == 0) {
                            throw new RuntimeException("newView is not null but newViewResId is 0");
                        }

                        if (newViewResId1 == R.layout.textview) {
                            mTextView = newView1.findViewById(R.id.textView);
                            mTextView.setText(descriptions[pos]);
                            mode = intToMode(pos);
                        } else {
                            throw new RuntimeException("invalid layout id");
                        }
                    }
                });
            }

            @Override
            public boolean checkParametersAreValid(Context context, View view) {
                return true;
            }

            @Override
            public void restoreParametersInEditView(Context context, View view) {
                if (position != -1) mSpinnerView.setPosition(position);
            }

            @Override
            public SpannableStringBuilder getParameterDescription() {
                return new DescriptionBuilder()
                        .append("PorterDuff Mode: ")
                        .appendBold(mode.name())
                        .getBuilder();
            }

            @Override
            public DataRunnable generateAction(Context context) {
                return null;
            }

            @Override
            public void write(Kryo kryo, Output output) {
                writeClass(PorterDuffMode.class, output);
                output.writeInt(position);
                output.writeString(mode.name());
            }

            @Override
            public void read(Kryo kryo, Input input) {
                if (readClass(PorterDuffMode.class, input)) {
                    position = input.readInt();
                    mode = PorterDuff.Mode.valueOf(input.readString());
                }
            }
        };
    }
}
