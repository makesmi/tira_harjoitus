

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import static pelinydin.NappulaTyyppi.RATSU;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;
import static pelinydin.Väri.VALKOINEN;



public class ShakkiSiirtoTest {
    
    ShakkiSiirto siirto;
    
    public ShakkiSiirtoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        siirto = new ShakkiSiirto(1, 2, 3, 4);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void perusKonstruktori(){
        assert(siirto.lähtöX == 1);
        assert(siirto.lähtöY == 2);
        assert(siirto.kohdeX == 3);
        assert(siirto.kohdeY == 4);
        Assert.assertNull(siirto.korotus);
    }

    @Test
    public void korotusKostruktori(){
        Nappula nappula = new Nappula(NappulaTyyppi.RATSU, Väri.VALKOINEN);
        
        siirto = new ShakkiSiirto(1, 2, 3, 4, nappula);
        assert(siirto.lähtöX == 1);
        assert(siirto.lähtöY == 2);
        assert(siirto.kohdeX == 3);
        assert(siirto.kohdeY == 4);
        assert(siirto.korotus == nappula);
    }
    
    @Test
    public void samatSiirrot(){
        ShakkiSiirto siirto2 = new ShakkiSiirto(1, 2, 3, 4);
        
        assert(siirto.onkoSama(siirto2));
    }
    
    @Test
    public void samanLaisetKorotussiirrot(){
        ShakkiSiirto a = new ShakkiSiirto(5, 6, 5, 7, new Nappula(RATSU, VALKOINEN));
        ShakkiSiirto b = new ShakkiSiirto(5, 6, 5, 7, new Nappula(RATSU, VALKOINEN));
        assert(a.onkoSama(b));
    }
    
    @Test
    public void eriSiirrot(){
        Nappula nappula = new Nappula(NappulaTyyppi.RATSU, Väri.VALKOINEN);

        ShakkiSiirto siirto2 = new ShakkiSiirto(1, 2, 3, 4, nappula);
        ShakkiSiirto siirto3 = new ShakkiSiirto(1, 0, 3, 4);
        
        Assert.assertFalse(siirto.onkoSama(siirto2));
        Assert.assertFalse(siirto.onkoSama(siirto3));
    }
    

}
