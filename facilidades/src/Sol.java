import static java.util.Arrays.fill;

public class Sol implements Comparable<Sol> {
    int facOf[]; //facilidade q atende cliente x
    int facY[]; //se facilidade abre ou n
    Fac fac; //referencia
    double facCost; //custo total
    double load[];
    int facCount;

    public Sol(Fac fac) {
        this.fac = fac;
        facY = new int[fac.N];
        facOf = new int[fac.cli];
        load = new double[fac.N];
    }

    /**
     * Quantidade de facilidades usadas
     */
    public int facCount() {
        int m = 0;
        for (int x : facY)
                m += x;
        return m;
    }

    public String toString() {
        return "Sol{" +
                "facCount=" + facCount() +
                ", cost=" + cost() +
                '}';
    }

    /**
     * calculo do custo: custo da abertura das facilidades usadas + custo do atendimento do cliente i pela facilidade facOf[i]
     */
    public double cost() {
        double c = 0;
        for (int i = 0; i < fac.N; i++) {
            if (facY[i] == 1) {
                c += fac.opCost[i];
            }
        }
        for (int i = 0; i < fac.cli; i++) {
            c += fac.atCost[facOf[i]][i];
        }
        return c;
    }

    public void run() {
        fill(facOf, -1);
        fill(facY, 0);
        fill(fac.sumDem, 0);
        boolean alt = false;

//        double dem = 0;
//        int k = 0;
//        int j;
//        for (int i = 0; i < fac.N; i++) {
//            for (j = k; j < fac.cli; j++) {
//                if (dem + fac.cliDem[j] <= fac.c[i]) {
//                    facY[i] = 1;
//                    facOf[j] = i;
//                    dem =+ fac.cliDem[j];
//                } else {
////                    dem = 0;
////                    break;
//                    System.out.println("aqui");
//                }
//
//            }
//            k = j;
//            if (facOf[fac.cli - 1] != -1) {
//                break;
//            }
//        }
        double custoCnd[] = new double[fac.cli]; // custo canditado para atendimento do cliente j
        fill(custoCnd, 999999999);

        for (int i = 0; i < fac.N; i++) { // para todos as facilidades
            alt = false;
            if (!alt){
                for (int j = 0; j < fac.cli; j++) { // para todos os clientes
                    double residuo = fac.c[i] - fac.cliDem[j] - fac.sumDem[i];
                    if (residuo >= 0) {
                        if ((fac.atCost[i][j]) < custoCnd[j]) {
                            if (custoCnd[j] == 999999999) {
                                if (fac.sumDem[i] + fac.cliDem[j] <= fac.c[i]) {
                                    facY[i] = 1;
                                    fac.sumDem[i] = fac.sumDem[i] + fac.cliDem[j];
                                    facOf[j] = i;
                                    custoCnd[j] = fac.atCost[i][j];
                                }
                            } else {
                                if (fac.sumDem[i] + fac.cliDem[j] <= fac.c[i]) {
                                    int fj = facOf[j];
                                    fac.sumDem[fj] = fac.sumDem[fj] - fac.cliDem[j];
                                    if (fac.sumDem[fj] == 0) {
                                        facY[fj] = 0;
                                    }
                                    if (facY[i] == 0) {
                                        facY[i] = 1;
                                    }
                                    facOf[j] = i;
                                    fac.sumDem[i] = fac.sumDem[i] + fac.cliDem[j];
                                    custoCnd[j] = fac.atCost[i][j];
                                    alt = true;
                                }
                            }
                        }
                    }
                }
                if (i!=0 && alt){
                    i = -1;
                }
            }
        }
        for (int k = 0; k < fac.sumDem.length; k++) {
            load[k] = fac.sumDem[k];
        }

//        System.out.println("oi");
//        System.out.println(facCount());
    }

    public int runRandom(int[] idx, int[] idy) {
        fill(facOf, -1);
        fill(facY, 0);
        fill(fac.sumDem, 0);
        boolean alt = false;
        int i, j;
        double custoCnd[] = new double[fac.cli]; // custo canditado para atendimento do cliente j
        fill(custoCnd, 999999999);

        for (int a = 0; a < fac.N; a++) { // para todos as facilidades
            alt = false;
            i = idx[a];
            if (!alt){
                for (int b = 0; b < fac.cli; b++) { // para todos os clientes
                    j = idy[b];
                    double residuo = fac.c[i] - fac.cliDem[j] - fac.sumDem[i];
                    if (residuo >= 0) {
                        if ((fac.atCost[i][j]) < custoCnd[j]) {
                            if (custoCnd[j] == 999999999) {
                                if (fac.sumDem[i] + fac.cliDem[j] <= fac.c[i]) {
                                    facY[i] = 1;
                                    fac.sumDem[i] = fac.sumDem[i] + fac.cliDem[j];
                                    facOf[j] = i;
                                    custoCnd[j] = fac.atCost[i][j];
                                }
                            } else {
                                if (fac.sumDem[i] + fac.cliDem[j] <= fac.c[i]) {
                                    int fj = facOf[j];
                                    fac.sumDem[fj] = fac.sumDem[fj] - fac.cliDem[j];
                                    if (fac.sumDem[fj] == 0) {
                                        facY[fj] = 0;
                                    }
                                    if (facY[i] == 0) {
                                        facY[i] = 1;
                                    }
                                    facOf[j] = i;
                                    fac.sumDem[i] = fac.sumDem[i] + fac.cliDem[j];
                                    custoCnd[j] = fac.atCost[i][j];
                                    alt = true;
                                }
                            }
                        }
                    }
                }
                if (i!=0 && alt){
                    a = -1;
                }
            }
        }
        for (int k = 0; k < fac.sumDem.length; k++) {
            load[k] = fac.sumDem[k];
        }
        return 0;
    }

    @Override
    public int compareTo(Sol t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateAtributes() {
        facCount = facCount();
        facCost = cost();
        fill(load, 0);

        for (int i = 0; i < fac.cli; i++)
            load[facOf[i]] += fac.cliDem[i];
        fill(facY,0);
        for (int i = 0; i < fac.cli; i++) {
            if (load[facOf[i]] > 0){
                facY[facOf[i]] = 1;
            }
        }

    }

    public void copy(Sol s) {
        for (int i = 0; i < facOf.length; i++)
            facOf[i] = s.facOf[i];
        for (int i = 0; i < facY.length; i++) {
            facY[i] = s.facY[i];
        }

        this.facCount = s.facCount;
        updateAtributes();
    }

}
