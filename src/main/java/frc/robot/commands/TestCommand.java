package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.subsystems.TestSubsystem;

import java.util.HashSet;
import java.util.Set;


public class TestCommand implements Command {
	private TestSubsystem _testSubsystem;
	private double _speed;
	private int _count;

	public TestCommand(double speed) {
		_testSubsystem = TestSubsystem.getInstance();

		_speed = speed;
	}

	@Override
	public void initialize() {
		_testSubsystem.zeroPosition();
		_testSubsystem.setSetpoint(500000); // Encoder ticks
		_testSubsystem.enable();
		execute();
	}

	@Override
	public void execute() {
		System.out.println("Position: " + _testSubsystem.getMotorPosition());
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> requirements = new HashSet<>();
		requirements.add(_testSubsystem);
		return requirements;
	}

	@Override
	public void end(boolean interrupted) {
		_testSubsystem.runMotor(0.0);
		_testSubsystem.zeroPosition();
	}
}
