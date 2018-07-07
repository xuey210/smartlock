package com.sectong.domain.mongomodle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyong on 16/7/13.
 * mobileeasy.
 */
public class MainFrameFactory implements Serializable {

    private transient static final long serialVersionUID = -8662497074013536276L;
    /**
    private static ConcurrentHashMap<String,EquipMent> bedroommainEq =new ConcurrentHashMap<>(14);
    private static ConcurrentHashMap<String,EquipMent>  diningEq = new ConcurrentHashMap<>(6);
    private static ConcurrentHashMap<String,EquipMent>  parlourEq = new ConcurrentHashMap<>(10);
    private static ConcurrentHashMap<String,EquipMent>  studtEq = new ConcurrentHashMap<>(8);
    private static ConcurrentHashMap<String,EquipMent>  toiletEq = new ConcurrentHashMap<>(4);


    static {
        bedroommainEq.put("air",new EquipMent("air", ""));
        bedroommainEq.put("tv",new EquipMent("tv", ""));
        bedroommainEq.put("music", new EquipMent("music", ""));
        bedroommainEq.put("floor", new EquipMent("floor", ""));
        bedroommainEq.put("lamp", new EquipMent("lamp", ""));
        bedroommainEq.put("lampbed", new EquipMent("lampbed", ""));
        bedroommainEq.put("curtain", new EquipMent("curtain", ""));
        bedroommainEq.put("window", new EquipMent("window", ""));
        bedroommainEq.put("lampwc", new EquipMent("lampwc", ""));
        bedroommainEq.put("lampparlour", new EquipMent("lampparlour", ""));
        bedroommainEq.put("security", new EquipMent("security", ""));
//        bedroommainEq.put("box", new EquipMent("box", ""));
//        bedroommainEq.put("purifier", new EquipMent("purifier", ""));
//        bedroommainEq.put("heater", new EquipMent("heater", ""));

        diningEq.put("air", new EquipMent("air", ""));
        diningEq.put("music", new EquipMent("music", ""));
        diningEq.put("floor", new EquipMent("floor", ""));
        diningEq.put("lamp", new EquipMent("lamp", ""));
        diningEq.put("lamppool", new EquipMent("lamppool", ""));
        diningEq.put("security", new EquipMent("security", ""));

        parlourEq.put("air", new EquipMent("air", ""));
        parlourEq.put("tv", new EquipMent("tv", ""));
        parlourEq.put("music", new EquipMent("music", ""));
        parlourEq.put("floor", new EquipMent("floor", ""));
        parlourEq.put("lamp", new EquipMent("lamp", ""));
        parlourEq.put("lamppool", new EquipMent("lamppool", ""));
        parlourEq.put("curtain", new EquipMent("curtain", ""));
        parlourEq.put("window", new EquipMent("window", ""));
        parlourEq.put("lampwall", new EquipMent("lampwall", ""));
        parlourEq.put("security", new EquipMent("security", ""));

        studtEq.put("air", new EquipMent("air", ""));
        studtEq.put("music", new EquipMent("music", ""));
        studtEq.put("floor", new EquipMent("floor", ""));
        studtEq.put("lamp", new EquipMent("lamp", ""));
        studtEq.put("curtain", new EquipMent("curtain", ""));
        studtEq.put("window", new EquipMent("window", ""));
        studtEq.put("lampfloor", new EquipMent("lampfloor", ""));
        studtEq.put("security", new EquipMent("security", ""));

        toiletEq.put("air", new EquipMent("air", ""));
        toiletEq.put("music", new EquipMent("music", ""));
        toiletEq.put("lampwc", new EquipMent("lampwc", ""));
        toiletEq.put("security", new EquipMent("security", ""));
    }
     */

    public static void initMainFrame(MainFrame mainFrame) {
        List<Room> roomList = new ArrayList<>(13);
        Room bedroommain = new Room("bedroommain", "");
//        bedroommain.setEquipment(bedroommainEq);
        roomList.add(bedroommain);
        Room bedroommain1 = new Room("bedroommain1", "");
//        bedroommain1.setEquipment(bedroommainEq);
        roomList.add(bedroommain1);
        Room bedroom = new Room("bedroom", "");
//        bedroom.setEquipment(bedroommainEq);
        roomList.add(bedroom);
        Room bedroom1 = new Room("bedroom1", "");
//        bedroom1.setEquipment(bedroommainEq);
        roomList.add(bedroom1);
        Room parlour = new Room("parlour", "");
//        parlour.setEquipment(parlourEq);
        roomList.add(parlour);
        Room parlour1 = new Room("parlour1", "");
//        parlour1.setEquipment(parlourEq);
        roomList.add(parlour1);
        Room dining = new Room("dining", "");
//        dining.setEquipment(diningEq);
        roomList.add(dining);
        Room dining1 = new Room("dining1", "");
//        dining1.setEquipment(diningEq);
        roomList.add(dining1);
        Room study = new Room("study", "");
//        study.setEquipment(studtEq);
        roomList.add(study);
        Room study1 = new Room("study1", "");
//        study1.setEquipment(studtEq);
        roomList.add(study1);
        Room toilet = new Room("toilet", "");
//        toilet.setEquipment(toiletEq);
        roomList.add(toilet);
        Room toilet1 = new Room("toilet1", "");
//        toilet1.setEquipment(toiletEq);
        roomList.add(toilet1);
        Room hall = new Room("hall", "");
        roomList.add(hall);//here should be add all eq.
        mainFrame.setRoomList(roomList);
    }
}
