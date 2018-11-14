import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class LCS {

	public static ArrayList<String> token_list = new ArrayList<>();
	
	public static HashMap<String, Integer> LCStable= new HashMap<String, Integer>();
	
	public static String delimiter = "[.;(){}+-/*%^=&|!][-+=&|]*";
	
	public static String all_valid_delimiter[] = { ".", ";", "(", ")", "{", "}", "+", "++", "+=", "-", "--", "-=",
			"%", "%=", "/", "/=", "*", "*=", "^", "^=", "==", "&&", "&", "||", "!", " ", "<=", ">="};
	
	public static ArrayList<String> present_delimiter = new ArrayList<>();
	
	//public static ArrayList<String> tableX = new ArrayList<>();

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
		for (i = 0; i < lt.size(); i++)
			LCStable.put(lt.get(i), 1);
		
		return lt;
	}
	
	public static int count(String string, String substring) {
		int count = 0;
		int idx = 0;

		while ((idx = string.indexOf(substring, idx)) != -1) {
			idx++;
			count++;
		}

		return count;
	}

	public static HashMap<String, Integer> checkLCS(String text, ArrayList<String> token, ArrayList<String> delim) {
		HashMap<String, Integer> table = new HashMap<String, Integer>();
		
		int i;
		int numToken = 0;
	
		for ( i = 0; i < token.size(); i++) {
			Iterator it = LCStable.entrySet().iterator();
			while (it.hasNext()) {
		        HashMap.Entry pair = (HashMap.Entry)it.next();		        
		        String tmp = pair.getKey().toString() + token.get(i);
		        int cnt = 0;
		        if (text.contains(tmp)) {
		        	if ((int)pair.getValue() > 1)
		        		cnt = count(text, tmp);
		        }
		        else {
		        	continue;
		        }
		        if (cnt > 1) {
		        	numToken = (int) pair.getValue() + 1;
		        	table.put(tmp, numToken);
		        	double score = (Math.log10(numToken)/Math.log10(2)) * (Math.log10(cnt)/Math.log10(2));
		        	String sb = score + "," + cnt + "," + tmp + "\n";
		        	//System.out.println(pair.getKey());
					pw.write(sb);
		        }
		    }
		}

		for ( i = 0; i < delim.size(); i++) {
			Iterator it = LCStable.entrySet().iterator();
			while (it.hasNext()) {
		        HashMap.Entry pair = (HashMap.Entry)it.next();		        
		        String tmp = pair.getKey().toString() + delim.get(i);
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
		        	double score = (Math.log10(numToken)/Math.log10(2)) * (Math.log10(cnt)/Math.log10(2));
		        	String sb = score + "," + cnt + "," + tmp + "\n";
		        	//System.out.println(pair.getKey());
					pw.write(sb);
		        }
		    }
		}

		return table;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader folder = new BufferedReader(new FileReader("list-100.txt"));
		pw = new PrintWriter(new File("LCS_Report100.csv"));
		
		String text = new String();
		String filename = folder.readLine();
		String tmp[];
		int i, j;
		
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
		
		//System.out.println(present_delimiter);
		//System.out.println(delimiter);
		
		
		/*String text = "System.out.println(\"Enter First Number: \");\r\n if (a==b) { System.out.println(\"Hi\"); }" + 
				"num1 = sc.nextInt();\r\n";
		System.out.println(text);*/
				
		
		//int size = delimiter.length;
		//System.out.println(size);

			tmp = text.split(delimiter);

			for (String a : tmp) {
				isTokenPresent(a, delimiter.substring(1, delimiter.length() - 1));
			}
			
			//System.out.println(token_list.toString());
			//System.out.println("------------------------");
		
		token_list = refineString(token_list);
		//System.out.println(LCStable);
		
		while (!LCStable.isEmpty())
			LCStable = checkLCS(text, token_list, present_delimiter);
		
		pw.close();
		
		System.out.println("Analysis completed!");
	}
}