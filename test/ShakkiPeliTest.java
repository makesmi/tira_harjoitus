/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.prism.ps.Shader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pelinydin.AloitusAsetelma;
import pelinydin.Linnoitus;
import pelinydin.Nappula;
import static pelinydin.Väri.*;
import static pelinydin.NappulaTyyppi.*;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;

/**
 *
 * @author risto_000
 */
public class ShakkiPeliTest {
    
    ShakkiPeli peli;
    ShakkiLauta tyhjäLauta;
    
    public ShakkiPeliTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        peli = new ShakkiPeli();
        tyhjäLauta = new ShakkiLauta();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void aloitusAsetelma(){
        assert(samanlainenLauta(peli.haeLauta(), AloitusAsetelma.haeLauta()));
    }
    
    @Test
    public void siirtoNumero(){
        teeSiirrot(new ShakkiSiirto(4, 1, 4, 3));
        teeSiirrot(new ShakkiSiirto(4, 6, 4, 4));
        assert(peli.haePeliTila().siirtoNumero == 2);
    }
    
    @Test
    public void siirtoLaudalla(){
        teeSiirrot(new ShakkiSiirto(4, 1, 4, 3));
        Nappula sotilas = new Nappula(SOTILAS, VALKOINEN);
        Nappula ruutu = peli.haeLauta().haeRuutu(4, 3);
        assert(samanlainenNappula(ruutu, sotilas));
        Assert.assertNull(peli.haeLauta().haeRuutu(4, 1));
    }
    
    @Test
    public void SyötyNappula(){
        teeSiirrot(
                new ShakkiSiirto(4, 1, 4, 3),
                new ShakkiSiirto(3, 6, 3, 4),
                new ShakkiSiirto(4, 3, 3, 4));
        
        Nappula sotilas = new Nappula(SOTILAS, MUSTA);
        Nappula syöty = peli.haePeliTila().syötyNappula;
        assert(samanlainenNappula(sotilas, syöty));
    }
    
    @Test
    public void eiSyötyäNappulaa(){
        peli.teeSiirto(new ShakkiSiirto(4, 1, 4, 3));
        
        Assert.assertNull(peli.haePeliTila().syötyNappula);
    }
    
    @Test
    public void peruutusLaudalla(){
        ShakkiLauta vanha = new ShakkiLauta();
        vanha.kopioiAsetelma(peli.haeLauta());
        teeSiirrot(new ShakkiSiirto(4, 1, 4, 3));
        peli.peruutaSiirto();
        assert(samanlainenLauta(vanha, peli.haeLauta()));
    }
    
    @Test
    public void peruutusTila(){
        PeliTila tilaOlio = peli.haePeliTila();
        teeSiirrot(new ShakkiSiirto(4, 1, 4, 3));
        peli.peruutaSiirto();
        assert(tilaOlio == peli.haePeliTila());
    }
    
    @Test
    public void valkoinenOhestaLyöntiTila(){
        valkeanOhestaLyöntiAsetelma();
        peli.teeSiirto(new ShakkiSiirto(3, 4, 4, 5));
        assert(peli.haePeliTila().ohestaLyönti);
    }
    
    @Test
    public void valkoinenOhestaLyöntiRuudunTyhjennys(){
        valkeanOhestaLyöntiAsetelma();
        peli.teeSiirto(new ShakkiSiirto(3, 4, 4, 5));
        Assert.assertNull(peli.haeLauta().haeRuutu(4, 4));
    }
    
    public void valkoinenOhestaLyöntiSyötyNappula(){
        valkeanOhestaLyöntiAsetelma();
        peli.teeSiirto(new ShakkiSiirto(3, 4, 4, 5));
        assert(samanlainenNappula(peli.haePeliTila().syötyNappula, new Nappula(SOTILAS, MUSTA)));
    }
        
