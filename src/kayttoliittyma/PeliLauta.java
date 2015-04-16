

package kayttoliittyma;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import pelinohjaus.SiirtojenGenerointi;
import pelinydin.AloitusAsetelma;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import static pelinydin.NappulaTyyppi.KUNINGATAR;
import static pelinydin.NappulaTyyppi.LÄHETTI;
import static pelinydin.NappulaTyyppi.RATSU;
import static pelinydin.NappulaTyyppi.SOTILAS;
import static pelinydin.NappulaTyyppi.TORNI;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;
import static pelinydin.Väri.VALKOINEN;


public class PeliLauta extends JPanel implements MouseListener, MouseMotionListener{
    
    private final Color tumma = new Color(190, 190, 150);
    private final Color vaalea = new Color(230, 230, 200);
    private final Color vihjeVäri = new Color(240, 40, 40);
    
    public static final int ruutuKoko = 80;
    private ShakkiLauta lauta;
    private PeliTila peliTila;
    private boolean väri;
    private boolean vihjeidenNäyttö;
    
    private Nappula siirtoNappula;
    private int siirtoX, siirtoY;
    private int hiiriX, hiiriY;
    private int lähtöRuutuX, lähtöRuutuY;
    private ShakkiSiirto ehdotettuSiirto;
    private List<ShakkiSiirto> vihjeet;
    private ActionListener siirtoKuuntelija;
    
    private LinkedList<ShakkiLauta> lautaHistoria = new LinkedList<>();
    private LinkedList<PeliTila> tilaHistoria = new LinkedList<>();
    
    
    public PeliLauta(){
        lauta = new ShakkiLauta();
        peliTila = AloitusAsetelma.haeTila();
        addMouseListener(this);
        addMouseMotionListener(this);
        this.väri = Väri.VALKOINEN;
        ehdotettuSiirto = null;
        siirtoNappula = null;
        siirtoKuuntelija = null;
        vihjeet = new LinkedList<>();
        vihjeidenNäyttö = false;
    }
    
    public ShakkiSiirto haeSiirto(){
        ShakkiSiirto siirto = ehdotettuSiirto;
        ehdotettuSiirto = null;
        return siirto;
    }
    
    public void asetaVäri(boolean väri){
        this.väri = väri;
        repaint();
    }
    
    public void asetaSiirtoKuuntelija(ActionListener kuuntelija){
        this.siirtoKuuntelija = kuuntelija;
    }
    
    public boolean haeVäri(){
        return väri;
    }
    
    public void päivitäLauta(ShakkiLauta lauta, PeliTila tila){
        tallennaHistoriaan();
        this.lauta = lauta;
        this.peliTila = tila;
        repaint();
    }
    
    private void tallennaHistoriaan(){
        ShakkiLauta lautaKopio = new ShakkiLauta();
        lautaKopio.kopioiAsetelma(lauta);
        lautaHistoria.push(lautaKopio);
        tilaHistoria.push(peliTila);
    }
    
    public void peruutaSiirto(){
        if(!lautaHistoria.isEmpty() && !tilaHistoria.isEmpty()){
            this.lauta = lautaHistoria.pop();
            this.peliTila = tilaHistoria.pop();
            repaint();
        }
    }
    
    public void tyhjennäHistoria(){
        lautaHistoria.clear();
        tilaHistoria.clear();
    }
    
    public void näytäSiirtoVihjeet(boolean näytä){
        vihjeidenNäyttö = näytä;
    }
       
