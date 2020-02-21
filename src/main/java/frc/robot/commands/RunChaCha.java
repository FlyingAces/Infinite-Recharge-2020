package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


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
