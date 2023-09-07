package kz.edu.nu.cs.se.hw;
import java.util.Arrays;
import java.util.List;

public class Line implements Indexable {
	 private List<String> words;
	 private int num;
	
	public Line(String line, int i) {
        num = i;
        words = Arrays.asList(line.split(" "));
    }

	
    public String getEntry(int index) {
    	 return words.get(index);
    	
    	
    }
    
    public int getLineNumber() {
    	return num;
    }
    
    public int getNumberOfWords() {
    	return words.size();
    }
}
