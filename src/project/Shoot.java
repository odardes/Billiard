package project;

import java.awt.*; 
import java.awt.event.MouseEvent; 
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import static java.lang.Math.abs;


public final class Shoot implements MouseListener, MouseMotionListener
{
    private Game game; 
    volatile private boolean mouseDown  = false;
    volatile private boolean isRunning  = false;
    private          int     frameCount = 0;   
    protected        boolean aiming     = false;   
    private          boolean cue        = true;   
    private          int     power      = 0, aim_r = 10;
    private double aim_oval_x = 0, aim_oval_y = 0;
    private int aim_line_x1, aim_line_x2, aim_line_y1, aim_line_y2;
    private int cue_x1, cue_x2, cue_y1, cue_y2, cue_head_x1, cue_head_y1, cue_middle_x1, cue_middle_y1;
    private static float[] fracs = {0.1f, 0.2f, 0.3f};

    Shoot(Game g)
    {
        this.game = g;
        game.addMouseListener(this);
        game.addMouseMotionListener(this);
    }

    public void draw(Graphics2D g)
    {


        g.setColor(Helper.BALL_WHITE);
        g.setStroke(new BasicStroke(1));
        g.drawRect(Helper.SW + Helper.BX + 20, Helper.SH/2, 20, 200);

        if (cue && !game.movingWhiteBall && game.readyForShoot)
        {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            g.setStroke(new BasicStroke(2));  //The thickness of cue mark
            g.drawOval((int) aim_oval_x, (int) aim_oval_y, aim_r * 2, aim_r * 2);    //The circle of cue mark
            g.drawLine(aim_line_x1, aim_line_y1, aim_line_x2, aim_line_y2);         //The line of cue mark
            g.setColor(Helper.BC.darker().darker());   // Set black the half of cue 
            g.setStroke(new BasicStroke(5));     // The thickness of cue
            g.drawLine(cue_x1, cue_y1, cue_x2, cue_y2);   //The second part of cue
            g.setColor(Helper.BALL_WHITE);  // The first part's color of cue
            g.drawLine(cue_middle_x1, cue_middle_y1, cue_x2, cue_y2);  //Set first part's of cue

            // Power draw
            Point2D start = new Point2D.Float(Helper.SW + Helper.BX - 10, 670);   
            Point2D end   = new Point2D.Float(0, power);        //Transition of colors
            Color[] colors = {Color.green, Color.yellow, Color.red};   
            g.setPaint(new LinearGradientPaint(start, end, fracs, colors));
            g.fillRect(Helper.SW + Helper.BX + 20, Helper.SH/2 + (200 - power * 10), 20, power * 10); // width, height of power draw

        }
    }

    @Override public void mouseClicked(MouseEvent e)
    {
    }

