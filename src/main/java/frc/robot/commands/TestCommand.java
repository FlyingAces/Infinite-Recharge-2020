package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.PneumaticSubsystem;


import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class TestCommand extends Command {

	private PneumaticSubsystem _pneumatic;

	public enum Direction{
		UP,DOWN
	}

	private Direction _direction;


	public TestCommand() {
		_pneumatic = PneumaticSubsystem.getInstance();
		requires(_pneumatic);

//		_direction = direction;
	}

	@Override
	protected void initialize() {
		switch (_pneumatic.getSolenoidState()){
			case FORWARD:
				_pneumatic.runElevator(PneumaticSubsystem.SolenoidState.REVERSE);
				_pneumatic.runAimer(PneumaticSubsystem.SolenoidState.REVERSE);
				break;
			case REVERSE:
				_pneumatic.runElevator(PneumaticSubsystem.SolenoidState.FORWARD);
				_pneumatic.runAimer(PneumaticSubsystem.SolenoidState.FORWARD);
				break;
			case OFF:
				_pneumatic.runElevator(PneumaticSubsystem.SolenoidState.FORWARD);
				_pneumatic.runAimer(PneumaticSubsystem.SolenoidState.FORWARD);
				break;
		}
	}

	@Override
	protected void execute() {
//		System.out.println("Compressor Current: "+_pneumatic.getCompressorCurrent());
//		System.out.println("Pressure Switch value:"+_pneumatic.isCompressorPressureSwitch());

		if (_pneumatic.getCompressorCurrent() == 0){
			_pneumatic.compressorOn();
		}
	}

	@Override
	protected boolean isFinished() {

		return _pneumatic.isElevatorFinished();
	}

	@Override
	protected void end() {
		_pneumatic.solenoidOff();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
