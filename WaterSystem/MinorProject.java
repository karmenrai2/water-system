package eecs1021;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import java.io.IOException;
import org.firmata4j.IODevice;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.Pin;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.firmata4j.firmata.FirmataDevice;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;

public class MinorProject {
    public static void main(String[]args) throws IOException, InterruptedException {
        //creating a strin with the board port location
        String Port = "/dev/cu.SLAB_USBtoUART";
        //intializing thr board as a firmata device
        IODevice arduino = new FirmataDevice(Port);

        //trying to start the board and catching if it fail
        //used from LabF Part 1
        try {
            arduino.start();
            System.out.println("Program is starting.");
            arduino.ensureInitializationIsDone();
        } catch (Exception ex) {
            System.out.println("Arduino board error, please recconect");
        } finally {

            //used Lab G Part 4 for this, Used the same line as potentiamoter but changed the pin to mach pump and sensor
            var pump = arduino.getPin(7);
            pump.setMode(Pin.Mode.OUTPUT);
            var sensor = arduino.getPin(15);
            sensor.setMode(Pin.Mode.ANALOG);

            //oled Intialization was used from LabG Part1, This intializes the oled
            I2CDevice i2cObject = arduino.getI2CDevice((byte) 0x3C);
            SSD1306 moisture_oled = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
            moisture_oled.init();

            //creating a new timertask that uses pumptask class
            //using labf part 2 button task, this uses pumptask instead
            var PumpTask = new pumptask(sensor, pump, moisture_oled);
            //timer code used from LabC Part B
            Timer timer = new Timer();
            //sets up a scheduled task for checking moisture values and using pump
            timer.schedule(PumpTask,0,1000);
        }
    }
}