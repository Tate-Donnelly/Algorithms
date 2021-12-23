import java.util.LinkedList;

public class Main {
    public static void main(String[] args){
        Test t=new Test();
        if(t.test()) {
            LinearProbingHashST lp = new LinearProbingHashST(14);
            lp.loadDocs("src/extensions.txt", lp);
            LinearProbingHashST DocumentFreq = new LinearProbingHashST();
            lp.buildTermTable(DocumentFreq);
            System.out.println(((Term) DocumentFreq.get(DocumentFreq.getRandomKey())).score);
            lp.search("cat");
            lp.Top10();
            DocumentFreq.top10((Doc) lp.get("bustopher-jones-the-cat-about-town"));

            System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nSearch Trials LinearProbingHashST");

            for(int i=0; i<10;i++) {
                LinkedList<Term> searchTerms=new LinkedList<>();
                for(int j=0;j<DocumentFreq.size()/10;j++){
                    searchTerms.add( (Term) DocumentFreq.get(DocumentFreq.getRandomKey()));
                }
                Stopwatch timer=new Stopwatch();
                for (Term term : searchTerms) {
                    lp.search(term.word);
                }
                System.out.println("Trial "+(i+1)+": "+timer.elapsedTime());

            }

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nConstuction Trials LinearProbingHashST");
            for(int i=0; i<10;i++) {
                Stopwatch timer=new Stopwatch();
                LinearProbingHashST test = new LinearProbingHashST();
                test.loadDocs("src/extensions.txt", test);
                LinearProbingHashST DocumentFreqTest = new LinearProbingHashST();
                test.buildTermTable(DocumentFreqTest);
                System.out.println("Trial "+(i+1)+": "+timer.elapsedTime());

            }

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nSearch Trials BST");
            BST<String, Doc> bst=new BST<String, Doc>();
            bst.loadDocs("src/extensions.txt",bst);
            BST<String, Term> bstDocFreq=new BST<String, Term>();
            bst.buildTermTable(bstDocFreq);
            //bst.search("bustopher");
            for(int i=0; i<10;i++) {
               String[] searchTerms = new String[bstDocFreq.size()/10];
                for(int j=0;j<bstDocFreq.size()/10;j++){
                    String key=bstDocFreq.getRandomKey();
                    searchTerms[j]=key;
                    //System.out.println("Does doc contain '"+key+"' "+bstDocFreq.contains(key));
                }
                Stopwatch timer=new Stopwatch();
                for (String term : searchTerms) {
                    //System.out.println(term);
                    bst.search(term);
                }
              System.out.println("Trial "+(i+1)+": "+timer.elapsedTime());
            }

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nConstuction Trials BST");
            for(int i=0; i<10;i++) {
                Stopwatch timer=new Stopwatch();
                BST test = new BST();
                test.loadDocs("src/extensions.txt", test);
                BST DocumentFreqTest = new BST();
                test.buildTermTable(DocumentFreqTest);
                System.out.println("Trial "+(i+1)+": "+timer.elapsedTime());
            }
        }
    }
}