    @Test
    public void valkoinenOhestaLyöntiPeruutus(){
        valkeanOhestaLyöntiAsetelma();
        ShakkiLauta ennen = kopioLaudasta();
        peli.teeSiirto(new ShakkiSiirto(3, 4, 4, 5));
        peli.peruutaSiirto();
        assert(samanlainenLauta(ennen, peli.haeLauta()));
    }

    
    @Test
    public void mustaOhestaLyöntiPeruutus(){
        mustanOhestaLyöntiAsetelma();
        ShakkiLauta ennen = kopioLaudasta();
        peli.teeSiirto(new ShakkiSiirto(3, 3, 4, 2));
        peli.peruutaSiirto();
        assert(samanlainenLauta(ennen, peli.haeLauta()));
    }
    
    @Test
    public void mustanOhestaLyöntiPelitila(){
        mustanOhestaLyöntiAsetelma();
        peli.teeSiirto(new ShakkiSiirto(3, 3, 4, 2));
        assert(peli.haePeliTila().ohestaLyönti);
    }
    
    @Test
    public void mustanOhestalyöntiRuudunTyhjennys(){
        mustanOhestaLyöntiAsetelma();
        peli.teeSiirto(new ShakkiSiirto(3, 3, 4, 2));
        Assert.assertNull(peli.haeLauta().haeRuutu(3, 3));
    }
    
    @Test
    public void mustanOhestaLyöntiSyötyNappula(){
        mustanOhestaLyöntiAsetelma();
        peli.teeSiirto(new ShakkiSiirto(3, 3, 4, 2));
        assert(samanlainenNappula(peli.haePeliTila().syötyNappula, new Nappula(SOTILAS, VALKOINEN)));
    }
        
    @Test
    public void valkoinenKorotus(){
        
        tyhjäLauta.asetaRuutu(0, 6, new Nappula(SOTILAS, VALKOINEN));
        peli = new ShakkiPeli(tyhjäLauta, AloitusAsetelma.haeTila());
        Nappula korotusNappula = new Nappula(RATSU, VALKOINEN);
        ShakkiSiirto korotusSiirto = new ShakkiSiirto(0, 6, 0, 7, korotusNappula);
        
        peli.teeSiirto(korotusSiirto);
        assert(peli.haeLauta().haeRuutu(0, 7) == korotusNappula);
        
        peli.peruutaSiirto();
        assert(samanlainenLauta(tyhjäLauta, peli.haeLauta()));
    }
    
    @Test
    public void mustaKorotus(){
        
        tyhjäLauta.asetaRuutu(0, 1, new Nappula(SOTILAS, MUSTA));
        peli = new ShakkiPeli(tyhjäLauta, AloitusAsetelma.haeTila());
        Nappula korotusNappula = new Nappula(RATSU, MUSTA);
        ShakkiSiirto korotusSiirto = new ShakkiSiirto(0, 1, 0, 0, korotusNappula);
        
        peli.teeSiirto(korotusSiirto);
        assert(peli.haeLauta().haeRuutu(0, 0) == korotusNappula);
        
        peli.peruutaSiirto();
        assert(samanlainenLauta(tyhjäLauta, peli.haeLauta()));
    }
    
    @Test
    public void valkoinenKuningasLinnoitus(){
        teeSiirrot(new ShakkiSiirto(4, 1, 4, 3),
                new ShakkiSiirto(5, 0, 3, 2),
                new ShakkiSiirto(6, 0, 5, 2));
        
        ShakkiLauta ennen = kopioLaudasta();
        peli.teeSiirto(new ShakkiSiirto(4, 0, 6, 0));
        Assert.assertNull(peli.haeLauta().haeRuutu(7, 0));
        
        Nappula torni = new Nappula(TORNI, VALKOINEN);
        Nappula ruutu = peli.haeLauta().haeRuutu(5, 0);
        assert(samanlainenNappula(ruutu, torni));
        
        peli.peruutaSiirto();
        assert(samanlainenLauta(ennen, peli.haeLauta()));
    }
    
