package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.HashSet;
import java.util.Set;


public class TurnToCommand implements Command {
	private DrivetrainSubsystem _drivetrain;

	public TurnToCommand(double degree) {
		_drivetrain =  DrivetrainSubsystem.getInstance();

		_drivetrain.setSetpoint(degree);
	}

	@Override
	public void initialize() {
		_drivetrain.enable();
	}

	@Override
	public void execute() {

	}

	@Override
	public boolean isFinished() {
		return _drivetrain.isOnTarget();
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_drivetrain);
		return requirements;
	}

	@Override
	public void end(boolean interrupted) {
		_drivetrain.tankDrive(0.0, 0.0);
		_drivetrain.disable();
	}
}
