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
		System.out.println("DriveTo init");
		_drivetrain.setSetpoint(_distance, DrivetrainSubsystem.CommandType.STRAIGHT);
		_drivetrain.enable();
	}

	@Override
	public void execute() {
		System.out.println("DriveTo exe");
	}

	@Override
	public boolean isFinished() {
		System.out.println(_drivetrain.getCurrentLeftPosition());
		System.out.println(_drivetrain.getCurrentRightPosition());
		return _drivetrain.isOnTarget() && false;
	}

	@Override
	public void end(boolean interrupted) {
		_drivetrain.tankDrive(0.0, 0.0);
		_drivetrain.disable();
		System.out.println("DriveTo end");
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_drivetrain);
		return requirements;
	}
}
