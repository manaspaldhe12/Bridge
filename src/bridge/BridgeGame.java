package bridge;
import java.util.*;

public class BridgeGame {
    BridgePlayer[] Players = new BridgePlayer[4];
    int[] not_played_cards=new int[52];
    //int no_played=0;
    int[] ondeck=new int[4];
    int no_on_deck=0;
    int trump=-1;
    int [] bid= new int[2];
    int bidder=-1;
    int[] round_winner = new int[13];

    public int[] insertionSort(int[] A){
        int n = A.length-1;
        for (int i=1; i<=n; i++){    
            for (int var=i; var>0; var--){
                if (A[var]<A[var-1]){
                    int temp=A[var-1];
                    A[var-1]=A[var];
                    A[var]=temp;
                }
                else {
                    break;
                }
            }
        }
        return A;
    }
        
    public int[] Randomize (int[] input){
        Random generator = new Random();
        int arraylength=input.length;
        for (int i=0; i<arraylength; i++){
            int randomIndex = generator.nextInt(arraylength-i )+i;
            // swap i and randomindex
            int temp=input[i];
            input[i]=input[randomIndex];
            input[randomIndex]=temp;
        }
        return input;
    }

    public void allotCards (){
        int[] cards = new int[52];
        for (int i=1; i<=52; i++){
            cards[i-1]=i;
        }
        cards = Randomize(cards);        
        for (int i=0; i<52; i++){
            Players[i/13].addCard(cards[i]);
        }
    }

    public void updateCardsonDeck(int card){
        if (no_on_deck<=3){
            ondeck[no_on_deck]=card;
            no_on_deck++;
        }
        if (no_on_deck==4){
            no_on_deck=0;
        }
    }

    public void plays (int player_no, int index){
        int card=Players[player_no].playCard(index);
        not_played_cards[card-1]=-2;// -2 is played.
        updateCardsonDeck(card);
    }

    public int getSuite (int card){
        return (card-1)/13;
    }
    
    public int decideRoundWinner (int[] roundcards, int starter){
        int start_card=roundcards[starter];
        int start_suite= (roundcards[starter]-1)/13;
        int winner=starter;
        int winner_suite=start_suite;
        
        for (int i=starter+1; i< starter+4; i++){
            int j= i;
            if (j>3){
                j=j-4;
            }
            int j_suite= (roundcards[j]-1)/13;            

            if (j_suite==trump){
                if (winner_suite==trump){
                    if (((roundcards[j]>roundcards[winner])&&(trump*13+1!=roundcards[winner]))||(trump*13+1==roundcards[j])){
                        winner=j;
                        winner_suite=j_suite;
                    }
                }
                else{
                    winner=j;
                    winner_suite=j_suite;
                }
            }
            else if (j_suite==start_suite){
                if (winner_suite != trump){
                    if (((roundcards[j]>roundcards[winner])&&(start_suite*13+1!=roundcards[winner]))||(start_suite*13+1==roundcards[j])){
                        winner=j;
                        winner_suite=j_suite;
                    }
                }                
            }
        }
        return winner;
    } 
    
    public void playGame (int start_player){
        int[] roundcards= new int[4];
        for (int round=1; round<=13; round++){
            int suite=-1;
            for (int i=start_player; i<start_player+4; i++){
                int j=i;
                if (j>3){
                    j=j-4;
                }
                if (i==start_player){
                    roundcards[j]=Players[j].bestMove_level1(not_played_cards, -1, ondeck, no_on_deck);                            
                    suite=(roundcards[j]-1)/13;
                }
                else{
                    roundcards[j]=Players[j].bestMove_level1(not_played_cards, suite, ondeck, no_on_deck);                            
                }
                int index=Players[j].getCardIndex(roundcards[j]);
                plays(j, index);                
                System.out.println(j+ " plays "+roundcards[j]);
            }
             
            start_player=decideRoundWinner(roundcards, start_player);
            start_player=decideRoundWinner(roundcards, start_player);
            System.out.println("Round Winner of " +round+ " is "+start_player);
            System.out.println("----------------------------------------------"); 
            round_winner[round-1]=start_player;
        }
                        
    }
    
    public void initialize (String[] names){        
        for (int i=0; i<4; i++){
            Players[i] = new BridgePlayer(names[i]);
            Players[i].number_of_cards=0;
            Players[i].hidden=true;
        }
        allotCards();
        for(int i=0; i<4; i++){
            Players[i].cards=insertionSort(Players[i].cards);
        }
        for(int i=1; i<53; i++){
            not_played_cards[i-1]=i;
        }
        bid[0]=-2;
        bid[1]=-1;
        bidder=-1;
        for (int i=0; i<13; i++){
            round_winner[i]=-1;
        }

    }
    
    public void playerBid (int player, int[] bids){
        bid=bids;
        bidder=player;
    }
    
    public boolean decideWinorLoss (){
        int bidding_team_wins=0;
        for (int i=0; i<13; i++){
            if ((round_winner[i]==bidder) || (round_winner[i]==(bidder+2)%4 )){
                bidding_team_wins++;
            }
        }
        System.out.println(bidding_team_wins);
        if (bidding_team_wins-6>bid[1]){
            return true;
        }
        else{
            return false;
        }
    }

}
