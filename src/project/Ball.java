package project;

import java.awt.*; 

public final class Ball
{
    protected double x, y; 
    protected int    r, number;
    protected double dx, dy;
    private   double ddy, ddx;

    private void setXY(int number)  //Ball location
    {
        int row = (int) Math.ceil((-1 + Math.sqrt(1 + 8 * number)) / 2);
        int col = row * (row - 1) / 2; //optimize the code

        if (number == 0)  
        {
            x = Helper.BX + Helper.SW / 4 - r; 
            y = Helper.BY + Helper.SH / 2 - r; 
        }
        else  
        {
            x = (Helper.BX + Helper.SW - Helper.SW / 4) + (- (r * 2 * (6 - row))) + 4 - row;
            y = (Helper.BY + Helper.SH / 2) - (r * 2 * (((row * (row + 1) / 2) - 1) - number)) + (row - 3) * r - r;
        }
    } 

    Ball(int number)          //size of the balls
    {
        r = 10;
        this.number = number;
        setXY(number);       
    }


    public void update()      // Location due to friction codes          
    {
        if (dy != 0 || dx != 0)
        {
            y += dy;
            x += dx;

            if(Helper.FR) handleBallFriction();
        }
    }

    protected void handleBounds()                       //Set the balls shoot
    {
        boolean bound = false;

        if (x > Helper.BX + Helper.SW - (Helper.TB + 2 * r))      //Game play area for white ball on x axes
        {
            dx = -Math.abs(dx);
            ddx = Math.abs(ddx);
            bound = true;
        }
        else if (x < Helper.BX + Helper.TB)
        {
            dx = Math.abs(dx);             //takes the absolute value of balls stays in table
            ddx = -Math.abs(ddx);          
            bound = true;
        }

        if (y < Helper.BY + Helper.TB) 
        {
            dy = Math.abs(dy);
            ddy = -Math.abs(ddy); 
            bound = true;
        }
        else if (y > Helper.BY + Helper.SH - (Helper.TB + 2 * r))         //gameplay are for y axes
        {
            dy = -Math.abs(dy);        //takes the absolute value
            ddy = Math.abs(ddy);
            bound = true;
        }

    } 


    public void startFriction()     //function for friction
    {
        double k = dx * dx + dy * dy;

        k = Math.sqrt(k) * Helper.FPS / 2;        //to have natural friction

        if (k == 0) return;

        ddy = -dy / k;
        ddx = -dx / k;
    }

    private void handleBallFriction()
    {
        dy += ddy;
        dx += ddx;

        if (dx > 0 == ddx > 0)
        {
            dx = 0;
        }

        if (dy > 0 == ddy > 0)
        {
            dy = 0;
        }
    }

    public void draw(Graphics2D graphics2D)   //extend graphics for 2d game
    {
        graphics2D.setColor(Helper.BALL_COLORS.get(number)[0]);  // Color of ball
        graphics2D.fillOval((int) x, (int) y, 2 * r, 2 * r);  // Size of balls
        graphics2D.setColor(Helper.BALL_COLORS.get(number)[1]);   // Color of ball
    }

    public double getCenterX()  
    {
        return this.x+ r;
    }

    public double getCenterY()   
    {
        return this.y + r;  
    }
}

