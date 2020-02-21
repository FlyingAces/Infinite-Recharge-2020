package frc.robot.config;


import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.util.Color;


public class RobotMap {
    public static final int K_TIMEOUT_MS = 30;

    public static final Color BLUE_TARGET = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public static final Color GREEN_TARGET = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public static final Color RED_TARGET = ColorMatch.makeColor(0.561, 0.232, 0.114);
    public static final Color YELLOW_TARGET = ColorMatch.makeColor(0.361, 0.524, 0.113);

    public enum FieldElementMeasurement {
        FIELD_CONTROL_TERMINAL(32.0);

        private double _inches;

        FieldElementMeasurement(double inches) {
            _inches = inches;
        }

        public double getInches() {
            return _inches;
        }
    }

    public enum Talon {
        LEFT_MASTER(0),
        LEFT_SLAVE(0),
        RIGHT_MASTER(0),
        RIGHT_SLAVE(0),
        CONTROL_TERMINAL(7),
        TEST_MOTOR(0);

        private int _channel;

        Talon(int channel) {
            _channel = channel;
        }

        public int getChannel() {
            return _channel;
        }
    }

    public enum Pneumatics {
        COMPRESSOR_MODULE(0),
        ELEVATOR_SOLENOID_FWD_CHANNEL(0),
        ELEVATOR_SOLENOID_REV_CHANNEL(2),
        INTAKE_SOLENOID_FWD_CHANNEL(1),
        INTAKE_SOLENOID_REV_CHANNEL(3);

        private int _channel;

        Pneumatics(int channel) {
            _channel = channel;
        }

        public int getChannel() {
            return _channel;
        }
    }

    public enum RobotMeasurement {
        ROBOT_WIDTH(28.0),
        ROBOT_LENGTH(32.0),
        ROBOT_HEIGHT(27.25),
        DRIVETRAIN_WHEEL_DIAMETER(0.0),
        CONTROL_TERMINAL_WHEEL_DIAMETER(4.0);

        private double _inches;

        RobotMeasurement(double inches) {
            _inches = inches;
        }

        public double getInches() {
            return _inches;
        }
    }
}
