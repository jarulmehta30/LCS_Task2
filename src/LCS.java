import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LCS {

    public static ArrayList<String> token_list = new ArrayList<>();

    public static HashMap<String, Integer> LCStable = new
    HashMap<String, Integer>();

    public static String delimiter = "[.;(){}+-/*%^=&|!][-+=&|]*";

    public static String all_valid_delimiter[] = { ".", ";", "(", ")", "{", "}", "+", "++", "+=", "-", "--", "-=", "%",
                                                   "%=", "/", "/=", "*", "*=", "^", "^=", "==", "&&", "&", "||", "!", " ", "<=", ">="
                                                 };

    public static ArrayList<String> present_delimiter = new ArrayList<>();

    public static PrintWriter pw;

    /*
     * This function is used to if the given delimiter is present in
     * the given string or not and form a list of all tokens
     *
     * String str [IN] string in which the delimiter is to be searched
     * String del [IN] delimiter to be checked for in the sring
     */
    public static void isTokenPresent(String str, String del)
    {
        if (token_list.isEmpty()) {
            token_list.add(str);
        }
        else if (token_list.equals(str + del)) {
            int index = token_list.indexOf(str + del);
            token_list.set(index, str);
        }
        else if (token_list.equals(str)) {
            return;
        }
        else {
            token_list.add(str);
        }
    }

    /*
     * This function removes spaces and empty lines from the tokens generated.
     *
     * ArrayList<String> lt [IN] arraylist of all the tokens
     *
     * ArrayList<String> [OUT] clean and refined arraylist
     */
    public static ArrayList<String> refineString(ArrayList<String> lt)
    {
        int i, j;

        for (i = 0; i < lt.size(); i++) {
            if (lt.get(i).startsWith("\"")) {
                continue;
            }
            if (lt.get(i).contains(" ")) {
                String tmp[] = lt.get(i).split("[ ]");
                for (j = 0; j < tmp.length; j++) {
                    lt.add(tmp[j]);
                }
                lt.remove(i);
                i--;
            }
        }

        for (i = 0; i < lt.size(); i++) {
            if (lt.get(i).startsWith("\"")) {
                continue;
            }
            lt.set(i, lt.get(i).replaceAll(" ", ""));
            lt.set(i, lt.get(i).replaceAll("\r", ""));
            lt.set(i, lt.get(i).replaceAll("\n", ""));
            lt.set(i, lt.get(i).replaceAll("\t", ""));
        }

        for (i = 0; i < lt.size(); i++) {
            if (lt.get(i).isEmpty()) {
                lt.remove(i);
                i--;
            }
        }

        for (i = 0; i < lt.size(); i++) {
            for (j = 0; j < i; j++) {
                if (lt.get(i).equals(lt.get(j))) {
                    lt.remove(i);
                    i--;
                }
            }
        }
        for (i = 0; i < lt.size(); i++) {
            LCStable.put(lt.get(i), 1);
        }

        return lt;
    }

    /*
     * This function returns the count of how many times a particular substring is repeated in the string
     *
     *  String string [IN] string in which the substring's total occurences are to be calculated
     *  String substring [IN] substring to be counted
     *
     *  int [OUT] count of total number of occurences of substring in string
     */
    public static int count(String string, String substring)
    {
        int count = 0;
        int idx = 0;

        while ((idx = string.indexOf(substring, idx)) != -1) {
            idx++;
            count++;
        }

        return count;
    }

    /*
     * This function checks for all valid common substrings and generates the output file.
     *
     * ArrayList<String> token [IN]
     */
    public static HashMap<String, Integer> checkLCS(String text)
    {
        HashMap<String, Integer> table = new HashMap<String, Integer>();

        int i;
        int numToken = 0;

        for (i = 0; i < token_list.size(); i++) {
            Iterator it = LCStable.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                String tmp = pair.getKey().toString() + token_list.get(i);
                int cnt = 0;
                if (text.contains(tmp)) {
                    if ((int) pair.getValue() > 1) {
                        cnt = count(text, tmp);
                    }
                }
                else {
                    continue;
                }
                if (cnt > 1) {
                    numToken = (int) pair.getValue() + 1;
                    table.put(tmp, numToken);
                    double score = (Math.log10(numToken) / Math.log10(2)) * (Math.log10(
                                       cnt) / Math.log10(2));
                    String sb = score + "," + cnt + "," + tmp + "\n";
                    pw.write(sb);
                    pw.flush();
                }
            }
        }

        for (i = 0; i < present_delimiter.size(); i++) {
            Iterator it = LCStable.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                String tmp = pair.getKey().toString() + present_delimiter.get(i);
                int cnt = 0;
                if (text.contains(tmp)) {
                    cnt = count(text, tmp);
                }
                else {
                    continue;
                }
                if (cnt > 1) {
                    numToken = (int) pair.getValue() + 1;
                    table.put(tmp, numToken);
                    double score = (Math.log10(numToken) / Math.log10(2)) * (Math.log10(
                                       cnt) / Math.log10(2));
                    String sb = score + "," + cnt + "," + tmp + "\n";
                    pw.write(sb);
                    pw.flush();
                }
            }
        }

        return table;
    }

    public static void main(String[] args) throws IOException
    {
        BufferedReader folder = new BufferedReader(new FileReader(args[0]));

        /* Output file */
        pw = new PrintWriter(new File("LCS_Report.csv"));

        String text = new String();
        String filename = folder.readLine();
        String tmp[];
        int i, j;

        /* reead the contents of all the files in a string */
        while (filename != null) {
            BufferedReader fd = new BufferedReader(new FileReader(filename));

            String temp = fd.readLine();
            while (temp != null) {
                text += "\n" + temp;
                temp = fd.readLine();
            }

            fd.close();
            filename = folder.readLine();
        }
        folder.close();

        /* determine which delimiters are present throughout all files */
        for (i = 0; i < all_valid_delimiter.length; i++) {
            if (text.contains(all_valid_delimiter[i])) {
                present_delimiter.add(all_valid_delimiter[i]);
            }
        }

        tmp = text.split(delimiter);

        for (String a : tmp) {
            isTokenPresent(a, delimiter.substring(1, delimiter.length() - 1));
        }

        // System.out.println(token_list.toString());

        token_list = refineString(token_list);
        // System.out.println(LCStable);

        /* Check all common substrings in the text */
        while (!LCStable.isEmpty()) {
            LCStable = checkLCS(text);
        }

        pw.close();

        System.out.println("Analysis completed!");
    }
}