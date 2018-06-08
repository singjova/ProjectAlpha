package uno;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * PostWildTurnSt
 */
public class PostWildTurnSt implements TurnSt {
  Uno uno;

  public PostWildTurnSt (Uno uno) {
    this.uno = uno;
  }

  @Override
  public void playCard(Player turnPlayer, String selectionChar) {
    int turnsCounter = uno.getTurnsCounter();
    String wildColor = uno.getWildColor();
    Card selectedCard = turnPlayer.getCards().get(selectionChar);
    LinkedList<Card> deck = uno.getDeck();
    LinkedList<Card> stack = uno.getStack();
    ListIterator playersIterator = uno.getPlayersIterator();
    
    if (selectedCard != null && selectedCard.getColor().equals(wildColor) || selectedCard.getColor().equals("Wild")) {
      stack.addFirst( turnPlayer.pickCard(selectionChar) );
      uno.turnSt = uno.normalTurnSt;

      switch(selectedCard.getNumber()) {
        case "Flip":
          if (uno.retrievePlayerSt instanceof NextPlayerSt) {
            uno.retrievePlayerSt = uno.prevPlayerSt;
            playersIterator.previous();
          } else {
            uno.retrievePlayerSt = uno.nextPlayerSt;
            playersIterator.next();
          }
          break;
        case "Block":
          uno.retrievePlayer();
          break;
        case "+2":
        case "+4":
        case "Wild":
          uno.setWildColor(Printer.askForWildColor());
          uno.turnSt = uno.postWildTurnSt;
          break;
        default:
          break;
      }

      uno.nextTurn();
      
    } else {
      // TODO: Display "this card doesn't exists" message
      uno.turn(turnPlayer);
    }
        
  }
  
  @Override
  public void turn(Player turnPlayer) {
    String selectionChar;
    int turnsCounter = uno.getTurnsCounter();
    LinkedList<Card> deck = uno.getDeck();
    LinkedList<Card> stack = uno.getStack();

    if (deck.size() == 0) 
      uno.refillDeck();

    if (turnsCounter == 1)
      Printer.printFirstTurn(turnPlayer);
    else   
      Printer.printTurn(turnPlayer, stack.getFirst());

    // Reading user input
    selectionChar = in.next();

    if (selectionChar.equals("pass") && turnsCounter > 1) 
      uno.pass();
    else if (selectionChar.equals("eat"))
      uno.eatCard(turnPlayer);
    else if (selectionChar.length() == 1)
      uno.playCard(turnPlayer, selectionChar);
    else 
      uno.turn(turnPlayer);    
  }
}