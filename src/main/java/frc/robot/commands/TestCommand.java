package frc.robot.commands;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.ControlTerminalSubsystem;
import frc.robot.subsystems.PneumaticSubsystem;



import java.util.HashSet;
import java.util.Set;


public class TestCommand implements Command {
	private ControlTerminalSubsystem _controlTerminal;
	private double _speed;

	public TestCommand(double speed){
		_controlTerminal = ControlTerminalSubsystem.getInstance();

		_speed = speed;
	}

	@Override
	public void initialize() {
		execute();
	}

	@Override
	public void execute() {
		_controlTerminal.runControlTerminal(_speed);
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
		_controlTerminal.runControlTerminal(0.0);
	}

}
