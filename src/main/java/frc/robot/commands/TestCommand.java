package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.PneumaticSubsystem;

public class TestCommand extends Command {

	public enum Direction {
		FORWARD, BACKWARD
	}

	public enum Aimer{
		intake, Shoot
	}

	private Direction _direction;
	private Aimer _aimer;

	private PneumaticSubsystem _pneumatic;

	public TestCommand(Direction direction, Aimer aimer) {
		_pneumatic = PneumaticSubsystem.getInstance();
		requires(_pneumatic);

		_direction = direction;
		_aimer = aimer;
	}

	@Override
	protected void initialize() {
		switch (_direction) {
			case FORWARD:
				_pneumatic.elevatorForward();
				break;
			case BACKWARD:
				_pneumatic.elevatorReverse();
				break;
		}
		
		switch (_aimer){
			case intake:
				_pneumatic.aimForward();
				break;
			case Shoot:
				_pneumatic.aimReverse();
				break;
		}
	}

	@Override
	protected void execute() {
		System.out.println("Compressor Current: "+_pneumatic.getCompressorCurrent());
		System.out.println("Pressure Switch value:"+_pneumatic.isCompressorPressureSwitch());

		if (_pneumatic.getCompressorCurrent() == 0){
			_pneumatic.compressorOn();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		_pneumatic.solenoidOff();
		_pneumatic.compressorOff();
	}

	@Override
	protected void interrupted() {

	}
}