    @Test
    public void valkoinenKuningatarLinnoitus(){
        teeSiirrot(new ShakkiSiirto(3, 1, 3, 3),
                new ShakkiSiirto(2, 0, 4, 2),
                new ShakkiSiirto(1, 0, 2, 2),
                new ShakkiSiirto(3, 0, 3, 1));
        
        ShakkiLauta ennen = kopioLaudasta();
        peli.teeSiirto(new ShakkiSiirto(4, 0, 2, 0));
        Assert.assertNull(peli.haeLauta().haeRuutu(0, 0));
        
        Nappula torni = new Nappula(TORNI, VALKOINEN);
        Nappula ruutu = peli.haeLauta().haeRuutu(3, 0);
        assert(samanlainenNappula(ruutu, torni));
        
        peli.peruutaSiirto();
        assert(samanlainenLauta(ennen, peli.haeLauta()));
    }

    
    @Test
    public void mustaKuningasLinnoitus(){
        teeSiirrot(new ShakkiSiirto(4, 6, 4, 4),
                new ShakkiSiirto(5, 7, 3, 5),
                new ShakkiSiirto(6, 7, 5, 5));
        
        ShakkiLauta ennen = kopioLaudasta();
        peli.teeSiirto(new ShakkiSiirto(4, 7, 6, 7));
        Assert.assertNull(peli.haeLauta().haeRuutu(7, 7));
        
        Nappula torni = new Nappula(TORNI, MUSTA);
        Nappula ruutu = peli.haeLauta().haeRuutu(5, 7);
        assert(samanlainenNappula(ruutu, torni));
        
        peli.peruutaSiirto();
        assert(samanlainenLauta(ennen, peli.haeLauta()));
    }
    
    @Test
    public void mustaKuningatarLinnoitus(){
        teeSiirrot(new ShakkiSiirto(3, 6, 3, 4),
                new ShakkiSiirto(2, 7, 4, 5),
                new ShakkiSiirto(1, 7, 2, 5),
                new ShakkiSiirto(3, 7, 3, 6));
        
        ShakkiLauta ennen = kopioLaudasta();
        peli.teeSiirto(new ShakkiSiirto(4, 7, 2, 7));
        Assert.assertNull(peli.haeLauta().haeRuutu(0, 7));
        
        Nappula torni = new Nappula(TORNI, MUSTA);
        Nappula ruutu = peli.haeLauta().haeRuutu(3, 7);
        assert(samanlainenNappula(ruutu, torni));
        
        peli.peruutaSiirto();
        assert(samanlainenLauta(ennen, peli.haeLauta()));
    }
    
    @Test
    public void eiOhestaLyöntiSarakettaMustalle(){
        peli.teeSiirto(new ShakkiSiirto(4, 1, 4, 2));
        assert(peli.haePeliTila().ohestaLyöntiSarake == -1);
    }
    
    @Test
    public void ohestaLyöntiSarakeMustalle(){
        peli.teeSiirto(new ShakkiSiirto(4, 1, 4, 3));
        assert(peli.haePeliTila().ohestaLyöntiSarake == 4);
    }

    
    //ohestalyönti saa toimia vain seuraavalla siirrolla
    @Test
    public void vanhentunutOhestaLyöntiSarakeMustalle(){
        peli.teeSiirto(new ShakkiSiirto(4, 1, 4, 3));
        peli.teeSiirto(new ShakkiSiirto(5, 6, 5, 6));
        peli.teeSiirto(new ShakkiSiirto(0, 1, 0, 2));
        assert(peli.haePeliTila().ohestaLyöntiSarake == -1);
    }
    
    @Test
    public void eiOhestaLyöntiSarakettaValkealle(){
        peli.teeSiirto(new ShakkiSiirto(0, 6, 0, 5));
        assert(peli.haePeliTila().ohestaLyöntiSarake == -1);
    }

    @Test
    public void ohestaLyöntiSarakeValkealle(){
        peli.teeSiirto(new ShakkiSiirto(0, 6, 0, 4));
        assert(peli.haePeliTila().ohestaLyöntiSarake == 0);
    }

