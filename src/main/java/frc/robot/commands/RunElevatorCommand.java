package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.ControlTerminalSubsystem;


public class RunElevatorCommand extends Command {
	private ControlTerminalSubsystem _elevator;


	public RunElevatorCommand() {
		_elevator = ControlTerminalSubsystem.getInstance();
		requires(_elevator);
	}

	@Override
	protected void initialize() {
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
	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		return _elevator.isElevatorFinished();
	}

	@Override
	protected void end() {
		_elevator.stopElevator();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
