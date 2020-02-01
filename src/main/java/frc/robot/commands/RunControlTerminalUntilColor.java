package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.ControlTerminalSubsystem;


public class RunControlTerminalUntilColor extends Command {
	private ControlTerminalSubsystem _controlTerminal;
	private String _color;
	private double _speed;

	public RunControlTerminalUntilColor(String color, double speed) {
		_controlTerminal = ControlTerminalSubsystem.getInstance();

		_color = color;
		_speed = speed;
	}

	@Override
	protected void initialize() {
		execute();
	}

	@Override
	protected void execute() {
		_controlTerminal.runControlTerminal(_speed);
	}

	@Override
	protected boolean isFinished() {
		return _controlTerminal.getColorString().equals(_color);
	}

	@Override
	protected void end() {
		_controlTerminal.runControlTerminal(0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
