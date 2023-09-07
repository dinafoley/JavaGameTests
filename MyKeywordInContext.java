package kz.edu.nu.cs.se.hw;

//import java.awt.Desktop;
import java.io.*;
//import kz.edu.nu.cs.se.hw.Line;
import java.util.*;

public class MyKeywordInContext implements KeywordInContext {

	private List<Line> lines;
	private Set<String> keywordsToIgnore;
	private String Name;
	private List<Line> index;

	public MyKeywordInContext(String name, String pathstring) throws FileNotFoundException, IOException {

		lines = new ArrayList<>();
		keywordsToIgnore = new HashSet<>();
		index = new ArrayList<>();
		Name = name;
		readWordsToIgnore("stopwords.txt");

		int i = 1;
		String input;
		try {
			FileReader fr = new FileReader(pathstring);
			try (BufferedReader br = new BufferedReader(fr)) {
				while ((input = br.readLine()) != null) {
					lines.add(new Line(input, i));
					i++;
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException while reading file." + ioe.getStackTrace());
			throw ioe;
		}

	}

	private void readWordsToIgnore(String file) throws IOException {
		try {
			FileReader fr = new FileReader(file);
			try (BufferedReader br = new BufferedReader(fr)) {
				String input;
				while ((input = br.readLine()) != null) {
					keywordsToIgnore.add(input.toLowerCase());
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException while reading file." + ioe.getStackTrace());
			throw ioe;
		}

	}

	@Override
	public int find(String word) {
		int i = 0;
		int size = lines.size();
		String w = word.toLowerCase();

		if (keywordsToIgnore.contains(word)) {
			return -1;
		}
		
		while (i < size) {
			int numberOfWords = lines.get(i).getNumberOfWords();
			for (int wordIndex = 0; wordIndex < numberOfWords; wordIndex++) {
				String curWord = lines.get(i).getEntry(wordIndex);
				String a = curWord.toLowerCase();

				a = a.replace(".", "");
				a = a.replace("!", "");
				a = a.replace("?", "");
				a = a.replace(";", "");

				if (w.equals(a)) {
					return i;
				}
			}
			i++;
		}
		return -1;

	}

	@Override
	public Indexable get(int i) {
		return lines.get(i);
	}

	@Override
	public void txt2html() throws Exception {

		// TODO Auto-generated method stub
		String h = ".html";
		String newName = Name + h;
		File f = new File(newName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write("<!DOCTYPE html>\n" + "<html><head><meta charset=\"UTF-8\"></head><body>\n" + "<div>" + "\n");

		int i = 0;
		int j = 1;
		int size = lines.size();
		char c = '"';

		while (i < size) {
			int numWords = get(i).getNumberOfWords();
			for (int it = 0; it < numWords; it++) {
				String curWord = lines.get(i).getEntry(it);
				bw.write(curWord + " ");
			}
			bw.write("<span id=" + c + "line_" + j + c + ">&nbsp&nbsp[" + j + "]</span><br>" + "\n");
			i++;
			j++;
		}
		bw.write("</div></body></html>");
		bw.close();
	}

	class SortbyInd implements Comparator<Line> {
		public int compare(Line a, Line b) {
			return (a.getEntry(0).toLowerCase()).compareTo(b.getEntry(0).toLowerCase());
		}
	}

	@Override
	public void indexLines() {
		int i = 0;
		int j = 0;
		int size = lines.size();
		while (i < size) {
			int numberOfWords = lines.get(i).getNumberOfWords();
			for (j = 0; j < numberOfWords; j++) {
				String ind = lines.get(i).getEntry(j);
				if (ind.length() > 3) {
					index.add(new Line(ind, i));
				}
			}
			i++;
		}
		Collections.sort(index, new SortbyInd());
	}

	@Override
	public void writeIndexToFile() throws IOException {
		String newName = "Kwic-"+Name +".html";
		File f = new File(newName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		
		bw.write("<!DOCTYPE html>\n"
				+ "<html><head><meta charset=\"UTF-8\"></head><body><div style=\"text-align:center;line-height:1.6\">"
				+ "\n");
		
		int i = 0;
		char c = '"';

		int size = index.size();

		while (i < size) {
			int b = index.get(i).getLineNumber();
			String indexWord = index.get(i).getEntry(0);
			int numWords = get(b).getNumberOfWords();

			for (int it = 0; it < numWords; it++) {
				String curWord = lines.get(b).getEntry(it);
				if (indexWord == curWord) {
					String up = indexWord.toUpperCase();
					int lineNum = lines.get(b).getLineNumber();
					bw.write("<a href=\""+ Name +".html#line_" + lineNum+c + ">" + up + "</a>" + " ");
				} else {
					bw.write(curWord + " ");
				}
			}

			i++;
			bw.write("<br>"+"\n");

		}

		bw.write("</div></body></html>");

		bw.close();

	}

}
