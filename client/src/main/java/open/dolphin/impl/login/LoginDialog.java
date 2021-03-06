package open.dolphin.impl.login;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.client.AutoRomanListener;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIFactory;
import open.dolphin.delegater.UserDelegater;
import open.dolphin.helper.SimpleWorker;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.Project;

/**
 * ログインダイアログ　クラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class LoginDialog extends AbstractLoginDialog {
    
    private LoginPanel view;
    //private SignInPanel view;
    private StateMgr stateMgr;
    private boolean loginFlag;

    
    /** Creates new LoginService */
    public LoginDialog() {
    }
    
    @Override
    protected void tryLogin() {
        
        if(loginFlag) {
            return;
        }else{
            loginFlag = true;
        }
        
        // User 情報を取得するためのデリゲータを得る
        if (userDlg == null) {
            userDlg = new UserDelegater();
        }
        
        // トライ出来る最大回数を得る
        if (maxTryCount == 0) {
            maxTryCount = ClientContext.getInt("loginDialog.maxTryCount");
        }
        
        java.util.ResourceBundle bundle = ClientContext.getMyBundle(LoginDialog.class);
        
        java.util.logging.Logger.getLogger(this.getClass().getName()).info(bundle.getString("message.startAuthentication"));
        
        // 試行回数 += 1
        tryCount++;

        //Task
        worker = new SimpleWorker<UserModel, Void>() {

            @Override
            protected UserModel doInBackground() throws Exception {
                String fid = Project.getFacilityId();
                String uid = view.getUserIdField().getText().trim();
                String password = new String(view.getPasswordField().getPassword());
                UserModel userModel = userDlg.login(fid, uid, password);
                return userModel;
            }
            
            @Override
            protected void succeeded(UserModel userModel) {
                
                if (userModel!=null) { 
                    // 5分間テストの場合、有効期間をテストする
                    if (isTestUser(userModel) && isExpired(userModel)) {
                        // 評価終了
                        showTestExpiredError();
                        setResult(LoginStatus.NOT_AUTHENTICATED); 
                        
                    } else {
                    
                        // 認証成功
                        String time = ModelUtils.getDateTimeAsString(new Date());
                        StringBuilder sb = new StringBuilder();
                        sb.append(time).append(":");
                        sb.append(userModel.getUserId()).append(bundle.getString("text.hasLoggedIn"));
                        java.util.logging.Logger.getLogger(this.getClass().getName()).info(sb.toString());
                        
                        //----------------------------------
                        // ユーザモデルを ProjectStub へ保存する
                        //----------------------------------
                        Project.getProjectStub().setUserModel(userModel);
//s.oh^ 2014/07/08 クラウド0対応
                        if(!checkCloudZero()) {
                            Project.getProjectStub().setUserModel(null);
                            loginFlag = false;
                            return;
                        }
//s.oh$
                        Project.getProjectStub().setFacilityId(userModel.getFacilityModel().getFacilityId());
                        Project.getProjectStub().setUserId(userModel.idAsLocal());  // facilityId無し

                        setResult(LoginStatus.AUTHENTICATED);
                        
//s.oh^ 2014/03/13 傷病名削除診療科対応
                        getOrcaDeptInfo();
//s.oh$
                        
                    }
                     
                } else {
                    if (tryCount <= maxTryCount) {
                        showUserIdPasswordError();

                    } else {
                        showTryOutError();
                        setResult(LoginStatus.NOT_AUTHENTICATED);
                    }
                    loginFlag = false;
                }
//                Enumeration<NetworkInterface> nics;
//                try {
//                    nics = NetworkInterface.getNetworkInterfaces();
//                    while(nics.hasMoreElements()) {
//                        NetworkInterface ni = nics.nextElement();
//                        System.out.println("Name : " + ni.getName());
//                        System.out.println("Display name : " + ni.getDisplayName());
//                        byte[] addr = ni.getHardwareAddress();
//                        StringBuilder sb = new StringBuilder();
//                        if(addr != null) {
//                            for(byte b : addr) {
//                                sb.append(String.format("%02X ", b));
//                            }
//                        }
//                        System.out.println("Hardware address : " + sb.toString());
//                    }
//                } catch (SocketException ex) {
//                    Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            
            @Override
            protected void failed(java.lang.Throwable cause) {
                java.util.logging.Logger.getLogger(this.getClass().getName()).warning(bundle.getString("TASK FAILED"));
                java.util.logging.Logger.getLogger(this.getClass().getName()).warning(cause.getCause().getMessage());
                java.util.logging.Logger.getLogger(this.getClass().getName()).warning(cause.getMessage());

                if (tryCount <= maxTryCount) {
                    //showMessageDialog(cause.getMessage());
                    showMessageDialog(bundle.getString("error.invalidLogiInfo"));
                    
                } else {
                    showTryOutError();
                    setResult(LoginStatus.NOT_AUTHENTICATED);
                }
                loginFlag = false;
            }
        };
        
        worker.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    setBusy(false);
                    worker.removePropertyChangeListener(this);
                } else if (SwingWorker.StateValue.STARTED == evt.getNewValue()) {
                    setBusy(true);
                }
            }  
        });
        
        worker.execute();
    }
    
    /**
     * GUI を構築する。
     * @return      */
    @Override
    protected JPanel createComponents() {

        view = new LoginPanel();
        //view = new SignInPanel();
        view.getCancelBtn().setText(GUIFactory.getCancelButtonText());
        // イベント接続を行う
        connect();
        
////s.oh^ RSS対応
//        view.getLogoLabel().addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if(e.getClickCount() == 2) {
//                    showRSSInfo();
//                }
//            }
//            @Override
//            public void mousePressed(MouseEvent e) {}
//            @Override
//            public void mouseReleased(MouseEvent e) {}
//            @Override
//            public void mouseEntered(MouseEvent e) {}
//            @Override
//            public void mouseExited(MouseEvent e) {}
//        });
////s.oh$

        return view;
    }
    
    /**
     * イベント接続を行う。
     */
    private void connect() {
        
        // Mediator ライクな StateMgr
        stateMgr = new StateMgr();
        
        // フィールドにリスナを登録する
        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                stateMgr.checkButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                stateMgr.checkButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                stateMgr.checkButtons();
            }
        };

        JTextField userIdField = view.getUserIdField();
        userIdField.getDocument().addDocumentListener(dl);
        userIdField.addFocusListener(AutoRomanListener.getInstance());
        userIdField.addActionListener((ActionEvent e) -> {
            stateMgr.onUserIdAction();
        });
        
        JPasswordField passwdField = view.getPasswordField();
        passwdField.getDocument().addDocumentListener(dl);
        passwdField.addFocusListener(AutoRomanListener.getInstance());
        passwdField.addActionListener((ActionEvent e) -> {
            stateMgr.onPasswordAction();
        });
    }

    @Override
    protected JButton getLoginButton() {
        return view.getLoginBtn();
    }

    @Override
    protected JButton getCancelButton() {
        return view.getCancelBtn();
    }

    @Override
    protected JButton getSettingButton() {
        return view.getSettingBtn();
    }

    @Override
    protected JProgressBar getProgressBar() {
        return view.getProgressBar();
    }
    
    /**
     * モデルを表示する。
     */
    @Override
    protected void doWindowOpened() {
        String uid = Project.getUserId();
        if (uid != null && (!uid.equals(""))) {
            view.getUserIdField().setText(uid);
            view.getPasswordField().requestFocus();
        }
    }
    
    /**
     * 設定ダイアログから通知を受ける。
     * 有効なプロジェクトでればユーザIDをフィールドに設定しパスワードフィールドにフォーカスする。
     * @param newValue
     **/
    @Override
    public void setNewParams(Boolean newValue) {             
        boolean valid = newValue;
        if (valid) {
            doWindowOpened();
        }
    }
    
    /**
     * ログインボタンを制御する簡易 StateMgr クラス。
     */
    class StateMgr  {
        
        private boolean okState;
        
        public StateMgr() {
        }
        
        /**
         * ログインボタンの enable/disable を制御する。
         */
        public void checkButtons() {
            boolean newOKState = true;
            newOKState = newOKState &&  (!view.getUserIdField().getText().equals(""));
            newOKState = newOKState && (view.getPasswordField().getPassword().length >0);
            
            if (newOKState != okState) {
                view.getLoginBtn().setEnabled(newOKState);
                okState = newOKState;
            }
        }
        
        /**
         * UserId フィールドでリターンきーが押された時の処理を行う。
         */
        public void onUserIdAction() {
            view.getPasswordField().requestFocus();
        }
        
        /**
         * Password フィールドでリターンきーが押された時の処理を行う。
         */
        public void onPasswordAction() {
            if (view.getUserIdField().getText().equals("")) {
                view.getUserIdField().requestFocus();
                
            } else if (view.getPasswordField().getPassword().length != 0 && okState) {
                view.getLoginBtn().doClick();
            }
        }
    }
}