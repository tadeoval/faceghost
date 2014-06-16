/*
 * The MIT License
 *
 * Copyright 2014 Tadeo Valencia.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.tadeoval.faceghost.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.tadeoval.faceghost.executor.ScriptExecutor;
import org.tadeoval.faceghost.gui.logger.GuiLogger;

/**
 *
 * @author Tadeo Valencia
 */
public class Main {
    
    private static final String TITLE = "Faceghost";
    private final JFrame frame;
    protected JTextArea guiLog;
    
    private JTextField usernameTextBox;
    private JPasswordField passwordTextBox;
    private JTextField albumSourceTextBox;
    private JFilePicker albumDestinationChooser;
    
    public static void main(String args[]){
        java.awt.EventQueue.invokeLater(() -> {
            Main app = new Main();
            app.showWindow();
        });
        
    }
    
    public Main(){
        frame = new JFrame();
        frame.setTitle(TITLE);
        loadComponents();
        //set for development/testing
        //frame.setSize(500, 500);
        //frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
    }
    
    public void showWindow(){
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void loadComponents() {
        JComponent pane = (JComponent) frame.getContentPane();
        pane.removeAll();
        pane.setLayout(new GridBagLayout());
        
        
        /**
         * Credentials section
         */
        JPanel credentialsPanel = new JPanel();
        credentialsPanel.setLayout(new GridBagLayout());
        Font credentialsFont = new Font(Font.DIALOG, Font.BOLD, 13);
        credentialsPanel.setBorder(new TitledBorder(credentialsPanel.getBorder(), "FB Credentials",
                TitledBorder.CENTER, TitledBorder.TOP, credentialsFont, Color.BLACK));
        
        GridBagConstraints gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 0;
        gridC.gridy = 0;
        gridC.insets = new Insets(0, 10, 0, 15);
        gridC.anchor = GridBagConstraints.LINE_END;
        JLabel usernameLabel = new JLabel("Username:");
        credentialsPanel.add(usernameLabel, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 1;
        gridC.gridy = 0;
        gridC.insets = new Insets(0, 0, 2, 0);
        usernameTextBox = new JTextField(20);
        credentialsPanel.add(usernameTextBox, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 0;
        gridC.gridy = 1;
        gridC.insets = new Insets(0, 10, 0, 15);
        gridC.anchor = GridBagConstraints.LINE_END;
        JLabel passwordLabel = new JLabel("Password:");
        credentialsPanel.add(passwordLabel, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 1;
        gridC.gridy = 1;
        gridC.insets = new Insets(2, 0, 0, 0);
        passwordTextBox = new JPasswordField(20);
        credentialsPanel.add(passwordTextBox, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.weighty = 0.5;
        gridC.gridx = 0;
        gridC.gridy = 0;
        gridC.insets = new Insets(10, 10, 10, 10);
        pane.add(credentialsPanel, gridC);
        
        /**
         * Album settings section
         */
        JPanel albumPanel = new JPanel();
        albumPanel.setLayout(new GridBagLayout());
        Font albumFont = new Font(Font.DIALOG, Font.BOLD, 13);
        albumPanel.setBorder(new TitledBorder(albumPanel.getBorder(), "Album settings",
                TitledBorder.CENTER, TitledBorder.TOP, albumFont, Color.BLACK));
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 0;
        gridC.gridy = 0;
        gridC.insets = new Insets(0, 10, 0, 15);
        gridC.anchor = GridBagConstraints.LINE_END;
        JLabel albumSourceLabel = new JLabel("Album's URL:");
        albumPanel.add(albumSourceLabel, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.anchor = GridBagConstraints.LINE_START;
        gridC.gridx = 1;
        gridC.gridy = 0;
        gridC.insets = new Insets(0, 10, 2, 20);
        albumSourceTextBox = new JTextField(20);
        albumPanel.add(albumSourceTextBox, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 0;
        gridC.gridy = 1;
        gridC.gridwidth = 2;
        gridC.insets = new Insets(2, 0, 0, 10);
        albumDestinationChooser = new JFilePicker("Album destination:", "Browse");
        albumDestinationChooser.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        albumDestinationChooser.setMode(JFilePicker.MODE_OPEN);
        albumPanel.add(albumDestinationChooser, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.weighty = 0.5;
        gridC.gridx = 0;
        gridC.gridy = 1;
        gridC.insets = new Insets(10, 10, 10, 10);
        pane.add(albumPanel, gridC);
        
        /**
         * Log section
         */
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridBagLayout());
        Font outputFont = new Font(Font.DIALOG, Font.BOLD, 13);
        outputPanel.setBorder(new TitledBorder(outputPanel.getBorder(), "Output",
                TitledBorder.CENTER, TitledBorder.TOP, outputFont, Color.BLACK));
        
        guiLog = new JTextArea(20, 20);
        guiLog.setMargin(new Insets(5, 5, 5, 5));
        guiLog.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(guiLog);
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 0;
        gridC.gridy = 0;
        gridC.insets = new Insets(10, 10, 10, 10);
        outputPanel.add(logScrollPane, gridC);
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridheight = 2;
        gridC.gridx = 1;
        gridC.gridy = 0;
        gridC.insets = new Insets(10, 10, 10, 10);
        pane.add(outputPanel, gridC);
        
        /**
         * Buttons section
         */
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        JButton runButton = new JButton("Run");
        runButton.addActionListener((ActionEvent evt) -> {
            runCommand();
        });
        
        gridC = new GridBagConstraints();
        gridC.fill = GridBagConstraints.BOTH;
        gridC.gridx = 0;
        gridC.gridy = 0;
        gridC.insets = new Insets(10, 10, 10, 10);
        buttonsPanel.add(runButton, gridC);
        
        gridC = new GridBagConstraints();
        gridC.gridwidth = 2;
        gridC.gridx = 0;
        gridC.gridy = 2;
        gridC.insets = new Insets(10, 10, 10, 10);
        pane.add(buttonsPanel, gridC);
    }
    
    private void runCommand() {
        
        String username = usernameTextBox.getText();
        String password = new String(passwordTextBox.getPassword());
        String albumSource = albumSourceTextBox.getText();
        String albumDestination = albumDestinationChooser.getSelectedFilePath();

        GuiLogger guiLogger = new GuiLogger(guiLog);
        
        ScriptExecutor exec = new ScriptExecutor(username, password, albumSource, albumDestination, guiLogger);
        exec.runCommand();
        
    }
}
