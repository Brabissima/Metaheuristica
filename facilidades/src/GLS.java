
import java.util.ArrayList;

import static java.util.Arrays.fill;

public class GLS {
    Sol bestSol;
    Fac fac;
    int idx[], idy[], ite, p[][], load[];

    public GLS(int ite) {
        this.ite = ite;
    }

    public int hc(Sol sol) {
        int count = sol.facCount();
        int load[] = new int[fac.N];
        int facof[] = sol.facOf;
        int facY[] = sol.facY;
        int cliDem[] = fac.cliDem;
        double sumCost[] = new double[fac.N];
        fill(sumCost, 0);
        for (int i = 0; i < fac.N; i++) {
            load[i] = fac.c[i];
            for (int j = 0; j < fac.cli; j++) {
                sumCost[i] += (fac.opCost[i] + fac.atCost[i][j]) * facY[i];
                load[i] += cliDem[j] * facY[i];
            }
        }
        boolean moved;
        do {
            moved = false;
            ls:
            for (int j = 0; j < fac.cli; j++) { // p cada cliente
                for (int i = 0; i < fac.N; i++) { //numero de facilidades
                    if (facof[j] != 999999) {
                        int fj = sol.facOf[j]; //facilidade do cliente j
                        if (fj != i && load[i] + cliDem[i] <= fac.c[i]) {
                            fac.sumDem[i] += cliDem[j];
                            fac.sumDem[fj] -= cliDem[j];
                            if (fac.sumDem[fj] == 0) sol.facY[fj] = 0;
                            if (fac.atCost[i][j] + (fac.opCost[i] * (1 - sol.facY[i])) <
                                    fac.atCost[fj][j] + (fac.opCost[fj] * (1 - sol.facY[fj]))) {
                                sol.facY[i] = 1;
                                sol.facOf[j] = i;
                                if (fac.sumDem[fj] == 0) {
                                    sol.facY[fj] = 0;
                                    count--;
                                }
                                moved = true;
                                break ls;
                            } else {
                                sol.facY[fj] = 1;
                                fac.sumDem[i] -= fac.cliDem[j];
                                fac.sumDem[fj] += fac.cliDem[j];
                            }
                        }
                    }
                }
//            }
            }
            System.out.println(sol.cost());
        } while (moved);
        return count;

    }

    public void setFac(Fac fac) {
        this.fac = fac;
        this.bestSol = new Sol(fac);
        idx = new int[fac.cli];
        idy = new int[fac.N];
        for (int i = 0; i < fac.N; i++) {
            idy[i] = i;
        }
        for (int i = 0; i < idx.length; i++){
            idx[i] = i;
        }
        p = new int[fac.cli][];
        for (int i = 0; i < idx.length; i++)
            p[i] = new int[i];
    }

    public Sol run() {
        //constroi solução inicial
        Sol sol = new Sol(fac);
        Utils.shuffler(idx);
        Utils.shuffler(idy);
        int count = sol.runRandom(idy, idx);
        bestSol.copy(sol);
        load = new int[count];
        for (int i = 0; i < sol.facOf.length; i++)
            load[sol.facOf[i]] += fac.cliDem[i];

        for (int i = 0; i < ite; i++) {
            hc(sol);
            if (sol.facCount() < bestSol.facCount()) {
                bestSol.copy(sol);
                System.out.println("GLS: " + bestSol.facCount());
                penaltyReset();
            }

            updateMatriz(sol);

        }

        return bestSol;
    }

    private void penaltyReset() {
        for (int i = 0; i < idx.length; i++)
            fill(p[i], 0);
    }


    private void updateMatriz(Sol sol) {
        int k = 0;
        k = Utils.rd.nextInt(load.length);

        for (int i = 0; i < fac.cli; i++)
            if (sol.facOf[i] == k) {
                for (int j = 0; j < i; j++)
                    if (sol.facOf[j] == k) {
                        p[i][j]++;
                    }
            }
    }

    private double penalty(Sol sol) {
        int s = 0;
        for (int i = 0; i < fac.cli; i++) {
            for (int j = 0; j < i; j++)
                if (sol.facOf[i] == sol.facOf[j]) {
                    s += p[i][j];
                }
        }
        return Math.exp(-s);
    }

    public Sol getSol() {
        return bestSol;
    }

}
