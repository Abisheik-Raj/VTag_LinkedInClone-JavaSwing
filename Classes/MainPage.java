package Classes;
import javax.imageio.ImageIO;
import javax.print.URIException;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com.mysql.cj.exceptions.RSAException;
import com.mysql.cj.protocol.Resultset;

import java.awt.*;
    import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
    import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.net.URI;
import java.net.URISyntaxException;

import Entities.User;
    import CustomizedComponents.*;
    public class MainPage {
        String email;
        String username;
        String password;

        //Frames
        JFrame mainPageFrame;
        JFrame chatFrame;
        //Labels
        JLabel profileDPLabel;
        JLabel profileNameLabel;
        JLabel profileEmailLabel;
        JLabel postsLabel;
        JLabel homeLabel;
        JLabel socialLabel;
        JLabel friendsLabel;
        JLabel newsLabel;
        JLabel messagingLabel;
        RoundedLabel sharesomethingLabel;
        RoundedLabel searchLabel;
        RoundedLabel searchLabelForMessaging;
        RoundedLabel chatContentLabel;
        JLabel fileIconLabel;
        JLabel imageIconLabel;
        JLabel postIconLabel;
        JLabel sendChatLabel;
        //Panels
        JPanel mainPanel;
        JPanel friendsListPanel;
        JPanel notificationsListPanel;
        JPanel messagingListPanel;
        RoundedPanel LSPanel;
        RoundedPanel RSPanelForPosts;
        RoundedPanel RSPanelForFriends;
        RoundedPanel RSPanelforMessaging;
        JPanel newsPanel;
        RoundedPanel postPanel;
        RoundedPanel searchPanel;
        RoundedPanel searchPanelForMessaging;
        //Buttons
        RoundedButton postsButton;
        RoundedButton friendsButton;
        RoundedButton messagingButton;
        RoundedButton logoutButton;
        // RoundedButton 

        //Textfields
        JTextField postTextField;
        JTextField searchTextField;
        JTextField searchTextFieldForMessaging;
        JTextField chatContentTextField;

        Statement statement;
        Connection connection;
        //Booleans
        boolean homeActive = true;
        boolean socialActive = false;
        boolean postsActive = true;
        boolean friendsActive = false;
        boolean messagingActive = false;
        boolean chatThreadActive = false;

        byte[] imageData;
        int colorNumber = 1;
        Desktop desktop = Desktop.getDesktop();
        String toFriend;

        public MainPage(User user) throws Exception{
            this.email = user.email;
            this.username = user.username;
            this.password = user.password;
            System.out.println(username + " " + email + " " + password);

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root");
            statement = connection.createStatement();

            statement.execute("create database if not exists VTagDatabase");
            statement.execute("use VTagDatabase");
            statement.execute("create table if not exists "  + username + " (post TEXT,image LONGBLOB)");
            statement.execute("create table if not exists friendsof"  + username + " (friends varchar(100),email varchar(100))");
            statement.execute("create table if not exists notificationsof"  + username + " (friends varchar(100),email varchar(100))");
            statement.execute("create table if not exists requestsby"  + username + " (friends varchar(100),email varchar(100))");

            mainPageFrame = new JFrame();
            mainPageFrame.setTitle("VTag-MainPage");
            mainPageFrame.setSize(1300,750);
            mainPageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPageFrame.setLocationRelativeTo(null);
            mainPageFrame.setResizable(false);
            ImageIcon VTagIcon = new ImageIcon("Images\\VTagIcon.png");
            mainPageFrame.setIconImage(VTagIcon.getImage());
            mainPageFrame.getContentPane().setBackground(Color.white);

            LSPanel = new RoundedPanel(40,Color.white,new Color(243,241,241));
            LSPanel.setBounds(10,30,240,600);

            profileDPLabel = new JLabel();
            profileDPLabel.setIcon(new ImageIcon("Images\\profileDPImageBoy.png"));
            profileDPLabel.setBounds(90,20,64,70);

            profileNameLabel = new JLabel(username,SwingConstants.CENTER);
            profileNameLabel.setFont(new Font("Helvetica",Font.BOLD,15));
            profileNameLabel.setBounds(0,100,240,20);

            profileEmailLabel = new JLabel(email,SwingConstants.CENTER);
            profileEmailLabel.setFont(new Font("Helvetica",Font.BOLD,12));
            profileEmailLabel.setBounds(0,120,240,20);
            profileEmailLabel.setForeground(Color.gray);
            
            postsButton = new RoundedButton("Posts",10,Color.BLACK,Color.black);
            postsButton.setForeground(Color.white);
            postsButton.setBackground(Color.black);
            postsButton.setIcon(new ImageIcon("Images\\postsButtonIconWhite.png"));
            postsButton.setBounds(20,170,200,40);
            postsButton.setBorder(new LineBorder(Color.white,1));

            postsButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    if(!postsActive){
                        postsButton.setForeground(Color.white);
                        postsButton.setBackground(Color.black);
                        postsButton.setIcon(new ImageIcon("Images\\postsButtonIconWhite.png"));}
                }
                public void mouseExited(MouseEvent e){
                    if(!postsActive){
                        postsButton.setForeground(Color.black);
                        postsButton.setBackground(Color.white);
                        postsButton.setIcon(new ImageIcon("Images\\postsButtonIconBlack.png"));}
                }
                public void mouseClicked(MouseEvent e){
                        chatThreadActive = false;
                        postsActive = true;
                        friendsActive = false;
                        messagingActive = false;
                        postsButton.setForeground(Color.white);
                        postsButton.setBackground(Color.black);
                        postsButton.setIcon(new ImageIcon("Images\\postsButtonIconWhite.png"));
                        friendsButton.setForeground(Color.black);
                        friendsButton.setBackground(Color.white);
                        friendsButton.setIcon(new ImageIcon("Images\\friendsButtonIconBlack.png"));
                        messagingButton.setForeground(Color.black);
                        messagingButton.setBackground(Color.white);   
                        messagingButton.setIcon(new ImageIcon("Images\\settingsButtonIconBlack.png"));  
                        
                        RSPanelForPosts.setVisible(true);
                        RSPanelForFriends.setVisible(false);
                        RSPanelforMessaging.setVisible(false);   
                        searchTextField.setText("");  

                }
            });

            ResultSet totalNotifications = statement.executeQuery("select * from notificationsof" + username);
            int x = 0; 
            while(totalNotifications.next()){x++;}

            if(x==0) friendsButton = new RoundedButton("Friends",10,Color.black,Color.black);
            else     friendsButton = new RoundedButton("Friends " + x,10,Color.black,Color.black);
            friendsButton.setForeground(Color.black);
            friendsButton.setBackground(Color.white);
            friendsButton.setIcon(new ImageIcon("Images\\friendsButtonIconBlack.png"));
            friendsButton.setBounds(20,220,200,40);
            friendsButton.setBorder(new LineBorder(Color.white,1));

            friendsButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    if(!friendsActive){
                        friendsButton.setForeground(Color.white);
                        friendsButton.setBackground(Color.black);
                        friendsButton.setIcon(new ImageIcon("Images\\friendsButtonIconWhite.png"));
                    }
                }
                public void mouseExited(MouseEvent e){
                    if(!friendsActive){
                        friendsButton.setForeground(Color.black);
                        friendsButton.setBackground(Color.white);
                        friendsButton.setIcon(new ImageIcon("Images\\friendsButtonIconBlack.png"));
                    }
                }
                public void mouseClicked(MouseEvent e){
                    chatThreadActive = false;
                    friendsActive = true;
                    postsActive = false;
                    messagingActive = false;
                    friendsButton.setText("Friends");
                    friendsButton.setForeground(Color.white);
                    friendsButton.setBackground(Color.black);
                    friendsButton.setIcon(new ImageIcon("Images\\friendsButtonIconWhite.png"));
                    postsButton.setForeground(Color.black);
                    postsButton.setBackground(Color.white);
                    postsButton.setIcon(new ImageIcon("Images\\postsButtonIconBlack.png"));
                    messagingButton.setForeground(Color.black);
                    messagingButton.setBackground(Color.white);   
                    messagingButton.setIcon(new ImageIcon("Images\\settingsButtonIconBlack.png"));

                    RSPanelForFriends.setVisible(true);
                    friendsListPanel.removeAll();
                    friendsListPanel.repaint();
                    try {
                        updateNotifications();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    RSPanelForPosts.setVisible(false);
                    RSPanelforMessaging.setVisible(false);
                }

            });

            messagingButton = new RoundedButton("Messaging",10,Color.black,Color.black);
            messagingButton.setForeground(Color.black);
            messagingButton.setBackground(Color.white);
            messagingButton.setIcon(new ImageIcon("Images\\settingsButtonIconBlack.png"));
            messagingButton.setBounds(20,270,200,40);
            messagingButton.setBorder(new LineBorder(Color.white));

            messagingButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    if(!messagingActive){
                        messagingButton.setForeground(Color.white);
                        messagingButton.setBackground(Color.black);
                        messagingButton.setIcon(new ImageIcon("Images\\settingsButtonIconWhite.png"));
                    }
                }
                public void mouseExited(MouseEvent e){
                    if(!messagingActive){
                        messagingButton.setForeground(Color.black);
                        messagingButton.setBackground(Color.white);
                        messagingButton.setIcon(new ImageIcon("Images\\settingsButtonIconBlack.png"));
                    }
                }
                public void mouseClicked(MouseEvent e){
                    messagingActive = true;
                    friendsActive = false;
                    postsActive = false;
                    messagingButton.setForeground(Color.white);
                    messagingButton.setBackground(Color.black);
                    messagingButton.setIcon(new ImageIcon("Images\\settingsButtonIconWhite.png")); 
                    friendsButton.setForeground(Color.black);
                    friendsButton.setBackground(Color.white);
                    friendsButton.setIcon(new ImageIcon("Images\\friendsButtonIconBlack.png"));
                    postsButton.setForeground(Color.black);
                    postsButton.setBackground(Color.white);     
                    postsButton.setIcon(new ImageIcon("Images\\postsButtonIconBlack.png"));

                    RSPanelforMessaging.setVisible(true);
                    try {
                        updateMessaging();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    messagingListPanel.removeAll();
                    messagingListPanel.repaint();
                    try {
                        updateMessaging();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    RSPanelForPosts.setVisible(false);
                    RSPanelForFriends.setVisible(false);
                }

            });

            logoutButton = new RoundedButton("Logout",10,Color.black,Color.black);
            logoutButton.setForeground(Color.black);
            logoutButton.setBackground(Color.white);
            logoutButton.setIcon(new ImageIcon("Images\\logoutButtonBlack.png"));
            logoutButton.setBounds(20,320,200,40);
            logoutButton.setBorder(new LineBorder(Color.white));

            logoutButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                        logoutButton.setForeground(Color.white);
                        logoutButton.setBackground(Color.black);
                        logoutButton.setIcon(new ImageIcon("Images\\logoutButtonWhite.png"));
                }
                public void mouseExited(MouseEvent e){
                        logoutButton.setForeground(Color.black);
                        logoutButton.setBackground(Color.white);
                        logoutButton.setIcon(new ImageIcon("Images\\logoutButtonBlack.png"));
                }
                public void mouseClicked(MouseEvent e){
                    mainPageFrame.dispose();
                    try {
                        new MainMenu();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

            });

        
            //Adding to profileDPPanel
            
            LSPanel.add(profileDPLabel);
            LSPanel.add(profileNameLabel);
            LSPanel.add(profileEmailLabel);
            LSPanel.add(postsButton);
            LSPanel.add(friendsButton);
            LSPanel.add(messagingButton);
            LSPanel.add(logoutButton);
            LSPanel.setLayout(null);


            RSPanelForPosts = new RoundedPanel(40,Color.white,Color.white);
            RSPanelForPosts.setBounds(260,30,525,600);

            postsLabel = new JLabel("Posts");
            postsLabel.setForeground(Color.black);
            postsLabel.setFont(new Font("Helvetica",Font.BOLD,20));
            postsLabel.setBounds(0,0,200,50);

            homeLabel = new JLabel("Home");
            homeLabel.setForeground(Color.white);
            homeLabel.setFont(new Font("Helvetica",Font.BOLD,13));
            homeLabel.setBounds(440,0,50,50);

            homeLabel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    if(!homeActive)
                        homeLabel.setForeground(Color.BLACK);
                }
                public void mouseExited(MouseEvent e){
                    if(!homeActive)
                        homeLabel.setForeground(Color.gray);
                }
                public void mouseClicked(MouseEvent e){
                    socialActive = false;
                    homeActive = true;
                    socialLabel.setForeground(Color.gray);
                    homeLabel.setForeground(Color.black);
                    mainPanel.removeAll();
                    try {
                        updatePosts();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mainPanel.repaint();
                }
            });

            socialLabel = new JLabel("Social");
            socialLabel.setForeground(Color.white);
            socialLabel.setFont(new Font("Helvetica",Font.BOLD,13));
            // socialLabel.setBounds(485,0,50,50);

            socialLabel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    if(!socialActive)
                        socialLabel.setForeground(Color.BLACK);
                }
                public void mouseExited(MouseEvent e){
                    if(!socialActive)
                        socialLabel.setForeground(Color.gray);
                }
                public void mouseClicked(MouseEvent e){
                    homeActive = false;
                    socialActive = true;
                    homeLabel.setForeground(Color.gray);
                    homeLabel.setBackground(Color.BLACK);
                    mainPanel.removeAll();
                    try {
                        updateSocialPosts();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }
            });

            postPanel = new RoundedPanel(30,new Color(243,241,241),new Color(243,241,241));
            postPanel.setBounds(0,50,525,120);

            sharesomethingLabel = new RoundedLabel(20, Color.white,Color.white);
            sharesomethingLabel.setText("       Share something");
            sharesomethingLabel.setForeground(Color.gray);
            sharesomethingLabel.setFont(new Font("Helvetica",Font.PLAIN,15));
            sharesomethingLabel.setBounds(20,15,485,40);

            fileIconLabel = new JLabel("File");
            fileIconLabel.setForeground(Color.gray);
            fileIconLabel.setIcon(new ImageIcon("Images\\flleIcon.png"));
            fileIconLabel.setBounds(170,60,50,50);

            fileIconLabel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    fileIconLabel.setForeground(Color.black);
                    fileIconLabel.setIcon(new ImageIcon("Images\\fileIconBlack.png"));
                }
                public void mouseExited(MouseEvent e){
                    fileIconLabel.setIcon(new ImageIcon("Images\\flleIcon.png"));
                    fileIconLabel.setForeground(Color.gray);
                }
                public void mouseClicked(MouseEvent e){
                    JFileChooser fileChooser = new JFileChooser();

                    int returnValue = fileChooser.showOpenDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        java.io.File selectedFile = fileChooser.getSelectedFile();
                        JOptionPane.showMessageDialog(mainPageFrame, "You selected: " + selectedFile.getAbsolutePath());

                        try {
                            FileInputStream fis = new FileInputStream(selectedFile);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                baos.write(buffer, 0, bytesRead);
                            }
                            imageData = baos.toByteArray();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainPageFrame, "No file selected.");
                    }
                }
            });

            imageIconLabel = new JLabel("Image");
            imageIconLabel.setForeground(Color.gray);
            imageIconLabel.setIcon(new ImageIcon("Images\\imageIcon.png"));
            imageIconLabel.setBounds(240,60,60,50);

            imageIconLabel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    imageIconLabel.setForeground(Color.black);
                    imageIconLabel.setIcon(new ImageIcon("Images\\imageIconBlack.png"));
                }
                public void mouseExited(MouseEvent e){
                    imageIconLabel.setIcon(new ImageIcon("Images\\\\imageIcon.png"));
                    imageIconLabel.setForeground(Color.gray);
                }
                public void mouseClicked(MouseEvent e){
                    JFileChooser fileChooser = new JFileChooser();

                    int returnValue = fileChooser.showOpenDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        java.io.File selectedFile = fileChooser.getSelectedFile();
                        JOptionPane.showMessageDialog(mainPageFrame, "You selected: " + selectedFile.getAbsolutePath());

                        try {
                            FileInputStream fis = new FileInputStream(selectedFile);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                baos.write(buffer, 0, bytesRead);
                            }
                            imageData = baos.toByteArray();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainPageFrame, "No file selected.");
                    }
                }
            });

            postTextField = new JTextField();
            postTextField.setText("Share something");
            postTextField.setBounds(40,20,440,30);
            postTextField.setBorder(new LineBorder(Color.white));
            postTextField.setForeground(Color.gray);
            postTextField.setFont(new Font("Helvetica",Font.PLAIN,15));
            postTextField.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    if(postTextField.getText().equals("Share something"))
                        postTextField.setText("");
                        postTextField.setForeground(Color.BLACK);
                }
            });

            postIconLabel = new JLabel("Post");
            postIconLabel.setForeground(Color.gray);
            postIconLabel.setIcon(new ImageIcon("Images\\send.png"));
            postIconLabel.setBounds(320,60,60,50);

            postIconLabel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    postIconLabel.setForeground(Color.black);
                    postIconLabel.setIcon(new ImageIcon("Images\\sendBlack.png"));
                }
                public void mouseExited(MouseEvent e){
                    postIconLabel.setIcon(new ImageIcon("Images\\send.png"));
                    postIconLabel.setForeground(Color.gray);
                }
                public void mouseClicked(MouseEvent e){
                    if(!postTextField.getText().isBlank())
                try {
                    String tableName = username;            
                    String insertQuery = "INSERT INTO " + tableName + " VALUES(?,?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, postTextField.getText());
                    insertStatement.setBytes(2,imageData);
                    insertStatement.executeUpdate();
 
                    updatePosts();
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    postTextField.setVisible(true);
                    postTextField.setText("Share something");
                } catch (SQLException | IOException e1) {
                    e1.printStackTrace();
                }
                }
            });

        
            //Adding to PostPanel
            postPanel.add(sharesomethingLabel);
            postPanel.add(postTextField);
            postPanel.add(fileIconLabel);
            postPanel.add(imageIconLabel);
            postPanel.add(postIconLabel);
            postPanel.setLayout(null);

            mainPanel  = new JPanel();
            mainPanel.setBackground(Color.white);

            mainPanel.setPreferredSize(new Dimension(500,10000));
            

            RoundedPanel bluePanel = new RoundedPanel(30,new Color(223,235,255),new Color(223,235,255));
            bluePanel.setBounds(0,0,525,300);

            RoundedPanel yellowPanel = new RoundedPanel(30,new Color(255,246,223),new Color(255,246,223));
            yellowPanel.setBounds(0,350,525,300);

            // mainPanel.add(bluePanel);
            // mainPanel.add(yellowPanel);
            updatePosts();
            mainPanel.setLayout(null);

            //Adding to ScrollPane aka pane
            JScrollPane pane = new JScrollPane(mainPanel);
            pane.setBounds(0,200,543,600);
            pane.setBorder(new LineBorder(Color.white));

            //Adding to RSPanelForPosts
            RSPanelForPosts.add(postsLabel);
            RSPanelForPosts.add(homeLabel);
            RSPanelForPosts.add(socialLabel);
            RSPanelForPosts.add(postPanel);
            RSPanelForPosts.add(pane);
            RSPanelForPosts.setLayout(null);


            //Contents in RSPanel for Friends
            RSPanelForFriends = new RoundedPanel(20, Color.white, Color.white);
            RSPanelForFriends.setBounds(260,30,525,600);

            friendsLabel = new JLabel("Friends");
            friendsLabel.setForeground(Color.black);
            friendsLabel.setFont(new Font("Helvetica",Font.BOLD,20));
            friendsLabel.setBounds(0,0,200,50);

            searchPanel = new RoundedPanel(30,Color.white,Color.white);
            searchPanel.setBounds(0,50,525,60);

            searchLabel = new RoundedLabel(20, new Color(243,241,241),new Color(243,241,241));
            searchLabel.setText("       Search");
            searchLabel.setForeground(Color.gray);
            searchLabel.setFont(new Font("Helvetica",Font.PLAIN,15));
            searchLabel.setBounds(20,15,485,40);

            searchTextField = new JTextField();
            searchTextField.setText("Search");
            searchTextField.setBounds(40,20,440,30);
            searchTextField.setBorder(new LineBorder(new Color(243,241,241)));
            searchTextField.setForeground(Color.gray);
            searchTextField.setBackground(new Color(243,241,241));
            searchTextField.setFont(new Font("Helvetica",Font.PLAIN,15));
            searchTextField.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    if(searchTextField.getText().equals("Search"))
                        searchTextField.setText("");
                        searchTextField.setForeground(Color.BLACK);
                }
            });

            friendsListPanel  = new JPanel();
            friendsListPanel.setBackground(Color.white);
            friendsListPanel.setPreferredSize(new Dimension(500,10000));
            friendsListPanel.setLayout(null);

            JScrollPane friendsPane = new JScrollPane(friendsListPanel);
            friendsPane.setBounds(0,130,543,600);
            friendsPane.setBorder(new LineBorder(Color.white));

            searchTextField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e){
                    
                }
                @Override
                public void keyPressed(KeyEvent e){
                    
                }
                @Override
                public void keyReleased(KeyEvent e){
                    if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE && searchTextField.getText().isBlank()){
                        friendsListPanel.removeAll();
                        try {
                            updateNotifications();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        friendsListPanel.repaint();
                    }
                    else{
                        try {
                            ResultSet totalUsers = statement.executeQuery("select username,email from userCredentials");
                            String[][] users = new String[1000][1000];
                            int x = 0; 
                            while(totalUsers.next()){
                                if(totalUsers.getString(1).toLowerCase().startsWith(searchTextField.getText().toLowerCase()) && !((totalUsers.getString(1).toLowerCase()).equals((username.toLowerCase())))){
                                    System.out.println(totalUsers.getString(1));
                                    users[x][0] = totalUsers.getString(1);
                                    users[x][1] = totalUsers.getString(2);
                                    x++;
                                }
                            }
                            ResultSet totalFriends = statement.executeQuery("select * from friendsof" + username);
                            String[] friends = new String[1000];
                            int noOfFriends = 0;
                            while(totalFriends.next()){
                                friends[noOfFriends] = totalFriends.getString(1);
                                noOfFriends++;
                            }

                            ResultSet totalRequests = statement.executeQuery("select * from requestsby" + username);
                            String[] requests = new String[1000];
                            int noOfRequests = 0;
                            while(totalRequests.next()){
                                requests[noOfRequests] = totalRequests.getString(1);
                                noOfRequests++;
                            }

                            ResultSet totalNotifications = statement.executeQuery("select * from notificationsof" + username);
                            String[] notifications = new String[1000];
                            int noOfNotifications = 0;
                            while(totalNotifications.next()){
                                notifications[noOfNotifications] = totalNotifications.getString(1);
                                noOfNotifications++;
                            }

                            friendsListPanel.removeAll();

                            JLabel usernameLabels[] = new JLabel[users.length];
                            JLabel emailLabels[]  = new JLabel[users.length];
                            JLabel followingLabel[] = new JLabel[users.length];
                            int yDelta = 5;
                            int zDelta = 25;
                            for(int i = 0;i<x;i++){
                                usernameLabels[i] = new JLabel(users[i][0]);
                                usernameLabels[i].setBounds(22,yDelta,100,20);
                                usernameLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));
                                usernameLabels[i].setForeground(Color.black);

                                emailLabels[i] = new JLabel(users[i][1]);
                                emailLabels[i].setBounds(22,zDelta,200,20);
                                emailLabels[i].setFont(new Font("Helvetica",Font.BOLD,12));
                                emailLabels[i].setForeground(Color.gray);

                                followingLabel[i] = new JLabel();

                                if(checkFriend(friends, usernameLabels[i].getText()))
                                    followingLabel[i].setText("Following");
                                else if(checkFriend(requests,usernameLabels[i].getText()))
                                    followingLabel[i].setText("Requested");
                                else if(checkFriend(notifications,usernameLabels[i].getText()))
                                    followingLabel[i].setText("Accept");
                                else
                                    followingLabel[i].setText("Follow"); 
                        
                                followingLabel[i].setBounds(400,yDelta,100,20);
                                followingLabel[i].setFont(new Font("Helvetica",Font.BOLD,15));
                                followingLabel[i].setForeground(Color.black);

                                final int effectiveFinalI = i;
                                followingLabel[i].addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e){
                                        if(followingLabel[effectiveFinalI].getText().equals("Following")){}
                                        else if(followingLabel[effectiveFinalI].getText().equals("Follow")){
                                            followingLabel[effectiveFinalI].setText("Requested");
                                            try {
                                                String insertQuery = "INSERT INTO requestsby" + username + " VALUES (?,?)";
                                                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                                                preparedStatement.setString(1, usernameLabels[effectiveFinalI].getText());
                                                preparedStatement.setString(2, emailLabels[effectiveFinalI].getText());
                                                preparedStatement.executeUpdate();

                                                String insertQuery2 = "INSERT INTO notificationsof" + usernameLabels[effectiveFinalI].getText() + " VALUES (?,?)";
                                                PreparedStatement preparedStatement2 = connection.prepareStatement(insertQuery2);
                                                preparedStatement2.setString(1, username);
                                                preparedStatement2.setString(2, email);
                                                preparedStatement2.executeUpdate();
                                            } catch (SQLException e1) {
                                                e1.printStackTrace();
                                            }
                                            
                                        }
                                        else if (followingLabel[effectiveFinalI].getText().equals("Accept")){
                                            followingLabel[effectiveFinalI].setText("Accepted");
                                            try {
                                                String insertQuery = "INSERT INTO friendsof" + username + " VALUES (?,?)";
                                                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                                                preparedStatement.setString(1, usernameLabels[effectiveFinalI].getText());
                                                preparedStatement.setString(2, emailLabels[effectiveFinalI].getText());
                                                preparedStatement.executeUpdate();

                                                String insertQuery2 = "INSERT INTO friendsof" + usernameLabels[effectiveFinalI].getText() + " VALUES (?,?)";
                                                PreparedStatement preparedStatement2 = connection.prepareStatement(insertQuery2);
                                                preparedStatement2.setString(1, username);
                                                preparedStatement2.setString(2, email);
                                                preparedStatement2.executeUpdate();

                                                String insertQuery3 = "DELETE FROM requestsby" + usernameLabels[effectiveFinalI].getText() + " where friends = ?" ;
                                                PreparedStatement preparedStatement3 = connection.prepareStatement(insertQuery3);
                                                preparedStatement3.setString(1,username);
                                                preparedStatement3.executeUpdate();

                                                String insertQuery4 = "DELETE FROM notificationsof" + username + " where friends = ?" ;
                                                PreparedStatement preparedStatement4 = connection.prepareStatement(insertQuery4);
                                                preparedStatement4.setString(1,usernameLabels[effectiveFinalI].getText());
                                                preparedStatement4.executeUpdate();

                                                
                                                } catch (SQLException e1) {
                                                    e1.printStackTrace();
                                                }
                                        }
                                    }
                                    
                                });

                                usernameLabels[i].addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e){
                                        try {
                                            if(!checkFriend(friends, usernameLabels[effectiveFinalI].getText())){}
                                            else{
                                                updatePostsOfFriends(usernameLabels[effectiveFinalI].getText());
                                                friendsListPanel.validate();
                                                friendsListPanel.repaint();}
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });

                                yDelta = yDelta  + 50;
                                zDelta = zDelta + 50;

                                friendsListPanel.add(usernameLabels[i]);
                                friendsListPanel.add(emailLabels[i]);
                                friendsListPanel.add(followingLabel[i]);

                                friendsListPanel.repaint();
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        friendsListPanel.revalidate();
                        friendsListPanel.repaint();
                    }
                }
            });

            //Adding to searchPanel
            searchPanel.add(searchLabel);
            searchPanel.add(searchTextField);
            searchPanel.setLayout(null);

            //Adding to RSPanelForFriends
            RSPanelForFriends.add(friendsLabel);
            RSPanelForFriends.add(searchPanel);
            RSPanelForFriends.add(friendsPane);
            RSPanelForFriends.setLayout(null);
            RSPanelForFriends.setVisible(false);

            newsPanel = new JPanel();
            newsPanel.setBackground(Color.white);
            newsPanel.setBounds(810,30,600,600);

            newsLabel = new JLabel("News");
            newsLabel.setForeground(Color.black);
            newsLabel.setFont(new Font("Helvetica",Font.BOLD,20));
            newsLabel.setBounds(0,0,200,50);

            updateNews();
            newsPanel.repaint();
            newsPanel.revalidate();

            //Adding to newsPanel
            newsPanel.add(newsLabel);
            newsPanel.setLayout(null);

            //Contents in RSPanelforMessaging
            RSPanelforMessaging = new RoundedPanel(20,Color.white,Color.white);
            RSPanelforMessaging.setBounds(260,30,525,600);
            RSPanelforMessaging.setVisible(false);

            messagingLabel = new JLabel("Messaging");
            messagingLabel.setForeground(Color.black);
            messagingLabel.setFont(new Font("Helvetica",Font.BOLD,20));
            messagingLabel.setBounds(0,0,200,50);

            searchPanelForMessaging = new RoundedPanel(30,Color.white,Color.white);
            searchPanelForMessaging.setBounds(0,50,525,60);

            searchLabelForMessaging = new RoundedLabel(20, new Color(243,241,241),new Color(243,241,241));
            searchLabelForMessaging.setText("       Search");
            searchLabelForMessaging.setForeground(Color.gray);
            searchLabelForMessaging.setFont(new Font("Helvetica",Font.PLAIN,15));
            searchLabelForMessaging.setBounds(20,15,485,40);

            searchTextFieldForMessaging = new JTextField();
            searchTextFieldForMessaging.setText("Search");
            searchTextFieldForMessaging.setBounds(40,20,440,30);
            searchTextFieldForMessaging.setBorder(new LineBorder(new Color(243,241,241)));
            searchTextFieldForMessaging.setForeground(Color.gray);
            searchTextFieldForMessaging.setBackground(new Color(243,241,241));
            searchTextFieldForMessaging.setFont(new Font("Helvetica",Font.PLAIN,15));
            searchTextFieldForMessaging.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    if(searchTextFieldForMessaging.getText().equals("Search"))
                        searchTextFieldForMessaging.setText("");
                        searchTextFieldForMessaging.setForeground(Color.BLACK);
                }
            });

            searchTextFieldForMessaging.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e){

                }
                public void keyReleased(KeyEvent e){
                    
                }
                public void keyTyped(KeyEvent e){
                    if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE && searchTextField.getText().isBlank()){
                        messagingListPanel.removeAll();
                        try {
                            updateMessaging();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        messagingListPanel.repaint();
                    }
                    else{
                        
                        try {
                            ResultSet totalFriends = statement.executeQuery("select * from friendsof" + username);
                            String[][] users = new String[1000][1000];
                            int x = 0;
                            while(totalFriends.next()){
                                if(totalFriends.getString(1).toLowerCase().startsWith(searchTextFieldForMessaging.getText().toLowerCase())){
                                    users[x][0] = totalFriends.getString(1);
                                    users[x][1] = totalFriends.getString(2);
                                    x++;
                                }
                                
                            }
                            
                            messagingListPanel.removeAll();
                            JLabel usernameLabels[] = new JLabel[x];
                            JLabel emailLabels[]  = new JLabel[x];
                            JLabel followingLabel[] = new JLabel[x];
                            int yDelta = 5;
                            int zDelta = 25;
                            for(int i = 0;i<x;i++){
                            usernameLabels[i] = new JLabel(users[i][0]);
                            usernameLabels[i].setBounds(22,yDelta,100,20);
                            usernameLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));
                            usernameLabels[i].setForeground(Color.black);

                            emailLabels[i] = new JLabel(users[i][1]);
                            emailLabels[i].setBounds(22,zDelta,200,20);
                            emailLabels[i].setFont(new Font("Helvetica",Font.BOLD,12));
                            emailLabels[i].setForeground(Color.gray);

                            int effectiveFinalI = i;
                            usernameLabels[i].addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent x){
                                    chatContentLabel.setVisible(true);
                                    chatContentTextField.setVisible(true);
                                    chatThreadActive = true;
                                    updateChats(usernameLabels[effectiveFinalI].getText());
                                    toFriend = usernameLabels[effectiveFinalI].getText();
                                } 
                            });

                            messagingListPanel.add(usernameLabels[i]);
                            messagingListPanel.add(emailLabels[i]);
                            yDelta = yDelta  + 50;
                            zDelta = zDelta + 50;
                            messagingListPanel.repaint();
                            messagingListPanel.revalidate();
                        } 
                    }catch (Exception e1) {
                                e1.printStackTrace();
                            }
                    } 
                }
            });

            messagingListPanel  = new JPanel();
            messagingListPanel.setBackground(Color.white);
            messagingListPanel.setPreferredSize(new Dimension(500,10000));
            messagingListPanel.setLayout(null);

            JScrollPane messagingListPane = new JScrollPane(messagingListPanel);
            messagingListPane.setBounds(0,130,543,370);
            messagingListPane.setBorder(new LineBorder(Color.white));

            chatContentTextField = new JTextField();
            chatContentTextField.setText("    Text Message");
            chatContentTextField.setBounds(30,505,440,30);
            chatContentTextField.setBorder(new LineBorder(new Color(243,241,241)));
            chatContentTextField.setForeground(Color.gray);
            chatContentTextField.setBackground(new Color(243,241,241));
            chatContentTextField.setFont(new Font("Helvetica",Font.PLAIN,15));
            chatContentTextField.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    if(chatContentTextField.getText().equals("    Text Message"))
                        chatContentTextField.setText("");
                        chatContentTextField.setForeground(Color.BLACK);
                        sendChatLabel.setVisible(true);
                }
            });
            chatContentTextField.setVisible(false);

            chatContentLabel = new RoundedLabel(20, new Color(243,241,241),new Color(243,241,241));
            chatContentLabel.setText("       Text Message");
            chatContentLabel.setForeground(Color.gray);
            chatContentLabel.setVisible(false);

            chatContentLabel.setFont(new Font("Helvetica",Font.PLAIN,15));
            chatContentLabel.setBounds(20,500,485,40);

            sendChatLabel = new JLabel(new ImageIcon("Images\\sendChat.png"));
            sendChatLabel.setBounds(460,500, 50, 40);
            sendChatLabel.setVisible(false);
            sendChatLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    try{
                        String insertQuery = "";
                        if(!chatContentTextField.getText().isBlank()){
                            if(doesTableExist(connection, "chatsof" + username + toFriend)){
                                insertQuery = "insert into chatsof" + username + toFriend + " values(?,?)";
                            }
                            else if(doesTableExist(connection, "chatsof" + toFriend + username)){
                                insertQuery = "insert into chatsof" + toFriend + username + " values(?,?)";                            }
                        }
                        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1,username);
                        preparedStatement.setString(2,chatContentTextField.getText());
                        preparedStatement.executeUpdate();
                        chatContentTextField.setText("");
                        
                    }catch(Exception x){
                        x.printStackTrace();
                    }
                }
            });
            
            //Adding to searchPanelForMessaging
            searchPanelForMessaging.add(searchLabelForMessaging);
            searchPanelForMessaging.add(searchTextFieldForMessaging);
            searchPanelForMessaging.setLayout(null);

            //Adding to RSPanelForMessaging
            RSPanelforMessaging.add(messagingLabel);
            RSPanelforMessaging.add(searchPanelForMessaging);
            RSPanelforMessaging.add(sendChatLabel);
            RSPanelforMessaging.add(chatContentTextField);
            RSPanelforMessaging.add(chatContentLabel);
            RSPanelforMessaging.add(messagingListPane);
            RSPanelforMessaging.setLayout(null);

            //Adding to mainPageFrame
            mainPageFrame.add(LSPanel);
            mainPageFrame.add(RSPanelForPosts);
            mainPageFrame.add(RSPanelForFriends);
            mainPageFrame.add(newsPanel);
            mainPageFrame.add(RSPanelforMessaging);
            mainPageFrame.setLayout(null);
            mainPageFrame.setVisible(true);
        }
        
        void updatePosts() throws SQLException,IOException{
        ResultSet totalPosts = statement.executeQuery("select * from " + username);
        int totalNumOfPosts = 0;
        
        String[] datas = new String[100];
        byte[] imageBytes = new byte[100];
        int x = 0;

        BufferedImage[] imageArray = new BufferedImage[100];

        while(totalPosts.next()){
            totalNumOfPosts++;
            datas[x] = totalPosts.getString(1);
            imageBytes = totalPosts.getBytes(2);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);
            imageArray[x] = image;
            System.out.println(datas[x]);
            x++;
        }

        mainPanel.removeAll();

        RoundedPanel demoPanel[] =  new RoundedPanel[totalNumOfPosts];
        int yDelta  = 0;
        JLabel demoLabels[] = new JLabel[totalNumOfPosts];
        JTextArea demoTextArea[] = new JTextArea[totalNumOfPosts];
        JLabel imageLabels[] = new JLabel[totalNumOfPosts];
        Color colors[] = {new Color(223,235,255),new Color(255,246,223)};

        for( int i = 0;i<totalNumOfPosts;i++){

            demoLabels[i] = new JLabel(username);
            demoLabels[i].setBounds(22,20,500,20);
            demoLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));

            imageLabels[i] = new JLabel(new ImageIcon(imageArray[i]));
            imageLabels[i].setBounds(0,150,500,400);
            imageLabels[i].setFont(new Font("Helvetica",Font.BOLD,18));

            // Color color = colors[(new Random()).nextInt(2)];
            Color color;
            if(colorNumber==0){
                colorNumber = 1;
                color = colors[colorNumber];
            }
            else{
                colorNumber = 0;
                color = colors[colorNumber];
            }
            
            demoTextArea[i] = new JTextArea();
            demoTextArea[i].setText(datas[i]);
            demoTextArea[i].setEditable(false);
            demoTextArea[i].setBounds(22,50,470,200);
            demoTextArea[i].setBackground(color);
            demoTextArea[i].setForeground(Color.black);
            demoTextArea[i].setFont(new Font("Helvetica",Font.PLAIN,15));
            demoTextArea[i].setLineWrap(true);
            demoTextArea[i].setWrapStyleWord(true);
            
            demoPanel[i] = new RoundedPanel(30,color,color);
            demoPanel[i].setBounds(0,yDelta,500,400);
            yDelta = yDelta  + 450;

            demoPanel[i].setLayout(null);
            demoPanel[i].add(demoLabels[i]);
            demoPanel[i].add(imageLabels[i]);
            demoPanel[i].add(demoTextArea[i]);
            mainPanel.add(demoPanel[i]);
        }    
    }

    void updatePostsOfFriends(String friendName) throws SQLException,IOException{
        ResultSet totalPosts = statement.executeQuery("select * from " + friendName);
        int totalNumOfPosts = 0;
        
        String[] datas = new String[100];
        byte[] imageBytes = new byte[100];
        int x = 0;

        BufferedImage[] imageArray = new BufferedImage[100];

        while(totalPosts.next()){
            totalNumOfPosts++;
            datas[x] = totalPosts.getString(1);
            imageBytes = totalPosts.getBytes(2);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);
            imageArray[x] = image;
            System.out.println(datas[x]);
            x++;
        }

        friendsListPanel.removeAll();

        RoundedPanel demoPanel[] =  new RoundedPanel[totalNumOfPosts];
        int yDelta  = 0;
        JLabel demoLabels[] = new JLabel[totalNumOfPosts];
        JTextArea demoTextArea[] = new JTextArea[totalNumOfPosts];
        JLabel imageLabels[] = new JLabel[totalNumOfPosts];
        Color colors[] = {new Color(223,235,255),new Color(255,246,223)};

        for( int i = 0;i<totalNumOfPosts;i++){

            demoLabels[i] = new JLabel(friendName);
            demoLabels[i].setBounds(22,20,500,20);
            demoLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));

            imageLabels[i] = new JLabel(new ImageIcon(imageArray[i]));
            imageLabels[i].setBounds(0,150,500,400);
            imageLabels[i].setFont(new Font("Helvetica",Font.BOLD,18));

            // Color color = colors[(new Random()).nextInt(2)];
            Color color;
            if(colorNumber==0){
                colorNumber = 1;
                color = colors[colorNumber];
            }
            else{
                colorNumber = 0;
                color = colors[colorNumber];
            }
            
            demoTextArea[i] = new JTextArea();
            demoTextArea[i].setText(datas[i]);
            demoTextArea[i].setEditable(false);
            demoTextArea[i].setBounds(22,50,470,200);
            demoTextArea[i].setBackground(color);
            demoTextArea[i].setForeground(Color.black);
            demoTextArea[i].setFont(new Font("Helvetica",Font.PLAIN,15));
            demoTextArea[i].setLineWrap(true);
            demoTextArea[i].setWrapStyleWord(true);
            
            demoPanel[i] = new RoundedPanel(30,color,color);
            demoPanel[i].setBounds(0,yDelta,500,400);
            yDelta = yDelta  + 450;

            demoPanel[i].setLayout(null);
            demoPanel[i].add(demoLabels[i]);
            demoPanel[i].add(imageLabels[i]);
            demoPanel[i].add(demoTextArea[i]);
            friendsListPanel.add(demoPanel[i]);
        }    
    }
    
    boolean checkFriend(String[] totalFriends,String friend){
            for(String each : totalFriends){
                if(each == null)
                    break;
                if(each.equals(friend))
                    return true;
            }
            return false;
        }
    void updateNotifications() throws Exception{
        ResultSet totalNotifications = statement.executeQuery("select * from notificationsof" + username);
        String[][] users = new String[1000][1000];
        int x = 0; 
        while(totalNotifications.next()){
            System.out.println(totalNotifications.getString(1));
            users[x][0] = totalNotifications.getString(1);
            users[x][1] = totalNotifications.getString(2);
            x++;
            }
        friendsListPanel.removeAll();

        JLabel usernameLabels[] = new JLabel[users.length];
        JLabel emailLabels[]  = new JLabel[users.length];
        JLabel acceptLabels[] = new JLabel[users.length];
        int yDelta = 5;
        int zDelta = 25;
        for(int i = 0;i<x;i++){
        usernameLabels[i] = new JLabel(users[i][0]);
        usernameLabels[i].setBounds(22,yDelta,100,20);
        usernameLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));
        usernameLabels[i].setForeground(Color.black);

        emailLabels[i] = new JLabel(users[i][1]);
        emailLabels[i].setBounds(22,zDelta,200,20);
        emailLabels[i].setFont(new Font("Helvetica",Font.BOLD,12));
        emailLabels[i].setForeground(Color.gray);

        acceptLabels[i] = new JLabel("Accept");
        acceptLabels[i].setBounds(400,yDelta,100,20);
        acceptLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));
        acceptLabels[i].setForeground(Color.black);

        final int effectiveFinalI = i;
        acceptLabels[i].addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e){
            acceptLabels[effectiveFinalI].setText("Accepted");
            try {
                String insertQuery = "INSERT INTO friendsof" + username + " VALUES (?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, usernameLabels[effectiveFinalI].getText());
                preparedStatement.setString(2, emailLabels[effectiveFinalI].getText());
                preparedStatement.executeUpdate();

                String insertQuery2 = "INSERT INTO friendsof" + usernameLabels[effectiveFinalI].getText() + " VALUES (?,?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(insertQuery2);
                preparedStatement2.setString(1, username);
                preparedStatement2.setString(2, email);
                preparedStatement2.executeUpdate();

                String insertQuery3 = "DELETE FROM requestsby" + usernameLabels[effectiveFinalI].getText() + " where friends = ?" ;
                PreparedStatement preparedStatement3 = connection.prepareStatement(insertQuery3);
                preparedStatement3.setString(1,username);
                preparedStatement3.executeUpdate();

                String insertQuery4 = "DELETE FROM notificationsof" + username + " where friends = ?" ;
                PreparedStatement preparedStatement4 = connection.prepareStatement(insertQuery4);
                preparedStatement4.setString(1,usernameLabels[effectiveFinalI].getText());
                preparedStatement4.executeUpdate();

                statement.execute("create table chatsof" + username + usernameLabels[effectiveFinalI].getText() +  "(sender varchar(100), message varchar(100))");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
        }
        });

        usernameLabels[i].addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e){
            try {
                if(acceptLabels[effectiveFinalI].getText().equals("Accept")){}
                else{
                    updatePostsOfFriends(usernameLabels[effectiveFinalI].getText());
                    friendsListPanel.validate();
                    friendsListPanel.repaint();}
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        yDelta = yDelta  + 50;
        zDelta = zDelta + 50;
        friendsListPanel.add(usernameLabels[i]);
        friendsListPanel.add(emailLabels[i]);
        friendsListPanel.add(acceptLabels[i]);
        friendsListPanel.repaint();
        }
    }

    void updateNews(){
        try{
            ResultSet totalNews = statement.executeQuery("select * from news");
            String[][] news = new String[1000][1000];
            int x = 0;
            while(totalNews.next()){
                news[x][0] = totalNews.getString(1);
                news[x][1] = totalNews.getString(2);
                news[x][2] = totalNews.getString(3);
                x++;
            }

            newsPanel.removeAll();

            JLabel newsLabels[] = new JLabel[x];     
            JLabel newsInfoLabels[] = new JLabel[x];
            int yDelta = 50;
            int zDelta = 70;
            for(int i = 0;i<x;i++){
                newsLabels[i] = new JLabel("•   " + news[i][0]);
                newsLabels[i].setBounds(0,yDelta,500,20);
                newsLabels[i].setForeground(Color.black);
                newsLabels[i].setFont(new Font("Helvetica",Font.PLAIN,15));

                newsInfoLabels[i] = new JLabel("      "+news[i][2] + " • 1 readers");
                newsInfoLabels[i].setBounds(0,zDelta,500,20);
                newsInfoLabels[i].setForeground(Color.gray);
                newsInfoLabels[i].setFont(new Font("Helvetica",Font.PLAIN,12));
                yDelta += 50;
                zDelta += 50;
                newsPanel.add(newsLabels[i]);
                newsPanel.add(newsInfoLabels[i]);

                int finalizedEffectiveI = i;
                newsLabels[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        try {
                            if(desktop.isDesktopSupported()){desktop.browse(new URI(news[finalizedEffectiveI][1]));}
                            else 
                                System.out.println("Desktop not supported!");
                        } catch (Exception e1) {}
                    }

                    @Override
                    public void mouseEntered(MouseEvent e){
                        newsLabels[finalizedEffectiveI].setForeground(Color.red);
                    }
                    @Override
                    public void mouseExited(MouseEvent e){
                        newsLabels[finalizedEffectiveI].setForeground(Color.black);
                    }
                });
            }
            newsPanel.repaint();
        }catch(Exception e){e.printStackTrace();}
    }

    void updateSocialPosts() throws Exception{

        ResultSet totalFriends = statement.executeQuery("select * from friendsof" + username);
        String[] datas = new String[1000];
        byte[] imageBytes = new byte[1000];
        String[] friends  = new String[1000];
        BufferedImage[] imageArray = new BufferedImage[1000];
        int x = 0;
        int totalNumOfPosts = 0;
        
            int j = 0;
            while (totalFriends.next()) {
                System.out.println("Friend");
                String friend = totalFriends.getString(1);
        
                ResultSet totalPosts = statement.executeQuery("SELECT * FROM dm");
                    while (totalPosts.next()) {
                        System.out.println("inner friend");
                        totalNumOfPosts++;
                        friends[x] = friend;
                        datas[x] = totalPosts.getString(1);
                        imageBytes = totalPosts.getBytes(2);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                        BufferedImage image = ImageIO.read(inputStream);
                        imageArray[x] = image;
                        System.out.println(datas[x]);
                        x++;
                    }
                }
                j++;
        
    
        mainPanel.removeAll();

        RoundedPanel demoPanel[] =  new RoundedPanel[totalNumOfPosts];
        int yDelta  = 0;
        JLabel demoLabels[] = new JLabel[totalNumOfPosts];
        JTextArea demoTextArea[] = new JTextArea[totalNumOfPosts];
        JLabel imageLabels[] = new JLabel[totalNumOfPosts];
        Color colors[] = {new Color(223,235,255),new Color(255,246,223)};

        for( int i = 0;i<totalNumOfPosts;i++){

            demoLabels[i] = new JLabel(friends[i]);
            demoLabels[i].setBounds(22,20,500,20);
            demoLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));

            imageLabels[i] = new JLabel(new ImageIcon(imageArray[i]));
            imageLabels[i].setBounds(0,150,500,400);
            imageLabels[i].setFont(new Font("Helvetica",Font.BOLD,18));

            Color color;
            if(colorNumber==0){
                colorNumber = 1;
                color = colors[colorNumber];
            }
            else{
                colorNumber = 0;
                color = colors[colorNumber];
            }
            
            demoTextArea[i] = new JTextArea();
            demoTextArea[i].setText(datas[i]);
            demoTextArea[i].setEditable(false);
            demoTextArea[i].setBounds(22,50,470,200);
            demoTextArea[i].setBackground(color);
            demoTextArea[i].setForeground(Color.black);
            demoTextArea[i].setFont(new Font("Helvetica",Font.PLAIN,15));
            demoTextArea[i].setLineWrap(true);
            demoTextArea[i].setWrapStyleWord(true);
            
            demoPanel[i] = new RoundedPanel(30,color,color);
            demoPanel[i].setBounds(0,yDelta,500,400);
            yDelta = yDelta  + 450;

            demoPanel[i].setLayout(null);
            demoPanel[i].add(demoLabels[i]);
            demoPanel[i].add(imageLabels[i]);
            demoPanel[i].add(demoTextArea[i]);
            mainPanel.add(demoPanel[i]);
        }    
    }

    void updateMessaging() throws Exception{
           try{
                ResultSet totalFriends = statement.executeQuery("select * from friendsof" + username);
                String[][] friends = new String[1000][1000];
                int noOfFriends = 0;
                while(totalFriends.next()){
                    friends[noOfFriends][0] = totalFriends.getString(1);
                    friends[noOfFriends][1] = totalFriends.getString(2);
                    noOfFriends++;
                    }

                messagingListPanel.removeAll();

                JLabel usernameLabels[] = new JLabel[noOfFriends];
                JLabel emailLabels[]  = new JLabel[noOfFriends];
                JLabel followingLabel[] = new JLabel[noOfFriends];
                int yDelta = 5;
                int zDelta = 25;
                for(int i = 0;i<noOfFriends;i++){
                usernameLabels[i] = new JLabel(friends[i][0]);
                usernameLabels[i].setBounds(22,yDelta,100,20);
                usernameLabels[i].setFont(new Font("Helvetica",Font.BOLD,15));
                usernameLabels[i].setForeground(Color.black);

                emailLabels[i] = new JLabel(friends[i][1]);
                emailLabels[i].setBounds(22,zDelta,200,20);
                emailLabels[i].setFont(new Font("Helvetica",Font.BOLD,12));
                emailLabels[i].setForeground(Color.gray);

                int effectiveFinalI = i;
                usernameLabels[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        chatContentLabel.setVisible(true);
                        chatContentTextField.setVisible(true);
                        chatThreadActive = true;
                        updateChats(usernameLabels[effectiveFinalI].getText());
                        toFriend = usernameLabels[effectiveFinalI].getText();
                    } 
                });

                messagingListPanel.add(usernameLabels[i]);
                messagingListPanel.add(emailLabels[i]);
                yDelta = yDelta  + 50;
                zDelta = zDelta + 50;
                messagingListPanel.repaint();
                }
            }catch(Exception e1){e1.printStackTrace();}
    }

    void updateChats(String friend){
        ChatManagementClass chatThread = new ChatManagementClass(friend);
        chatThread.start();
    }
    
