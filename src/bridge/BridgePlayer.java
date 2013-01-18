package bridge;
import java.util.*;

public class BridgePlayer {
    String Name;
    int number_of_cards;
    boolean hidden;    
    int[] cards = new int[13];
    boolean is_human= false;
    BridgePlayer(String name){
       Name=name;
    }

    public void printCards (){
        System.out.println("Printing cards of player: "+Name);
        for (int i=0; i<number_of_cards; i++){
            System.out.print(cards[i]+",  ");
        }
        System.out.println();
    }
    
    public void addCard (int card){
        cards[number_of_cards]=card;
        number_of_cards++;
    }

    public int playCard (int index){
        int out_card=cards[index];
        for (int i=index; i<number_of_cards-1; i++){
            cards[i]=cards[i+1];
        }
        number_of_cards--;
        return out_card;
    }
 
    public int[] suiteExists (int suite){
        int[] out = {-1,-1};
        for (int i=0; i<number_of_cards; i++){
            if (((cards[i]-1)/13)==suite ){
                if (out[0]==-1){
                    out[0]=i;
                }                
            }
            else{
                if (out[0]==-1);
                else{
                    if (out[1]==-1){
                        out[1]=i-1;
                    }                    
                }
            }
        }
        if ((out[0]>-1)&&(out[1]==-1)){
            out[1]=number_of_cards-1;
        }
        return out;
    }
    
    public int[] possibleMoves (int suite){
        if (suite==-1){
            int[] possible = new int[number_of_cards];
            for (int i=0; i<number_of_cards; i++){
                possible[i]=cards[i];
            }
            return possible;
        }
        else {
            int[] range=suiteExists(suite);
            if (range[0]==-1){
                return possibleMoves(-1);
            }
            else{
                int[] possible = new int[range[1]-range[0]+1];
                for (int i=0; i<range[1]-range[0]+1; i++){
                    possible[i]=cards[i+range[0]];
                }
                return possible;
            }
        }
    }

    public int getCardIndex (int card){
        int index=-1;
        for (int i=0; i<number_of_cards; i++ ){
            if (cards[i]==card){
                return i;
            }
        }
        return index;
    }

    public int isHigestAvailableofSuite (int suite){
        int[] range=suiteExists(suite);
        if (range[0]==-1){
            return -1;
        }        
        int[] possible = possibleMoves(suite);
        int max=possible[0];
        if (suite*13+1==max){
            return max;
        }
        else{
            for (int i=1; i<possible.length; i++){
                if (possible[i]>max){
                    max=possible[i];
                }
            }
        }
        return max;
    }

    public int isHighestofSuite (int suite, int[] cards_not_played, int[] cards_on_deck, int no_on_deck){
        int high_available=isHigestAvailableofSuite(suite);
        boolean win=true;
        if (suite*13+1==high_available){
            return high_available;
        }
        for (int i=0; i<cards_not_played.length; i++){
            if ((cards_not_played[i]>high_available)&&((cards_not_played[i])/13==suite)){
                win=false;
            }     
            if ((suite*13+1==cards_not_played[i])){
                win=false;
            }
        }
        for (int i=0; i<no_on_deck; i++){
            if ((cards_on_deck[i]>high_available)&&((cards_on_deck[i])/13==suite)){
                win=false;
            }     
            if ((suite*13+1==cards_on_deck[i])){
                win=false;
            }
        }
        if (win==true){
            return high_available;
        }
        else {
            return -1;
        }
        
    }

    public int lowestofSuite (int suite){
        int[] range=suiteExists(suite);
        boolean min=true;
        if (suite==-1){
            return possibleMoves(suite)[0];
        }
        if (range[0] != -1){
            return possibleMoves(suite)[0];
        }
        else{
            return -1;
        }
    }

    public int askHuman( int[] possible){
        System.out.println();
        System.out.println("Play from: ");
        for (int i=0; i<possible.length; i++){
            System.out.print(possible[i]+",  ");
        }
        boolean isokay=false;
        int num=-1;
        while (isokay==false){
            System.out.println();
            Scanner in = new Scanner(System.in);
            num = in.nextInt();
            for (int i=0; i<possible.length; i++){
                if (num==possible[i]){
                    isokay=true;
                    break;
                }
            }
            if (isokay==false){
                System.out.println("Not a valid Move!!");
            }
        }
        return num;
    }
    
    public int bestMove_level1 (int[] not_played_cards, int suite, int[] ondeck, int no_on_deck){
        if (is_human==true){
            return (askHuman(possibleMoves(suite)));
        }
        if (suite==-1){
            for (int i=0; i<4; i++){
                int high = isHighestofSuite(i, not_played_cards, ondeck, no_on_deck);
                if (high != -1){
                    return high;
                }
            }
            // no sure win
            for (int i=0; i<4; i++){
                int low=lowestofSuite(suite);   
                if (low != -1){
                    return low;
                }
            }
            return -1;
        }
        else{
            int high = isHighestofSuite(suite, not_played_cards, ondeck, no_on_deck);
            if (high != -1){
                return high;
            }
            else{
                int low=lowestofSuite(suite);   
                if (low != -1){
                    return low;
                }
                else{
                  // nothing available
                    for (int i=0; i<4; i++){
                        low=lowestofSuite(i);   
                        if (low != -1){
                            return low;
                        }
                    }
                  return -1;
                }                
            }
        }
    }
    
    public int[] scoreSuite (int suite){//{count, score} is returned;
        int[] range=suiteExists(suite);
        int count;
        int score;
        int[] bid_para= {-1, -1};
        
        if (suite==-1){// No trump
            score=0;
            count=0;
            for (int i=0; i<4; i++){
                int[] suite_para= scoreSuite(i);
                if (suite_para[0]>7){
                    bid_para[0]=0; // 8 is too high to bid NT
                    bid_para[1]=0; // 8 is too high to bid NT
                    return bid_para;
                }
                else{
                    score=score +suite_para[1];
                    count+= suite_para[0];
                }
            }
            bid_para[0]=count;
            bid_para[1]=score;            
            return bid_para;
        }
        else{
            if (range[0]==-1){
                count=0;
                score=0;
            }
            else{
                int[] possible=possibleMoves(suite);
                score=0;
                count=possible.length;
                for (int i=0; i<possible.length; i++){
                    if (possible[i]==suite*13+1){// Ace
                        score=score+4;
                    }
                    else if (possible[i]==suite*13+13){//King
                        score=score+3;
                    }
                    else if (possible[i]==suite*13+12){//Queen
                        score=score+2;
                    }
                    else if (possible[i]==suite*13+11){//Jack
                        score=score+1;
                    }
                }
            }
            bid_para[0]=count;
            bid_para[1]=score;        
            return bid_para;
        }
    }
    
    /*
     * WORKING ON BIDDING POSTPONED... NOW WORKING ON win deciding
    public boolean canBid (int[] bid_paras){// bid_paras is {suite, bid_value};
        int[] scores = scoreSuite(bid_paras[0]);
        int[] scorestotal = scoreSuite(-1);

        if (bid_paras[0]==2 || bid_paras[0]==3){ // clubs or diamonds
            
        }
    }
    
    public int[] bid_level1 (int[] min_bid){
        for (int i=0; i<4; i++){
            int suite=min_bid[1]+i;
            if (suite>=4){
                suite=suite-4;
                min_bid[0]++;
            }
             //if we can bid this
             // bid and return   
        }
        //no bid possible, so return {-1,-1};
        
    }    
    */
}

