import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Erfan Rahnemoon on 6/26/17.
 * Very simple user interface Because the professor(😜) does not know how to work with the terminal 😞😞😞😞
 */
public class Main {
    static int numberOfTransaction = 0;
    static JLabel label = new JLabel("        ");
    public static void main(String[] args) {
        JFrame frame = new JFrame("Database implemention");
        JPanel panelMain = new JPanel(new BorderLayout());
        JPanel panelText = new JPanel(new BorderLayout());
        JPanel panelButton = new JPanel(new GridLayout(1,3));
        JPanel panelEnd = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panelText);
        JPanel panelNumberTransaction = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JButton buttonCon = new JButton("concurrency");
        JButton button2PL = new JButton("2PL");
        JButton buttonDead = new JButton("Deadlock");
        JTextField fieldNumberTransaction = new JTextField();
        label.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
        JButton buttonSetNumTransaction = new JButton("set");

        panelNumberTransaction.add(fieldNumberTransaction,BorderLayout.WEST);
        panelNumberTransaction.add(buttonSetNumTransaction,BorderLayout.EAST);
        panelButton.add(buttonCon);
        panelButton.add(button2PL);
        panelButton.add(buttonDead);
        panelEnd.add(panelButton,BorderLayout.SOUTH);
        panelEnd.add(label,BorderLayout.NORTH);
        panelText.add(textArea);
        panelMain.add(scrollPane,BorderLayout.CENTER);
        panelMain.add(panelEnd,BorderLayout.SOUTH);

        panelMain.add(panelNumberTransaction,BorderLayout.NORTH);
        textArea.setEnabled(false);

        fieldNumberTransaction.setPreferredSize(new Dimension(350,30));
        scrollPane.setPreferredSize(new Dimension(351,475));

        buttonSetNumTransaction.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                if (fieldNumberTransaction.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Field is empty");
                }else {
                    try {
                        numberOfTransaction = Integer.valueOf(fieldNumberTransaction.getText());
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null,"Field context is only number");
                    }
                    textArea.setEnabled(true);
                    fieldNumberTransaction.setEnabled(false);
                }
            }
        });

        buttonCon.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                if (textArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Text Area is empty");
                }
                checkConcurrency(textArea.getText());
            }
        });

        button2PL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Text Area is empty");
                }
                check2PL(textArea.getText());
            }
        });
        buttonDead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Text Area is empty");
                }
                checkDeadlock(textArea.getText());
            }
        });

        frame.add(panelMain);
        frame.setSize(400,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private static void checkConcurrency(String textArea) {
        ArrayList<String> listOfTransAction = new ArrayList<>();
        ArrayList<String> listOfData = new ArrayList<>();
        String [] tempSeqOfRunnig = textArea.split("\n");
        for (String item : tempSeqOfRunnig) {
            // System.out.println(item);
            listOfTransAction.add(item);
        }
        for(int i = 0; i<tempSeqOfRunnig.length;i++){
            String [] tempSplit = tempSeqOfRunnig[i].split("\\(");
            listOfData.add(tempSplit[1].substring(0, tempSplit[1].length()-1));
        }

        DetectConcurrency detectConcurrency = new DetectConcurrency(listOfTransAction,numberOfTransaction);
        if (detectConcurrency.getIsConcurrent()){
            label.setText("توالی پذیر است");
        }else{
            label.setText("توالی پذیر نیست");
        }
//        System.out.println(detectConcurrency.getIsConcurrent());
    }
    private static void check2PL(String textArea){
        ArrayList<String> listOfTransAction = new ArrayList<>();
        ArrayList<String> listOfData = new ArrayList<>();
        String [] tempSeqOfRunnig = textArea.split("\n");
        for (String item : tempSeqOfRunnig) {
            listOfTransAction.add(item);
        }
        for(int i = 0; i<tempSeqOfRunnig.length;i++){
            String [] tempSplit = tempSeqOfRunnig[i].split("\\(");
            listOfData.add(tempSplit[1].substring(0, tempSplit[1].length()-1));
        }
        Detect2PL detect2PL =new Detect2PL(listOfTransAction,numberOfTransaction);
        if (detect2PL.isPlan2PL()){
            label.setText("پروکل دو مرحله ای رعایت شده است");
        }else{
            label.setText("پروکل دو مرحله ای رعایت نشده است");
        }
//        System.out.println(detect2PL.isPlan2PL());
    }
    private static void checkDeadlock(String textArea){
        ArrayList<String> listOfTransAction = new ArrayList<>();
        ArrayList<String> listOfData = new ArrayList<>();
        String [] tempSeqOfRunnig = textArea.split("\n");
        for (String item : tempSeqOfRunnig) {
            listOfTransAction.add(item);
        }
        for(int i = 0; i<tempSeqOfRunnig.length;i++){
            String [] tempSplit = tempSeqOfRunnig[i].split("\\(");
            listOfData.add(tempSplit[1].substring(0, tempSplit[1].length()-1));
        }
        DetectDeadlock detectDeadlock = new DetectDeadlock(listOfTransAction,numberOfTransaction);
        if(detectDeadlock.isDeadlock()){
            label.setText("بن بست اتفاق نمی افتد");
        }else{
            label.setText("بن بست اتفاق می افتد");
        }
//        System.out.println(detectDeadlock.isDeadlock());
    }
}
