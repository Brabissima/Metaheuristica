import static java.util.Arrays.fill;

public class SA implements Solver {
    private Fac fac;
    public Sol bestSol;
    double t0, tf, l;

    public SA(double t0, double tf, double l) {
        this.t0 = t0;
        this.tf = tf;
        this.l = l;
    }

    @Override
    public String toString() {
        return "SA{" +
                "t0=" + t0 +
                ", tf=" + tf +
                ", l=" + l +
                '}';
    }

    @Override
    public void setFac(Fac fac) {
        this.fac = fac;
    }

    @Override
    public double run() {
        Sol current = new Sol(fac);
        bestSol = new Sol(fac);
        bestSol.copy(current);
        current.run();
        double best = current.cost();
        double costTest = best;

        Sol aux = new Sol(fac);
        int idx[] = new int[fac.N];
        int idy[] = new int[fac.cli];
        for (int i = 0; i < fac.N; i++) {
            idx[i] = i;
        }
        for (int i = 0; i < fac.cli; i++) {
            idy[i] = i;
        }
        aux.runRandom(idx, idy);
        double costCurr = current.cost();
        for (double T = t0; T > tf; T *= l) {
            if (!pertub(current, aux))
                continue;
            double costAux = aux.cost();
//            System.out.println(costCurrent);
            if (costAux < costCurr) {
                current.copy(aux);
                costCurr = costAux;
                if (costCurr < best) {
                    best = costCurr;
                    bestSol.copy(current);
                    System.out.println("SA: " + best + " | facCount:" + current.facCount());
                    T = t0;
                }

                //System.out.println(T + " "+currFA);
            } else if (Utils.rd.nextDouble() < P(costCurr - costAux, T)) {
                current.copy(aux);
                System.out.println(T + " "+costCurr+" *");
            }


        }
        return bestSol.cost();
    }

    private double P(double delta, double t) {
        return Math.exp(delta / t);
    }

    private boolean pertub(Sol curr, Sol aux) {
        aux.copy(curr);
        double load[] = new double[fac.N];
        fill(load,0);
        for (int i = 0; i < fac.cli; i++)
            load[curr.facOf[i]] += fac.cliDem[i];
        if (Utils.rd.nextBoolean()) { //um item troca de pacote
            int idx[] = new int[fac.N];
            for (int i = 0; i < fac.N; i++)
                idx[i] = i;
            Utils.shuffler(idx);

            for (int pack = 0; pack < fac.N; pack++) {
                double sobra = fac.c[pack] - load[pack];
                int x;
                for (x = fac.cli - 1; x >= 0 && curr.load[curr.facOf[x]] <= sobra; x--) {
                    //nada
                }
                int s = fac.cli - x - 1;
                if (s > 0) {
                    int item = Utils.rd.nextInt(s) + x + 1;
                    if (curr.facOf[item]!= pack) {
                        int bi = curr.facOf[item];
                        aux.facOf[item] = pack;
                        load[bi] -= fac.cliDem[item];
                        //se o pacote do item esvaziar
                        aux.updateAtributes();
                        return true;
                    }
                }
            }

        } else { // dois itens trocam de pacotes entre si
            int idx[] = new int[fac.cli];
            for (int i = 0; i < fac.cli; i++)
                idx[i] = i;
            Utils.shuffler(idx);
            for (int a = 0; a < fac.cli; a++) {
                int i = idx[a];
                int bi = curr.facOf[i];
                for (int b = 0; b < a; b++) {
                    int j = idx[b];
                    int bj = curr.facOf[j];
                    if (bi != bj) {
                        double delta = load[bi] - curr.load[curr.facOf[i]] + curr.load[curr.facOf[j]];
                        delta += load[bj] - curr.load[curr.facOf[j]] + curr.load[curr.facOf[i]];
                        if (delta > -0.00001) {
                            aux.facOf[i] = bj;
                            aux.facOf[j] = bi;
                            aux.updateAtributes();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}