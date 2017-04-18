/** This class is for calculating the Soundex code for an input String.
 * The soundex method uses traditional English table, while the two
 * adjusted methods uses adjusted tables.
 * The soundex method is in reference to the code on this website:
 * http://introcs.cs.princeton.edu/java/31datatype/Soundex.java.html
 * @Author: Jiayu Wang
 * @Date: Mar 24, 2017
 */

public class Soundex {
    public static String soundex(String s) {
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                case 'F':
                case 'P':
                case 'V':
                    x[i] = '1';
                    break;

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z':
                    x[i] = '2';
                    break;

                case 'D':
                case 'T':
                    x[i] = '3';
                    break;

                case 'L':
                    x[i] = '4';
                    break;

                case 'M':
                case 'N':
                    x[i] = '5';
                    break;

                case 'R':
                    x[i] = '6';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }

    public static String adjustedSoundex1(String s) {
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                    x[i] = '1';
                    break;

                case 'C':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                    x[i] = '2';
                    break;

                case 'D':
                    x[i] = '3';
                    break;

                case 'F':
                case 'P':
                    x[i] = '4';
                    break;

                case 'G':
                case 'J':
                    x[i] = '5';
                    break;

                case 'L':
                    x[i] = '6';
                    break;

                case 'M':
                    x[i] = '7';
                    break;

                case 'N':
                    x[i] = '8';
                    break;

                case 'R':
                    x[i] = '9';
                    break;

                case 'T':
                    x[i] = '!';
                    break;

                case 'V':
                case 'W':
                    x[i] = '@';
                    break;

                case 'Y':
                case 'Z':
                    x[i] = '#';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }

    public static String adjustedSoundex2(String s) {
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                    x[i] = '1';
                    break;

                case 'C':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                    x[i] = '2';
                    break;

                case 'D':
                    x[i] = '3';
                    break;

                case 'F':
                case 'P':
                    x[i] = '4';
                    break;

                case 'G':
                case 'J':
                case 'Z':
                case 'T':
                    x[i] = '5';
                    break;

                case 'L':
                    x[i] = '6';
                    break;

                case 'M':
                    x[i] = '7';
                    break;

                case 'N':
                    x[i] = '8';
                    break;

                case 'R':
                    x[i] = '9';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }
}
