
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

class Shoot {
    private int x;
    private int y;

    public Shoot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
}
public class Game extends JPanel implements KeyListener,ActionListener{
    Timer timer = new Timer(1, this);
    
    private int timePassed = 0;
    private int shootsUsed = 0;
    private BufferedImage hunter;
    private BufferedImage bullet;
    private BufferedImage background;
    private BufferedImage bird;
    private ArrayList<Shoot> shoots = new ArrayList<>();
    
    private int shootMovesYAxisBy = 1;
    private int birdX = 0;
    private int birdMovesXAxisBy = 2;
    private int hunterX = 0;
    private int hunterMovesXAxisBy = 20;
    
    public boolean isShot() {
        for (Shoot shoot: shoots) {
            if (new Rectangle(shoot.getX(), shoot.getY(), bullet.getWidth() / 20, bullet.getHeight() / 20).intersects(new Rectangle(birdX,0,bird.getWidth() / 5,bird.getHeight() / 5))) {
                return true;
            }
        }
        return false;
    }
    public Game() {
        try {
            hunter = ImageIO.read(new FileImageInputStream(new File("hunter.png")));
            bullet = ImageIO.read(new FileImageInputStream(new File("bullet.png")));
            background = ImageIO.read(new FileImageInputStream(new File("background.jpg")));
            bird = ImageIO.read(new FileImageInputStream(new File("bird.png")));
            setBackground(Color.GREEN);
            timer.start();
            gameMusic();
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void gameMusic() {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File("Call to Adventure.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        timePassed += 5;
        
        g.drawImage(background, 0, 0, this);
        g.drawImage(bird, birdX, 0, bird.getWidth() / 3, bird.getHeight() / 3, this);
        g.drawImage(hunter, hunterX, 870, hunter.getWidth() / 3, hunter.getHeight() / 3, this);
        for (Shoot shoot: shoots) {
            if (shoot.getY() < 0) {
                shoots.remove(shoot);
            }
        }
        for (Shoot shoot: shoots) {
            g.drawImage(bullet, shoot.getX(), shoot.getY(), bullet.getHeight() /20 ,bullet.getWidth() / 20, this);
            
        }
        
        if (isShot()) {
            timer.stop();
            String message = "You Won!\n" +
                             "Shots Fired : " + shootsUsed + "\n" +
                             "Time Passed : " + timePassed / 1000.0 + " seconds." + "\n" + "\n" +
                             "**************************************************" + "\n" +
                             "The game is made by Ferec Hamitbeyli" + "\n" +
                             "ferechamitbeyli@gmail.com" + "\n" +
                             "**************************************************" + "\n" +
                             "The duck image is under CC Attribution 3.0 Unported (CC BY 3.0) " + "\n" +
                             "The hunter image is under CC0 1.0 Universal (CC0 1.0) licences.";
                          
            JOptionPane.showMessageDialog(this, message);
            System.exit(0);
        }
    }

    @Override
    public void repaint() {
        super.repaint(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            if (hunterX <= 0) {
                hunterX = 0;
            }
            else {
                hunterX -= hunterMovesXAxisBy;
            }
        }
        else if (c == KeyEvent.VK_RIGHT) {
            if (hunterX >= 1650) {
                hunterX = 1650;
            }
            else {
                hunterX += hunterMovesXAxisBy;
            }
        }
        else if (c == KeyEvent.VK_CONTROL) {
            shoots.add(new Shoot(hunterX+15, 860));
            shootsUsed++;
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Shoot shoot: shoots) {
            shoot.setY(shoot.getY() - shootMovesYAxisBy);
        }
        birdX += birdMovesXAxisBy;
        if (birdX >= 1800) {
            birdMovesXAxisBy = -birdMovesXAxisBy;
            
        }
        if (birdX <= 0) {
            birdMovesXAxisBy = -birdMovesXAxisBy;
            
        }
        repaint();
        
    }
    
}
