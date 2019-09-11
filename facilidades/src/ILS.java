
import static java.util.Arrays.fill;

import java.util.Random;
import static java.util.Arrays.fill;

public class ILS implements Solver {
    Fac fac;
    Sol bestSol;
    private int k, fidx[], idx[];
    public int ite; //ite: n° de iterações, k: número de facilidades alteradas a cada nova iteração

    public ILS(int ite, int k) {
        this.ite = ite;
        this.k = k;
    }


    public void setFac(Fac fac) {
        this.fac = fac;
        this.bestSol = new Sol(fac);
        idx = new int[fac.cli];
        fidx = new int[fac.N];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
        for (int i = 0; i < fac.N; i++) {
            fidx[i] = i;
        }
    }

    /**
     * Alocar clientes sem facilidade em ordem randomica
     */
    public int fit(Sol current) {
        int facof[] = current.facOf;
        int facY[] = current.facY;
        int facCount = current.facCount;
        double[] load = current.load;
//        System.out.println(load);
        Utils.shuffler(idx);
        Utils.shuffler(idx);
        Utils.shuffler(fidx);

        for (int a = 0; a < fac.cli; a++) { //cliente aleatorio
            int i = idx[a];
            if (facof[i] == -1)
                for (int p = 0; p < fac.N; p++) { // facilidade aleatória
                    int k = fidx[p];
                    if (load[k] + fac.cliDem[i] <= fac.c[k]) {
                        if (facY[k] == 0) {
                            facY[k] = 1;
                        }
                        load[k] += fac.cliDem[i];
                        facof[i] = k;
                        break;
//                        for (int b = a + 1; b < fac.N; b++) {
//                            int j = idx[b];
//                            if (facof[j] == -1 && fac.cliDem[j] <= sobra[k]) {
//                                facof[j] = facCount;
//                                sobra[k] -= fac.cliDem[j];
//                            }
//                        }
//                        facCount++;
                    }
            }

        }
        current.updateAtributes();
        return facCount;
    }

    /**
     * Fecha k facilidades e realoca seus clientes em ordem aleatória
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
            for (int j = 0; j < fac.cli; j++)
                if (current.facOf[j] == x)
                    current.facOf[j] = -1;
            current.facY[x] = 0;
        }
        fit(current);
    }

    @Override
    public double run() {
        Sol current = new Sol(fac);
        current.run();
        VND vnd = new VND(fac, current);

//        Utils.shuffler(idx);
//        current.bestFitRandom(idx);
        vnd.run();
        double best = current.cost();

        System.out.println(current);

//        bestSol.copy(current);
//        System.out.println(bestSol);
        for (int i = 1; i < ite; i++) {

            pertub(k, current, bestSol);

            vnd.run();
            double x = current.cost();

            if (x < best) {
                best = x;
                bestSol.copy(current);
//                bestSol.updateAtributes();
                System.out.println(bestSol);
            }
        }
        System.out.println("E a melhor solução foi:");
        System.out.println(bestSol);
        return best;
    }

    @Override
    public String toString() {
        return "ILS{" +
                "k=" + k +
                ", ite=" + ite +
                '}';
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }

}
