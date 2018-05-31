package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.Player;
import it.polimi.ingsw.board.dice.*;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.Restriction;
import it.polimi.ingsw.board.windowpattern.WindowPattern;
import it.polimi.ingsw.client.ClientInterface;

import java.util.ArrayList;

public class ObjectsFactory {

    public static Dice DiceFactory(int value, Color color) {
        return new Dice(value, color);
    }


    public static Cell CellFactory(Restriction restriction, Dice d)
    {
        Dice d2;
        d2 = DiceFactory(d.getValue(), d.getColor());
        Cell c = new Cell(restriction);
        if(!restriction.equals(null)){
        c.putDice(d2);}
        return c;
    }

    public static WindowPattern WindowPatternFactory(String name, int difficulty, Cell c1, Cell c2, Cell c3, Cell c4, Cell c5, Cell c6, Cell c7, Cell c8, Cell c9, Cell c10, Cell c11, Cell c12, Cell c13, Cell c14, Cell c15, Cell c16, Cell c17, Cell c18, Cell c19, Cell c20 ) throws Exception{

        Cell[][] cells = new Cell[4][5];
        
        cells[0][0]  = CellFactory(c1.getRestriction(),c1.getDice());
        cells[0][1]  = CellFactory(c2.getRestriction(), c2.getDice());
        cells[0][2]  = CellFactory(c3.getRestriction(), c3.getDice());
        cells[0][3]  = CellFactory(c4.getRestriction(), c4.getDice());
        cells[0][4]  = CellFactory(c5.getRestriction(), c5.getDice());
        cells[1][0]  = CellFactory(c6.getRestriction(), c6.getDice());
        cells[1][1]  = CellFactory(c7.getRestriction(), c7.getDice());
        cells[1][2]  = CellFactory(c8.getRestriction(), c8.getDice());
        cells[1][3]  = CellFactory(c9.getRestriction(), c9.getDice());
        cells[1][4]  = CellFactory(c10.getRestriction(), c10.getDice());
        cells[2][0]  = CellFactory(c11.getRestriction(), c11.getDice());
        cells[2][1]  = CellFactory(c12.getRestriction(), c12.getDice());
        cells[2][2]  = CellFactory(c13.getRestriction(), c13.getDice());
        cells[2][3]  = CellFactory(c14.getRestriction(), c14.getDice());
        cells[2][4]  = CellFactory(c15.getRestriction(), c15.getDice());
        cells[3][0]  = CellFactory(c16.getRestriction(), c16.getDice());
        cells[3][1]  = CellFactory(c17.getRestriction(), c17.getDice());
        cells[3][2]  = CellFactory(c18.getRestriction(), c18.getDice());
        cells[3][3]  = CellFactory(c19.getRestriction(), c19.getDice());
        cells[3][4]  = CellFactory(c20.getRestriction(), c20.getDice());
        WindowPattern wp = new WindowPattern(name, difficulty, cells);
        return wp;
    }

    public static Player PlayerFactory(ClientInterface ci, String username, String sessionID, boolean state, WindowPattern wp, int favourTokens){
        if(state) {//player is playing
            Player p = new Player(ci,username, sessionID);
            try {
                WindowPattern wp2 = WindowPatternFactory(wp.getName(), wp.getDifficulty(), wp.getCell(0, 0), wp.getCell(0, 1), wp.getCell(0, 2), wp.getCell(0, 3), wp.getCell(0, 4), wp.getCell(1, 0), wp.getCell(1, 1), wp.getCell(1, 2), wp.getCell(1, 3), wp.getCell(1, 4), wp.getCell(2, 0), wp.getCell(2, 1), wp.getCell(2, 2), wp.getCell(2, 3), wp.getCell(2, 4), wp.getCell(3, 0), wp.getCell(3, 1), wp.getCell(3, 2), wp.getCell(3, 3), wp.getCell(3, 4));
                p.setWindowPattern(wp2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.setFavourTokens(favourTokens);
            return p;
        }
        else { //player dosen't play
            Player p2 = new Player(ci, username, sessionID);
            p2.setIsOnline(false);
            return p2;
        }
    }

    public static Player curPlayer(int index, Player p){
        Player pl = PlayerFactory(p.getClientInterface(), p.getPlayerName(), p.getSessionID(), p.getIsOnline(), p.getWindowPattern(), p.getFavourTokens());
        //p1.setIndex(index);
        return pl;
    }

   // public static void updateRoundTrackDices(ArrayList<Dice> dices, int Round, RoundTrack rt) {
     //       for(Dice d : dices){
       //         Dice d

        //}
    //}

    //player receive the dices in the Draft
    public static ArrayList<Dice> sendDicesInDraft(Draft Draft){
            ArrayList<Dice> d = new ArrayList();
            for(int j=0; j <Draft.getSize(); j++){
                Dice dice = DiceFactory(Draft.getDices().get(j).getValue(),Draft.getDices().get(j).getColor());
                d.add(dice);
        }
        return d;
    }

}
