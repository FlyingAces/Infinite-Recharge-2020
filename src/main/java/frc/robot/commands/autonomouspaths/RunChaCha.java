package frc.robot.commands.autonomouspaths;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveToCommand;
import frc.robot.commands.RunElevatorCommand;
import frc.robot.commands.TurnToCommand;


public class RunChaCha extends SequentialCommandGroup {
	public RunChaCha() {
		super(new TurnToCommand(90),
				new DriveToCommand(35),
				new DriveToCommand(-35),
				new TurnToCommand(250),
				new DriveToCommand(2),
				new RunElevatorCommand(),
				new RunElevatorCommand(),
				new RunElevatorCommand(),
				new RunElevatorCommand()
				);

	}
}
