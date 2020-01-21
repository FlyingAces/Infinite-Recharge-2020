package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Robot;


public class ColorSensorSubsystem extends Subsystem{
    private static ColorSensorSubsystem _instance;

    public void initDefaultCommand() {

    }

    public static ColorSensorSubsystem getInstance() {
        if(_instance == null) {
            _instance = new ColorSensorSubsystem();
        }

        return _instance;
    }

}
