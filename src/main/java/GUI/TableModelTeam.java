package GUI;

import Model.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModelTeam extends AbstractTableModel {

    private ArrayList<Team> elencoTeam;

    private String[] nomiColonne = {"Nome Team", "Punti", "Hackathon"};

    public void setData(ArrayList<Team> data) {
        this.elencoTeam = data;
        fireTableDataChanged();
    }
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
            case 1: return t.getVoto();
            case 2: return t.getHackathon();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) { return nomiColonne[column]; }
}
