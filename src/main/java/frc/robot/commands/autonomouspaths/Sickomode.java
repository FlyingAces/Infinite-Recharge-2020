package frc.robot.commands.autonomouspaths;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.RunElevatorCommand;
import frc.robot.commands.TurnToCommand;


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
