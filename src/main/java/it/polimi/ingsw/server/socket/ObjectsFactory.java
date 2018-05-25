package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.board.Color;
import it.polimi.ingsw.board.dice.Dice;
import it.polimi.ingsw.board.dice.DiceBag;
import it.polimi.ingsw.board.windowpattern.Cell;
import it.polimi.ingsw.board.windowpattern.WindowPattern;

public class ObjectsFactory {
    public static Dice DiceFactory(int value, Color color) {
        return new Dice(value, color);
    }


    public static Cell CellFactory(Object restriction, Dice d)
    {
        Dice d2;
        d2 = DiceFactory(d.getValue(), d.getColor());
        Cell c = new Cell(restriction);
        c.putDice(d2);
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


}
