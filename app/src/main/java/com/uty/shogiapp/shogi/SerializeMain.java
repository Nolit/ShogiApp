package com.uty.shogiapp.shogi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializeMain {
    public static void main(String[] args){
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(new FileOutputStream("Board.obj"));
            Player sente = new Player("先手");
            Player gote = new Player("後手");
            Board board = new Board(sente,gote);
            sente.move(33, 43);
            out.writeObject(board);
            out.flush();
            out.close();

            ObjectInputStream in=new ObjectInputStream(new FileInputStream("Board.obj"));
            Board outBoard=(Board)in.readObject();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(SerializeMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerializeMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
