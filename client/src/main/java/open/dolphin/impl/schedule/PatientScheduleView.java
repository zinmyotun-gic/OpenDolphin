/*
 * PatientSearchView.java
 *
 * Created on 2007/11/22, 18:43
 */

package open.dolphin.impl.schedule;

import open.dolphin.impl.psearch.*;

/**
 * (予定カルテ対応)
 * @author  kazushi
 */
public class PatientScheduleView extends javax.swing.JPanel {
   
    
    /** Creates new form PatientSearchView */
    public PatientScheduleView() {
        initComponents();
    }

    public javax.swing.JLabel getCountLbl() {
        return countLbl;
    }

    public javax.swing.JTable getTable() {
        return table;
    }

    public javax.swing.JTextField getKeywordFld() {
        return keywordFld;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new AddressTipsTable();
        keywordFld = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        countLbl = new javax.swing.JLabel();
        rpButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        claimChk = new javax.swing.JCheckBox();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        keywordFld.setToolTipText("右クリックで予定日を選択します。");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("予定日:");

        countLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        countLbl.setText("0 件");

        rpButton.setText("処方適用");
        rpButton.setToolTipText("前回処方を適用し予定日のカルテを作成します。");

        updateButton.setText("更 新");
        updateButton.setToolTipText("予定リストを更新します。");

        claimChk.setText("CLAIM送信");
        claimChk.setToolTipText("カルテの作成と同時にORCAへ送信します。");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(keywordFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(countLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(updateButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(rpButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(claimChk)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(keywordFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(countLbl)
                    .add(updateButton)
                    .add(rpButton)
                    .add(claimChk))
                .add(7, 7, 7)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox claimChk;
    private javax.swing.JLabel countLbl;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField keywordFld;
    private javax.swing.JButton rpButton;
    private javax.swing.JTable table;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JButton getRpButton() {
        return rpButton;
    }

    public javax.swing.JCheckBox getClaimChk() {
        return claimChk;
    }

    public javax.swing.JButton getUpdateButton() {
        return updateButton;
    }
}