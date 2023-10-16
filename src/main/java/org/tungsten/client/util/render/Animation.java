package org.tungsten.client.util.render;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Range;

/**
 * An animation state
 */
public class Animation {
    double state;
    boolean invert = false;

    /**
     * Creates a new animation with an initial value
     * @param initialState The initial value of the animation
     */
    public Animation(double initialState) {
        this.state = initialState;
    }

    /**
     * Creates a new animation with the initial value set to 0. Equivalent to calling {@code new Animation(0d);}
     */
    public Animation() {
        this.state = 0;
    }

    /**
     * Sets the animation state
     * @param d The new animation state
     */
    public void setAnimation(@Range(from = 0, to = 1) double d) {
        Preconditions.checkArgument(0 <= d && d <= 1, "d is not within range 0-1");
        this.state = d;
    }

    /**
     * Ticks the animation forward. Clamped at range 0-1
     *
     * @param delta How much to tick the animation forward
     */
    public void tickAnimation(double delta) {
        if (invert) {
            delta *= -1;
        }
        state += delta;
        state = MathHelper.clamp(state, 0, 1);
    }

    /**
     * Inverts this animation. All {@link #tickAnimation(double)} calls will be inverted if this is set to true
     *
     * @param b Whether to invert this animation or not
     */
    public void setInvert(boolean b) {
        invert = b;
    }

    /**
     * Returns the animation with linear easing applied
     *
     * @return The animation state with linear easing
     */
    public double getAnimation() {
        return state;
    }

    /**
     * Returns the animation with custom easing applied
     *
     * @param easing The easing to apply
     *
     * @return The eased animation state
     */
    public double getAnimation(Easing easing) {
        return easing.apply(state);
    }

    /**
     * A collection of easings
     *
     * @author <a href="https://easings.net/">easings.net</a>, 0x150
     */
    public enum Easing {
        LINEAR(x -> x),
        SINE_IN(x -> 1 - Math.cos((x * Math.PI) / 2)),
        SINE_OUT(x -> Math.sin((x * Math.PI) / 2)),
        SINE_IN_OUT(x -> -(Math.cos(Math.PI * x) - 1) / 2),

        CUBIC_IN(x -> Math.pow(x, 3)),
        CUBIC_OUT(x -> 1 - Math.pow(1 - x, 3)),
        CUBIC_IN_OUT(x -> x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2),

        QUINT_IN(x -> Math.pow(x, 5)),
        QUINT_OUT(x -> 1 - Math.pow(1 - x, 5)),
        QUINT_IN_OUT(x -> x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2),

        CIRC_IN(x -> 1 - Math.sqrt(1 - Math.pow(x, 2))),
        CIRC_OUT(x -> Math.sqrt(1 - Math.pow(x - 1, 2))),
        CIRC_IN_OUT(x -> x < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2),

        ELASTIC_IN(x -> {
            double c4 = (2 * Math.PI) / 3;

            return x == 0 ? 0 : x == 1 ? 1 : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4);
        }),
        ELASTIC_OUT(x -> {
            double c4 = (2 * Math.PI) / 3;

            return x == 0 ? 0 : x == 1 ? 1 : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
        }),
        ELASTIC_IN_OUT(x -> {
            double c5 = (2 * Math.PI) / 4.5;

            double sin = Math.sin((20 * x - 11.125) * c5);
            return x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? -(Math.pow(2, 20 * x - 10) * sin) / 2 : (Math.pow(2, -20 * x + 10) * sin) / 2 + 1;
        }),

        QUAD_IN(x -> x * x),
        QUAD_OUT(x -> 1 - (1 - x) * (1 - x)),
        QUAD_IN_OUT(x -> x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2),

        QUART_IN(x -> x * x * x * x),
        QUART_OUT(x -> 1 - Math.pow(1 - x, 4)),
        QUART_IN_OUT(x -> x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2),

        EXPO_IN(x -> x == 0 ? 0 : Math.pow(2, 10 * x - 10)),
        EXPO_OUT(x -> x == 1 ? 1 : 1 - Math.pow(2, -10 * x)),
        EXPO_IN_OUT(x -> x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2 : (2 - Math.pow(2, -20 * x + 10)) / 2),

        BACK_IN(x -> {
            double c1 = 1.70158;
            double c3 = c1 + 1;

            return c3 * x * x * x - c1 * x * x;
        }),
        BACK_OUT(x -> {
            double c1 = 1.70158;
            double c3 = c1 + 1;

            return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
        }),
        BACK_IN_OUT(x -> {
            double c1 = 1.70158;
            double c2 = c1 * 1.525;

            return x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
        }),

        BOUNCE_OUT(x -> {
            double n1 = 7.5625;
            double d1 = 2.75;

            if (x < 1 / d1) {
                return n1 * x * x;
            } else if (x < 2 / d1) {
                return n1 * (x -= 1.5 / d1) * x + 0.75;
            } else if (x < 2.5 / d1) {
                return n1 * (x -= 2.25 / d1) * x + 0.9375;
            } else {
                return n1 * (x -= 2.625 / d1) * x + 0.984375;
            }
        }),
        BOUNCE_IN(x -> 1 - Easing.BOUNCE_OUT.apply(x)),
        BOUNCE_IN_OUT(x -> x < 0.5 ? (1 - BOUNCE_OUT.apply(1 - 2 * x)) / 2 : (1 + BOUNCE_OUT.apply(2 * x - 1)) / 2);


        private final EasingOperation func;

        Easing(EasingOperation func) {
            this.func = func;
        }

        public double apply(double d) {
            return func.apply(d);
        }

        public interface EasingOperation {
            double apply(double x);
        }
    }
}

