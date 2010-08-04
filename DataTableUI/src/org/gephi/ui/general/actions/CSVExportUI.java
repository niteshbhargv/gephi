/*
Copyright 2008 WebAtlas
Authors : Mathieu Bastian, Mathieu Jacomy, Julian Bilcke, Eduardo Ramos
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.ui.general.actions;

import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * UI for selecting CSV export options.
 * @author Eduardo Ramos <eduramiba@gmail.com>
 */
public class CSVExportUI extends javax.swing.JPanel {
    private JTable table;
    private JCheckBox[] columnsCheckBoxes;

    /** Creates new form CSVExportUI */
    public CSVExportUI(JTable table) {
        initComponents();
        this.table=table;
        separatorComboBox.addItem(new SeparatorWrapper((','), getMessage("CSVExportUI.comma")));
        separatorComboBox.addItem(new SeparatorWrapper((';'), getMessage("CSVExportUI.semicolon")));
        separatorComboBox.addItem(new SeparatorWrapper(('\t'), getMessage("CSVExportUI.tab")));
        separatorComboBox.addItem(new SeparatorWrapper((' '), getMessage("CSVExportUI.space")));
        refreshColumns();
    }

    private void refreshColumns() {
        TableModel model = table.getModel();
        columnsCheckBoxes = new JCheckBox[model.getColumnCount()];
        columnsPanel.removeAll();
        columnsPanel.setLayout(new MigLayout("", "[pref!]"));
        for (int i = 0; i < model.getColumnCount(); i++) {
            columnsCheckBoxes[i] = new JCheckBox(model.getColumnName(i), true);
            columnsPanel.add(columnsCheckBoxes[i], "wrap");
        }
        columnsPanel.revalidate();
        columnsPanel.repaint();
    }

    public String getDisplayName() {
        return getMessage("CSVExportUI.title");
    }

    public Character getSelectedSeparator() {
        Object item = separatorComboBox.getSelectedItem();
        if (item instanceof SeparatorWrapper) {
            return ((SeparatorWrapper) item).separator;
        } else {
            return item.toString().charAt(0);
        }
    }

    public Integer[] getSelectedColumnsIndexes(){
        ArrayList<Integer> columnsIndexes=new ArrayList<Integer>();
        for (int i = 0; i < columnsCheckBoxes.length; i++) {
            if(columnsCheckBoxes[i].isSelected()){
                columnsIndexes.add(i);
            }
        }
        return columnsIndexes.toArray(new Integer[0]);
    }

    class SeparatorWrapper {

        private Character separator;
        private String displayText;

        public SeparatorWrapper(Character separator) {
            this.separator = separator;
        }

        public SeparatorWrapper(Character separator, String displayText) {
            this.separator = separator;
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            if (displayText != null) {
                return displayText;
            } else {
                return String.valueOf(separator);
            }
        }
    }

    private String getMessage(String resName) {
        return NbBundle.getMessage(CSVExportUI.class, resName);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        separatorLabel = new javax.swing.JLabel();
        separatorComboBox = new javax.swing.JComboBox();
        scroll = new javax.swing.JScrollPane();
        columnsPanel = new javax.swing.JPanel();
        columnsLabel = new javax.swing.JLabel();

        separatorLabel.setText(org.openide.util.NbBundle.getMessage(CSVExportUI.class, "CSVExportUI.separatorLabel.text")); // NOI18N

        columnsPanel.setLayout(new java.awt.GridLayout());
        scroll.setViewportView(columnsPanel);

        columnsLabel.setText(org.openide.util.NbBundle.getMessage(CSVExportUI.class, "CSVExportUI.columnsLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(separatorLabel)
                        .addGap(18, 18, 18)
                        .addComponent(separatorComboBox, 0, 104, Short.MAX_VALUE))
                    .addComponent(columnsLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(separatorLabel)
                    .addComponent(separatorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(columnsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel columnsLabel;
    private javax.swing.JPanel columnsPanel;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JComboBox separatorComboBox;
    private javax.swing.JLabel separatorLabel;
    // End of variables declaration//GEN-END:variables
}
