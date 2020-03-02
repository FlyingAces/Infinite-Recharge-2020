package frc.robot.config;


import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.util.Color;


public class RobotMap {
    public static final int K_TIMEOUT_MS = 30;

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
        // Masters and slaves are switched
        LEFT_MASTER(4),
        LEFT_SLAVE(3),
        RIGHT_MASTER(2),
        RIGHT_SLAVE(1),
        CONTROL_TERMINAL(5),
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
        ELEVATOR_SOLENOID_FWD_CHANNEL(2),
        ELEVATOR_SOLENOID_REV_CHANNEL(3),
        INTAKE_SOLENOID_FWD_CHANNEL(0),
        INTAKE_SOLENOID_REV_CHANNEL(1);

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
        ROBOT_LENGTH_FROM_WHEEL_CENTER(23.0),
        ROBOT_WIDTH_FROM_WHEEL_CENTER(27.0),
        ROBOT_LENGTH(32.0),
        ROBOT_HEIGHT(27.25),
        DRIVETRAIN_WHEEL_DIAMETER(6.0),
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
