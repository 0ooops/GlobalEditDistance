/** This class is for calculating the global edit distance between two strings.
 * The weights used for insertion, deletion and replacement can be accessed and
 * altered by function my_attr(insertion, deletion, replacement).
 * The function distance(init, target) takes two parameters, sequences matters.
 * @Author: Jiayu Wang
 * @Date: Mar 20, 2017
 */

import java.io.*;
import net.sf.json.JSONArray;


public class GlobalEditDistance {
    private double insertWeight;
    private double deleteWeight;
    private double replaceWeight;
    private double[][] replaceMatrix;

    public GlobalEditDistance() {
        this.insertWeight = 1;
        this.deleteWeight = 1;
        this.replaceWeight = 1;
    }

    public GlobalEditDistance(double[][] matrix) {
        this.insertWeight = 1;
        this.deleteWeight = 1;
        this.replaceMatrix = matrix;
    }

    public GlobalEditDistance(double insert, double delete) {
        this.insertWeight = insert;
        this.deleteWeight = delete;
        this.replaceWeight = 1;
    }

    public GlobalEditDistance(double insert, double delete, double[][] matrix) {
        this.insertWeight = insert;
        this.deleteWeight = delete;
        this.replaceMatrix = matrix;
    }

    public static double[][] getMatrix() throws IOException {
        File matrix_file = new File(".\\percentage_matrix.json");
        InputStreamReader reader_train = new InputStreamReader(
                new FileInputStream(matrix_file));
        BufferedReader br_matrix = new BufferedReader(reader_train);
        String line_matrix = br_matrix.readLine();

        JSONArray matrix = JSONArray.fromObject(line_matrix);
        double[][] Matrix = new double[27][26];
        int len = matrix.size();
        for (int i = 0;i < len; i++){
            JSONArray temp = JSONArray.fromObject(matrix.get(i));
            int inner_len = temp.size();
            for (int j = 0; j < inner_len; j++) {
                Matrix[i][j] = Double.parseDouble(temp.get(j).toString());
            }
        }
        return Matrix;
    }

    public double distance(String init, String target) throws IOException {
        int l1 = init.length() + 1;
        int l2 = target.length() + 1;
        String init_word[] = init.split("");
        String target_word[] = target.split("");
        double distanceMatrix[][] = new double[l2][l1];

        for (int i = 0; i < l2; i++) {
            distanceMatrix[i][0] = i * this.insertWeight;
        }
        for (int j = 1; j < l1; j++) {
            distanceMatrix[0][j] = j * this.deleteWeight;
        }

        for (int i = 1; i < l2; i++) {
            for (int j = 1; j < l1; j++) {
                double insertion = distanceMatrix[i - 1][j] + this.insertWeight;
                double deletion = distanceMatrix[i][j - 1] + this.deleteWeight;
                double replacement = distanceMatrix[i - 1][j - 1] +
                        (init_word[j - 1].equals(target_word[i - 1]) ? 0 : 1) * this.replaceWeight;
                distanceMatrix[i][j] = Math.min(Math.min(insertion, deletion), replacement);
            }
        }
        return distanceMatrix[l2 - 1][l1 - 1];
    }

    public double distanceReplace(String init, String target) throws IOException {
        int l1 = init.length() + 1;
        int l2 = target.length() + 1;
        String init_word[] = init.split("");
        String target_word[] = target.split("");
        double distanceMatrix[][] = new double[l2][l1];

        for (int i = 0; i < l2; i++) {
            distanceMatrix[i][0] = i * this.insertWeight;
        }
        for (int j = 1; j < l1; j++) {
            distanceMatrix[0][j] = j * this.deleteWeight;
        }

        for (int i = 1; i < l2; i++) {
            for (int j = 1; j < l1; j++) {
                double insertion = distanceMatrix[i - 1][j] + this.insertWeight;
                double deletion = distanceMatrix[i][j - 1] + this.deleteWeight;
                int row = (init_word[j - 1].equals("'") ? 26 : (init_word[j - 1].charAt(0) - 'a'));
                int col = target_word[i - 1].charAt(0) - 'a';
                double replacement = distanceMatrix[i - 1][j - 1] +
                        (init_word[j - 1].equals(target_word[i - 1]) ? 0 : 1) * (1 - replaceMatrix[row][col]);
                distanceMatrix[i][j] = Math.min(Math.min(insertion, deletion), replacement);
            }
        }
        return distanceMatrix[l2 - 1][l1 - 1];
    }

}
