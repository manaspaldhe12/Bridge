package bridge;
import java.util.*;

public class Bridge {
    
    public static void main(String[] args) {
        BridgeGame game = new BridgeGame();
        String[] names={"Manas", "Noddy", "Psycho", "Mungre"};
        game.initialize(names);
        for (int i=0; i<4; i++){
            game.Players[i].printCards();
        }
        
        System.out.println("You are Manas");
        System.out.println("Please bid");
        game.Players[0].is_human=true;
        game.bid[0]=0;
        game.bid[1]=1;
        game.bidder=0;
        game.trump=0;        
        game.playGame(0);
        boolean result=game.decideWinorLoss();
        System.out.println(result);
//        // Testing the roundwinner
//        game.trump=1;
//        int[] roundcards={6,9,1,3};
//        System.out.println(game.decideRoundWinner (roundcards, 3));
        
//        for (int i=0; i<4; i++){
//            game.Players[i].printCards();
//        }
//        System.out.println();
        /*
        for (int i=0; i<4; i++){
            System.out.println("Best move of player "+ game.Players[i].Name+" "+game.Players[i].bestMove_level1(cards_not_played, -1));
        }
        */
//        int suite=0;
//        for (int i=0; i<13; i++){
//            System.out.println(" ROUND "+i+": ");
//            //int j=0;
//            for (int j=0; j<4; j++){
//                int card=0;
//                if (j==0){
//                    card=game.Players[j].bestMove_level1(game.not_played_cards, -1, game.ondeck, game.no_on_deck);
//                    suite=(card-1)/13;
//                }
//                else{
//                    card=game.Players[j].bestMove_level1(game.not_played_cards, suite, game.ondeck, game.no_on_deck);
//                }
//                System.out.print(card+", ");
////                card=game.Players[j].bestMove_level1(game.not_played_cards, suite, game.ondeck, game.no_on_deck);
//                int index=game.Players[j].getCardIndex(card);
//                game.plays(j, game.Players[j].getCardIndex(card));                
//            }
//            System.out.println();
//            System.out.println("---------------------------------------------");
//        }
////
    }    
}

