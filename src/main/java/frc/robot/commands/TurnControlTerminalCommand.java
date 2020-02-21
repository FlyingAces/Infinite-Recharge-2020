package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.RobotMap;

import java.util.HashSet;
import java.util.Set;


public class TurnControlTerminalCommand implements Command {
	private ControlTerminalSubsystem _controlTerminal;

	private double _speed;
	private int _rotations;

	public TurnControlTerminalCommand(double speed, int rotations) {
		_controlTerminal = ControlTerminalSubsystem.getInstance();

		_speed = speed;
		_rotations = rotations;
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
		return _controlTerminal.getEncoderPosition() >=
				((RobotMap.FieldElementMeasurement.FIELD_CONTROL_TERMINAL.getInches() / RobotMap.RobotMeasurement.CONTROL_TERMINAL_WHEEL_DIAMETER.getInches()) * 4096) * _rotations;
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
		_controlTerminal.resetEncoder();
	}
}
