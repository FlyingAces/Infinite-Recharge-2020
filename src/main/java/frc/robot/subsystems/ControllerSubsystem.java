package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.DriveToCommand;
import frc.robot.commands.TestCommand;
import frc.robot.commands.TurnToCommand;
import frc.robot.config.RobotMap;


public class ControllerSubsystem implements Subsystem {
    private Joystick _joystick;

    private static ControllerSubsystem _instance;

    private ControllerSubsystem() {
        _joystick = new Joystick(RobotMap.Controller.JOYSTICK_PORT.getChannel());

        JoystickButton xButton = new JoystickButton(_joystick, RobotMap.Controller.X_BUTTON.getChannel());
        xButton.toggleWhenPressed(new DriveToCommand(12.0));

        JoystickButton bButton = new JoystickButton(_joystick, RobotMap.Controller.B_BUTTON.getChannel());
        bButton.toggleWhenPressed(new DriveToCommand(-12.0));
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
