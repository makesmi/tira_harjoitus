

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import pelinydin.ShakkiLauta;
import pelinydin.Väri;


public class ShakkiLautaTest {
    
    ShakkiLauta lauta;
    Nappula nappula;
    
    public ShakkiLautaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        nappula = new Nappula(NappulaTyyppi.RATSU, Väri.MUSTA);
        lauta = new ShakkiLauta();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void AlussaTyhjä(){
        for(int x = 0; x < 8; x++)
            for(int y = 0; y< 8; y++){
                Assert.assertNull(lauta.haeRuutu(x, y));
            }
    }

    @Test
    public void RuudunAsetus(){
        lauta.asetaRuutu(5, 5, nappula);
        assert(lauta.haeRuutu(5, 5) == nappula);
    }
    
    @Test
    public void Tyhjennys(){
        lauta.asetaRuutu(5, 5, nappula);
        lauta.asetaRuutu(5, 5, null);
        assert(lauta.haeRuutu(5, 5) == null);
    }
    
    @Test
    public void Kopiointi(){
        ShakkiLauta lauta2 = new ShakkiLauta();
        
        lauta.asetaRuutu(5, 5, nappula);
        lauta2.asetaRuutu(1, 1, nappula);        
        lauta.kopioiAsetelma(lauta2);
        
        Assert.assertNull(lauta.haeRuutu(5, 5));
        assert(lauta.haeRuutu(1, 1) == nappula);
    }
}
