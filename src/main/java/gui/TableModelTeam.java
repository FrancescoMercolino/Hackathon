package GUI;

import Model.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModelTeam extends AbstractTableModel {

    private ArrayList<Team> elencoTeam;

    private String[] nomiColonne = {"Nome", "Dimensione", "Punti"};

    public void setData(ArrayList<Team> data) { this.elencoTeam = data; }
    @Override
    public int getRowCount() {
        if(elencoTeam != null)
            return elencoTeam.size();
        return 0;
    }

    @Override
    public int getColumnCount() {
        return nomiColonne.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Team t = elencoTeam.get(rowIndex);

        switch(columnIndex) {
            case 0: return t.getNome();
            case 1: return t.getDimensioneMassima();
            case 2: return t.getPunti();
           // case 3: return t.getPosizione();
        }

        return null;
    }

    @Override
    public String getColumnName(int column) { return nomiColonne[column]; }
}
