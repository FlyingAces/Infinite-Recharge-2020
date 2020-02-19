package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;


public class LimelightCameraSubsystem implements Subsystem {
    private NetworkTable _table;
    private NetworkTableEntry _tx;
    private NetworkTableEntry _ty;
    private NetworkTableEntry _ta;

    public LimelightCameraSubsystem() {
        _table = NetworkTableInstance.getDefault().getTable("limelight");
        _tx = _table.getEntry("tx");
        _ty = _table.getEntry("ty");
        _ta = _table.getEntry("ta");

        double x = _tx.getDouble(0.0);
        double y = _ty.getDouble(0.0);
        double area = _ta.getDouble(0.0);
    }

    public NetworkTable getImage() {
        return _table;
    }

    @Override
    public void setDefaultCommand(Command defaultCommand) {

    }
}
