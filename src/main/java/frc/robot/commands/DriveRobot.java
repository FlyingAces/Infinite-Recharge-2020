package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;


public class DriveRobot extends Command {
    private DrivetrainSubsystem _drive;
    private ControllerSubsystem _controller;

    public DriveRobot() {
        super("CommandByControllerAnalog");

        _drive = DrivetrainSubsystem.getInstance();
        requires(_drive);

        _controller = ControllerSubsystem.getInstance();
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        double driveSpeed = _controller.getJoystick().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_RT.getChannel()) -
                _controller.getJoystick().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_LT.getChannel());
        double driveAngle = _controller.getJoystick().getRawAxis(RobotMap.Controller.LEFT_AXIS_X.getChannel());

        _drive.arcadeDrive(driveSpeed, driveAngle);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        _drive.tankDrive(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}