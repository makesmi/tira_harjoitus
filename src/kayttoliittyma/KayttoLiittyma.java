

package kayttoliittyma;

import Tutkimus.HakuPuu;
import Tutkimus.HakuPuuSelaaja;
import Tutkimus.TutkimusAlgoritmi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;
import pelinohjaus.Pelaaja;
import pelinohjaus.PelinOhjaus;
import pelinydin.AloitusAsetelma;
import pelinydin.ShakkiPeli;
import pelinydin.Väri;
import tekoaly.MinMax;
import tekoaly.PositioArviointi;
import tekoaly.TekoÄly;


public class KayttoLiittyma extends JFrame implements ActionListener{

    private static final double satunnaisuus = 0.5;
    
    private PeliLauta lauta;
    private PeliTilaPaneeli peliTilaPaneeli;
    private PelaajaLiittymä käyttäjä;
    private TekoÄly tekoÄly;

    private PelinOhjaus peli = null;
    private final JRadioButton ihminenTekoÄly = new JRadioButton("ihminen vs tekoäly");
    private final JRadioButton ihminenIhminen = new JRadioButton("ihminen vs ihminen");
    private final JRadioButton tekoÄlyTekoÄly = new JRadioButton("tekoäly vs tekoäly");
    private final AlgoritmiValitsin valitsin = new AlgoritmiValitsin();
    private HakuPuuSelaaja tutkimusIkkuna = null;
    private Map<String, Runnable> komennot;
    private String[] komennotJärjestyksessä;
    
    public KayttoLiittyma(){
        setVisible(true);
        setTitle("PeliLauta");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocation(200, 50);
        
        luoKomennot();
        lauta = new PeliLauta();
        lauta.päivitäLauta(AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
        lauta.asetaVäri(Väri.VALKOINEN);
        lauta.näytäSiirtoVihjeet(true);
        add(lauta);
        validate();
        luoOhjausIkkuna();
        käyttäjä = new PelaajaLiittymä(lauta, peliTilaPaneeli);
    }

    
    private void luoKomennot(){
        komennot = new TreeMap<>();
        komennot.put("pelaa", this::käynnistäPeli);
        komennot.put("keskeytä", this::pysäytäPeli);
        komennot.put("peruuta", this::peruutaSiirto);
        komennot.put("käännä", this::käännäLauta);
        komennot.put("alkuasetelma", this::meneAloitusAsetelmaan);
        komennot.put("tutki", this::tutkiSiirtoja);
        
        komennotJärjestyksessä = new String[]
            {"pelaa", "keskeytä", "peruuta", "käännä", "alkuasetelma", "tutki"};
    }
    
    
    private void luoOhjausIkkuna(){
        JFrame ohjausIkkuna = new JFrame();
        ohjausIkkuna.setSize(200, 600);
        ohjausIkkuna.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ohjausIkkuna.setTitle("ShakkiPeli");
        ohjausIkkuna.setVisible(true);
        ohjausIkkuna.setLayout(new BorderLayout());
        ohjausIkkuna.setLocation(100, 100);
        
        ButtonGroup pelaajaValikko = new ButtonGroup();
        pelaajaValikko.add(ihminenTekoÄly);
        pelaajaValikko.add(ihminenIhminen);
        pelaajaValikko.add(tekoÄlyTekoÄly);
        ihminenTekoÄly.setSelected(true);
        
        JPanel painikeRivi = new JPanel();
        painikeRivi.setLayout(new GridLayout(0, 1, 5, 0));
        painikeRivi.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        painikeRivi.add(ihminenTekoÄly);
        painikeRivi.add(ihminenIhminen);
        painikeRivi.add(tekoÄlyTekoÄly);
        painikeRivi.add(valitsin);
        
        Stream.of(komennotJärjestyksessä)
                .map((teksti) -> new JButton(teksti))
                .peek((painike) -> painike.addActionListener(this))
                .forEachOrdered(painikeRivi::add);
        
        peliTilaPaneeli = new PeliTilaPaneeli();
        ohjausIkkuna.add(peliTilaPaneeli, BorderLayout.NORTH);
        ohjausIkkuna.add(painikeRivi, BorderLayout.CENTER);
        ohjausIkkuna.validate();
    }
    
    
    public void pelaa(){
        if(!onkoPeliKäynnissä()){
            
            tekoÄly = new TekoÄly(new PositioArviointi(satunnaisuus), valitsin.haeHakuAlgoritmi());
            
            Pelaaja valkoinen = null;
            Pelaaja musta = null;
            if(ihminenTekoÄly.isSelected()){
                if(lauta.haeVäri() == Väri.VALKOINEN){
                    valkoinen = käyttäjä;
                    musta = tekoÄly;
                }else{
                    valkoinen = tekoÄly;
                    musta = käyttäjä;
                }
            }else if(ihminenIhminen.isSelected()){
                valkoinen = käyttäjä;
                musta = käyttäjä;
            }else{
                valkoinen = tekoÄly;
                musta = new TekoÄly(new PositioArviointi(satunnaisuus), new MinMax(3));
            }
            
            peli = new PelinOhjaus(valkoinen, musta, lauta.haeLauta(), lauta.haePeliTila());
            peli.lisääKatsoja(new PelinLopetusIlmoittaja());
            if(tekoÄlyTekoÄly.isSelected()){
                peli.lisääKatsoja(käyttäjä);
            }
            lauta.tyhjennäSiirtoEhdotus();
            peli.pelaa();
        }
    }
    
    private void käynnistäPeli(){
            (new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    pelaa();
                    return null;
                }
            }).execute();
    }
    
    private void pysäytäPeli(){
        if(onkoPeliKäynnissä()){
            peli.poistaKatsojat();
            peli.pysäytäPeli();
        }
    }
    
    private void peruutaSiirto(){
        pysäytäPeli();
        if(lauta.haePeliTila().siirtoNumero > 0){
            lauta.peruutaSiirto();
            peliTilaPaneeli.päivitä(lauta.haeLauta(), lauta.haePeliTila());
        }
    }
    
    public void tutkiSiirtoja(){
        pysäytäPeli();
        
        tutkimusIkkuna = new HakuPuuSelaaja(lauta, peliTilaPaneeli);
        
        TutkimusAlgoritmi algoritmi = valitsin.haeTutkimusAlgoritmi();
        ShakkiPeli testiPeli = new ShakkiPeli(lauta.haeLauta(), lauta.haePeliTila());
        HakuPuu puu = algoritmi.tutki(testiPeli, new PositioArviointi(0.0));
        tutkimusIkkuna.asetaHakuPuu(puu);
    }
    
    private void meneAloitusAsetelmaan(){
        pysäytäPeli();
        lauta.tyhjennäHistoria();
        lauta.päivitäLauta(AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
        peliTilaPaneeli.päivitä(lauta.haeLauta(), lauta.haePeliTila());
    }
    
    private void käännäLauta(){
        pysäytäPeli();
        lauta.asetaVäri(!lauta.haeVäri());
    }

    @Override
    public void actionPerformed(ActionEvent klikkaus) {
        String nimi = klikkaus.getActionCommand();
        Runnable komento = komennot.get(nimi);
        if(komento != null){
            komento.run();
        }
    }
    
    public boolean onkoPeliKäynnissä(){
        return peli != null && peli.peliKäynnissä();
    }
    
    public static void main(String[] args) {
        KayttoLiittyma ikkuna = new KayttoLiittyma();
    }
}
