package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.commands.RunControlTerminalUntilColor;
import frc.robot.commands.RunElevatorCommand;
import frc.robot.commands.TestCommand;
import frc.robot.commands.TurnControlTerminalCommand;
import frc.robot.config.ControllerMap;


public class ControllerSubsystem implements Subsystem {
    private Joystick _joystick;
    private static ControllerSubsystem _instance;

    private ControllerSubsystem() {
        _joystick = new Joystick(ControllerMap.JOYSTICK_PORT);

        JoystickButton xButton = new JoystickButton(_joystick, ControllerMap.X_BUTTON);
        xButton.whenPressed(new RunControlTerminalUntilColor("Green"));

        JoystickButton aButton = new JoystickButton(_joystick, ControllerMap.A_BUTTON);
        aButton.whenPressed(new RunElevatorCommand());

        JoystickButton bButton = new JoystickButton(_joystick, ControllerMap.B_BUTTON);
        bButton.whenPressed(new TurnControlTerminalCommand(3));

        JoystickButton yButton = new JoystickButton(_joystick, ControllerMap.Y_BUTTON);
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
