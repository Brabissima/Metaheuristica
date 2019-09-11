
public class RMS implements Solver{
    Fac fac;
    Sol bestSol;
    int ite = 100, idx[], idy[];
    
    public RMS(int ite){
        this.ite=ite;
    }

    @Override
    public void setFac(Fac fac) {
        this.fac = fac;
        this.bestSol = new Sol(fac);
        idx = new int[fac.N];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;
        idy = new int[fac.cli];
        for (int i = 0; i < idy.length; i++) {
            idy[i] = i;
        }
    }

    @Override
    public double run() {
        Sol current = new Sol(fac);
        VND vnd = new VND(fac, current);
        double best = 99999999;
        for (int i = 0; i < ite; i++) {

            Utils.shuffler(idx);
            Utils.shuffler(idy);
            current.runRandom(idx, idy);
            vnd.run();
            double x = current.cost();
            if (x < best) {
                best = x;
                bestSol.copy(current);
                System.out.println(bestSol);
            }
        }
        System.out.println("E a melhor solução foi: ");
        System.out.println(bestSol);
        return best;
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
    public String toString() {
        return "RMS{" +
                "ite=" + ite +
                '}';
    }
    
}
