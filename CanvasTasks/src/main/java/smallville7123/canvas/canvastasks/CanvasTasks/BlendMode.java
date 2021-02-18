package smallville7123.canvas.canvastasks.CanvasTasks;

import android.content.Context;
import android.os.Build;
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

public class BlendMode implements TaskList.Builder {
    @Override
    public View generateEditView(Context context, LayoutInflater inflater, TaskList task) {
        mSpinnerView = (SpinnerView) inflater.inflate(R.layout.blendmode, null);
        return mSpinnerView;
    }

    private SpinnerView mSpinnerView;

    android.graphics.BlendMode mode = null;

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mode = android.graphics.BlendMode.SRC_OVER;
        }
    }

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
            "Adds the source pixels to the destination pixels and saturates the result.",
            "Multiplies the source and destination pixels.",
            "Adds the source and destination pixels, then subtracts the source pixels multiplied by the destination.",
            "Multiplies or screens the source and destination depending on the destination color.",
            "Retains the smallest component of the source and destination pixels.",
            "Retains the largest component of the source and destination pixel.",
            "Makes destination brighter to reflect source.",
            "Makes destination darker to reflect source.",
            "Makes destination lighter or darker, depending on source.",
            "Makes destination lighter or darker, depending on source.",
            "Subtracts darker from lighter with higher contrast.",
            "Subtracts darker from lighter with lower contrast.",
            "Multiplies the source and destination pixels.",
            "Replaces hue of destination with hue of source, leaving saturation and luminosity unchanged.",
            "Replaces saturation of destination with saturation of source, leaving hue and luminosity unchanged.",
            "Replaces hue and saturation of destination with hue and saturation of source, leaving luminosity unchanged.",
            "Replaces luminosity of destination with luminosity of source, leaving hue and saturation unchanged."
    };

    TextView mTextView;

    public static android.graphics.BlendMode intToMode(int val) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (val) {
                default:
                case 0:
                    return android.graphics.BlendMode.CLEAR;
                case 1:
                    return android.graphics.BlendMode.SRC;
                case 2:
                    return android.graphics.BlendMode.DST;
                case 3:
                    return android.graphics.BlendMode.SRC_OVER;
                case 4:
                    return android.graphics.BlendMode.DST_OVER;
                case 5:
                    return android.graphics.BlendMode.SRC_IN;
                case 6:
                    return android.graphics.BlendMode.DST_IN;
                case 7:
                    return android.graphics.BlendMode.SRC_OUT;
                case 8:
                    return android.graphics.BlendMode.DST_OUT;
                case 9:
                    return android.graphics.BlendMode.SRC_ATOP;
                case 10:
                    return android.graphics.BlendMode.DST_ATOP;
                case 11:
                    return android.graphics.BlendMode.XOR;
                case 12:
                    return android.graphics.BlendMode.PLUS;
                case 13:
                    return android.graphics.BlendMode.MODULATE;
                case 14:
                    return android.graphics.BlendMode.SCREEN;
                case 15:
                    return android.graphics.BlendMode.OVERLAY;
                case 16:
                    return android.graphics.BlendMode.DARKEN;
                case 17:
                    return android.graphics.BlendMode.LIGHTEN;
                case 18:
                    return android.graphics.BlendMode.COLOR_DODGE;
                case 19:
                    return android.graphics.BlendMode.COLOR_BURN;
                case 20:
                    return android.graphics.BlendMode.HARD_LIGHT;
                case 21:
                    return android.graphics.BlendMode.SOFT_LIGHT;
                case 22:
                    return android.graphics.BlendMode.DIFFERENCE;
                case 23:
                    return android.graphics.BlendMode.EXCLUSION;
                case 24:
                    return android.graphics.BlendMode.MULTIPLY;
                case 25:
                    return android.graphics.BlendMode.HUE;
                case 26:
                    return android.graphics.BlendMode.SATURATION;
                case 27:
                    return android.graphics.BlendMode.COLOR;
                case 28:
                    return android.graphics.BlendMode.LUMINOSITY;
            }
        }
        return null;
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
                        .append("BlendMode: ")
                        .appendBold(mode.name())
                        .getBuilder();
            }

            @Override
            public DataRunnable generateAction(Context context) {
                return null;
            }

            @Override
            public void write(Kryo kryo, Output output) {
                writeClass(BlendMode.class, output);
                output.writeInt(position);
                output.writeString(mode.name());
            }

            @Override
            public void read(Kryo kryo, Input input) {
                if (readClass(BlendMode.class, input)) {
                    position = input.readInt();
                    mode = android.graphics.BlendMode.valueOf(input.readString());
                }
            }
        };
    }
}
