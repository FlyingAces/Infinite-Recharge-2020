package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.ControlTerminalSubsystem;

import java.util.HashSet;
import java.util.Set;


public class RunControlTerminalUntilColor implements Command {
	private ControlTerminalSubsystem _controlTerminal;
	private String _color;
	private double _speed;

	public RunControlTerminalUntilColor(String color, double speed) {
		_controlTerminal = ControlTerminalSubsystem.getInstance();

		_color = color;
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
		return _controlTerminal.getColorString().equals(_color);
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_controlTerminal);
		return requirements;
	}

	@Override
	public void end(boolean interrupted) {
		_controlTerminal.runControlTerminal(0.0);
	}
}
