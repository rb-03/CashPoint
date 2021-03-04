/*
 * CSCI1130 Java Exercise
 *
 * I declare that the assignment here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Section     : CSCI 1130 A 
 * Student Name: Rishika Bajaj
 * Student ID  : 1155138397
 * Date        : 19.10.20
 */
package cashpoint;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.text.DecimalFormat; 
/**
 *
 * @author Rishika Bajaj
 */
public class CashPoint {
    private static float balance = 109700.00F;
    
    public static String showMainMenu(){
        //shows the main menu for options
        return JOptionPane.showInputDialog("Cash Point: Input your task\n"+"1. Check Balance\n"+"2. Check Deposit\n"+"3. Check Withdraw\n"+"4. Exit\n","<type [1-4] here>");
    }
    
    public static String showDepositMenu(){
        //shows the menu for deposit
        return JOptionPane.showInputDialog("Cash Deposit(HKD):\n"+" Input # of $100 banknotes [20 max]");       
    }
    
    public static String showWithdrawMenu(){
        //shows the menu for withdrawal
        return JOptionPane.showInputDialog("Cash Withdrawal: Choose your currency\n1. Hong Kong Dollar (HKD)\n2. Korean Won (KRW)","<type [1 or 2] here>");
    }
    
    public static String showHkdRange(){
        //shows the menu for range available withdrawal of Hong Kong Dollars
        return JOptionPane.showInputDialog("Cash Withdrawal (HKD):\n100 min, 10000 max");
    }
    
    public static String showKrwRange(){
        //shows the menu for range available withdrawal of KWR
        return JOptionPane.showInputDialog("Cash Withdrawal (KRW):\n1000 min, 200000 max");
    }
    
    public static void checkBalance(){
        //method to check balance 
        String balance_str = String.format("Balance (HKD): %.2f\n", balance);
        JOptionPane.showMessageDialog(null, balance_str);
        main(new String[] {"Anything"});
    }
    
    public static void deposit(){
        //method used to deposit money 
        String number = showDepositMenu(); 
        if(number != null){
            int num = Integer.valueOf(number);
            if((num >= 1)&&(num <= 20)){
                //allows the user to deposit cash according to the range available
                int deposit_amount = 100*num;
                String dep_amount = String.format("HKD "+deposit_amount+" deposited");
                JOptionPane.showMessageDialog(null, dep_amount);
                balance += deposit_amount;
                main(new String[] {"Anything"});
            }
            else{
                //returns to the same menu incase of invalid input/out of range
                invalid();
                deposit();
            } 
        }
        else{
            //returns the previous menu
            main(new String[] {"Anything"});   
        }
    } 
    
    public static void withdraw(){
        //method used to withdraw money according to currency
        String number = showWithdrawMenu();
        if(number != null){
            int num = Integer.valueOf(number);
            if(num == 1){
                //calls when the user chooses HKD as currency
                hkd();
            }
            else if(num == 2){
                //calls when the user chooses KRW as currency
                krw();
            }
            else{
                //returns to the same menu incase of invalid input
                invalid();
                withdraw();
            }
        }
        else{
            //returns to previous menu 
            main(new String[] {"Anything"}); 
        }
    }
    
    public static void hkd(){
        ////Method reponsible for reducing balance and calculating required notes in HKD
        String HKD = showHkdRange();
        if(HKD != null){
            int hkd =  Integer.valueOf(HKD);
            if(hkd >= 100 && hkd <= 10000){
                if(balance<hkd){
                    //asks user to re-enter if balance is insuffiecient
                    bankBalance();
                    hkd();    
                }
                else{
                    //calculates the notes needed in the required denomination
                    int amount = hkd-hkd%100;
                    balance = balance - amount;
                    int notes_fivehundred = amount/500;
                    int remainder = amount%500;
                    int notes_hundred = remainder/100;
                    popHkd(amount,notes_fivehundred,notes_hundred );
                }
            }
            else{
                //if input is out of range, calls the method for the same and returns to the same menu
                invalid();
                hkd();
            }
        }
        else{
            //returns to previous menu if cancel is pressed
            withdraw();
        }
    }
    
    public static void popHkd(int amount, int notes_fivehundred,int notes_hundred){
        //Method to ask whether the user wants to withdraw money or not in HKD
        int choice = JOptionPane.showConfirmDialog (null, "Bank notes provided for HKD are 500 & 100\nWithdraw HKD "+amount+" or not?","Message", JOptionPane.YES_NO_OPTION);
        if(choice != JOptionPane.NO_OPTION){
            //proceeds to the confirmation menu
            hkdConfirmation(amount,notes_fivehundred,notes_hundred);
        }
        else{
            //returns to previous menu if user chooses cancel
            hkd();
        }
    }
    
