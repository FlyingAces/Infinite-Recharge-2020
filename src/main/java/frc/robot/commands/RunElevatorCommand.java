package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.ControlTerminalSubsystem;

import java.util.HashSet;
import java.util.Set;


public class RunElevatorCommand implements Command {
	private ControlTerminalSubsystem _elevator;


	public RunElevatorCommand() {
		_elevator = ControlTerminalSubsystem.getInstance();
	}

	@Override
	public void initialize() {
		switch (_elevator.getElevatorDirection()) {
			case DOWN:
				_elevator.runElevator(ControlTerminalSubsystem.ElevatorDirection.UP);
				break;
			case UP:
				_elevator.runElevator(ControlTerminalSubsystem.ElevatorDirection.DOWN);
				break;
		}
	}

	@Override
	public void execute() {

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
		_elevator.stopElevator();
	}

}
