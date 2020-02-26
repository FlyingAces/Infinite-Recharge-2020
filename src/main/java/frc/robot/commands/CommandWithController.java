package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.ControllerMap;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;
import java.util.Map;


public class CommandWithController implements Command {
    private DrivetrainSubsystem _drive;
    private ControllerSubsystem _controller;


    public CommandWithController() {
        _drive = DrivetrainSubsystem.getInstance();
        _controller = ControllerSubsystem.getInstance();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double driveSpeed = _controller.getJoystick().getRawAxis(ControllerMap.AXIS_TRIGGER_RT) -
                _controller.getJoystick().getRawAxis(ControllerMap.AXIS_TRIGGER_LT);
        double driveAngle = _controller.getJoystick().getRawAxis(ControllerMap.LEFT_AXIS_X);

        System.out.println("Left: " + _drive.getCurrentLeftPosition() + " || Right: " + _drive.getCurrentRightPosition());

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