    @Override public void mousePressed(MouseEvent e)
    {
        if (!game.readyForShoot)
        {
            return;
        }

        Ball b;
        for (int ball = 0; ball < game.balls.size(); ball++)
        {
            b = game.balls.get(ball);

            if (Math.abs((int) b.x - e.getPoint().x + b.r) <= b.r && Math.abs((int) (b.y) - e.getPoint().y + b.r) <= b.r)
            {
                if (ball == game.indexOfWhiteBall && !game.movingWhiteBall)
                {
                    aiming = true;
                }
            }
        }

        if (game.readyForShoot && aiming)
        {
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                mouseDown = true;
                initThread();
            }
        }

    }

    @Override public void mouseEntered(MouseEvent e)
    {
    }

    @Override public void mouseExited(MouseEvent e)
    {

    }

    @Override public void mouseReleased(MouseEvent e)
    {

        if (!game.readyForShoot)
        {
            return;
        }

        Ball b = game.balls.get(game.indexOfWhiteBall);

        cue = false;

        if (game.movingWhiteBall)
        {
            game.movingWhiteBall = false;
            aiming = true;
        }
        else if (aiming)
        {
            game.readyForShoot = false;

            double vX  = aim_oval_x - b.x;
            double vY  = aim_oval_y - b.y;
            double len = Math.sqrt((vX * vX) + (vY * vY));

            b.dx = vX / len * power * 60 / Helper.FPS;
            b.dy = vY / len * power * 60 / Helper.FPS;

            b.startFriction();
            aiming = false;
        }

        if (e.getButton() == MouseEvent.BUTTON1)
        {
            mouseDown = false;
            power = 0;
        }
    }


    @Override public void mouseDragged(MouseEvent e)
    {
        if (!game.readyForShoot)
        {
            return;
        }

        Ball b = game.balls.get(game.getCurrentIndexOfWhiteBall(game.balls));

        int middleX = e.getX();
        int middleY = e.getY();

        if (game.movingWhiteBall)
        {
            b.x = middleX;
            b.y = middleY;
        }
    }

    @Override public void mouseMoved(MouseEvent e)
    {
        if (game.indexOfWhiteBall == -1) return;

        Ball b = game.balls.get(game.indexOfWhiteBall);

        if (game.movingWhiteBall)
        {
            double nx = e.getX(), ny = e.getY();

            double xr = Helper.BX + Helper.TB + b.r;
            double xl = Helper.BX + Helper.SW - b.r - Helper.TB;

            double yb = Helper.BY + Helper.SH - b.r - Helper.TB;
            double yt = Helper.BY + Helper.TB + b.r;

            if (nx < xr)
                nx = xr;
            else if (nx > xl)
                nx = xl;
            if (ny > yb)
                ny = yb;
            else if (ny < yt)
                ny = yt;

            b.x = nx - b.r;
            b.y = ny - b.r;

        }
        else if (aiming)
        {
            // Aiming line
            aim_oval_x = e.getX() - aim_r;
            aim_oval_y = e.getY() - aim_r * 2;

            aim_line_x1 = (int) aim_oval_x + aim_r;
            aim_line_y1 = (int) aim_oval_y + aim_r;

            double pa = Math.hypot(aim_line_x1 - b.getCenterX(), aim_line_y1 - b.getCenterY()) - b.r * 2;
            double dx = (b.x) + b.r - aim_line_x1;
            double dy = (b.y) + b.r - aim_line_y1;
            double i  = pa / (Math.sqrt(dx * dx + dy * dy));

            aim_line_x2 = (int) ((dx * i) + aim_line_x1);
            aim_line_y2 = (int) ((dy * i) + aim_line_y1);

            // Cue
            double angle = Math.atan2(aim_line_x1 - b.getCenterX(), aim_line_y1 - b.getCenterY());
            cue_x2 = (int) (b.getCenterX() - Math.sin(angle) * 20);
            cue_y2 = (int) (b.getCenterY() - Math.cos(angle) * 20);
            cue_x1 = (int) (cue_x2 - Math.sin(angle) * 250);
            cue_y1 = (int) (cue_y2 - Math.cos(angle) * 250);

            //Cue middle
            cue_middle_x1 = (int) (cue_x2 - Math.sin(angle) * 150);
            cue_middle_y1 = (int) (cue_y2 - Math.cos(angle) * 150);

            // Cue head
            cue_head_x1 = (int) (cue_x2 - Math.sin(angle) * 4);
            cue_head_y1 = (int) (cue_y2 - Math.cos(angle) * 4);

            cue = true;
        }
    }

    private synchronized boolean checkAndMark()
    {
        if (isRunning) return false;
        isRunning = true;
        return true;
    }

    private void initThread()
    {

        if (checkAndMark())
        {
            new Thread()
            {
                boolean up   = true;
                boolean down = false;

                public void run()
                {
                    while (mouseDown)
                    {
                        long startTime, timeMillis, waitTime;
                        long targetTime = 1000 / Helper.FPS;

                        startTime = System.nanoTime();

                        timeMillis = (System.nanoTime() - startTime) / 1000000;
                        waitTime = abs(targetTime - timeMillis);

                        try
                        {
                            Thread.sleep(waitTime);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        if (game.readyForShoot)
                        {
                            frameCount++;

                            if (frameCount > Helper.FPS / 20)
                            {
                                frameCount = 0;

                                if(up)
                                {
                                    power++;

                                    if(power >= 20)
                                    {
                                        up = false;
                                        down = true;
                                    }
                                }
                                else
                                {
                                    power--;

                                    if(power <= 1)
                                    {
                                        up = true;
                                        down = false;
                                    }
                                }
                            }

                            System.out.println("frameCount: " + frameCount);
                        }
                    }

                    isRunning = false;
                }
            }.start();
        }
    }
}
