package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


public class Sickomode extends SequentialCommandGroup {
	public Sickomode() {
		super(new TurnToCommand(90),
				new TurnToCommand(360),
				new RunElevatorCommand(),
				new TurnToCommand(360),
				new RunElevatorCommand(),
				new TurnToCommand(360),
				new RunElevatorCommand(),
				new TurnToCommand(360),
				new RunElevatorCommand()
				);

	}
}
