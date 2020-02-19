package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.PneumaticSubsystem;

import java.util.HashSet;
import java.util.Set;

public class PneumaticCommand implements Command {
	private PneumaticSubsystem _pneumatic;


	public PneumaticCommand() {
		_pneumatic = PneumaticSubsystem.getInstance();
	}

	@Override
	public void initialize() {
		switch (_pneumatic.getSolenoidState()){
			case FORWARD:
				_pneumatic.runElevator(PneumaticSubsystem.SolenoidState.REVERSE);
				break;
			default:
				_pneumatic.runElevator(PneumaticSubsystem.SolenoidState.FORWARD);
				break;
		}
	}

	@Override
	public void execute() {

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
	}
}
