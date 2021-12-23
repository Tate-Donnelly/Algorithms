import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class LinearProbingHashST<Key, Value> {
    private int N; // number of key-value pairs in the table
    private int M = 16; // size of linear-probing table
    int docNum=0;
    public Key[] keys; // the keys
    public Value[] vals; // the values
    LinkedList<Doc> docList=new LinkedList<Doc>();
    //LinearProbingHashST<Key, Term> DocFreq;

    public LinearProbingHashST() {
        keys = (Key[]) new Object[M];
        vals = (Value[]) new Object[M];
    }
    public String getRandomKey(){
        int index=ThreadLocalRandom.current().nextInt(0, M);
        if(keys[index]!=null){
            return (String) keys[index];
        }
        return getRandomKey();
    }
    public String[] keys(){
        return (String[]) keys;
    }
    public int size(){
        return M;
    }
    public LinearProbingHashST(int cap) {
        M=cap;
        keys = (Key[]) new Object[cap];
        vals = (Value[]) new Object[cap];
    }
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    private void resize(int cap) { // See page 474.
        LinearProbingHashST<Key, Value> t;
        t = new LinearProbingHashST<Key, Value>(cap);
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                t.put(keys[i], vals[i]);
            }
        }
        keys = t.keys;
        vals = t.vals;
        M = t.M;
    }
    public void put(Key key, Value val) {
        if (N >= M/2) {
            resize(2 * M); // double M (see text)
        }
        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
        }
        keys[i] = key;
        vals[i] = val;
        N++;
    }
    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                return vals[i];
            }
        }
        return null;
    }

    public void loadDocs(String allDocs, LinearProbingHashST hash){

        String stream=null;
        try {
            stream = new String(Files.readAllBytes(Paths.get(allDocs)));
            String[] lines = stream.split("\\r?\\n");
            for(String line :lines){
                String[] arr=line.split(" ");
                Doc doc=new Doc();
                doc.doc(arr[0],arr[1]);
                hash.put(arr[0],doc);
                doc.readDoc();
                docList.add(doc);
                docNum++;
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int countTotalOccurences(String word){
        int total=0;
        for(int i=0;i< docList.size();i++) {
            Doc doc=docList.get(i);
            if (doc != null) {
                if(doc.termTable.get(word)!=null){
                    Term term=(Term)doc.termTable.get(word);
                    total+=term.docFrequency;
                }
            }
        }
        //System.out.println("Total entries of '"+word+ "': " + total);
        return total;
    }

    public void buildTermTable(LinearProbingHashST DocFreq){
        for(Doc doc: docList) {
            if (doc != null) {
                LinearProbingHashST terms=doc.termTable;
                Key[] arr2=(Key[]) terms.keys;
                for(Key word:arr2){
                    if(word!=null) {
                        Term t = ((Term) terms.get(word));
                        t.doc=doc;
                        t.totalOccurences = this.countTotalOccurences((String) word);
                        t.totalDocs = docNum;
                        t.lp = this;
                        DocFreq.put(word, t);
                    }
                }
            }
        }
        scoreTerms(DocFreq);
    }
    public void scoreTerms(LinearProbingHashST DocFreq){
        Key[] arr= (Key[]) DocFreq.keys;
        for(Key word : arr) {
            if(word!=null){
                double score=((Term) DocFreq.get(word)).tfTimesIDF();
                //System.out.println(word+": "+ score);
                Key[] docs=(Key[]) keys;
                for(Key doc : docs) {
                    if(doc!=null){
                        Doc tempDoc=((Doc) get(doc));
                        if(tempDoc!=null){
                            if(tempDoc.termTable.get(word)!=null){
                                ((Term) tempDoc.termTable.get(word)).score=score;
                            }
                        }
                    }
                }
            }
        }
        /*for(Doc doc:docList){
            DocFreq.top10(doc);
        }*/
    }

    public void updateScores(){
        for(Doc doc:docList){
            for(Key key: (Key[])doc.termTable.keys){
                if(doc.termTable.get(key)!=null) {
                    //((Term) get(key)).tfTimesIDF();
                }
            }
        }
    }
    public double countOfDocsWithTerm(Key word){
        double result=0;
        for(Doc doc :docList){
            for(Key term: (Key[]) doc.termTable.keys){
                if(term!= null && ((Term)doc.termTable.get(term)).word.equals(word)){
                    result++;
                    break;
                }
            }
        }
        return result;
    }
    public LinkedList<Doc> search(Key word){
        LinkedList<Doc> list=new LinkedList<Doc>();
        System.out.println("        '"+word +"' appeared ");
        for(Doc doc: docList) {
            if (doc != null) {
                LinearProbingHashST terms = doc.termTable;
                Key[] arr2 = (Key[]) terms.keys;
                for (Key term : arr2) {
                    if (term != null && ((String) term).equals(word)) {
                        System.out.println("            "+((Term) doc.termTable.get(term)).docFrequency+" time(s) (Score: "+ ((Term) doc.termTable.get(term)).score+") in "+doc.fileName );
                        list.add(doc);
                        break;
                    }
                }
            }
        }
        //System.out.println("}");
        return list;
    }
    public void Top10(){
        for(Doc doc: docList){
            top10(doc);
        }
    }
    public Term[] top10(Doc doc){
        Term[] topTerms=new Term[10];
        loadTerms(topTerms,doc, this);
        Key[] terms=(Key[]) doc.termTable.keys;
        System.out.println("Top Terms in "+doc.fileName+": ");
        for(int j=0; j<topTerms.length;j++){
            Term t=topTerms[j];
            System.out.println("  "+(j+1)+". "+topTerms[j].word+" - "+topTerms[j].score);
        }
        System.out.println();
        return topTerms;
    }
    public Term[] loadTerms(Term[] topTerms, Doc doc, LinearProbingHashST DocFreq){
        Key[] terms=(Key[]) doc.termTable.keys;
        int i=0;
        for(Key key: terms){
            if(i!=10 && topTerms[i]==null){
                if(key!=null && doc.termTable.get(key)!=null){
                    ((Term) doc.termTable.get(key)).tfTimesIDF();
                 //   System.out.println(((Term) doc.termTable.get(key)).word+" " +((Term) doc.termTable.get(key)).score);
                    topTerms[i]=(Term) doc.termTable.get(key);
                    i++;
                }
            }else {
                break;
            }
        }
        //Compare scores is done multiple times to ensure accuracy
        for(int k=0; k<10;k++){
            topTerms=compareScores(topTerms, doc, terms);
        }
        return topTerms;
    }
    public Term[] compareScores(Term[] topTerms, Doc doc, Key[] terms){
        for(Key key: terms){
            if(key!=null && doc.termTable.get(key)!=null) {
                Term word = (Term) doc.termTable.get(key);
                for (int j = 0; j < topTerms.length; j++) {
                    if(word.score>topTerms[j].score && !containsTerm(topTerms,word)){
                        topTerms[j]=word;
                        break;
                    }
                }
            }
        }
        return topTerms;
    }
    public boolean containsTerm(Term[] topTerms, Term term){
        for(int i=0; i<topTerms.length;i++){
            if(topTerms[i].equals(term)){
                return true;
            }
        }
        return false;
    }
}
