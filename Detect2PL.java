import java.util.ArrayList;

/**
 * Created by Erfan Rahnemoon on 6/27/17.
 * 
 * detect deadlock Exactly in the same form and algorithm that the book explains with no Optimization
 * In fact, code with awful performance and scalability is worse
 */
public class Detect2PL {
    ArrayList<String> listOfAction ;
    int numberOfTransaction;
    String [][] transactionTable;
    boolean [] transactionIs2PL;
    boolean isPlan2PL;
    public Detect2PL(ArrayList<String> listOfAction,int numberOfTransaction){
        this.listOfAction = listOfAction;
        this.numberOfTransaction = numberOfTransaction;
        transactionTable = new String[listOfAction.size()][numberOfTransaction];
        transactionIs2PL= new boolean[numberOfTransaction];
        for (int i = 0; i < transactionIs2PL.length; i++) {
            transactionIs2PL[i] = true;
        }
        createTableTransaction(listOfAction);
        check2PL(transactionTable);
        sumCheck2PL(transactionIs2PL);
    }
    private void createTableTransaction(ArrayList list) {
        for(int i=0; i<numberOfTransaction;i++){
            for (int j=0;j<listOfAction.size();j++) {
                transactionTable[j][i] = "null";
            }
        }
        for(int i=0; i<numberOfTransaction;i++){
            for (int j=0;j<listOfAction.size();j++) {
                if(listOfAction.get(j).toUpperCase().contains("T"+(i+1))){
                    transactionTable[j][i] = listOfAction.get(j).split(":")[1].toUpperCase();
                }
            }
        }
    }
    private void check2PL(String [][] matrix){
        int lastline = 0;
        for (int i = 0; i < transactionTable[0].length; i++) {
            for (int j = 0; j < transactionTable.length; j++) {
                if (transactionTable[j][i].contains("UNLOCK")){
                    //transactionIs2PL[i] = true;
                    lastline = j;
                }else if((transactionTable[j][i].contains("LOCK_X") || transactionTable[j][i].contains("LOCK_S")) && j > lastline){
                    transactionIs2PL[i] = false;
                }
            }
        }
    }
    private void sumCheck2PL(boolean [] array){
        boolean tempSumCheck = array[0];
        for (int i = 0; i < array.length; i++) {
            tempSumCheck = tempSumCheck && array[i];
        }
        setPlan2PL(tempSumCheck);
    }

    public boolean isPlan2PL() {
        return isPlan2PL;
    }
    public void setPlan2PL(boolean isPlan2PL){
        this.isPlan2PL = isPlan2PL;
    }
}