class ChatManagementClass extends Thread{

    String friend;

    public ChatManagementClass(String friend){
        this.friend =  friend;
    }

    public void run(){
        while(chatThreadActive){
            try{
                messagingListPanel.removeAll();
                ResultSet chatSet;
                if(doesTableExist(connection, "chatsof" + username + friend))
                    chatSet = statement.executeQuery("select * from chatsof" + username + friend);
                else 
                    chatSet = statement.executeQuery("select * from chatsof" + friend + username);
                
                String[][] chats = new String[1000][1000];
                int x = 0;
                while(chatSet.next()){
                    chats[x][0] = chatSet.getString(1);
                    chats[x][1] = chatSet.getString(2);
                    x++;
                }

                JLabel[] label = new JLabel[x];
                int yDelta = 0;
                for(int i = 0;i<x;i++){
                    label[i] = new JLabel(chats[i][1]);
                    if(chats[i][0].equals(username)){
                        label[i].setHorizontalAlignment(SwingConstants.RIGHT);
                        // label[i].setBounds(250,yDelta,250,20);
                        }
                    else{
                        // label[i].setBounds(0,yDelta,250,20);
                        label[i].setHorizontalAlignment(SwingConstants.LEFT);}
                
                    label[i].setBounds(0,yDelta,500,20);
                    label[i].setFont(new Font("Helvetica",Font.ITALIC,15));
                    
                    yDelta += 30;

                    messagingListPanel.add(label[i]);
                }
                messagingListPanel.repaint();
                messagingListPanel.revalidate();
                Thread.sleep(1000);
                
            }catch (Exception e){e.printStackTrace();}
        }
    }
}

public boolean doesTableExist(Connection connection, String tableName) {
    try {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
        
        return resultSet.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }   
    }
}




