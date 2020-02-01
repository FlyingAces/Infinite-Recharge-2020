package frc.robot.util;

import frc.robot.config.RobotMap;

public class Conversions {
    public static double inchToEncoderPosition(double inches) {
        double circumference = RobotMap.Measurement.DRIVETRAIN_WHEEL_DIAMETER.getInches() * Math.PI;
        return ((inches / circumference) * 4.0) * 1024.0;
    }

    public static double encoderPositionToInches(double encoderPosition) {
        double circumference = RobotMap.Measurement.DRIVETRAIN_WHEEL_DIAMETER.getInches() * Math.PI;
        return (encoderPosition / (4.0 * 1024.0)) * circumference;
    }

    public static double angleToEncoderPosition(double angle) {
        double lengthOfArc = (angle / 360.0) * RobotMap.Measurement.ROBOT_WIDTH.getInches() * Math.PI;
        return inchToEncoderPosition(lengthOfArc);
    }

    public static double radianToDegree(double radian) {
        return (radian * 180) / Math.PI;
    }

    public static double degreeToRadian(double degree) {
        return (degree * Math.PI) / 180;
    }
}
