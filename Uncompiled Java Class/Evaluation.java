/** This class is for evaluating and recording the performance of back-transliteration
 * by using global edit distance and soundex with different parameters.
 * The output contains two files: one is a result file, the other is a log file.
 * You may comment out any method you don't want to evaluate from the main function.
 * @Author: Jiayu Wang
 * @Date: Mar 20, 2017
 */
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Evaluation {
    public static void main(String args[]) throws IOException {
        String choice = args[0];
        switch(choice){
            case "1":
                //Evaluate Global Edit Distance with Levenshtein Parameters
                evaluateInitDistance();
                break;
            case "2":
                //Evaluate Global Edit Distance with Replacement Matrix
                evaluateReplaceDistance();
                break;
            case "3":
                //Evaluate with Changing Additions and Deletions
                for (double i = 0.1; i < 1; i += 0.1) {
                    evaluateAdditionDistance(i);
                }
                break;
            case "4":
                //Evaluate with Changing Additions and Deletions and Replacement Matrix
                for (double i = 0.1; i < 1; i += 0.1) {
                    evaluateReAndAdditionDistance(i);
                }
                break;
            case "5":
                //Evaluate Soundex with English Table
                evaluateSoundex();
                break;
            case "6":
                //Evaluate Soundex with English Table
                evaluateAdjustedSoundex1();
                break;
            case "7":
                //Evaluate Soundex with Further Adjusted Table
                evaluateAdjustedSoundex2();
                break;
            default:
                evaluateInitDistance();
                evaluateReplaceDistance();
                for (double i = 0.1; i < 1; i += 0.1) {
                    evaluateAdditionDistance(i);
                    evaluateReAndAdditionDistance(i);
                }
                evaluateSoundex();
                evaluateAdjustedSoundex1();
                evaluateAdjustedSoundex2();
                break;
        }
        System.out.println("Done");
    }

    public static void evaluateInitDistance() {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        GlobalEditDistance changeReplace = new GlobalEditDistance();

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            File result_file = new File(".\\result_init_levenshtein.txt");
            File log_file = new File(".\\log_init_levenshtein.txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0].toLowerCase();
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                double min_dis = 200;
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    double distance = changeReplace.distance(init_word, dic_word);

                    if (distance == min_dis) {
                        current_best.add(dic_word);
                    } else if (distance < min_dis) {
                        current_best.clear();
                        min_dis = distance;
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + min_dis);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateReplaceDistance() throws IOException {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        GlobalEditDistance changeReplace = new GlobalEditDistance(GlobalEditDistance.getMatrix());

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            File result_file = new File(".\\result_replace.txt");
            File log_file = new File(".\\log_replace.txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0].toLowerCase();
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                double min_dis = 200;
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    double distance = changeReplace.distanceReplace(init_word, dic_word);

                    if (distance == min_dis) {
                        current_best.add(dic_word);
                    } else if (distance < min_dis) {
                        current_best.clear();
                        min_dis = distance;
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + min_dis);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateAdditionDistance(double index) {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        GlobalEditDistance changeReplace = new GlobalEditDistance(1 - index, 1 + index);

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            String indexS = nf.format(index);
            File result_file = new File(".\\result_insert_delete_" + indexS + ".txt");
            File log_file = new File(".\\log_insert_delete_" + indexS + ".txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0].toLowerCase();
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                double min_dis = 200;
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    double distance = changeReplace.distance(init_word, dic_word);

                    if (distance == min_dis) {
                        current_best.add(dic_word);
                    } else if (distance < min_dis) {
                        current_best.clear();
                        min_dis = distance;
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + min_dis);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateReAndAdditionDistance(double index) throws IOException {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        GlobalEditDistance changeReplace = new GlobalEditDistance(1 - index, 1 + index, GlobalEditDistance.getMatrix());

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            String indexS = nf.format(index);
            File result_file = new File(".\\result_insert_delete_replace_" + indexS + ".txt");
            File log_file = new File(".\\log_insert_delete_replace_" + indexS + ".txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0].toLowerCase();
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                double min_dis = 200;
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    double distance = changeReplace.distanceReplace(init_word, dic_word);

                    if (distance == min_dis) {
                        current_best.add(dic_word);
                    } else if (distance < min_dis) {
                        current_best.clear();
                        min_dis = distance;
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + min_dis);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateSoundex() {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        int noResultCount = 0;

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            File result_file = new File(".\\result_soundex.txt");
            File log_file = new File(".\\log_soundex.txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0];
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                String init_code_record = "";
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    String init_code = Soundex.soundex(init_word);
                    String dic_code = Soundex.soundex(dic_word);
                    init_code_record = init_code;

                    if (init_code.equals(dic_code)) {
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }
                if(current_best.size() == 0) {
                    noResultCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + init_code_record);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount + ". The train word without prediction is " + noResultCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount + ". The train word without prediction is " + noResultCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateAdjustedSoundex1() {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        int noResultCount = 0;

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            File result_file = new File(".\\result_adjusted_soundex1.txt");
            File log_file = new File(".\\log_adjusted_soundex1.txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0];
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                String init_code_record = "";
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    String init_code = Soundex.adjustedSoundex1(init_word);
                    String dic_code = Soundex.adjustedSoundex1(dic_word);
                    init_code_record = init_code;

                    if (init_code.equals(dic_code)) {
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }
                if(current_best.size() == 0) {
                    noResultCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + init_code_record);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount + ". The train word without prediction is " + noResultCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount + ". The train word without prediction is " + noResultCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateAdjustedSoundex2() {
        int correctCount = 0;
        int predictCount = 0;
        int trainCount = 0;
        int noResultCount = 0;

        try {
            File train_file = new File(".\\train.txt");
            File dic_file = new File(".\\names.txt");
            File result_file = new File(".\\result_adjusted_soundex2.txt");
            File log_file = new File(".\\log_adjusted_soundex2.txt");

            result_file.createNewFile();
            log_file.createNewFile();

            BufferedWriter out_result = new BufferedWriter(new FileWriter(result_file));
            BufferedWriter out_log = new BufferedWriter(new FileWriter(log_file));
            InputStreamReader reader_train = new InputStreamReader(new FileInputStream(train_file));
            BufferedReader br_train = new BufferedReader(reader_train);
            String line_train;
            line_train = br_train.readLine();

            while (line_train != null) {
                trainCount++;
                String init_word = line_train.split("\\t")[0];
                String target_word = line_train.split("\\t")[1].split("\\n")[0];

                InputStreamReader reader_dic = new InputStreamReader(new FileInputStream(dic_file));
                BufferedReader br_dic = new BufferedReader(reader_dic);
                String line_dic;
                line_dic = br_dic.readLine();
                String init_code_record = "";
                ArrayList<String> current_best = new ArrayList();

                while (line_dic != null) {
                    String dic_word = line_dic.split("\\n")[0];
                    String init_code = Soundex.adjustedSoundex2(init_word);
                    String dic_code = Soundex.adjustedSoundex2(dic_word);
                    init_code_record = init_code;

                    if (init_code.equals(dic_code)) {
                        current_best.add(dic_word);
                    }

                    line_dic = br_dic.readLine();
                }

                predictCount += current_best.size();

                if(current_best.contains(target_word)) {
                    correctCount++;
                }
                if(current_best.size() == 0) {
                    noResultCount++;
                }

                out_log.write(init_word + "\t" + target_word + "\t" + init_code_record);
                out_log.newLine();
                out_log.write(current_best + "");
                out_log.newLine();

                line_train = br_train.readLine();
            }

            System.out.println("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount + ". The train word without prediction is " + noResultCount);
            out_result.newLine();
            out_result.write("The trainCount is " + trainCount + ". The total predictCount is " + predictCount +
                    ". The correctCount is " + correctCount + ". The train word without prediction is " + noResultCount);

            out_result.flush();
            out_result.close();
            out_log.flush();
            out_log.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}