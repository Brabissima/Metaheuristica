import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class Main {
    
//   public static double benchmark(String dir, Solver solver) throws FileNotFoundException {
//        File file = new File(dir);
//        File arq[] = file.listFiles((d, name) -> name.endsWith(".txt"));
//        Arrays.sort(arq);
//        int count = 0;
//        double gap = 0;
//        long time = 0;
////        System.out.println(solver);
//        for (File f : arq) {
//            count++;
//            Fac fac = new Fac(f.getPath());
//            Sol sol;
//            double lb = fac.LB();
//
//            Utils.rd.setSeed(7);
//
//            solver.setFac(fac);
//            long t = System.currentTimeMillis();
//            int x = solver.run();
//            t = System.currentTimeMillis() - t;
//            time += t;
//            gap += x / lb - 1;
//            System.out.println(count + " - " + x + "  " + f + ": " + (x / lb - 1) + " T: " + t);
//        }
//        System.out.printf("%s\t %.2f\t %d\n", solver, 100 * gap / count, time / count);
//
//        return 100 * gap / count;
//    }

    public static void main(String args[]) throws FileNotFoundException {
//        String dir = "instances/cap42";
//        String dir = "instancias/cap41";
//        String dir = "instances/cap43";
//        benchmark(dir, new RMS(10));
//        String dir = "instances";
//        benchmark(dir, new VNS(100,10,5));
//        benchmark(dir, new ILS(10, 100));
//        benchmark(dir, new GRASP(50, 10));
//        benchmark(dir, new GLS(100));
//        benchmark(dir, new ILS(1000, 2));


        Fac fac = new Fac("instancias/cap41.txt");
        Sol sol = new Sol(fac);
        sol.run();
        sol.updateAtributes();
        System.out.println(sol);
//
//        HC hill = new HC(fac, sol);
//        hill.run();
//        VND vnd = new VND(fac,sol);
//        vnd.run();
        

//        ILS ils = new ILS(100000, 5);
//        ils.setFac(fac);
//        ils.run();

//        RMS rms = new RMS(10000);
//        rms.setFac(fac);
//        rms.run();

//        GRASP graps = new GRASP(5,10000);
//        graps.setFac(fac);
//        graps.run();

//        SA sa = new SA(1000,.001,.9999);
//        sa.setFac(fac);
//        sa.run();

//        GA ga = new GA(100,0.1,10,100);
//        ga.setFac(fac);
//        ga.run();

//        VNS vns = new VNS(1000,10,1);
//        vns.setFac(fac);
//        vns.run();

    }
}
