import static java.util.Arrays.fill;

/**
 * Hill Climbing
 */
public class HC {
    Fac fac;
    Sol sol;

    public HC(Fac fac, Sol sol) {
        this.fac = fac;
        this.sol = sol;
    }

    public int run() {
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
}