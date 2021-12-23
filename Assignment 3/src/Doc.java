import java.nio.file.Files;
import java.nio.file.Paths;

public class Doc {
    String extension;
    String fileName;
    String[] arr;
    LinearProbingHashST termTable=new LinearProbingHashST();
    BST termTree=new BST();
    public void doc(String fileName, String extension){
        this.fileName=fileName;
        this.extension=extension;
    }
    public void readDoc() {
        String stream=null;
        try {
            stream = new String(Files.readAllBytes(Paths.get(this.extension))).toLowerCase();
            stream=stream.replaceAll("[^\\h\\v\\p{L}'-]+|(?<=\\P{L}|^)'|(?=\\P{L}|$)", "");
            arr=stream.split("[^\\w-']+");
            for(String term : arr){
                //System.out.println("'"+term+"'");
                if(this.termTable.get(term)==null){
                    Term temp=new Term();
                    temp.doc=this;
                    temp.word=term;
                    temp.docFrequency=count(term);

                    termTable.put(term, temp);
                }
                if(!termTree.contains(term)){
                    Term temp=new Term();
                    temp.doc=this;
                    temp.word=term;
                    temp.docFrequency=count(term);
                    termTree.put(term, temp);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int count(String term){
        int result=0;
        for(String word : arr){
            if(word.equals(term)){
                result++;
            }
        }
        return result;
        //return termTable.countOccurences(term);
    }

}
