import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

public class AlunoMain {
    public static List<Aluno> lerAlunos(String arquivo) {
        List<Aluno> listaDeAlunos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                if (campos.length == 3) {
                    try {
                        int matricula = Integer.parseInt(campos[0]);
                        String nome = campos[1];
                        double nota = Double.parseDouble(campos[2].replace(',', '.'));
                        listaDeAlunos.add(new Aluno(matricula, nome, nota));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter número: " + linha);
                    }
                } else {
                    System.err.println("Linha com número incorreto de campos: " + linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaDeAlunos;
    }

    public static void processarDados(List<Aluno> listaDeAlunos, String arquivoResumo) {
        int totalAlunos = listaDeAlunos.size();
        int aprovados = 0;
        int reprovados = 0;
        double menorNota = Double.MAX_VALUE;
        double maiorNota = Double.MIN_VALUE;
        double somaNotas = 0;

        for (Aluno aluno : listaDeAlunos) {
            double nota = aluno.getNota();
            if (nota >= 6) {
                aprovados++;
            } else {
                reprovados++;
            }
            if (nota < menorNota) {
                menorNota = nota;
            }
            if (nota > maiorNota) {
                maiorNota = nota;
            }
            somaNotas += nota;
        }

        double mediaGeral = somaNotas / totalAlunos;

        try (FileWriter writer = new FileWriter(arquivoResumo)) {
            writer.append("Total de Alunos,").append(String.valueOf(totalAlunos)).append('\n');
            writer.append("Aprovados,").append(String.valueOf(aprovados)).append('\n');
            writer.append("Reprovados,").append(String.valueOf(reprovados)).append('\n');
            writer.append("Menor Nota,").append(String.valueOf(menorNota)).append('\n');
            writer.append("Maior Nota,").append(String.valueOf(maiorNota)).append('\n');
            writer.append("Média Geral,").append(String.valueOf(mediaGeral)).append('\n');
            System.out.println("Dados exportados para " + arquivoResumo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String arquivoAlunos = "alunos.csv";
        String arquivoResumo = "resumo.csv";

        List<Aluno> listaDeAlunos = lerAlunos(arquivoAlunos);
        System.out.println("Total de alunos lidos: " + listaDeAlunos.size());
        processarDados(listaDeAlunos, arquivoResumo);
    }
}
