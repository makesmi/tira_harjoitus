
package Tutkimus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import kayttoliittyma.PeliLauta;
import pelinydin.Nappula;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;


public class HakuPuuSelaaja extends JPanel implements MouseListener, MouseWheelListener{
    private HakuPuu puu = null;
    private int solmujaYhteensä = 0;
    private int haaroitusKerroin = 0;
    
    private final JFrame ikkuna;
    private final PeliLauta lauta;
    private List<SelausSolmu> solmut = new LinkedList<>();
    private final int solmuLeveys = 150;
    private final int solmuKorkeus = 25;
    private final int näytäVanhempia = 2;
    private final int ikkunaLeveys = 1000;
    private final int ikkunaKorkeus = 600;
    private final Color musta = new Color(0, 0, 0);
    private final Font fontti = new Font("Tahoma", Font.BOLD, 20);
    private final Color tumma = new Color(150, 200, 150);
    private final Color vaalea = new Color(200, 250, 200);
    private final DecimalFormat desimaaliKarsija = new DecimalFormat("0.###");
            
    private SelausSolmu kohdeSolmu = null;
    private int yPaikka = 0;
    
    
    public HakuPuuSelaaja(PeliLauta lauta){
        this.lauta = lauta;
        ikkuna = new JFrame("pelipuun selaaminen");
        ikkuna.setSize(ikkunaLeveys, ikkunaKorkeus);
        ikkuna.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ikkuna.setVisible(true);
        ikkuna.add(this);
        this.addMouseListener(this);
        ikkuna.addMouseWheelListener(this);
    }
    
    public void asetaHakuPuu(HakuPuu puu){
        this.puu = puu;
        
        asetaKohdeSolmu(puu.haeJuuri());
        solmujaYhteensä = puu.haeJuuri().solmujaYhteensä();
        haaroitusKerroin = puu.haeJuuri().solmujaTasossaKeskimäärin();
        ikkuna.setTitle("pelipuun selaaminen (solmuja: " + solmujaYhteensä + ") (haaroituskerroin: " + haaroitusKerroin + ")");
    }
    
    private void asetaKohdeSolmu(PeliSolmu solmu){
        if(puu == null){
            return;
        }
        
        ShakkiPeli peli = new ShakkiPeli(puu.haeAloitusLauta(), puu.haeAloitusTila());
        solmut.clear();
        int tasoja = näytäVanhempia + 2;
        int tasoLeveys = (ikkunaLeveys - 30) / tasoja;
        int riviKorkeus = solmuKorkeus + 8;
        LinkedList<PeliSolmu> polku = solmu.haePolkuJuuresta();
        while(polku.size() > tasoja){
            PeliSolmu vanhempi = polku.pop();
            if(vanhempi.haeSiirto() != null){
                peli.teeSiirto(vanhempi.haeSiirto());
            }
        }
        
        SelausSolmu vanhempi = null;
        SelausSolmu seuraavaVanhempi = null;

        for(int taso = 0; taso < tasoja && !polku.isEmpty(); taso++){
            solmu = polku.pop();
            if(solmu.haeSiirto() != null){
                peli.teeSiirto(solmu.haeSiirto());
            }
            int x = 30 + taso * tasoLeveys;
            int y = 30;
            vanhempi = seuraavaVanhempi;
            boolean siirtäjä = peli.haePeliTila().vuoro();
            solmu.järjestäLapset();
            
            for (PeliSolmu lapsi : solmu.haeLapset()) {
                SelausSolmu loota = new SelausSolmu(lapsi, x, y, vanhempi, haeNotaatio(peli, lapsi.haeSiirto()), siirtäjä);
                solmut.add(loota);
                y += riviKorkeus;
                if(!polku.isEmpty() && lapsi == polku.getFirst()){
                    seuraavaVanhempi = loota;
                }
            }
        }
        
        kohdeSolmu = vanhempi;
        yPaikka = 0;
        repaint();
        lauta.päivitäLauta(peli.haeLauta(), peli.haePeliTila());
    }
    
    private String haeNotaatio(ShakkiPeli peli, ShakkiSiirto siirto){
        if(siirto == null){
            return "()";
        }
        Nappula nappula = peli.haeLauta().haeRuutu(siirto.lähtöX, siirto.lähtöY);
        boolean syönti = peli.haeLauta().haeRuutu(siirto.kohdeX, siirto.kohdeY) != null;
        return Notaatio.haeSiirtoNotaatio(siirto, nappula, syönti);
    }
    
    @Override
    protected void paintComponent(Graphics piirto){
        super.paintComponent(piirto);
        piirto.setColor(musta);
        piirto.setFont(fontti);
        
        for (SelausSolmu solmu : solmut) {
            if(solmu.vanhempi != null){
                SelausSolmu v = solmu.vanhempi;
                int väli = solmuKorkeus / 2;
                piirto.drawLine(solmu.x, solmu.y + väli + yPaikka, v.x + solmuLeveys, v.y + väli + yPaikka);
            }
        }
        
        for (SelausSolmu solmu : solmut) {
            piirto.setColor(solmu.siirtäjä == Väri.VALKOINEN ? vaalea : tumma);
            piirto.fillRect(solmu.x, solmu.y + yPaikka, solmuLeveys, solmuKorkeus);
            piirto.setColor(musta);
            piirto.drawRect(solmu.x, solmu.y + yPaikka, solmuLeveys, solmuKorkeus);
            if(solmu == kohdeSolmu){
                for(int k = 3; k < 6; k++){
                    piirto.drawRect(solmu.x - k, solmu.y - k + yPaikka, solmuLeveys + k*2, solmuKorkeus + k*2);
                }
            }
            String arvo = desimaaliKarsija.format(solmu.solmu.haeArvo());
            String teksti = solmu.notaatio + "   " + arvo + "   " + solmu.solmu.haeTagit();
            int tekstiY = solmu.y + solmuKorkeus - 4;
            piirto.drawString(teksti, solmu.x + 4, tekstiY + yPaikka);
        }
    }

    @Override
    public void mouseClicked(MouseEvent hiiri) {
        int x = hiiri.getX();
        int y = hiiri.getY() - yPaikka;

        for (SelausSolmu solmu : solmut) {
            if(x >= solmu.x && x < solmu.x + solmuLeveys && y >= solmu.y && y < solmu.y + solmuKorkeus){
                asetaKohdeSolmu(solmu.solmu);
                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent vieritys) {
        yPaikka -= vieritys.getWheelRotation() * 27;
        repaint();
    }
}
