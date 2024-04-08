import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        String linhas[] = new String[100000];
        int numLinhas = 0;

        Path filePath = Paths.get("./transacoes_00020.csv");

        // Ler o arquivo
        try (BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset())) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                linhas[numLinhas] = line;
                numLinhas++;
            }
        } catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: ", e);
        }

        // Matriz para armazenar o valor total de vendas por loja x mês
        int[][] vendasPorLojaMes = new int[4][12]; // 4 lojas, 12 meses

        for (int i = 0; i < numLinhas; i++) {
            String[] campos = linhas[i].split(",");

            // Determinar o mês da transação
            int mes = getMonthIndex(campos[1]);

            // Determinar a loja da transação
            String loja = campos[2];

            // Determinar o valor da transação
            int valor = Integer.parseInt(campos[3]);

            // Encontrar o índice correspondente à loja
            int lojaIndex = getStoreIndex(loja);

            // Atualizar o valor total de vendas para a loja e mês correspondentes
            vendasPorLojaMes[lojaIndex][mes] += valor;
        }

        // Encontrar os três meses com maior valor de vendas
        int[] topMonths = findTopMonths(vendasPorLojaMes);

        // Encontrar o mês com menor venda
        int lowestMonth = findLowestMonth(vendasPorLojaMes);

        // Calcular o total geral de vendas
        int totalSales = calculateTotalSales(vendasPorLojaMes);

        // Escrever os resultados no arquivo resultados.txt
        Path outputPath = Paths.get("./resultados.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, Charset.defaultCharset())) {
            // Escrever a matriz de vendas
            writer.write("Matriz de vendas\n");
            writer.write("Jan Fev Mar Abr Mai Jun Jul Ago Set Out Nov Dez\n");
            for (int i = 0; i < 4; i++) {
                writer.write(getStoreIndex(Integer.toString(i)) + " ");
                for (int j = 0; j < 12; j++) {
                    writer.write(vendasPorLojaMes[i][j] + " ");
                }
                writer.write("\n");
            }

            // Escrever os três meses com maior valor de vendas
            writer.write("Três meses com mais vendas\n");
            for (int i = 0; i < 3; i++) {
                writer.write(getMonthIndex(Integer.toString(topMonths[i])) + " " + vendasPorLojaMes[getStoreIndex("Matriz")][topMonths[i]]
                        + "\n");
            }

            // Escrever o mês com menor venda
            writer.write("Mês com menor venda\n");
            writer.write(
                    getMonthIndex(Integer.toString(lowestMonth)) + " " + vendasPorLojaMes[getStoreIndex("Matriz")][lowestMonth] + "\n");

            // Escrever o total geral de vendas
            writer.write("Total geral de vendas\n");
            writer.write(totalSales + "\n");
        } catch (IOException e) {
            System.err.format("Erro na escrita do arquivo: ", e);
        }
    }

    private static int getStoreIndex(String loja) {
        switch (loja) {
            case "Matriz":
                return 0;
            case "Filial Sul":
                return 1;
            case "Filial Norte":
                return 2;
            case "Filial Nordeste":
                return 3;
            default:
                return -1;
        }
    }

    private static int getMonthIndex(String mes) {
        switch (mes) {
            case "Janeiro":
                return 0;
            case "Fevereiro":
                return 1;
            case "Marco":
                return 2;
            case "Abril":
                return 3;
            case "Maio":
                return 4;
            case "Junho":
                return 5;
            case "Julho":
                return 6;
            case "Agosto":
                return 7;
            case "Setembro":
                return 8;
            case "Outubro":
                return 9;
            case "Novembro":
                return 10;
            case "Dezembro":
                return 11;
            default:
               return -1;
        }
    }





    private static int[] findTopMonths(int[][] vendasPorLojaMes) {
        // Implementar a lógica para encontrar os três meses com maior valor de vendas
        // Retornar um array com os índices dos meses
        int[] topMonths = new int[3];
        int[] sortedMonths = new int[12];
        for (int i = 0; i < 12; i++) {
            sortedMonths[i] = i;
        }
        for (int i = 0; i < 12; i++) {
            for (int j = i + 1; j < 12; j++) {
                if (vendasPorLojaMes[getStoreIndex("Matriz")][sortedMonths[j]] > vendasPorLojaMes[getStoreIndex(
                        "Matriz")][sortedMonths[i]]) {
                    int temp = sortedMonths[i];
                    sortedMonths[i] = sortedMonths[j];
                    sortedMonths[j] = temp;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            topMonths[i] = sortedMonths[i];
        }
        return topMonths;
    }

    private static int findLowestMonth(int[][] vendasPorLojaMes) {
        // Implementar a lógica para encontrar o mês com menor venda
        // Retornar o índice do mês
        int lowestMonth = 0;
        for (int i = 1; i < 12; i++) {
            if (vendasPorLojaMes[getStoreIndex("Matriz")][i] < vendasPorLojaMes[getStoreIndex("Matriz")][lowestMonth]) {
                lowestMonth = i;
            }
        }
        return lowestMonth;
    }

    private static int calculateTotalSales(int[][] vendasPorLojaMes) {
        // Implementar a lógica para calcular o total geral de vendas
        // Retornar o valor total
        int totalSales = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                totalSales += vendasPorLojaMes[i][j];
            }
        }
        return totalSales;
    }
}
