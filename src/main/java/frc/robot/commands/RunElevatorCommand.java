package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.ControlTerminalSubsystem;

import java.util.HashSet;
import java.util.Set;


public class RunElevatorCommand implements Command {
	private ControlTerminalSubsystem _pneumatic;

	public RunElevatorCommand() {
		_pneumatic = ControlTerminalSubsystem.getInstance();
	}

	@Override
	public void initialize() {
		switch (_pneumatic.getSolenoidState()){
			case UP:
				_pneumatic.runElevator(ControlTerminalSubsystem.SolenoidState.DOWN);
				break;
			default:
				_pneumatic.runElevator(ControlTerminalSubsystem.SolenoidState.UP);
				break;
		}
	}

	@Override
	public void execute() {
		if (_pneumatic.isCompressorPressureSwitch()){
			_pneumatic.compressorOn();
		}
	}

	@Override
	public boolean isFinished(){
		return _pneumatic.isElevatorFinished();
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_pneumatic);
		return requirements;
	}

	@Override
	public void end(boolean interrupted) {
		_pneumatic.solenoidOff();
		//System.out.println("Command ended.");
	}
}
