package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ControlTerminalSubsystem;


public class TurnControlTerminal extends Command {
	private ControlTerminalSubsystem _controlTerminal;

	private double _speed;
	private int _rotations;

	public TurnControlTerminal(double speed, int rotations) {
		_controlTerminal = ControlTerminalSubsystem.getInstance();
		requires(_controlTerminal);

		_speed = speed;
		_rotations = rotations;
	}

	@Override
	protected void initialize() {
		execute();
	}

	@Override
	protected void execute() {
		_controlTerminal.runControlTerminal(_speed);
		System.out.println(_controlTerminal.getEncoderPosition());
	}

	@Override
	protected boolean isFinished() {
		return _controlTerminal.getEncoderPosition() >=
				((32.0 / RobotMap.Measurement.CONTROL_TERMINAL_WHEEL_DIAMETER.getInches()) * 4096) * _rotations;
	}

	@Override
	protected void end() {
		_controlTerminal.resetEncoder();
		_controlTerminal.runControlTerminal(0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
