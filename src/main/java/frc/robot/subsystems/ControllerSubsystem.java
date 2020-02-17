package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

import frc.robot.commands.TestCommand;
import frc.robot.config.RobotMap;


public class ControllerSubsystem extends Subsystem {
    private Joystick _joystick;

    private static ControllerSubsystem _instance;

    private ControllerSubsystem() {
        _joystick = new Joystick(RobotMap.Controller.JOYSTICK_PORT.getChannel());

        JoystickButton xButton = new JoystickButton(_joystick, RobotMap.Controller.X_BUTTON.getChannel());
        xButton.whenPressed(new TestCommand());
        /*
        JoystickButton yButton = new JoystickButton(_joystick, RobotMap.Controller.Y_BUTTON.getChannel());
        yButton.toggleWhenPressed(new TestCommand(TestCommand.Direction.BACKWARD));

        JoystickButton aButton = new JoystickButton(_joystick, RobotMap.Controller.A_BUTTON.getChannel());
        aButton.whenPressed(new TestCommand(TestCommand.Direction.intake));

        JoystickButton bButton = new JoystickButton(_joystick, RobotMap.Controller.B_BUTTON.getChannel());
        bButton.whenPressed(new TestCommand(TestCommand.Direction.Shoot));

         */

    }

    public void initDefaultCommand() {

    }

    public static ControllerSubsystem getInstance() {
        if(_instance == null) {
            _instance = new ControllerSubsystem();
        }

        return _instance;
    }

    public Joystick getJoystick() {
        return _joystick;
    }
}

