package eecs1021;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.Pin;
import java.io.IOException;


public class pumptask extends TimerTask {
    //used LabF Part 2 and 3 for this part, specfically the button task and pin construction but replaced with objects for this project
    //sets up the constructos for the moisture sensor, pump, oled display and hashmap
    private Pin mySensorPin;
    private Pin myPumpPin;
    //used Lab G Part 4 to create oled display constructor
    private final SSD1306 display;

    //class contructor
    //used LAB G Part 4 for this part
    pumptask(Pin pin, Pin pump, SSD1306 display){
        //creats objects in this class
        this.mySensorPin = pin;
        this.myPumpPin = pump;
        this.display = display;
    }
    //this will occur with schedule
    //used Lab G button listner for this
    @Override
    public void run() {
        int timecounter = 0; // sets up i to be used as the x valye for the graph
        //variables for various parts of the program
        int moisutre_threshold = 600; //the point at which the plant changed from dry to wet
        int pump_on = 1; //pump on value
        int pump_off = 0; //pump off value
        int graph_resetlimit = 1500; //the limit at which the graph resets
        String sensor_message; //intialize the message to to oled
        String console_message; //intialize the message to the console
        //setting up hashmap as data pairs
        //used module 10, graphing data pairs in java video for this
        HashMap <Integer,Double> graphingvalues = new HashMap<>();
        for (;;) { //executes this code in an infiite loop source: https://www.baeldung.com/infinite-loops-java
            //used potentiometer task from Lab G Part 4
            //creates a string that is the value from the moisture sensor
            String sensvalue = String.valueOf(mySensorPin.getValue());
            //conditional statemnts for the moisture sensor
            if (this.mySensorPin.getValue() >= moisutre_threshold) { //checks if the moisture level is low, executes once reaching this number
                try { //try catch becuase the .setvalye for pump won't work otherwise
                    myPumpPin.setValue(pump_on); //sets value of the pump to be on
                } catch (Exception ex) {
                } finally {
                    console_message = "moisture level is low: "; //changed the message displayed to the console
                    sensor_message = "Pump Status: ON, "; //changes the message displayed to the oled
                }
            } else { //for any other value the plant will be wet
                try { //try catch becuase the .setvalye for pump won't work otherwise
                    myPumpPin.setValue(pump_off); //sets value of the pump to be off
                } catch (Exception ex) {
                } finally {
                    console_message = "moisture level is good: "; //changes the message displayed to the oled
                    sensor_message = "Pump Status: OFF, "; //changes the message displayed to the oled
                }
            }
            //used LABG button listener for this part, instead of displaying pot value it displays sensor
            System.out.println(console_message); //prints out the moisture level status to the console
            display.getCanvas().clear(); //cleats the oled display
            display.getCanvas().drawString(0,0,sensor_message + sensvalue); //displays the pump status and sensor value on the oled
            display.display();

            if (timecounter > graph_resetlimit) { //clears graph once it's reached 1500
                StdDraw.clear();
            }

            //the values from the moisture sensor are put into the hashmap
            //used from module 10 video on graphing Data pairs
            graphingvalues.put(timecounter, (double) mySensorPin.getValue());
            //sets up graph, also from module 10
            StdDraw.setXscale(0, graph_resetlimit); //X-Axis up to the reset limit
            StdDraw.setYscale(0, 721); //Y-Axis moisture values usually don't go above 721
            //sets the colour of the pen to black and thickness
            //used from module 10 making a simple graph using stddraw
            StdDraw.setPenRadius(0.005);
            StdDraw.setPenColor(StdDraw.BLACK);
            //Drawing the axis lines
            //This was used from the module 10 video making a simple graph using a java StdLib
            StdDraw.line(0, 0, 0, 721); //y-axis line
            StdDraw.line(0, 0, 1500, 0); //x-axis line
            //these are the lables for the voltage, seconds and the maximum value
            //the code for the lables was also from module 10
            StdDraw.text(-50, 361, "v");
            StdDraw.text(750, -30, "s");
            StdDraw.text(1500, -30, "1500");
            //this is the for each loop that is used to populate the graph
            //also from module 10 video
            //draws a star for each point using StdDraw.text
            graphingvalues.forEach((xValue, yValue) -> StdDraw.text(xValue, yValue, "*"));

            StdDraw.show();

            timecounter++; //incroments the timecounter so that the x value continues
        }

    }

}

