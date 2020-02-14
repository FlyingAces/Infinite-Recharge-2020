package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.ControlTerminalSubsystem;


public class RunElevatorCommand extends Command {
	private ControlTerminalSubsystem _elevator;

	public enum Direction {
		FORWARD, BACKWARD
	}


	private Direction _direction;


	public RunElevatorCommand(Direction direction) {
		_elevator = ControlTerminalSubsystem.getInstance();
		requires(_elevator);

		_direction = direction;
	}

	@Override
	protected void initialize() {
		/*
		switch (_elevator.getElevatorDirection()) {
			case DOWN:
				_elevator.runElevator(ControlTerminalSubsystem.ElevatorDirection.UP);
				break;
			case UP:
				_elevator.runElevator(ControlTerminalSubsystem.ElevatorDirection.DOWN);
				break;
		}

		 */
		switch (_direction) {
			case FORWARD:
				_elevator.solenoidForward();
				break;
			case BACKWARD:
				_elevator.solenoidReverse();
				break;
		}
	}

	@Override
	protected void execute() {
		System.out.println("Compressor Current: "+_elevator.getCompressorCurrent());
		System.out.println("Pressure Switch value:"+_elevator.isCompressorPressureSwitch());
	}

	@Override
	protected boolean isFinished() {
		return _elevator.isElevatorFinished();
	}

	@Override
	protected void end() {
		_elevator.stopElevator();
		_elevator.solenoidOff();
		_elevator.compressorOff();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
