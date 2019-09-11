
import static java.util.Arrays.fill;

public class VND {
    Fac fac;
    Sol sol;
    int fidx[], idx[], cidx[], lb, count;
    int load[], cliDem[];
    int facof[], facY[];

    public VND(Fac fac, Sol sol) {
        this.fac = fac;
        this.sol = sol;
        idx = new int[fac.cli];
        fidx = new int[fac.N];
        cidx = new int[fac.cli];
        lb = fac.LB();
        for (int i = 0; i < fac.N; i++) {
            fidx[i] = i;
        }
        for (int i = 0; i < fac.cli; i++) {
            idx[i] = i;
            cidx[i] = i;
        }

    }

    public boolean move1() {
        int fi;
        for (int i : idx) {
            fi = facof[i];
            for (int fj : fidx) {
                if ((fi != fj) && (load[fj] + cliDem[i] <= fac.c[fj])) { //verifica se o cliente i cabe na facilidade fj;
                    double delta = fac.atCost[fj][i] - fac.atCost[fi][i];
                    if (load[fj] == 0){
                        delta += fac.opCost[fj];
                    }
                    if (load[fi] - fac.cliDem[i] == 0){
                        delta -= fac.opCost[fi];
                    }
                    if (delta < -0.00001) { // verifica se é melhor atender por fj ou por fi;
                        load[fj] += cliDem[i];
                        load[fi] -= cliDem[i];
                        facof[i] = fj;
                        if (load[fi] == 0) { //se facilidade não atender nenhum cliente, ela é fechada
                            facY[fi] = 0;

//                            fidx = new int[fac.N];
//                            for (int z = 0; z < fac.N; z++)
//                                fidx[z] = z;
                        }
                        load[fj] = 1;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean move2() { 
        int N = facof.length;
        for (int a = 0; a < fac.cli; a++) {
            int i = idx[a];
                for (int b = a + 1; b < fac.cli; b++) {
                    int j = idx[b];
                        if (facof[i] != facof[j] &&
                                load[facof[i]] - cliDem[i] + cliDem[j] <= fac.c[facof[i]] &&
                                load[facof[j]] - cliDem[j] + cliDem[i] <= fac.c[facof[j]]) {
                            double delta = fac.atCost[facof[j]][i] - fac.atCost[facof[i]][i];
                            delta += fac.atCost[facof[i]][j] - fac.atCost[facof[j]][j];
                            if (delta < -0.00001) { //troca os clientes de facilidade
                                load[facof[i]] += -cliDem[i] + cliDem[j];
                                load[facof[j]] += -cliDem[j] + cliDem[i];
                                int aux = facof[i];
                                facof[i] = facof[j];
                                facof[j] = aux;
//                                System.out.println("OBA");
                                return true;
                            }
                        }

                }


        }
        return false;
    }

    public boolean move3() {
        return false;
    }

    public void run() {
        count = sol.facCount();
        load = new int[fac.N];
        facof = sol.facOf;
        cliDem = fac.cliDem;
        facY = sol.facY;
        for (int i = 0; i < facof.length; i++)
            load[facof[i]] += cliDem[i];
        for (int i = 0; i < fidx.length; i++)
            fidx[i] = i;
        
        boolean flag = false;
        do {
            Utils.shuffler(fidx);
            Utils.shuffler(idx);
            Utils.shuffler(cidx);

            flag = move1();
            if (!flag)
//                System.out.println("oi");
                flag = move2();
//            System.out.println(sol);
            sol.updateAtributes();
        } while (flag);
//        System.out.println(sol);
    }
}
