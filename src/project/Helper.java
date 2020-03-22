package project;

import java.awt.*;
import java.util.HashMap;

public final class Helper
{
    protected static final int FPS = 120;
    protected static final int BX = 100, BY = 100;  // Background x y
    protected static final int SW  = 640, SH = 358; // Screen width and height
    protected static final int     TB = 15; // Table border width
    protected static final int     HR = 0; // Hole radius                       -------------------
    protected static       boolean FR = true; // Friction on/off
    protected static       boolean HA = false; // Holes on/off                    -------------------

   protected static final int HM = 1; // Hole margin from border                -------------------

    protected static final Color BC         = new Color(125, 92, 33); // Border color
    protected static final Color TC         = new Color(112, 104, 236); // Table color
    protected static final Color BALL_WHITE = new Color(255, 247, 200); // White
    protected static final Color BALL_RED = new Color(198, 53,53); // Red
    protected static final Color BALL_YELLOW = new Color(245, 195, 18); //Yellow


    protected static final HashMap<String, int[]> HOLES = new HashMap<String, int[]>()              //-------------
    	    {{
    	    	put("TL", new int[]{Helper.BX + TB / 2, Helper.BY + TB / 2});
    	        put("TC", new int[]{Helper.BX + SW / 2, Helper.BY + HM});
    	        put("TR", new int[]{Helper.BX + SW - (TB / 2 + (HR * 2)), Helper.BY + TB / 2});
    	      
    	    }}; 
 
    protected static final HashMap<Integer, Color[]> BALL_COLORS = new HashMap<Integer, Color[]>()         //Set the balls color
    {{
        put(0, new Color[]{BALL_WHITE, BALL_WHITE, BALL_WHITE});                           //Cue ball 
        put(1, new Color[]{new Color(245, 195, 18), BALL_YELLOW, BALL_YELLOW});            //1. ball
        put(2, new Color[]{new Color(198, 53,53), BALL_RED,  BALL_RED});                   //2. ball
    }};
}
