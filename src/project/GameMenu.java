package project;

import javax.swing.*; // For using GUI elements, for frame buttons
import java.awt.*; // For using GUI elements, for frames buttons
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class GameMenu extends JFrame implements ActionListener
{

    private Game game;

    GameMenu()               
    {
        super();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        game = new Game();
        setTitle("3 Cushion Billiard Game ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenuBar menu = new JMenuBar();

        JMenu file = new JMenu("Game"), opts = new JMenu("High Score");

        menu.add(file);
        menu.add(opts);

        addMenuItems(file, "New game-About");
        addMenuItems(opts, "0");

        setJMenuBar(menu);
        add(game, BorderLayout.CENTER);
        pack(); // Size
        Helper.HA = false; 
        setVisible(true);
        requestFocus();
    }

    private void addMenuItems(JMenu menu, String items)            // The function of add the menu items
    {
        JMenuItem i;

        for (String str : items.split("-"))
        {
            i = str.substring(0, 1).equals("@") ? new JCheckBoxMenuItem(str.substring(1), true) : new JMenuItem(str);
            i.addActionListener(this);
            menu.add(i);
        }
    }

    public void actionPerformed(ActionEvent e)                  //The function for realization of tasks
    {

        switch (e.getActionCommand())
        {
            case "New game":
                System.out.println("New game started");
                game.createNewGame();
                break;
            case "About":
                JOptionPane.showMessageDialog(this, "Advanced programming term project game ", "About", JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

}