    @Test
    public void vanhentunutOhestaLyöntiSarakeValkealle(){
        peli.teeSiirto(new ShakkiSiirto(0, 6, 0, 4));
        peli.teeSiirto(new ShakkiSiirto(3, 1, 3, 3));
        peli.teeSiirto(new ShakkiSiirto(7, 6, 7, 5));
        assert(peli.haePeliTila().ohestaLyöntiSarake == -1);
    }
    
    
    @Test
    public void tyhjäPeruutus(){
        ShakkiLauta vanha = kopioLaudasta();
        PeliTila tila = peli.haePeliTila();
        
        peli.peruutaSiirto();
        
        assert(peli.haePeliTila() == tila);
        assert(samanlainenLauta(vanha, peli.haeLauta()));
    }
    
    @Test
    public void linnoitusMahdollisuudetSäilyy(){
        peli.teeSiirto(new ShakkiSiirto(0, 1, 0, 3));
        assert(peli.haePeliTila().linnoitusMahdollisuudet == Linnoitus.kaikki);
    }

    @Test
    public void linnoitusMahdollisuudetSäilyyTyhjäSiirto(){
        teeSiirrot(
                new ShakkiSiirto(4, 7, 4, 7),
                new ShakkiSiirto(0, 7, 0, 7),
                new ShakkiSiirto(0, 0, 0, 0),
                new ShakkiSiirto(7, 7, 7, 7),
                new ShakkiSiirto(7, 0, 7, 0),                
                new ShakkiSiirto(4, 0, 4, 0)
        );
        assert(peli.haePeliTila().linnoitusMahdollisuudet == Linnoitus.kaikki);
    }
    
    /**
     * Siirrot, jotka poistavat linnoitusmahdollisuuksia:
     */
    
    @Test 
    public void linnoitusMahdollisuudetValkoinenKuningasSiirtyy(){
        teeSiirrot( // e4, e5, Ke2
                new ShakkiSiirto(4, 1, 4, 3),
                new ShakkiSiirto(4, 6, 4, 4),
                new ShakkiSiirto(4, 0, 4, 1)
        );
        
        int lm = peli.haePeliTila().linnoitusMahdollisuudet;
        int pitäisiOlla = (Linnoitus.kaikki & ( ~ (Linnoitus.valkeaKuningas | Linnoitus.valkeaKuningatar) ));

        assert(lm == pitäisiOlla);
    }

    @Test 
    public void linnoitusMahdollisuudetMustaKuningasSiirtyy(){
        teeSiirrot(  // e4, e5, d4, Ke7
                new ShakkiSiirto(4, 1, 4, 3),
                new ShakkiSiirto(4, 6, 4, 4),
                new ShakkiSiirto(5, 1, 5, 3),
                new ShakkiSiirto(4, 7, 4, 6)
        );
        
        int lm = peli.haePeliTila().linnoitusMahdollisuudet;
        int pitäisiOlla = (Linnoitus.kaikki & ( ~ (Linnoitus.mustaKuningas | Linnoitus.mustaKuningatar) ));

        assert(lm == pitäisiOlla);
    }
    
    @Test
    public void linnoitusMahdollisuudetValkoinenKuningatarLinnoitus(){
        teeSiirrot(  // Nc3, a5, Rb1, h6
                new ShakkiSiirto(1, 0, 2, 2),
                new ShakkiSiirto(0, 6, 0, 4),
                new ShakkiSiirto(0, 0, 1, 0),
                new ShakkiSiirto(7, 6, 7, 5)
        );
        
        int lm = peli.haePeliTila().linnoitusMahdollisuudet;
        int pitäisiOlla = (Linnoitus.kaikki & (~ Linnoitus.valkeaKuningatar ));
        assert(lm == pitäisiOlla);
    }

    @Test
    public void linnoitusMahdollisuudetValkoinenKuningasLinnoitus(){
        teeSiirrot(  // Nf3, a5, Rg1, h6
                new ShakkiSiirto(6, 0, 2, 2),
                new ShakkiSiirto(0, 6, 0, 4),
                new ShakkiSiirto(7, 0, 6, 0),
                new ShakkiSiirto(7, 6, 7, 5)
        );
        
        int lm = peli.haePeliTila().linnoitusMahdollisuudet;
        int pitäisiOlla = (Linnoitus.kaikki & (~ Linnoitus.valkeaKuningas ));
        assert(lm == pitäisiOlla);
    }

