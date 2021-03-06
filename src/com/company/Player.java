package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {


    int commonFloor = 45;

    boolean atTop = false;
    boolean emergencyStop = false;
    boolean[][] allBoolMove = new boolean[2][6];
    boolean[] whichPlayer = new boolean[3];

    ImageIcon[][] allPic = new ImageIcon[4][6];

    ArrayList<Projectile> allBulltes = new ArrayList<>();

    JLabel icon = new JLabel();

    Timer movementTimer;
    Timer jumpTimer;
    Timer stopTimer;
    Timer bulletTimer;

    int jumpHeight;
    int jumpSpeed = 4;
    int moveSpeed;
    int count = 0;

    int spinDown = 80;
    int lightUp = 600;

    int RobKickDistance = 100;
    int RobPunchDistance = 100;
    int RobShootDistance = 100;

    int projectSpeed;
    int projectStart = 0;
    int facing = 0;

    final int JUMP_HEIGHT = 44;
    final int ROB_SHOOT = -40;

    Bar hpMagic = new Bar(1, whichPlayer);


    Player() {


        stopMoving();

        moveAct(10, e -> {

            outOfBounds();
            //if pressed D
            if (allBoolMove[1][2]) {

                //move right
                moveHorizon(moveSpeed);
                //     set(1,2,allPic);

            }

            //if press A
            if (allBoolMove[1][0]) {

                //move left
                moveHorizon(-moveSpeed);
                //   set(1,0,allPic);

            }


        });

        jumpAct(20, e -> {

            if (jumpHeight == 0) {

                atTop = true;

            }

            if (!atTop) {

                icon.setLocation(icon.getX(), icon.getLocation().y - jumpHeight);
                jumpHeight -= jumpSpeed;

            } else {

                icon.setLocation(icon.getX(), icon.getLocation().y + jumpHeight);
                jumpHeight += jumpSpeed;

            }

            if (emergencyStop) {

                atTop = false;
                jumpHeight = JUMP_HEIGHT;
                emergencyStop = false;
                jumpTimer.stop();


            }

            if (icon.getLocation().y >= FightClub.height - icon.getHeight() - commonFloor) {

                atTop = false;
                reset(0, 1);
                jumpHeight = JUMP_HEIGHT;
                jumpTimer.stop();

            }


        });

        bulletAct(20, e -> {

            for (int i = 0; i < allBulltes.size(); i++) {

                if (allBulltes.get(i).face == -1) {

                    allBulltes.get(i).face = facing;

                }


                if (allBulltes.get(i).face == 0) {

                    allBulltes.get(i).moveHorizon(projectSpeed);

                } else if (allBulltes.get(i).face == 2) {

                    allBulltes.get(i).moveHorizon(-projectSpeed);

                }


                if (allBulltes.get(i).getX() >= FightClub.width || allBulltes.get(i).getX() <= -allBulltes.get(i).getWidth()) {

                    allBulltes.get(i).remove();

                }

            }


        });


    }

    void setMotionBack(int a, int b, int c) {

        if (whichPlayer[0]) {

            setBack(a);

        } else if (whichPlayer[1]) {

            setBack(b);

        } else if (whichPlayer[2]) {

            setBack(c);

        }

    }

    boolean facingLeft() {

        if (facing == 2) {

            return true;

        }

        return false;

    }

    boolean facingRight() {

        if (facing == 0) {

            return true;

        }

        return false;

    }

    void setJumpHeight(int a, int b) {



    }

    void setWhichPlayer(int whichPlayerNum, JComponent RootPane) {


        if (whichPlayerNum == 1) {

            facing = 0;
            setKeyBindingP1(RootPane);

        } else if (whichPlayerNum == 2) {

            facing = 2;
            setKeyBindingP2(RootPane);

        }


    }

    //sets the movementTimer speed
    void setMoveSpeed(int s) {

        moveSpeed = s;

    }

    //sets the movementTimer speed
    void setProjectSpeed(int s) {

        projectSpeed = s;

    }

    //makes wizard move horizontally
    void moveHorizon(int m) {

        icon.setLocation(icon.getLocation().x + m, icon.getY());

    }

    void setBack(int a) {

        if (facing == 2) {

            moveHorizon(a);

        } else if (facing == 0) {

            moveHorizon(-a);

        }

    }

    void outOfBounds() {

        if (icon.getX() + icon.getWidth() >= FightClub.width) {

            icon.setLocation(FightClub.width - icon.getWidth(), icon.getY());

        } else if (icon.getX() <= 0) {

            icon.setLocation(0, icon.getY());

        }

    }

    //sets all the keybinders for player 1
    void setKeyBindingP1(JComponent RootPane) {

        //TODO: make the exit button into gui, not escape
        addKeyBinder(RootPane, KeyEvent.VK_ESCAPE, "P1Exit", e -> {

            System.exit(0);

        });

        //sets the movement block
        addKeyBinder(RootPane, KeyEvent.VK_S, "P1Block", e -> {

            if (!isAttacking() && !isJumping()) {

                stopMoving();
                set(1, 1);

            }


        }, e -> {

            reset(1, 1);

        });

        //sets the movement right
        addKeyBinder(RootPane, KeyEvent.VK_D, "P1MoveRight", e -> {
            if (!isAttacking()) {

                set(1, 2);

            }


        }, e -> {

            if (!isAttacking()) {

                reset(1, 2);

            }

        });

        //sets the movement left
        addKeyBinder(RootPane, KeyEvent.VK_A, "P1MoveLeft", e -> {
            if (!isAttacking()) {

                set(1, 0);

            }

        }, e -> {

            if (!isAttacking()) {

                reset(1, 0);

            }


        });

        //sets the movement up
        addKeyBinder(RootPane, KeyEvent.VK_W, "P1Jump", e -> {

            if (!isAttacking() && !isJumping()) {

                set(0, 1);
                jumpHeight = JUMP_HEIGHT;
                jumpTimer.start();

            }

        });

        //sets the movement hit
        addKeyBinder(RootPane, KeyEvent.VK_F, "P1Hit", e -> {

            if (!isAttacking()) {

                set(1, 3);
                stopTimer.start();

            }

        });

        //sets the movement kick
        addKeyBinder(RootPane, KeyEvent.VK_G, "P1Kick", e -> {

            if (!isAttacking() && !atTop) {

                icon.setLocation(icon.getLocation().x, icon.getY() + spinDown);
                set(1, 4);
                stopTimer.start();

            }

        });

        //sets the movement shoot
        addKeyBinder(RootPane, KeyEvent.VK_H, "P1Shoot", e -> {

            if (!isAttacking()) {

                set(1, 5);
                bulletCreation();
                bulletTimer.start();
                stopTimer.start();

            }

        });

        //sets the movement super
        addKeyBinder(RootPane, KeyEvent.VK_R, "P1Super", e -> {

            if (!isAttacking() && !isJumping()) {

                set(0, 3);
                emergencyStop = true;
                icon.setLocation(icon.getLocation().x, FightClub.height - allPic[0][0].getIconHeight() - commonFloor - lightUp);
                stopTimer.start();

            }

        });


    }

    //sets all the keybinders for player 2
    void setKeyBindingP2(JComponent RootPane) {

        //TODO: make the exit button into gui, not escape
        addKeyBinder(RootPane, KeyEvent.VK_ESCAPE, "Exit", e -> {

            System.exit(0);

        });

        //sets the movement block
        addKeyBinder(RootPane, KeyEvent.VK_DOWN, "P2Block", e -> {

            if (!isAttacking() && !isJumping()) {

                stopMoving();
                set(1, 1);

            }


        }, e -> {

            if (!isAttacking() && !isJumping()) {

                reset(1, 1);

            }

        });

        //sets the movement right
        addKeyBinder(RootPane, KeyEvent.VK_RIGHT, "P2MoveRight", e -> {
            if (!isAttacking()) {

                set(1, 2);

            }


        }, e -> {

            if (!isAttacking()) {

                reset(1, 2);

            }

        });

        //sets the movement left
        addKeyBinder(RootPane, KeyEvent.VK_LEFT, "P2MoveLeft", e -> {
            if (!isAttacking()) {

                set(1, 0);

            }

        }, e -> {

            if (!isAttacking()) {

                reset(1, 0);

            }


        });

        //sets the movement up
        addKeyBinder(RootPane, KeyEvent.VK_UP, "P2Jump", e -> {

            if (!isAttacking() && !isJumping()) {

                set(0, 1);
                jumpHeight = JUMP_HEIGHT;
                jumpTimer.start();

            }

        });

        //sets the movement hit
        addKeyBinder(RootPane, KeyEvent.VK_J, "P2Hit", e -> {

            if (!isAttacking()) {

                if (whichPlayer[1]) {

                    if (facing == 2) {

                        icon.setLocation(icon.getLocation().x - RobKickDistance, icon.getY());

                    } else if (facing == 0) {

                        icon.setLocation(icon.getLocation().x + RobKickDistance, icon.getY());

                    }

                }

                set(1, 3);
                stopTimer.start();

            }

        });

        //sets the movement kick
        addKeyBinder(RootPane, KeyEvent.VK_K, "P2Kick", e -> {

            if (!isAttacking() && !atTop) {

                if (whichPlayer[0]) {

                    icon.setLocation(icon.getLocation().x, icon.getY() + spinDown);

                } else if (whichPlayer[1]) {

                    if (facing == 2) {

                        icon.setLocation(icon.getLocation().x - RobKickDistance, icon.getY());

                    } else if (facing == 0) {

                        icon.setLocation(icon.getLocation().x + RobKickDistance, icon.getY());

                    }

                }

                set(1, 4);
                stopTimer.start();

            }

        });

        //sets the movement shoot
        addKeyBinder(RootPane, KeyEvent.VK_L, "P2Shoot", e -> {

            if (!isAttacking()) {

                set(1, 5);
                bulletCreation();
                bulletTimer.start();
                stopTimer.start();

            }

        });

        //sets the movement super
        addKeyBinder(RootPane, KeyEvent.VK_U, "P2Super", e -> {

            if (!isAttacking() && !isJumping()) {

                set(0, 3);
                if (whichPlayer[0]) {

                    emergencyStop = true;
                    icon.setLocation(icon.getLocation().x, FightClub.height - allPic[0][0].getIconHeight() - commonFloor - lightUp);

                }
                stopTimer.start();

            }

        });


    }

    void addKeyBinder(JComponent comp, int KeyCode, String id, ActionListener actionListener) {

        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap ap = comp.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyCode, 0, false), "Pressed Once " + id);

        ap.put("Pressed Once " + id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });


    }

    void addKeyBinder(JComponent comp, int KeyCode, String id, ActionListener actionListenerP, ActionListener actionListenerR) {

        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap ap = comp.getActionMap();


        im.put(KeyStroke.getKeyStroke(KeyCode, 0, false), "Pressed " + id);
        im.put(KeyStroke.getKeyStroke(KeyCode, 0, true), "Released " + id);

        ap.put("Pressed " + id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListenerP.actionPerformed(e);
            }
        });

        ap.put("Released " + id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListenerR.actionPerformed(e);
            }
        });


    }

    void moveAct(int delay, ActionListener actionListener) {

        movementTimer = new Timer(delay, e -> {

            actionListener.actionPerformed(e);

        });
    }

    void jumpAct(int delay, ActionListener actionListener) {

        jumpTimer = new Timer(delay, e -> {

            actionListener.actionPerformed(e);

        });

    }

    void stopAct(int delay, ActionListener actionListener) {

        stopTimer = new Timer(delay, e -> {

            actionListener.actionPerformed(e);

        });

    }

    void bulletAct(int delay, ActionListener actionListener) {

        bulletTimer = new Timer(delay, e -> {

            actionListener.actionPerformed(e);

        });

    }

    void set(int w, int h) {

        allBoolMove[w][h] = true;

        if (facing == 0) {

            icon.setSize(allPic[w][h].getIconWidth(), allPic[w][h].getIconHeight());
            icon.setIcon(allPic[w][h]);

        } else if (facing == 2) {

            icon.setSize(allPic[w + facing][h].getIconWidth(), allPic[w + facing][h].getIconHeight());
            icon.setIcon(allPic[w + facing][h]);

        }


    }

    void reset(int w, int h) {

        allBoolMove[w][h] = false;
        count = 0;

        if (facing == 0) {

            allPic[w][h].getImage().flush();

            if (isAllBoolFalse(allBoolMove)) {

                icon.setIcon(allPic[0][0]);
                icon.setSize(allPic[0][0].getIconWidth(), allPic[0][0].getIconHeight());

            }

            for (int i = 0; i < 2; i++) {

                for (int j = 0; j < 6; j++) {

                    if (allBoolMove[i][j]) {

                        icon.setIcon(allPic[i][j]);
                        icon.setSize(allPic[i][j].getIconWidth(), allPic[i][j].getIconHeight());

                    }

                }

            }

        } else if (facing == 2) {

            allPic[w + facing][h].getImage().flush();

            if (isAllBoolFalse(allBoolMove)) {

                icon.setIcon(allPic[facing][0]);
                icon.setSize(allPic[facing][0].getIconWidth(), allPic[facing][0].getIconHeight());

            }

            for (int i = 0; i < 2; i++) {

                for (int j = 0; j < 6; j++) {

                    if (allBoolMove[i][j]) {

                        icon.setIcon(allPic[i + facing][j]);
                        icon.setSize(allPic[i + facing][j].getIconWidth(), allPic[i + facing][j].getIconHeight());

                    }

                }

            }

        }


    }

    void setLocGround() {

        icon.setLocation(icon.getX(), FightClub.height - allPic[0][0].getIconHeight() - commonFloor);

    }

    void stopMoving() {

        //boolean for recording which button is pressed
        for (int i = 0; i < 2; i++) {

            for (int j = 0; j < 6; j++) {

                allBoolMove[i][j] = false;

            }

        }


    }

    void bulletCreation() {

        if (whichPlayer[0]) {

            allBulltes.add(new Projectile(icon, facing));


        } else if (whichPlayer[1]) {

            allBulltes.add(new Projectile(icon, facing, ROB_SHOOT));

        }

        Main.frame.add(allBulltes.get(projectStart), 0);
        ++projectStart;

    }

    boolean isAllBoolFalse(boolean[][] t) {

        for (boolean[] a : t) {

            for (boolean x : a) {

                if (x) {

                    return false;

                }

            }

        }

        return true;

    }

    boolean isAttacking() {

        if (allBoolMove[0][3]) {

            return true;

        }

        if (allBoolMove[1][3]) {

            return true;

        }

        if (allBoolMove[1][4]) {

            return true;

        }

        if (allBoolMove[1][5]) {

            return true;

        }

        return false;

    }

    boolean isJumping() {

        return allBoolMove[0][1];

    }

    boolean isWalking() {

        if (!allBoolMove[1][0] && !allBoolMove[1][2]) {

            return false;

        }

        return true;

    }


}
