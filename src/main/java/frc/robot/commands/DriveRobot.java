package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.HashSet;
import java.util.Set;


public class DriveRobot implements Command {
    private DrivetrainSubsystem _drive;
    private ControllerSubsystem _controller;

    public DriveRobot() {
        _drive = DrivetrainSubsystem.getInstance();
        _controller = ControllerSubsystem.getInstance();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double driveSpeed = _controller.getJoystick().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_RT.getChannel()) -
                _controller.getJoystick().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_LT.getChannel());
        double driveAngle = _controller.getJoystick().getRawAxis(RobotMap.Controller.LEFT_AXIS_X.getChannel());

        _drive.arcadeDrive(driveSpeed, driveAngle);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        Set<Subsystem> requirements = new HashSet<>();
        requirements.add(_drive);
        return requirements;
    }

    @Override
    public void end(boolean interrupted) {
        _drive.tankDrive(0, 0);
    }
}