    public void käsitteleSiirto(){
        int keskiX = hiiriX - siirtoX + ruutuKoko / 2;
        int keskiY = hiiriY - siirtoY + ruutuKoko / 2;
        int x = keskiX / ruutuKoko;
        int y = keskiY / ruutuKoko;
        if(väri == Väri.VALKOINEN) {
            y = 7 - y;
        }else{
            x = 7 - x;
        }
        
        if(x >= 0 && x < 8 && y >= 0 && y < 8){
            Nappula korotus = null;
            if(siirtoNappula.tyyppi == SOTILAS && (y == 7 || y == 0)){
                korotus = kysyKorotusUpseeri(siirtoNappula.väri);
            }
            ehdotettuSiirto = new ShakkiSiirto(lähtöRuutuX, lähtöRuutuY, x, y, korotus);
        }
        
        siirtoNappula = null;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics piirto){
        try{
            super.paintComponent(piirto);

            piirto.setColor(vaalea);
            piirto.fillRect(0, 0, ruutuKoko * 8, ruutuKoko * 8);


            piirto.setColor(tumma);

            for(int y = 0; y < 8; y++) {
                for(int x = 0; x < 8; x++) {

                    int näyttöX = x * ruutuKoko;
                    int näyttöY = y * ruutuKoko;
                    if(väri == Väri.VALKOINEN){
                        näyttöY = 7*ruutuKoko - näyttöY;
                    }else {
                        näyttöX = 7*ruutuKoko - näyttöX;
                    }

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
                if(vihjeidenNäyttö){
                    piirräSiirtoVihjeet(piirto);
                }
                BufferedImage kuva = NappulaKuvat.haeKuva(siirtoNappula);
                if(kuva != null){
                    piirto.drawImage(kuva, hiiriX - siirtoX, hiiriY - siirtoY, null);
                }
            }
        }catch(ExceptionInInitializerError vika){
            
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
        if(väri == Väri.VALKOINEN){
            y = 7 - y;
        }else{
            x = 7 - x;
        }
        
        if(x >= 0 && x < 8 && y >= 0 && y < 8){
            siirtoNappula = lauta.haeRuutu(x, y);
            siirtoX = hiiriX % ruutuKoko;
            siirtoY = hiiriY % ruutuKoko;
            lähtöRuutuX = x;
            lähtöRuutuY = y;
            
            haeSiirtoVihjeet();
        }
        
    }

    //siirtovihjeitä varten
    private void haeSiirtoVihjeet(){
        if(vihjeidenNäyttö && peliTila.vuoro() == väri){
            vihjeet = SiirtojenGenerointi.haeSiirrot(lauta, peliTila);
            List<ShakkiSiirto> poistettavat = new LinkedList<>();
            for (ShakkiSiirto siirto : vihjeet) {
                if(siirto.lähtöX != lähtöRuutuX || siirto.lähtöY != lähtöRuutuY){
                    poistettavat.add(siirto);
                }
            }
            vihjeet.removeAll(poistettavat);
        }else{
            vihjeet = new LinkedList<>();
        }        
    }
    
    private void piirräSiirtoVihjeet(Graphics piirto){
        piirto.setColor(vihjeVäri);
        
        for (ShakkiSiirto siirto : vihjeet) {
            int x = siirto.kohdeX;
            int y = siirto.kohdeY;
            
            if(väri == VALKOINEN){
                y = 7 - y;
            }else{
                x = 7 - x;
            }
            
            int näyttöX = ruutuKoko * x;
            int näyttöY = ruutuKoko * y;
            int reunaPaksuus = 4;
            
            for(int reuna = 0; reuna < reunaPaksuus; reuna ++){
                int koko = ruutuKoko - 2 * reuna;
                piirto.drawRect(näyttöX + reuna, näyttöY + reuna, koko, koko);
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent hiiri) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                käsitteleSiirto();
                return null;
            }
        }.execute();
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
    
    public ShakkiLauta haeLauta(){
        return lauta;
    }
    
    public PeliTila haePeliTila(){
        return peliTila;
    }
    
    private Nappula kysyKorotusUpseeri(boolean upseeriVäri){
        NappulaTyyppi upseerit[] = {RATSU, LÄHETTI, TORNI, KUNINGATAR};
        JFrame ikkuna = new JFrame("Miksi upseeriksi korotetaan?");
        ikkuna.setLocation(300, 300);
        ikkuna.setSize(600, 200);
        ikkuna.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ikkuna.setVisible(true);
        ikkuna.setLayout(new FlowLayout());
        VastausOdottaja.vastaus = null;

        for(NappulaTyyppi tyyppi : upseerit){
            final Nappula nappula = new Nappula(tyyppi, upseeriVäri);
            JButton painike = new JButton(new ImageIcon(NappulaKuvat.haeKuva(nappula)));
            ikkuna.add(painike);
            painike.addActionListener(new VastausOdottaja(nappula));
        }
        ikkuna.validate();
        
        while(VastausOdottaja.vastaus == null){
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                
            }
        }
        
        ikkuna.dispose();
        return VastausOdottaja.vastaus;
    }
    
    
}

class VastausOdottaja implements ActionListener{
    public static Nappula vastaus = null;
    private final Nappula nappula;

    public VastausOdottaja(Nappula nappula){
        this.nappula = nappula;
    }
    @Override public void actionPerformed(ActionEvent e) {
        vastaus = nappula;
    }
};

