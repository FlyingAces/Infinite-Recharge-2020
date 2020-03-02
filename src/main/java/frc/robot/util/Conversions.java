package frc.robot.util;


import frc.robot.config.RobotMap;
import frc.robot.subsystems.ControlTerminalSubsystem;


public class Conversions {
    public static double inchToEncoderPosition(double inches) {
        double circumference = RobotMap.RobotMeasurement.DRIVETRAIN_WHEEL_DIAMETER.getInches() * Math.PI;
        return ((inches / circumference) * 4.0) * 2048.0;
    }

    public static double encoderPositionToInches(double encoderPosition) {
        double circumference = RobotMap.RobotMeasurement.DRIVETRAIN_WHEEL_DIAMETER.getInches() * Math.PI;
        return (encoderPosition / (4.0 * 2048.0)) * circumference;
    }

    public static double angleToEncoderPosition(double angle) {
        double lengthOfArc = (angle / 360.0) * RobotMap.RobotMeasurement.ROBOT_WIDTH.getInches() * Math.PI;
        return inchToEncoderPosition(lengthOfArc);
    }

    public static double colorWheelToEncoderTicks(double rotations) {
        double colorWheelCircumference = Math.PI * RobotMap.FieldElementMeasurement.FIELD_CONTROL_TERMINAL.getInches();
        double robotWheelCircumference = Math.PI * RobotMap.RobotMeasurement.CONTROL_TERMINAL_WHEEL_DIAMETER.getInches();

        return ((colorWheelCircumference / robotWheelCircumference) * (1024 * 4)) * rotations;
    }

    public static double radianToDegree(double radian) {
        return (radian * 180) / Math.PI;
    }

    public static double degreeToRadian(double degree) {
        return (degree * Math.PI) / 180;
    }

    public static ControlTerminalSubsystem.ColorTypes colorWheelDelta(String fieldMessageColor) {
        if (fieldMessageColor.equals("red")) {
            return ControlTerminalSubsystem.ColorTypes.BLUE;
        }

        if (fieldMessageColor.equals("yellow")) {
            return ControlTerminalSubsystem.ColorTypes.GREEN;
        }

        if (fieldMessageColor.equals("blue")) {
            return ControlTerminalSubsystem.ColorTypes.RED;
        }

        if (fieldMessageColor.equals("green")) {
            return ControlTerminalSubsystem.ColorTypes.YELLOW;
        }

        return null;
    }
}
