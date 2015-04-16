
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pelinohjaus.SiirtojenGenerointi;
import pelinydin.AloitusAsetelma;
import pelinydin.Linnoitus;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.*;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;
import static pelinydin.Väri.MUSTA;
import static pelinydin.Väri.VALKOINEN;

/**
 *
 * @author Markus
 */
public class SiirtojenGenerointiTest {
    
    ShakkiLauta lauta;
    PeliTila valkoisenSiirto;
    PeliTila mustanSiirto;
    List<ShakkiSiirto> siirrot;

    
    public SiirtojenGenerointiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        lauta = new ShakkiLauta();
        valkoisenSiirto = AloitusAsetelma.haeTila();
        mustanSiirto = new PeliTila(null, null, -1, false, false, Linnoitus.kaikki, 1);
    }
    
    @After
    public void tearDown() {
    }
    
    private void lisääOhestaLyöntiSarake(int x){
        valkoisenSiirto = new PeliTila(null, null, x, false, false, Linnoitus.kaikki, 0);
        mustanSiirto = new PeliTila(null, null, x, false, false, Linnoitus.kaikki, 1);
    }
    
    private void aseteLinnoitusMahdollisuudet(int lm){
        valkoisenSiirto = new PeliTila(null, null, 0, false, false, lm, 0);
        mustanSiirto = new PeliTila(null, null, 0, false, false, lm, 1);
    }
    
    private void generoi(boolean siirtäjä){
        PeliTila tila = (siirtäjä == VALKOINEN) ? valkoisenSiirto : mustanSiirto;
            
        siirrot = SiirtojenGenerointi.haeSiirrot(lauta, tila);
    }
    
    private boolean löytyySiirto(int lähtöX, int lähtöY, int kohdeX, int kohdeY){
        return löytyySiirto(lähtöX, lähtöY, kohdeX, kohdeY, null);
    }
    
    private boolean samanlainenNappula(Nappula a, Nappula b){
        if(a == b)
            return true;
        else if(a == null || b == null)
            return false;
        else
            return a.väri == b.väri && a.tyyppi == b.tyyppi;
    }
    
    private boolean löytyySiirto(int lähtöX, int lähtöY, int kohdeX, int kohdeY, Nappula korotus){
        for (ShakkiSiirto siirto : siirrot) {
            if(siirto.lähtöX == lähtöX && siirto.lähtöY == lähtöY
                    && siirto.kohdeX == kohdeX && siirto.kohdeY == kohdeY
                    && samanlainenNappula(siirto.korotus, korotus)){
                return true;
            }
        }
        
        return false;
    }
        
    
    /* SOTILASSIIRROT */
    
    @Test
    public void valkeaSotilasYksiEteenpäin(){
        lauta.asetaRuutu(1, 3, new Nappula(SOTILAS, VALKOINEN));
        generoi(VALKOINEN);
        assert(siirrot.size() == 1);
        assert(löytyySiirto(1, 3, 1, 4));
    } 
    
    @Test
    public void valkeaSotilasYksiEteenpäinNappulaEdessä(){
        lauta.asetaRuutu(1, 3, new Nappula(SOTILAS, VALKOINEN));
        lauta.asetaRuutu(1, 4, new Nappula(SOTILAS, MUSTA));
        generoi(VALKOINEN);
        assert(siirrot.isEmpty());
    }
    
    @Test
    public void mustaSotilasYksiEteenpäin(){
        lauta.asetaRuutu(4, 5, new Nappula(SOTILAS, MUSTA));
        generoi(MUSTA);
        assert(siirrot.size() == 1);
        assert(löytyySiirto(4, 5, 4, 4));
    }
    
    @Test
    public void mustaSotilasYksiEteenpäinNappulaEdessä(){
        lauta.asetaRuutu(4, 5, new Nappula(SOTILAS, MUSTA));
        lauta.asetaRuutu(4, 4, new Nappula(SOTILAS, VALKOINEN));
        generoi(MUSTA);
        assert(siirrot.isEmpty());
    }
    
    @Test
    public void valkoinenSotilasKaksiEteenpäin(){
        lauta.asetaRuutu(3, 1, new Nappula(SOTILAS, VALKOINEN));
        generoi(VALKOINEN);
        assert(löytyySiirto(3, 1, 3, 3));
    }

    @Test
    public void valkoinenSotilasKaksiEteenpäinNappulaEdessä(){
        lauta.asetaRuutu(3, 1, new Nappula(SOTILAS, VALKOINEN));
        lauta.asetaRuutu(3, 3, new Nappula(SOTILAS, MUSTA));
        generoi(VALKOINEN);
        assertFalse(löytyySiirto(3, 1, 3, 3));
    }
    
    @Test
    public void valkoinenSotilasKaksiEteenpäinNappulaVälissä(){
        lauta.asetaRuutu(3, 1, new Nappula(SOTILAS, VALKOINEN));
        lauta.asetaRuutu(3, 2, new Nappula(RATSU, MUSTA));
        generoi(VALKOINEN);
        assertFalse(löytyySiirto(3, 1, 3, 3));
    }

    @Test
    public void mustaSotilasKaksiEteenpäin(){
        lauta.asetaRuutu(3, 6, new Nappula(SOTILAS, MUSTA));
        generoi(MUSTA);
        assert(löytyySiirto(3, 6, 3, 4));
    }

    @Test
    public void mustaSotilasKaksiEteenpäinNappulaEdessä(){
        lauta.asetaRuutu(3, 6, new Nappula(SOTILAS, MUSTA));
        lauta.asetaRuutu(3, 4, new Nappula(KUNINGATAR, VALKOINEN));
        generoi(MUSTA);
        assertFalse(löytyySiirto(3, 6, 3, 4));
    }

    @Test
    public void mustaSotilasKaksiEteenpäinNappulaVälissä(){
        lauta.asetaRuutu(3, 6, new Nappula(SOTILAS, MUSTA));
        lauta.asetaRuutu(3, 5, new Nappula(RATSU, VALKOINEN));
        generoi(MUSTA);
        assertFalse(löytyySiirto(3, 6, 3, 4));
    }
    
    @Test
    public void valkoinenOhestaLyönti(){
        lauta.asetaRuutu(3, 4, new Nappula(SOTILAS, MUSTA));
        lauta.asetaRuutu(4, 4, new Nappula(SOTILAS, VALKOINEN));
        lisääOhestaLyöntiSarake(3);
        generoi(VALKOINEN);
        assert(löytyySiirto(4, 4, 3, 5));
    }

    @Test
    public void valkoinenEiOhestaLyöntiä(){
        lauta.asetaRuutu(3, 4, new Nappula(SOTILAS, MUSTA));
        lauta.asetaRuutu(4, 4, new Nappula(SOTILAS, VALKOINEN));
        generoi(VALKOINEN);
        assertFalse(löytyySiirto(4, 4, 3, 5));
    }

    @Test
    public void mustaOhestaLyönti(){
        lauta.asetaRuutu(4, 3, new Nappula(SOTILAS, VALKOINEN));
        lauta.asetaRuutu(5, 3, new Nappula(SOTILAS, MUSTA));
        lisääOhestaLyöntiSarake(4);
        generoi(MUSTA);
        assert(löytyySiirto(5, 3, 4, 2));
    }

    @Test
    public void mustaEiOhestaLyöntiä(){
        lauta.asetaRuutu(4, 3, new Nappula(SOTILAS, MUSTA));
        lauta.asetaRuutu(5, 3, new Nappula(SOTILAS, VALKOINEN));
        generoi(MUSTA);
        assertFalse(löytyySiirto(5, 3, 4, 2));
    }
    
    @Test
    public void valkoinenKorotus(){
       lauta.asetaRuutu(1, 6, new Nappula(SOTILAS, VALKOINEN));
       generoi(VALKOINEN);
       assert(siirrot.size() == 4);
       assert(löytyySiirto(1, 6, 1, 7, new Nappula(RATSU, VALKOINEN)));
       assert(löytyySiirto(1, 6, 1, 7, new Nappula(KUNINGATAR, VALKOINEN)));
       assert(löytyySiirto(1, 6, 1, 7, new Nappula(TORNI, VALKOINEN)));
       assert(löytyySiirto(1, 6, 1, 7, new Nappula(LÄHETTI, VALKOINEN)));
    }
    
    @Test 
    public void valkoinenKorotusSyönti(){
       lauta.asetaRuutu(1, 6, new Nappula(SOTILAS, VALKOINEN));
       lauta.asetaRuutu(2, 7, new Nappula(KUNINGATAR, MUSTA));
       generoi(VALKOINEN);
       assert(siirrot.size() == 8);
       assert(löytyySiirto(1, 6, 2, 7, new Nappula(RATSU, VALKOINEN)));
       assert(löytyySiirto(1, 6, 2, 7, new Nappula(KUNINGATAR, VALKOINEN)));
       assert(löytyySiirto(1, 6, 2, 7, new Nappula(TORNI, VALKOINEN)));
       assert(löytyySiirto(1, 6, 2, 7, new Nappula(LÄHETTI, VALKOINEN)));
    }
    
    @Test
    public void mustaKorotus(){
       lauta.asetaRuutu(1, 1, new Nappula(SOTILAS, MUSTA));
       generoi(MUSTA);
       assert(siirrot.size() == 4);
       assert(löytyySiirto(1, 1, 1, 0, new Nappula(RATSU, MUSTA)));
       assert(löytyySiirto(1, 1, 1, 0, new Nappula(KUNINGATAR, MUSTA)));
       assert(löytyySiirto(1, 1, 1, 0, new Nappula(TORNI, MUSTA)));
       assert(löytyySiirto(1, 1, 1, 0, new Nappula(LÄHETTI, MUSTA)));
    }

    @Test
    public void mustaKorotusSyönti(){
       lauta.asetaRuutu(1, 1, new Nappula(SOTILAS, MUSTA));
       lauta.asetaRuutu(2, 0, new Nappula(RATSU, VALKOINEN));
       generoi(MUSTA);
       assert(siirrot.size() == 8);
       assert(löytyySiirto(1, 1, 2, 0, new Nappula(RATSU, MUSTA)));
       assert(löytyySiirto(1, 1, 2, 0, new Nappula(KUNINGATAR, MUSTA)));
       assert(löytyySiirto(1, 1, 2, 0, new Nappula(TORNI, MUSTA)));
       assert(löytyySiirto(1, 1, 2, 0, new Nappula(LÄHETTI, MUSTA)));
    }    
    
    /* UPSEERIEN SIIRROT */
    
    @Test
    public void hevosenSiirtojenMäärä(){
        lauta.asetaRuutu(0, 6, new Nappula(RATSU, MUSTA));
        generoi(MUSTA);
        assert(siirrot.size() == 3);
    }
    
    @Test
    public void hevosenSiirrot(){
        lauta.asetaRuutu(0, 6, new Nappula(RATSU, MUSTA));
        generoi(MUSTA);
        assert(löytyySiirto(0, 6, 2, 7));
        assert(löytyySiirto(0, 6, 2, 5));
        assert(löytyySiirto(0, 6, 1, 4));
    }
    
    @Test
    public void upseerillaSyönti(){
        lauta.asetaRuutu(0, 6, new Nappula(RATSU, MUSTA));
        lauta.asetaRuutu(2, 7, new Nappula(KUNINGATAR, VALKOINEN));
        generoi(MUSTA);
        assert(löytyySiirto(0, 6, 2, 7));
    }
    
    @Test
    public void upseeriOmaNappulaEdessä(){
        lauta.asetaRuutu(0, 6, new Nappula(RATSU, MUSTA));
        lauta.asetaRuutu(2, 7, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        assertFalse(löytyySiirto(0, 6, 2, 7));
    }
    
    @Test
    public void lähettiSiirtojenMäärä(){
        lauta.asetaRuutu(5, 5, new Nappula(LÄHETTI, VALKOINEN));
        generoi(VALKOINEN);
        assert(siirrot.size() == 11);
    }
    
    @Test
    public void lähettiOikealleAlas(){
        lauta.asetaRuutu(5, 5, new Nappula(LÄHETTI, VALKOINEN));
        generoi(VALKOINEN);
        assert(löytyySiirto(5, 5, 6, 4));
        assert(löytyySiirto(5, 5, 7, 3));
    }

    @Test
    public void lähettiOikealleYlös(){
        lauta.asetaRuutu(5, 5, new Nappula(LÄHETTI, VALKOINEN));
        generoi(VALKOINEN);
        assert(löytyySiirto(5, 5, 6, 6));
        assert(löytyySiirto(5, 5, 7, 7));
    }
    
    @Test
    public void lähettiVasemmalleYlös(){
        lauta.asetaRuutu(5, 5, new Nappula(LÄHETTI, VALKOINEN));
        generoi(VALKOINEN);
        assert(löytyySiirto(5, 5, 4, 6));
        assert(löytyySiirto(5, 5, 3, 7));
    }
    
    @Test
    public void lähettiVasemmalleAlas(){
        lauta.asetaRuutu(5, 5, new Nappula(LÄHETTI, VALKOINEN));
        generoi(VALKOINEN);
        assert(löytyySiirto(5, 5, 0, 0));
        assert(löytyySiirto(5, 5, 1, 1));
        assert(löytyySiirto(5, 5, 2, 2));
        assert(löytyySiirto(5, 5, 3, 3));
        assert(löytyySiirto(5, 5, 4, 4));
    }
    
    @Test
    public void lähettiNappulaVälissä(){
        lauta.asetaRuutu(0, 0, new Nappula(LÄHETTI, MUSTA));
        lauta.asetaRuutu(2, 2, new Nappula(RATSU, VALKOINEN));
        generoi(MUSTA);
        assertFalse(löytyySiirto(0, 0, 4, 4));
    }

    @Test
    public void lähettiNappulaVälissäSiirtojenMäärä(){
        lauta.asetaRuutu(0, 0, new Nappula(LÄHETTI, MUSTA));
        lauta.asetaRuutu(2, 2, new Nappula(RATSU, VALKOINEN));
        generoi(MUSTA);
        assert(siirrot.size() == 2);
    }    
    
    @Test
    public void lähettiSyönti(){
        lauta.asetaRuutu(0, 0, new Nappula(LÄHETTI, MUSTA));
        lauta.asetaRuutu(2, 2, new Nappula(RATSU, VALKOINEN));
        generoi(MUSTA);
        assert(löytyySiirto(0, 0, 2, 2));
    }    
    
    @Test
    public void torniSiirtojenMäärä(){
        lauta.asetaRuutu(2, 2, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        assert(siirrot.size() == 14);
    }
    
    @Test
    public void torniVasemmalle(){
        lauta.asetaRuutu(2, 2, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        assert(löytyySiirto(2, 2, 1, 2));
        assert(löytyySiirto(2, 2, 0, 2));
    }

    @Test
    public void torniAlas(){
        lauta.asetaRuutu(2, 2, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        assert(löytyySiirto(2, 2, 2, 1));
        assert(löytyySiirto(2, 2, 2, 0));
    }

    @Test
    public void torniOikealle(){
        lauta.asetaRuutu(2, 2, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        assert(löytyySiirto(2, 2, 3, 2));
        assert(löytyySiirto(2, 2, 4, 2));
        assert(löytyySiirto(2, 2, 5, 2));
        assert(löytyySiirto(2, 2, 6, 2));
        assert(löytyySiirto(2, 2, 7, 2));
    }
    
    @Test
    public void torniYlös(){
        lauta.asetaRuutu(2, 2, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        assert(löytyySiirto(2, 2, 2, 3));
        assert(löytyySiirto(2, 2, 2, 4));
        assert(löytyySiirto(2, 2, 2, 5));
        assert(löytyySiirto(2, 2, 2, 6));
        assert(löytyySiirto(2, 2, 2, 7));
    }
    
    @Test
    public void kuningatarSiirtojenMäärä(){
        lauta.asetaRuutu(3, 5, new Nappula(KUNINGATAR, MUSTA));
        generoi(MUSTA);
        assert(siirrot.size() == 25);        
    }
    
    @Test
    public void kuningatarSiirrot(){
        lauta.asetaRuutu(6, 5, new Nappula(RATSU, VALKOINEN));
        
        lauta.asetaRuutu(3, 5, new Nappula(TORNI, MUSTA));
        generoi(MUSTA);
        List<ShakkiSiirto> torniSiirrot = siirrot;
        lauta.asetaRuutu(3, 5, new Nappula(LÄHETTI, MUSTA));
        generoi(MUSTA);
        List<ShakkiSiirto> lähettiSiirrot = siirrot;
        lauta.asetaRuutu(3, 5, new Nappula(KUNINGATAR, MUSTA));
        generoi(MUSTA);

        for (ShakkiSiirto siirto : lähettiSiirrot) {
            assert(löytyySiirto(siirto.lähtöX, siirto.lähtöY, siirto.kohdeX, siirto.kohdeY));
        }
        for (ShakkiSiirto siirto : torniSiirrot) {
            assert(löytyySiirto(siirto.lähtöX, siirto.lähtöY, siirto.kohdeX, siirto.kohdeY));
        }
        assert(siirrot.size() == (lähettiSiirrot.size() + torniSiirrot.size()));
    }
    
    @Test
    public void kuningasSiirtojenMäärä(){
        aseteLinnoitusMahdollisuudet(0);
        lauta.asetaRuutu(5, 0, new Nappula(KUNINGAS, VALKOINEN));
        generoi(VALKOINEN);
        assert(siirrot.size() == 5);
    }
    
    @Test
    public void kuningasSiirrot(){
        aseteLinnoitusMahdollisuudet(0);
        lauta.asetaRuutu(5, 0, new Nappula(KUNINGAS, VALKOINEN));
        generoi(VALKOINEN);
        assert(löytyySiirto(5, 0, 4, 0));
        assert(löytyySiirto(5, 0, 4, 1));
        assert(löytyySiirto(5, 0, 5, 1));
        assert(löytyySiirto(5, 0, 6, 1));
        assert(löytyySiirto(5, 0, 6, 0));
    }
}