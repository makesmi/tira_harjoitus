

package kayttoliittyma;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import shakkipeli.Nappula;
import shakkipeli.ShakkiLauta;
import shakkipeli.ShakkiSiirto;
import shakkipeli.Väri;


public class PeliLauta extends JPanel implements MouseListener, MouseMotionListener{
    
    private final Color tumma = new Color(190, 190, 150);
    private final Color vaalea = new Color(230, 230, 200);
    
    private final int ruutuKoko = 100;
    private ShakkiLauta lauta;
    boolean väri;
    
    private Nappula siirtoNappula;
    private int siirtoX, siirtoY;
    private int hiiriX, hiiriY;
    private int lähtöRuutuX, lähtöRuutuY;
    private ShakkiSiirto ehdotettuSiirto;
    
    
    public PeliLauta(){
        lauta = new ShakkiLauta();
        addMouseListener(this);
        addMouseMotionListener(this);
        this.väri = Väri.VALKOINEN;
        ehdotettuSiirto = null;
        siirtoNappula = null;
    }
    
    public ShakkiSiirto haeSiirto(){
        return ehdotettuSiirto;
    }
    
    public void asetaVäri(boolean väri){
        this.väri = väri;
    }
    
    public void päivitäLauta(ShakkiLauta lauta){
        this.lauta = lauta;
        repaint();
    }
    
    public void käsitteleSiirto(){
        int keskiX = hiiriX - siirtoX + ruutuKoko / 2;
        int keskiY = hiiriY - siirtoY + ruutuKoko / 2;
        int x = keskiX / ruutuKoko;
        int y = keskiY / ruutuKoko;
        if(väri == Väri.VALKOINEN) y = 7 - y;
        
        if(x >= 0 && x < 8 && y >= 0 && y < 8){
            ehdotettuSiirto = new ShakkiSiirto(lähtöRuutuX, lähtöRuutuY, x, y);
        }
        
        siirtoNappula = null;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics piirto){
        
        piirto.setColor(vaalea);
        piirto.fillRect(0, 0, ruutuKoko * 8, ruutuKoko * 8);
        
        piirto.setColor(tumma);
        
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                
                int näyttöX = x * ruutuKoko;
                int näyttöY = y * ruutuKoko;
                if(väri == Väri.VALKOINEN)
                    näyttöY = 7*ruutuKoko - näyttöY;
                
                if((x + y) % 2 == 0){
                    piirto.fillRect(näyttöX, näyttöY, ruutuKoko, ruutuKoko);
                }
                
                Nappula nappula = lauta.haeRuutu(x, y);
                if(nappula != null && nappula != siirtoNappula){
                    BufferedImage kuva = NappulaKuvat.haeKuva(nappula);
                    piirto.drawImage(kuva, näyttöX, näyttöY, null);
                }
            }
        }
        
        if(siirtoNappula != null){
            BufferedImage kuva = NappulaKuvat.haeKuva(siirtoNappula);
            piirto.drawImage(kuva, hiiriX - siirtoX, hiiriY - siirtoY, null);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent hiiri) {
        hiiriX = hiiri.getX();
        hiiriY = hiiri.getY();
        
        int x = hiiriX / ruutuKoko;
        int y = hiiriY / ruutuKoko;
        if(väri == Väri.VALKOINEN)
            y = 7 - y;
        
        if(x >= 0 && x < 8 && y >= 0 && y < 8){
            siirtoNappula = lauta.haeRuutu(x, y);
            siirtoX = hiiriX % ruutuKoko;
            siirtoY = hiiriY % ruutuKoko;
            lähtöRuutuX = x;
            lähtöRuutuY = y;
        }
    }

    @Override
    public void mouseReleased(MouseEvent hiiri) {
        käsitteleSiirto();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent hiiri) {
        hiiriX = hiiri.getX();
        hiiriY = hiiri.getY();
        
        if(siirtoNappula != null){
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent hiiri) {
        hiiriX = hiiri.getX();
        hiiriY = hiiri.getY();
    }
}
