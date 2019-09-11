import java.util.ArrayList;
import java.util.Collections;
 
public class GA implements Solver{
    Fac fac;
    Sol bestSol;
    int popSize; //tamanho da população
    double muteRatio; //prob de sofrer mutação
    int popIdx[];
    int idx[], idy[];
    int ite, k; //ite: numero de iterações, k: membros por torneio
    ArrayList<Sol> pop;
 
    public GA(int popSize, double muteRatio, int k, int ite){
        this.popSize = popSize;
        this.muteRatio = muteRatio;
        this.popSize = popSize;
        this.k = k;
        popIdx = new int[popSize];
        for(int i = 0; i< popSize; i++)
            popIdx[i] = i;
    }   
 
    @Override
    public void setFac(Fac fac) {
        this.fac = fac;
        this.bestSol = new Sol(fac);
        pop = new ArrayList<>();
        idx = new int[fac.N];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;
        idy = new int[fac.cli];
        for (int i = 0; i < idy.length; i++) {
            idy[i] = i;
        }
    }

    public void popIni() {
        pop.clear();
        for (int i = 0; i < popSize; i++) {
            Utils.shuffler(idx);
            Utils.shuffler(idy);
            Sol sol = new Sol(fac);
            sol.runRandom(idx, idy);
            sol.updateAtributes();
            pop.add(sol);
        }
    }
 
    public Sol[] select(){
        Utils.shuffler(popIdx);
        Sol dad = pop.get(0);
        for(int i = 1; i < k; i++)
            if(pop.get(i).compareTo(dad) < 0)
                dad = pop.get(i);
 
        Utils.shuffler(popIdx);
        Sol mom = null;
        for(int i = 0; i < k; i++)
            if(pop.get(i) != dad && (mom == null || pop.get(i).compareTo(mom) < 0))
                mom = pop.get(i);
 
        return new Sol[]{dad,mom};
    }
 
    public static Sol crossover(Sol dad, Sol mom){
        Fac fac = dad.fac;
 
        int a = fac.cli/3; // 1/3 da quantidade de clientes vai pertencer ao pai para fazer o crossover 
        int b = 2*a;// 2/3 da mãe
        //instancia o filho
        Sol son = new Sol(fac);
        //o filho não pode ter solução pior que o de seus pais (custo deve ser menor)
        int count = Math.max(mom.facCount, dad.facCount);
        int load[] = new int[count];
 
        //a parte central do filho é igual a parte central do pai
        for(int i = 0; i < a; i++){
            if(load[mom.facOf[i]] + fac.cliDem[i] <= fac.c[son.facOf[i]]/* && atCost[son.facOf[i]][i] < atCost[mom.facOf[i]]*/){
                son.facY[son.facOf[i]] = 1;
                son.facOf[i] = mom.facOf[i];
                load[son.facOf[i]] += fac.cliDem[i];
            }else{
                boolean fit = false; //variavel indicando se o load coube na capacidade da facilidade
                for(int j = 0; j < count; j++){
                    if(load[j] + fac.cliDem[i] <= fac.c[son.facOf[j]]){
                        load[j] += fac.cliDem[j];
                        son.facOf[i] = j;
                        fit = true;
                        break;
                    }
                }if(!fit) return null;
            }
        }
        for(int i = b; i < fac.cli; i++){
            if(load[mom.facOf[i]] + fac.cliDem[i] <= fac.c[mom.facOf[i]]){
                son.facOf[i] = mom.facOf[i];
                load[son.facOf[i]] += fac.cliDem[i];
            }else{
                boolean fit = false;
                for(int j = 0; j < count; j++){
                    if(load[j] + fac.cliDem[i] <= fac.c[j]){
                        load[j] += fac.cliDem[i];
                        son.facOf[i] = j;
                        fit = true;
                        break;
                    }
                }if(!fit) return null;
            }
        }
        boolean mudou = false;
        do{
            son.updateAtributes();
            mudou = false;
            for(int i = 0; i < son.facCount; i++){
                if(son.load[i] == 0){
                    //fecha facilidades vazias
                    for(int k = 0; k < fac.cli; k++)
                        if(son.facOf[k] > i){
                            son.facOf[k] = -1;
                            son.facY[son.facOf[k]] = 0;
                        }
                        mudou = true;
                        break;
                }
            }
        }while(mudou);
        return son;
    }
 
    @Override
    public double run(){
        popIni();
        for (int i = 0; i < ite; i++) {

            Collections.sort(pop);
//            System.out.println(pop.get(0));
            while (pop.size() > popSize)
                pop.remove(pop.size() - 1);

            Sol parents[] = select();

            Sol son1 = crossover(parents[0], parents[1]);
            if (son1 != null) {
                if (Utils.rd.nextDouble() < muteRatio)
                    mutate(son1);
                if (!pop.contains(son1))
                    pop.add(son1);
            }


            Sol son2 = crossover(parents[1], parents[0]);
            if (son2 != null) {
                if (Utils.rd.nextDouble() < muteRatio)
                    mutate(son2);
                if (!pop.contains(son2))
                    pop.add(son2);
            }
            System.out.println(bestSol.facCost);
        }
        bestSol.copy(pop.get(0));
        System.out.println(bestSol);
        return bestSol.facCount;
    }

    private void mutate(Sol sol) {
        Utils.shuffler(idx);
        for (int i : idx) {
            int pIdx[] = new int[sol.facCount];
            int fi = sol.facOf[i];
            for (int j = 0; j < pIdx.length; j++)
                pIdx[j] = j;
            Utils.shuffler(pIdx);
            for (int j : pIdx) {
                if (fi != j
                        && sol.load[j] + fac.cliDem[i] <= fac.c[fi]
                        && fac.atCost[fi][i] > fac.atCost[j][i]) {
                    sol.load[j] += fac.cliDem[i];
                    sol.load[fi] -= fac.cliDem[i];
                    sol.facOf[i] = j;
                    if (sol.load[fi] == 0) {
                        System.out.println("mutation OBA!");
                        //fecha fac vazia
                        for (int k = 0; k < fac.cli; k++)
                            if (sol.facOf[k] == -1)
                                sol.facY[sol.facOf[k]] = 0;
                        sol.facCount--;
                    }
                    sol.updateAtributes();
                    return;
                }
            }
        }
    }
 
    @Override
    public Sol getSol() {
        return bestSol;
    }
 
    @Override
    public String toString() {
        return "GA{" +
                "popSize=" + popSize +
                ", muteRatio=" + muteRatio +
                ", k=" + k +
                '}';
    }
 
 
}