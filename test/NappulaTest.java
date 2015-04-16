

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import static pelinydin.NappulaTyyppi.KUNINGATAR;
import pelinydin.Väri;
import static pelinydin.Väri.MUSTA;
import static pelinydin.Väri.VALKOINEN;



public class NappulaTest {
    
    public NappulaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void nappulaTesti(){
        Nappula nappula = new Nappula(NappulaTyyppi.KUNINGAS, Väri.VALKOINEN);
        
        assert (nappula.tyyppi == NappulaTyyppi.KUNINGAS);
        assert (nappula.väri == Väri.VALKOINEN);
    }
    
    @Test
    public void samanlainenNappula(){
        Nappula a = new Nappula(KUNINGATAR, MUSTA);
        Nappula b = new Nappula(KUNINGATAR, MUSTA);
        assert(Nappula.onkoSamanlainenNappula(a, b));        
    }
    
    @Test
    public void erilainenNappula(){
        Nappula a = new Nappula(KUNINGATAR, MUSTA);
        Nappula b = new Nappula(KUNINGATAR, VALKOINEN);
        assertFalse(Nappula.onkoSamanlainenNappula(a, b));
    }
}