    public static void hkdConfirmation(int amount, int notes_fivehundred, int notes_hundred ){
        //Method for cofirming the withdrawal of cash in HKD
        String pop = String.format("HKD "+amount+" withdrawn\nHKD 500 x "+notes_fivehundred+"\nHKD 100 x "+notes_hundred);
        if(pop != null){
            //displays confirmation dialog and returns to main menu
            JOptionPane.showMessageDialog(null, pop);
            main(new String[] {"Anything"});
        }
    }
    
    public static void krw(){
        //Method reponsible for reducing balance and calculating required notes in KRW
        String KRW = showKrwRange();
        if(KRW != null){
            int krw =  Integer.valueOf(KRW);
            if(krw >= 1000 && krw <= 200000){
                int amount = krw-krw%1000;
                float hkd_equivalent = amount/150.0F; //converts the KRW input to HKD
                if(balance<hkd_equivalent){
                    //Calls id the balance is insuffiecient
                    bankBalance();
                    krw();
                }
                else{
                    //calculates the number of notes needed for withdrawal
                    balance = balance - hkd_equivalent;
                    int notes_tenthousand = amount/10000;
                    int remainder = amount%10000;
                    int notes_thousand = remainder/1000;
                    popKrw(amount,hkd_equivalent,notes_tenthousand,notes_thousand);
                }        
            }
            else{
                //returns to the same menu if input is out of range/invalid
                invalid();
                krw();
            }
        }
        else{
            //returns to previous menu
            withdraw();
        }
    }
    
    public static void popKrw(int amount, float hkdEquivalent,int notes_tenthousand,int notes_thousand){
        //Method to ask whether the user wants to withdraw money or not in KRW and display HKD equivalent
        DecimalFormat precision = new DecimalFormat( "0.00" ); //used to display floating point upto 2 point
        int choice = JOptionPane.showConfirmDialog(null,"Bank notes provided for KWR are 10000 & 1000\nWithdraw KRW "+amount+" (HKD "+precision.format(hkdEquivalent)+") or not?","Message", JOptionPane.YES_NO_OPTION);
        if(choice != JOptionPane.NO_OPTION){
            //goes to next menu
            krwConfirmation(amount,notes_tenthousand,notes_thousand);
        }
        else{
            //returns to previous menu
            krw();
        }
    }
    
    public static void krwConfirmation(int amount ,int notes_tenthousand,int notes_thousand){
        //Method for cofirming the withdrawal of cash in KRW
        String pop = String.format("KRW "+amount+" withdrawn\nKRW 10000 x "+notes_tenthousand+"\nKRW 1000 x "+notes_thousand);
        JOptionPane.showMessageDialog(null, pop);
        main(new String[] {"Anything"});
    }
    
    public static void bankBalance(){
        //Method to display insufficient balance for withdrawal of cash
        String bbalance = String.format("Not enough balance, input again.");
        JOptionPane.showMessageDialog(null, bbalance);
    }
    
    public static void invalid(){
        //Method for displaying invalid input whenever the valus are out of range
        String validity = String.format("Invalid Input");
        JOptionPane.showMessageDialog(null, validity);
    }
    
    public static void exit(){
        //Exits the program as and when needed
        String exit = String.format("Hope to serve you again");
        JOptionPane.showMessageDialog(null, exit);
        System.exit(0);
    }
    
    public static void main(String[] args) {
        //requests processed in a loop according to the input by the user
        String choice;
        choice = showMainMenu();
        while(choice != null ){
        if ("1".equals(choice)){
            //calls menu for option 1 - Displaying Balance
            System.out.println("User picked 1"); 
            checkBalance();
        }
        else if("2".equals(choice)){
            //calls menu for option 2 - Depositing Cash
            System.out.println("User picked 2");
            deposit();
        } 
        else if("3".equals(choice)){
            //calls menu for option 3 - Withdrawaing cash in 2 currencies
            System.out.println("User picked 3");
            withdraw();
        }
        else if("4".equals(choice)){
            //calls menu for option 4 - exiting the terminal
            System.out.println("User picked 4");
            exit();
          
        }
        else{
            //calls method if range of input is invalid and returns to main menu
            invalid();
            main(new String[] {"Anything"});      
        }
        //calls menu for exit if user cancels from main menu
        System.out.println("User closed or cancelled dialog box");
        exit();
        }
    }
    
}