    @Test
    public void linnoitusMahdollisuudetMustaKuningatarLinnoitus(){
        teeSiirrot(  // e4, Nc6, d4, Rb8, d5
                new ShakkiSiirto(4, 1, 4, 3),
                new ShakkiSiirto(1, 7, 2, 5),
                new ShakkiSiirto(3, 1, 3, 3),
                new ShakkiSiirto(0, 7, 1, 7),
                new ShakkiSiirto(3, 3, 3, 4)
        );
        
        int lm = peli.haePeliTila().linnoitusMahdollisuudet;
        int pitäisiOlla = (Linnoitus.kaikki & (~ Linnoitus.mustaKuningatar ));
        assert(lm == pitäisiOlla);
    }
 
    @Test
    public void linnoitusMahdollisuudetMustaKuningasLinnoitus(){
        teeSiirrot(  // e4, Nf6, e5, Rg8, exf
                new ShakkiSiirto(4, 1, 4, 3),
                new ShakkiSiirto(6, 7, 5, 5),
                new ShakkiSiirto(4, 3, 4, 4),
                new ShakkiSiirto(7, 7, 6, 7),
                new ShakkiSiirto(4, 4, 5, 5)
        );
        
        int lm = peli.haePeliTila().linnoitusMahdollisuudet;
        int pitäisiOlla = (Linnoitus.kaikki & (~ Linnoitus.mustaKuningas ));
        assert(lm == pitäisiOlla);
    }
    
    @Test
    public void tyhjäSiirto(){
        //lähtöruutuun siirrettäessä se ei saa tulla tyhjäksi
        
        teeSiirrot(new ShakkiSiirto(0, 0, 0, 0));
        Nappula nappulaNurkassa = peli.haeLauta().haeRuutu(0, 0);
        Nappula sotilas = new Nappula(TORNI, VALKOINEN); 
                
        assert(samanlainenNappula(nappulaNurkassa, sotilas));
    }
    
    private void valkeanOhestaLyöntiAsetelma(){
        //valkean d-sotilas voi lyödä mustan e-sotilaan
        
        teeSiirrot( // d4, a6, d5, e5
                new ShakkiSiirto(3, 1, 3, 3), 
                new ShakkiSiirto(0, 6, 0, 5),
                new ShakkiSiirto(3, 3, 3, 4),
                new ShakkiSiirto(4, 6, 4, 4));
    }
    
    private void mustanOhestaLyöntiAsetelma(){
        //mustan d-sotilas voi lyödä valken e-sotilaan
        
        teeSiirrot(  //a3, d5, a4, d4, e4
                new ShakkiSiirto(0, 1, 0, 2), 
                new ShakkiSiirto(3, 6, 3, 4),
                new ShakkiSiirto(0, 2, 0, 3),
                new ShakkiSiirto(3, 4, 3, 3),
                new ShakkiSiirto(4, 1, 4, 3)
        );
    }
    
    private ShakkiLauta kopioLaudasta(){
        ShakkiLauta lauta = new ShakkiLauta();
        lauta.kopioiAsetelma(peli.haeLauta());
        return lauta;
    }
    
    private boolean samanlainenLauta(ShakkiLauta a, ShakkiLauta b){
        for(int y = 0; y< 8; y++)
            for(int x = 0; x  <8; x++)
                if(!samanlainenNappula(a.haeRuutu(x, y), b.haeRuutu(x, y)))
                    return false;
        
        return true;
    }
    
    private boolean samanlainenNappula(Nappula a, Nappula b){
        if(a == b)
            return true;
        else if(a == null || b == null)
            return false;
        else
            return a.väri == b.väri && a.tyyppi == b.tyyppi;
    }
    
        
    private void teeSiirrot(ShakkiSiirto... siirrot){
        
        for (ShakkiSiirto siirto : siirrot) {
            peli.teeSiirto(siirto);
        }
    }

}
