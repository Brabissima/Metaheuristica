
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import static java.util.Arrays.fill;
import java.util.Locale;
import java.util.Scanner;

public class Fac {
    int cli; //quantidade de clientes
    int N; //quantidade de facilidades
    int c[]; //capacidade de cada facilidade
    double opCost[]; //custo de abertura da facilidade x
    double atCost[][]; //custo de atendimento da facilidade x para cliente y
    int cliDem[]; //demanda do cliente
    double sumDem[];
    
    public String toString(){
        return "Fac{ "+
                "Clientes = "+cli+
                ", Facilidades = "+N+
                ", Capacidade = "+c+
                "}";
    }
    

    public Fac(String file) throws FileNotFoundException{
        Scanner sc = new Scanner(new FileInputStream(file));
        sc.useLocale(Locale.US);
        N = sc.nextInt();
        cli = sc.nextInt();
        c = new int[N];
        opCost = new double[N];
        atCost = new double[N][cli];
        cliDem = new int[cli];
        sumDem = new double[N];
        for(int i=0; i<N;i++){
            c[i] = sc.nextInt();
            opCost[i] = sc.nextDouble();
        }
        for (int j = 0; j < cli; j++) {
            cliDem[j] = sc.nextInt();

            for (int i = 0; i < N; i++)
                atCost[i][j] = sc.nextDouble();
        }
        for (int i = 0; i < cli; i++) {
            if (cliDem[i] > c[0]){
                cliDem[i] = 0;
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < cli; j++) {
                if (cliDem[j] == 0) {
                    atCost[i][j] = 0;
                }
            }
        }

    }

    /**
     * Lower bound para a quantidade de facilidades
     */
    public int LB() {
        int sumCap = 0;
        int sumDem = 0;
        for (int i = 0; i < N; i++)
            sumCap += c[i];
        for (int j = 0; j < cli; j++)
            sumDem += cliDem[j];
        double mediaCap = sumCap/N;
        return (int)Math.ceil(sumDem/mediaCap);
    }
    
}
