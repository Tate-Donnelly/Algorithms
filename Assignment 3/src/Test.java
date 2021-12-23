import java.util.LinkedList;

public class Test {
    public boolean test(){
        boolean result=countOfDocsWithTerm()&&findOccurrences() && findTotalOccurrences() &&testScore();//&&searchRand();
        return result;
    }
    public boolean findTotalOccurrences(){
        boolean result=false;
        Doc masterDoc=new Doc();
        masterDoc.doc("allFiles.txt","src/allFiles.txt");
        masterDoc.readDoc();
        String randomword=masterDoc.termTable.getRandomKey();
        LinearProbingHashST docs=new LinearProbingHashST();
        docs.loadDocs("src/extensions.txt",docs);
        int numTotalOccurences=masterDoc.count(randomword);
        //System.out.println("There's "+numTotalOccurences+" total occurrences of '"+randomword+"'");
        if(docs.countTotalOccurences(randomword)==numTotalOccurences){
            System.out.println("findTotalOccurrences works");
            result=true;
        }else{
            System.out.println("findTotalOccurrences failed");
        }
        return result;
    }
    public boolean findOccurrences(){
        boolean result=false;
        LinearProbingHashST docs=new LinearProbingHashST();
        docs.loadDocs("src/extensions.txt",docs);
        Doc doc=((Doc) docs.get("gus-the-theater-cat"));
        if(doc.count("a")==16){
            System.out.println("findOccurrences works");
            result=true;
        }else{
            System.out.println("findOccurrences failed");
        }
        return result;
    }
    public boolean countOfDocsWithTerm(){
        boolean result=false;
        LinearProbingHashST lp = new LinearProbingHashST(14);
        lp.loadDocs("src/extensions.txt", lp);
        if(1 == lp.countOfDocsWithTerm("bustopher")){
            result=true;
            System.out.println("countOfDocsWithTerm() worked");
        }else{
            System.out.println("countOfDocsWithTerm() failed");
        }
        return result;
    }
    public boolean testScore(){
        boolean result=false;
        LinearProbingHashST lp = new LinearProbingHashST(14);
        lp.loadDocs("src/extensions.txt", lp);
        LinearProbingHashST DocumentFreq = new LinearProbingHashST();
        lp.buildTermTable(DocumentFreq);
        Term term=((Term) DocumentFreq.get("bustopher"));
        if(term.totalOccurences>0) {
            if (term.score == Math.log10(4)*Math.log10(14)) {
                result = true;
                System.out.println("scoreTest works");
            } else {
                System.out.println("scoreTest Failed");
            }
        }
        return result;
    }
    public boolean searchRand(){
        boolean result=false;
        LinkedList<Doc> docList=new LinkedList<Doc>();
        LinearProbingHashST docs=new LinearProbingHashST();
        LinearProbingHashST DocFreq=new LinearProbingHashST();
        Doc masterDoc=new Doc();
        docs.loadDocs("src/extensions.txt",docs);
        docs.buildTermTable(DocFreq);
        masterDoc.doc("allFiles.txt","src/allFiles.txt");
        masterDoc.readDoc();
        String randomword=masterDoc.termTable.getRandomKey();
        docList=docs.search(randomword);
        System.out.println("Searching for '"+randomword+"' reveals that it's in "+docList.size()+" document(s)");
        if(docList.size()== docs.countOfDocsWithTerm(randomword)){
            System.out.println("searchRand works");
            result=true;
        }else{
            System.out.println("searchRand failed");
        }
        return result;
    }

    /*public boolean searchRandBST(){
        boolean result=false;
        LinkedList<Doc> docList=new LinkedList<Doc>();
        BST docs=new BST();
        BST DocFreq=new BST();
        Doc masterDoc=new Doc();
        docs.loadDocs("src/extensions.txt",docs);
        docs.buildTermTable(DocFreq);
        masterDoc.doc("allFiles.txt","src/allFiles.txt");
        masterDoc.readDoc();
        String randomword=masterDoc.termTable.getRandomKey();
        docList=docs.search(randomword);
        System.out.println("Searching for '"+randomword+"' reveals that it's in "+docList.size()+" document(s)");
        if(docList.size()== docs.countOfDocsWithTerm(randomword)){
            System.out.println("searchRand works");
            result=true;
        }else{
            System.out.println("searchRand failed");
        }
        return result;
    }*/

}
