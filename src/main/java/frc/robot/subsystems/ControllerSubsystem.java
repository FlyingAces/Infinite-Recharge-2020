package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.commands.*;
import frc.robot.config.ControllerMap;


public class ControllerSubsystem implements Subsystem {
    private Joystick _joystick;
    private static ControllerSubsystem _instance;

    private ControllerSubsystem() {
        _joystick = new Joystick(ControllerMap.JOYSTICK_PORT);

        JoystickButton xButton = new JoystickButton(_joystick, ControllerMap.X_BUTTON);
        xButton.whenPressed(new RunControlTerminalUntilColorCommand("Green"));

        JoystickButton aButton = new JoystickButton(_joystick, ControllerMap.A_BUTTON);
        aButton.whenPressed(new RunElevatorCommand());

        JoystickButton bButton = new JoystickButton(_joystick, ControllerMap.B_BUTTON);
        bButton.whenPressed(new VariableRunControlTerminalCommand(3));

        JoystickButton yButton = new JoystickButton(_joystick, ControllerMap.Y_BUTTON);
        yButton.whenPressed(new DriveToCommand(12));

        JoystickButton leftBumper = new JoystickButton(_joystick, ControllerMap.LEFT_BUMPER);
        leftBumper.whileHeld(new RunControlTerminalCommand(-1.0));

        JoystickButton rightBumper = new JoystickButton(_joystick, ControllerMap.RIGHT_BUMPER);
        rightBumper.whileHeld(new RunControlTerminalCommand(1.0));
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
