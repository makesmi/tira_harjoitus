

package Tutkimus;


public class SelausSolmu {
    public final PeliSolmu solmu;
    public final int x;
    public final int y;
    public final SelausSolmu vanhempi;
    public final String notaatio;
    public final boolean siirtäjä;

    public SelausSolmu(PeliSolmu solmu, int x, int y, SelausSolmu vanhempi, String notaatio, boolean siirtäjä) {
        this.solmu = solmu;
        this.x = x;
        this.y = y;
        this.vanhempi = vanhempi;
        this.notaatio = notaatio;
        this.siirtäjä = siirtäjä;
    }
}
