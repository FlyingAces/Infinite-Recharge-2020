package frc.robot.commands;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.PneumaticSubsystem;

import java.util.HashSet;
import java.util.Set;


public class TestCommand implements Command {

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {

	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		//requirements.add(subsystem);
		return requirements;	}

	@Override
	public void end(boolean interrupted) {
	}

}
