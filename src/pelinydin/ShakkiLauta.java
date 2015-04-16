

package pelinydin;


/**
 * Shakkilautaa vastaava 8x8 ruudukko. Siis yhteensä 64 ruutua.
 * Yhdessä ruudussa voi olla kerrallaan yksi nappula tai ruutu voi olla tyhjä (null).
 * Kahdessa ruudussa ei saa olla viittausta samaan nappula-olioon.
 * Ruudut yksilöidään x ja y -koordinaattien perusteella.
 * Koordinaatit menevät niin, että laudan vasemman laidan vaaka(x)-koordinaatti on 0 ja oikean laidan 7.
 * Kun valkoiset nappulat ovat alapuolella, alimman rivin pysty(y)-koordinaatti on 0 ja ylimman rivin 7.
 * Alussa kaikki ruudut ovat tyhjiä.
 * @author markumus
 */

public class ShakkiLauta{

    private final Nappula[][] taulu;
    
    /**
     * Aluksi kaikki ruudut ovat tyhjiä (null)
     */
    
    public ShakkiLauta(){
         taulu = new Nappula[8][8];
    }
    
    /**
     * Asettaa haluttuun ruutuun halutun nappulan tai asettaa ruudun tyhjäksi.
     * @param x Vaakakoordinaatti väliltä 0-7. 0 vastaa vasenta laitaa ja 7 oikeaa laitaa.
     * @param y Pystykoordinaatti väliltä 0-7. 0 vastaa valkoisen upseeririviä ja 7 vastaa mustan upseeririviä.
     * @param nappula Mikä tahansa nappula tai null, jos halutaan asettaa ruutu tyhjäksi
     */
    
    public void asetaRuutu(int x, int y, Nappula nappula) {
        taulu[y][x] = nappula;
    }

    /**
     * 
     * @param x Vaakakoordinaatti väliltä 0-7. 0 vastaa vasenta laitaa ja 7 oikeaa laitaa.
     * @param y Pystykoordinaatti väliltä 0-7. 0 vastaa valkoisen upseeririviä ja 7 vastaa mustan upseeririviä.
     * @return Ruudussa oleva nappula tai null, jos ruutu on tyhjä.
     */

    public Nappula haeRuutu(int x, int y) {
        return taulu[y][x];
    }
    
    /**
     * Muuttaa tämän laudan jokaisen ruudun vastaamaan täsmällisesti toisen laudan ruutuja.
     * @param lauta Toinen lauta-olio. Ei saa olla null.
     */
    
    public void kopioiAsetelma(ShakkiLauta lauta){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                this.taulu[y][x] = lauta.taulu[y][x];
            }
        }
    }

}
