package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.TestSubsystem;


public class TestCommand extends Command {
	private TestSubsystem _testSubsystem;
	private double _speed;

	public TestCommand(double speed) {
		_testSubsystem = TestSubsystem.getInstance();

		_speed = speed;
	}

	@Override
	public void initialize() {
		execute();
	}

	@Override
	public void execute() {
		_testSubsystem.runMotor(_speed);
		System.out.println(_testSubsystem.getMotorPosition());
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		_testSubsystem.runMotor(0.0);
	}
}
