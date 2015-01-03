package dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import com.yuan.gui.app.consts.Constants;

import sun.awt.VerticalBagLayout;

public class AboutDialog extends AbstractDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(Frame parent) {
        super(parent);
    }

    @Override
    protected void initDialog() {
        this.setTitle(Constants.ABOUTDLG_TITLE);
    }

    @Override
    protected void initLayout() {
        this.setLayout(new BorderLayout());

        JPanel p = new JPanel(new VerticalBagLayout());
        p.add(new Label("Author: Yuan Jinyong 00185170"));
        p.add(new Label("Version 2.0"));
        this.add(BorderLayout.NORTH, p);

        p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(createButton(Constants.BTN_OK));
        this.add(BorderLayout.SOUTH, p);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Constants.BTN_OK.equals(e.getActionCommand())) {
            this.dispose();
        }
    }
}
