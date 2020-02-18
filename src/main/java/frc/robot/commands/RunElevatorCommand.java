package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.PneumaticSubsystem;

import java.util.HashSet;
import java.util.Set;


public class RunElevatorCommand implements Command {
	private PneumaticSubsystem _elevator;

	public RunElevatorCommand() {
		_elevator = PneumaticSubsystem.getInstance();
	}

	@Override
	public void initialize() {
		switch (_elevator.getSolenoidState()){
			case FORWARD:
				_elevator.runElevator(PneumaticSubsystem.SolenoidState.REVERSE);
				break;
			case REVERSE:
				_elevator.runElevator(PneumaticSubsystem.SolenoidState.FORWARD);
				break;
			case OFF:
				_elevator.runElevator(PneumaticSubsystem.SolenoidState.FORWARD);
				break;
		}
	}

	@Override
	public void execute() {
		if (_elevator.isCompressorPressureSwitch()){
			_elevator.compressorOn();
		}
	}

	@Override
	public boolean isFinished() {
		return _elevator.isElevatorFinished();
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_elevator);
		return requirements;
	}

	@Override
	public void end(boolean interrupted) {
		_elevator.solenoidOff();
		_elevator.compressorOff();
	}
}
