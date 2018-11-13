import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LCS {

	public static ArrayList<String> token_list = new ArrayList<>();
	
	//public static String delimiter[] = {"[.;(){}+-/*%^=&|!][-+=&|]*"};
	
	public static String all_valid_delimiter[] = { ".", ";", "(", ")", "{", "}", "+", "++", "+=", "-", "--", "-=",
			"%", "%=", "/", "/=", "*", "*=", "^", "^=", "==", "&&", "&", "||", "!", " "};
	
	public static ArrayList<String> present_delimiter = new ArrayList<>();

	public static PrintWriter pw;
	
	public static void isTokenPresent(String str, String del) {
		if (token_list.isEmpty())
			token_list.add(str);
		else if (token_list.equals(str + del)) {
			int index = token_list.indexOf(str + del);
			token_list.set(index, str);
		}
		else if (token_list.equals(str))
			return;
		else {
			token_list.add(str);
		}
	}
	
	public static ArrayList<String> refineString(ArrayList<String> lt) {
		int i, j;
		
		for (i = 0; i < lt.size(); i++) {
			if (lt.get(i).startsWith("\""))
				continue;
			if (lt.get(i).contains(" ")) {
				String tmp[] = lt.get(i).split("[ ]");
				for (j = 0; j < tmp.length; j++) {
					lt.add(tmp[j]);
				//	System.out.println(tmp[j]);
				}
				lt.remove(i);
				i--;
			}
		}
		
		for (i = 0; i < lt.size(); i++) {
			if (lt.get(i).startsWith("\""))
				continue;
			lt.set(i, lt.get(i).replaceAll(" ", ""));
			lt.set(i, lt.get(i).replaceAll("\r", ""));
			lt.set(i, lt.get(i).replaceAll("\n", ""));
			lt.set(i, lt.get(i).replaceAll("\t", ""));
		}
		
		//System.out.println(lt);
		
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
		
		return lt;
	}
	
	public static ArrayList<String> checkLCS(String text, ArrayList<String> token, ArrayList<String> delim) {
		ArrayList<String> table = new ArrayList<>();
		int count = 2;
		int i,j;
		
		for (i = 0; i < token.size(); i++) {
			for (j = 0; j < delim.size(); j++) {
				String tmp = token.get(i) + delim.get(j);
				//System.out.println(tmp);
				if (text.contains(tmp))
					//count = text.split(tmp, -1).length-1;
				
				if (count > 1) {
					table.add(tmp);
					double score = Math.log10(count)/Math.log10(2);

					String sb = score + "," + count + "," + tmp;
					pw.write(sb);
				}
			}
		}
		
		//pw.write(sb.toString());
		return table;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader folder = new BufferedReader(new FileReader("list-2.txt"));
		pw = new PrintWriter(new File("LCS_Report.csv"));
		
		String text = new String();
		String filename = folder.readLine();
		String tmp[];
		int i, j;
		String delimiter[] = {"[.;(){}+-/*%^=&|!][-+=&|]*"};
		
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
		
		for (i = 0; i < all_valid_delimiter.length; i++) {
			if (text.contains(all_valid_delimiter[i]))
				present_delimiter.add(all_valid_delimiter[i]);
		}
		
		System.out.println(present_delimiter);
		
		/*String text = "System.out.println(\"Enter First Number: \");\r\n if (a==b) { System.out.println(\"Hi\"); }" + 
				"num1 = sc.nextInt();\r\n";
		System.out.println(text);*/
				
		
		//int size = delimiter.length;
		//System.out.println(size);

		for (i = 0; i < delimiter.length; i++) {
			if (i == 0) {
				tmp = text.split(delimiter[i]);
			}
			else {
				tmp = token_list.toString().split(delimiter[i]);
			}
			
			for (String a : tmp) {
				isTokenPresent(a, delimiter[i].substring(1, delimiter[i].length() - 1));
			}
			
			//System.out.println(token_list.toString());
			//System.out.println("------------------------");
		}
		
		token_list = refineString(token_list);
		//System.out.println(token_list);
		
		System.out.println(checkLCS(text, token_list, present_delimiter));
		
		pw.close();
	}
}