
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pelinydin.Linnoitus;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import pelinydin.PeliTila;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;

/**
 *
 * @author risto_000
 */
public class PeliTilaTest {
    
    ShakkiSiirto siirto;
    Nappula nappula;

    
    public PeliTilaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        siirto = new ShakkiSiirto(0, 0, 1, 1);
        nappula = new Nappula(NappulaTyyppi.RATSU, Väri.VALKOINEN);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void konstruktori(){
        PeliTila tila = new PeliTila(siirto, nappula, 5, 
                false, false, Linnoitus.kaikki, 4);
        
        assert(tila.korotus == false);
        assert(tila.syötyNappula == nappula);
        assert(tila.ohestaLyöntiSarake == 5);
        assert(tila.siirto == siirto);
        assert(tila.ohestaLyönti == false);
        assert(tila.linnoitusMahdollisuudet == Linnoitus.kaikki);
        assert(tila.siirtoNumero == 4);
    }

    
    @Test
    public void sallittuLinnoitus(){
        PeliTila tila = new PeliTila(null, null, -1, 
                false, false, Linnoitus.mustaKuningatar, 0);
        
        assert(tila.voikoLinnoittaa(Linnoitus.mustaKuningatar));
    }
    
    @Test
    public void laitonLinnoitus(){
        int linnoitus = Linnoitus.kaikki & (~Linnoitus.mustaKuningatar);
        PeliTila tila = new PeliTila(null, null, -1, 
                false, false, linnoitus, 0);
        
        assertFalse(tila.voikoLinnoittaa(Linnoitus.mustaKuningatar));
    }
    
    @Test
    public void valkoisenVuoro(){
        PeliTila tila = new PeliTila(null, null, -1, 
                false, false, Linnoitus.kaikki, 0);
        
        assert(tila.vuoro() == Väri.VALKOINEN);
    }

    @Test
    public void mustanVuoro(){
        PeliTila tila = new PeliTila(siirto, null, -1, 
                false, false, Linnoitus.kaikki, 3);
        
        assert(tila.vuoro() == Väri.MUSTA);
    }

}
