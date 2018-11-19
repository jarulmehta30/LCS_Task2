# LCS_Task2

/* STATEMENT OF PURPOSE */
This java program is developed with the intension of creating an application that would list all the shared SEQUENCES of tokens from n number of JAVA code files. The number of files can be 2, 10 or 100 files.

The output of this appliction is a CSV report file, LCS_Report.csv, that lists all the possible sequences of tokens.

The concept of this task is similar to that of determining the Largest Common Substring. The twist here is to list all such possible combination of tokens that would satisfy the criteria of having a positive "score", where score is calculated as follow,
score = log2(#tokens)*log2(#count)

The CSV lists the analyzed information in the following format:
score,tokens,count,"sourcecode"

/* LIMITATIONS */
It is observed that if 2 identifiers, c and i are present in the code, string "ic static void main(String[] args)" is reported as a common substring, where "ic" belongs to "public" originally.

/* BUILDING AND RUNNING THE APPLICATION */
$ javac LCS.java
$ java LCS <list-2/list-10/list-100>

/* OUTPUT CSV */
LCS_Report.csv is generated as an outcome of this application.
Please note that the formatting of LCS_Report.csv has been found to vary, depending on the eclipse version.
Some pre-generated output files, LCS_Report2.csv, LCS_Report10.csv and LCS_Report100.csv is availabe along with the source code.
