import java.util.Arrays;

import static java.util.Arrays.fill;
import static java.util.Arrays.sort;

public class GRASP implements Solver {
    Fac fac;
    Sol bestSol;
    int ite, k;
    int candidato[];

    public GRASP(int k, int ite) { //k: qtdd de candidatos, ite: numero de iterações
        this.k = k;
        this.ite = ite;
        candidato = new int[k];
    }

    @Override
    public void setFac(Fac fac) {
        this.fac = fac;
        this.bestSol = new Sol(fac);
    }

    public void greedyRandom(Sol current) {
        int facof[] = current.facOf;
//        int facCount = 0;
        int load[] = new int[fac.N];
        fill(facof, -1);
        fill(load, 0);
        Integer idx[] = new Integer[fac.cli];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;

        for (int f = 0; f < fac.N; f++) {
            double linha[] = fac.atCost[f];
            Arrays.sort(idx,(a,b)->Double.compare(linha[a],linha[b]));

            for (int a = 0; a < fac.cli; a++) {
                int i = idx[a];
                if (facof[i] == -1 && fac.cliDem[i] + load[f] <= fac.c[f]) {
                    if (current.facY[f] == 0) current.facY[f] = 1;
                    load[f] += fac.cliDem[i];
                    facof[i] = f;
                    int s;
                    do {
                        s = 0;

                        for (int b = a + 1; b < fac.cli && s < k; b++) {
                            int j = idx[b];
                            if (facof[j] == -1 && fac.cliDem[i] + load[f] <= fac.c[f]) {
                                candidato[s] = j;
                                s++;
                            }
                        }
                        if (s > 0) {
                            int x = Utils.rd.nextInt(s);
                            facof[candidato[x]] = f;
                            load[f] += fac.cliDem[candidato[x]];
                        }
                    } while (s > 1);
//                    facCount++;
                }
            }
        }
        //return facCount;

    }

    @Override
    public double run() {
        Sol current = new Sol(fac);
        VND vnd = new VND(fac, current);

        greedyRandom(current);
        vnd.run();
        double best = current.cost();

        bestSol.copy(current);

        for (int i = 1; i < ite; i++) {

            greedyRandom(current);
            vnd.run();
            double x = current.cost();

            if (x < best) {
                best = x;
                bestSol.copy(current);
                System.out.println(i + " GRASP: " + x);
            }
        }

        return best;
    }

    public String toString() {
        return "GRASP{" +
                "k=" + k +
                ", ite=" + ite +
                '}';
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }

}
