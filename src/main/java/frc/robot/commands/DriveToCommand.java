package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.HashSet;
import java.util.Set;


public class DriveToCommand implements Command {
	private DrivetrainSubsystem _drivetrain;
	private double _distance;

	public DriveToCommand(double distance) {
		_drivetrain = DrivetrainSubsystem.getInstance();

		_distance = distance;
	}

	@Override
	public void initialize(){
		_drivetrain.setSetpoint(_distance, DrivetrainSubsystem.CommandType.STRAIGHT);
		_drivetrain.enable();
		//execute();
	}

	@Override
	public void execute() {
		//System.out.println("Left: " + _drivetrain.getCurrentLeftPosition() + " || Right: " + _drivetrain.getCurrentRightPosition());
	}

	@Override
	public boolean isFinished() {
		return _drivetrain.isOnTarget();
		//return false;
	}

	@Override
	public void end(boolean interrupted) {
		_drivetrain.tankDrive(0.0, 0.0);
		_drivetrain.disable();
		System.out.println("Finished command.");
		_drivetrain.resetEncoders();
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_drivetrain);
		return requirements;
	}
}
