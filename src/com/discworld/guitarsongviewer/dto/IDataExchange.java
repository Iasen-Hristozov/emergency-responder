package com.discworld.guitarsongviewer.dto;

import com.discworld.guitarsonglib.CChordsVerse;
import com.discworld.guitarsonglib.CVerseSet;

public interface IDataExchange
{
   public void setLinesNbr(int iLinesNbr);
   public int getLinesNbr();
   public CVerseSet getPage(int iNdx);
   public int getTextSize();
   public int getEnuDisplayChords();
   public CChordsVerse getChordsVerse(String sID);
}
