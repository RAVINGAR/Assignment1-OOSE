package com.ravingarinc.oose.assignment1.character;

import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.Symbol;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Player {
    private final List<Colour> keys;
    private final Position position;
    private List<String> messages;
    private String symbol;
    private boolean finished;

    public Player(int row, int column) {
        keys = new LinkedList<>();
        finished = false;
        position = new Position(row, column);
        symbol = Colour.WHITE + Symbol.UP.toString();
        messages = new LinkedList<>();
    }

    public boolean hasKey(Colour colour) {
        return keys.contains(colour);
    }

    public void addKey(Colour colour) {
        if(!keys.contains(colour)) {
            keys.add(colour);
        }
        /*
        Only add the key if the player doesn't already have it.
        We do this so that when displaying the hotbar, it will only show a maximum
        of 6 keys and won't overflow the terminal line.
         */
    }

    public void finishGame() {
        finished = true;
    }

    public boolean isFinished() { return finished; }

    public void move(Direction direction) {
        symbol = Colour.WHITE + direction.getSymbol();
        position.move(direction);
    }

    public int row() { return position.row(); }
    public int column() { return position.column(); }

    public void sendMessage(String message) {
        messages.add(message);
    }

    public String getSymbol() { return symbol; }

    public String getHotbar() {
        if(keys.isEmpty()) {
            return "";
        }
        else {
            StringBuilder builder = new StringBuilder();
            builder.append("Backpack | ");
            keys.forEach(k -> {
                builder.append(k.toString());
                builder.append(Symbol.KEY);
                builder.append(" ");
            });
            return builder.toString();
        }
    }

    public String getMessage() {
        if(messages.isEmpty()) {
            return "";
        }
        //Slightly overly complicated code to get good formatting..
        StringBuilder builder = new StringBuilder();
        List<String> keyMessages = new LinkedList<>();

        Iterator<String> iterator = new LinkedList<>(messages).iterator();
        while(iterator.hasNext()) {
            String m = iterator.next();
            if(m.contains("key") && m.contains("\033[")) {
                keyMessages.add(m);
                messages.remove(m);
            }
        }
        messages.forEach(m -> {
            builder.append(m).append("\n");
        });

        iterator = keyMessages.iterator();

        if(!keyMessages.isEmpty()) {
            builder.append("You have found a magical ");
            String prev = null;
            while(iterator.hasNext()) {
                String key = iterator.next();
                if(key.equals(prev)) {
                    builder.append("another ");
                }
                else if(!key.equals(keyMessages.get(0))) {
                    builder.append("a ");
                }
                builder.append(key);
                if(iterator.hasNext()) {
                    builder.append(" AND ");
                    prev = key;
                }
                else {
                    builder.append("!\n");
                }
            }
        }

        messages = new LinkedList<>(); //Reset messages for next movement

        return builder.toString();
    }
}
