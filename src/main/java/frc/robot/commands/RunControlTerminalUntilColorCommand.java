package frc.robot.commands;


import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.MotorSpeeds;
import frc.robot.subsystems.ControlTerminalSubsystem;
import frc.robot.util.Conversions;

import java.util.HashSet;
import java.util.Set;


public class RunControlTerminalUntilColorCommand implements Command {
	private ControlTerminalSubsystem _controlTerminal;
	private ControlTerminalSubsystem.ColorTypes _colorType;

	public RunControlTerminalUntilColorCommand() {
		_controlTerminal = ControlTerminalSubsystem.getInstance();
	}

	@Override
	public void initialize() {
		_colorType = Conversions.colorWheelDelta(DriverStation.getInstance().getGameSpecificMessage().toLowerCase());
		execute();
	}

	@Override
	public void execute() {
		_controlTerminal.runControlTerminal(0.75);
	}

	@Override
	public boolean isFinished() {
		return _controlTerminal.getColorType() == _colorType;
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
