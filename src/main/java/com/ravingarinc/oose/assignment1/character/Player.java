package com.ravingarinc.oose.assignment1.character;

import com.ravingarinc.oose.assignment1.maze.Colour;
import com.ravingarinc.oose.assignment1.maze.Direction;
import com.ravingarinc.oose.assignment1.maze.Position;
import com.ravingarinc.oose.assignment1.maze.Symbol;
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
        keys.add(colour);
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

    public String getMessage() {
        if(messages.isEmpty()) {
            return "Use W, S, A and D to move. Try and reach the end at the '" + Symbol.END + "' if you can!";
        }
        //Slightly overly complicated code to get good formatting..
        StringBuilder builder = new StringBuilder();
        String lastMessage = null;
        List<String> leftoverMessages = new LinkedList<>(messages);

        for(String message : messages) {
            if(message.contains("key") && (lastMessage != null && lastMessage.contains("key"))) {
                builder.append(lastMessage.substring(0, lastMessage.length()-1));
                builder.append(" AND ");
                int colourIdx = message.indexOf("\033[");
                if(message.substring(colourIdx+3).equals(lastMessage.substring(colourIdx+3))) {
                    builder.append("another ");
                }
                else {
                    builder.append("a ");
                }
                builder.append(message.substring(colourIdx));
                builder.append("\n");
                leftoverMessages.remove(lastMessage);
                leftoverMessages.remove(message);
                lastMessage = null;
            }
            else {
                lastMessage = message;
            }
        }

        leftoverMessages.forEach(message -> {
            builder.append(message).append("\n");
        });
        messages = new LinkedList<>();

        return builder.toString();
    }
}
