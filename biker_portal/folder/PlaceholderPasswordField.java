package com.mycompany.biker_portal.folder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceholderPasswordField extends JPasswordField implements FocusListener {
    private String placeholder;
    private boolean showingPlaceholder;

    public PlaceholderPasswordField(String placeholder) {
        this.placeholder = placeholder;
        setText(placeholder);
        setEchoChar((char) 0);
        setForeground(Color.GRAY);
        showingPlaceholder = true;
        addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (showingPlaceholder) {
            setText("");
            setEchoChar('●'); // Common password character
            setForeground(Color.BLACK);
            showingPlaceholder = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (getPassword().length == 0) {
            setText(placeholder);
            setEchoChar((char) 0);
            setForeground(Color.GRAY);
            showingPlaceholder = true;
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
