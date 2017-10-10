package game;

/**
 * Created by jansene on 9/28/17.
 */
public final class Card {

    private final int number;
    private final Color color;

    public Card(int number, Color color) {
        this.number = number;
        this.color = color;
    }

    public Card(Card other) {
        this.number = other.number;
        this.color = other.color;
    }

    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Card{" +
                "number=" + number +
                ", color=" + color +
                '}';
    }

    public enum Color {
        White(1), Red(2), Blue(3), Yellow(4), Green(5), Unknown(0);

        private int value;

        Color(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
