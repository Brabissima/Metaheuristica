
import static java.util.Arrays.fill;

import java.util.Random;

/**
 * Variable Neighborhood Search
 */
public class VNS implements Solver {
    Fac fac;
    Sol bestSol;
    int idx[], idy[];
    int ite, v, passo; //ite: qtd de iterações, v: vizinhanças, passo: quantidade de facilidades fechadas q aumenta a cada iteração

    public VNS(int ite, int v, int passo) {
        this.ite = ite;
        this.v = v;
        this.passo = passo;
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

    public void fit(Sol current) {
        int facof[] = current.facOf;
        int facY[] = current.facY;
        int facCount = current.facCount;
        int load[] = new int[fac.N];
        fill(load, 0);
        Random r = new Random();
        Utils.shuffler(idy);
        Utils.shuffler(idx);

        for (int i = 0; i < fac.cli; i++)
            if (facof[i] >= 0)
                load[facof[i]] += fac.cliDem[i];


        for (int a = 0; a < fac.cli; a++)
            if (facof[idy[a]] == -1) { //cliente aleatorio
                int i = idy[a];
                for (int k : idx)
                    if (load[k] + fac.cliDem[i] <= fac.c[k]) {
                        if (facY[k] == 0) facY[k] = 1;
                        load[k] += fac.cliDem[i];
                        facof[i] = k;
//                        for (int b = a + 1; b < fac.cli; b++) {
//                            int j = idy[b];
//                            if (facof[j] == -1 && load[k] + fac.cliDem[i] <= fac.c[k]) {
//                                facof[j] = k;
//                                load[k] -= fac.cliDem[j];
//                            }
//                        }
                        break;
                    }

            }

    }

    /**
     * Fecha k facilidades aleatórias e realoca os clientes em ordem aleatória
     *
     * @param k       numero de facilidades fechadas
     * @param current saída com solução best pertubada
     * @param best    solução de entrada
     */
    private void pertub(int k, Sol current, Sol best) {
        current.copy(best);
        int n = fac.N;
        for (int i = 0; i < k; i++) {
            int x = Utils.rd.nextInt(n);
            current.facY[x] = 0;
            for (int j = 0; j < fac.cli; j++)
                if (current.facOf[j] == x)
                    current.facOf[j] = -1;
        }
        fit(current);
        current.updateAtributes();
    }

    @Override
    public String toString() {
        return "VNS{" +
                "ite=" + ite +
                ", v=" + v +
                ", passo=" + passo +
                '}';
    }

    @Override
    public double run() {
        Sol current = new Sol(fac);
        VND vnd = new VND(fac, current);

        Utils.shuffler(idx);
        current.runRandom(idx, idy);
        vnd.run();
        double best = current.cost();

        bestSol.copy(current);
        int k = 1;
        int cont = 0;
        while (cont < v) {
            for (int i = 1; i < ite; i++) {
                pertub(k, current, bestSol);
                vnd.run();
                double x = current.cost();
                if (x < best) {
                    System.out.println(k + " VNS: " + x);
                    best = x;
                    bestSol.copy(current);
                    k = 1;
                    cont = 0;
                    i = -1;
                }
            }
            cont++;
            k += passo;
        }
        return best;
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }

}
