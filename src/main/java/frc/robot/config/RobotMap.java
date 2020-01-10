package frc.robot.config;

public class RobotMap {
    public static final int K_TIMEOUT_MS = 30;

    public enum Talon {
        LEFT_MASTER(0),
        LEFT_SLAVE(0),
        RIGHT_MASTER(0),
        RIGHT_SLAVE(0);

        private int _channel;

        Talon(int channel) {
            _channel = channel;
        }

        public int get_channel() {
            return _channel;
        }
    }

    public enum Controller {
        JOYSTICK_PORT(0),
        AXIS_TRIGGER_LT(2),
        AXIS_TRIGGER_RT(3),
        TRIGGER_LB(5),
        TRIGGER_RB(6),
        LEFT_AXIS_X(0),
        LEFT_AXIS_Y(1),
        RIGHT_AXIS_X(4),
        RIGHT_AXIS_Y(5),
        A_BUTTON(1),
        B_BUTTON(2),
        X_BUTTON(3),
        Y_BUTTON(4);

        private int _channel;

        Controller(int channel) {
            _channel = channel;
        }

        public int getChannel() {
            return _channel;
        }
    }

    public static enum Measurement {
        //width of inside of track
        ROBOT_WIDTH(0.0),
        ROBOT_LENGTH(0.0),
        ROBOT_HEIGHT(0.0),
        WHEEL_DIAMETER(0.0);

        private double _inches;

        private Measurement(double inches) {
            _inches = inches;
        }

        public double getInches() {
            return _inches;
        }
    }
}
