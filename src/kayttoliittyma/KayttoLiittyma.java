

package kayttoliittyma;

import Tutkimus.HakuPuu;
import Tutkimus.HakuPuuSelaaja;
import Tutkimus.TutkimusAlgoritmi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private PeliLauta lauta;
    private PelaajaLiittymä käyttäjä;
    private TekoÄly tekoÄly;

    private PelinOhjaus peli = null;
    private final JRadioButton ihminenTekoÄly = new JRadioButton("ihminen vs tekoäly");
    private final JRadioButton ihminenIhminen = new JRadioButton("ihminen vs ihminen");
    private final JRadioButton tekoÄlyTekoÄly = new JRadioButton("tekoäly vs tekoäly");
    private final AlgoritmiValitsin valitsin = new AlgoritmiValitsin();
    private HakuPuuSelaaja tutkimusIkkuna = null;
    
    public KayttoLiittyma(){
        setVisible(true);
        setTitle("ShakkiTekoäly");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 720);
        setLocation(200, 50);
        
        lauta = new PeliLauta();
        lauta.päivitäLauta(AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
        lauta.asetaVäri(Väri.VALKOINEN);
        lauta.näytäSiirtoVihjeet(true);
        add(lauta);
        validate();
        luoOhjausIkkuna();
        käyttäjä = new PelaajaLiittymä(lauta);
        
    }

    
    private void luoSiirtoKuuntelija(){
        lauta.asetaSiirtoKuuntelija(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!onkoPeliKäynnissä()){
                    
                }
            }
        });
    }
    
    private void luoOhjausIkkuna(){
        JFrame ohjausIkkuna = new JFrame();
        ohjausIkkuna.setSize(200, 600);
        ohjausIkkuna.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ohjausIkkuna.setTitle("ShakkiPeli");
        ohjausIkkuna.setVisible(true);
        ohjausIkkuna.setLayout(new BorderLayout());
        
        ButtonGroup pelaajaValikko = new ButtonGroup();
        pelaajaValikko.add(ihminenTekoÄly);
        pelaajaValikko.add(ihminenIhminen);
        pelaajaValikko.add(tekoÄlyTekoÄly);
        ihminenTekoÄly.setSelected(true);
        
        String painikkeet[] = {"pelaa", "käännä", "alkuasetelma", "keskeytä", "tutki", "peruuta"};
        JPanel painikeRivi = new JPanel();
        painikeRivi.setLayout(new GridLayout(0, 1));
        painikeRivi.add(ihminenTekoÄly);
        painikeRivi.add(ihminenIhminen);
        painikeRivi.add(tekoÄlyTekoÄly);
        painikeRivi.add(valitsin);

        for (String teksti : painikkeet) {
            JButton painike = new JButton(teksti);
            painikeRivi.add(painike);
            painike.addActionListener(this);
        }
        
        ohjausIkkuna.add(painikeRivi);
        ohjausIkkuna.validate();
    }
    
    public void pelaa(){
        if(!onkoPeliKäynnissä()){
            
            tekoÄly = new TekoÄly(new PositioArviointi(0.0), valitsin.haeHakuAlgoritmi());
            
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
                musta = new TekoÄly(new PositioArviointi(0.0), new MinMax(3));
            }
            peli = new PelinOhjaus(valkoinen, musta, lauta.haeLauta(), lauta.haePeliTila());
            peli.lisääKatsoja(new PelinLopetusIlmoittaja());
            if(tekoÄlyTekoÄly.isSelected()){
                peli.lisääKatsoja(käyttäjä);
            }
            peli.pelaa();
        }
    }
    
    public void tutkiSiirtoja(){
        tutkimusIkkuna = new HakuPuuSelaaja(lauta);
        
        TutkimusAlgoritmi algoritmi = valitsin.haeTutkimusAlgoritmi();
        ShakkiPeli testiPeli = new ShakkiPeli(lauta.haeLauta(), lauta.haePeliTila());
        HakuPuu puu = algoritmi.tutki(testiPeli, new PositioArviointi(0.0));
        tutkimusIkkuna.asetaHakuPuu(puu);
    }

    @Override
    public void actionPerformed(ActionEvent klikkaus) {
        String komento = klikkaus.getActionCommand();
        if(komento.equals("pelaa")){
            (new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    pelaa();
                    return null;
                }
            }).execute();
        }else if(komento.equals("tutki") && !onkoPeliKäynnissä()){
            tutkiSiirtoja();
        }else if(komento.equals("käännä") && !onkoPeliKäynnissä()){
            lauta.asetaVäri(!lauta.haeVäri());
        }else if(komento.equals("alkuasetelma") && !onkoPeliKäynnissä()){
            lauta.tyhjennäHistoria();
            lauta.päivitäLauta(AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
        }else if(komento.equals("keskeytä") && onkoPeliKäynnissä()){
            peli.poistaKatsojat();
            käyttäjä.luovuta();
            tekoÄly.luovuta();
        }else if(komento.equals("peruuta") && !onkoPeliKäynnissä()){
            lauta.peruutaSiirto();
        }
    }
    
    public boolean onkoPeliKäynnissä(){
        return peli != null && peli.peliKäynnissä();
    }
    
    public static void main(String[] args) {
        KayttoLiittyma ikkuna = new KayttoLiittyma();
    }
}
