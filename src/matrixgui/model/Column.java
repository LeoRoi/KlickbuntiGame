package matrixgui.model;

import java.util.List;

/**
 * one column from the game field
 */
public class Column {

    private int columnNumber;
    private List <Data> listOfBoxes;

    public Column(int columnNumber, List<Data> listOfBoxes) {
        this.columnNumber = columnNumber;
        this.listOfBoxes = listOfBoxes;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public List<Data> getListOfBoxes() {
        return listOfBoxes;
    }
}
