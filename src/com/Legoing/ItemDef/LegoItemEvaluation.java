package com.Legoing.ItemDef;

import java.util.List;

public class LegoItemEvaluation {
    String itemNo;
    int bricksetScore;
    int lugnetScore;
    int lugnetNumber;
    int itemTotalScore;
    List<LegoingUserEvaluation> itemComment;
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public int getBricksetScore() {
        return bricksetScore;
    }
    public void setBricksetScore(int bricksetScore) {
        this.bricksetScore = bricksetScore;
    }
    public int getLugnetScore() {
        return lugnetScore;
    }
    public void setLugnetScore(int lugnetScore) {
        this.lugnetScore = lugnetScore;
    }
    public int getLugnetNumber() {
        return lugnetNumber;
    }
    public void setLugnetNumber(int lugnetNumber) {
        this.lugnetNumber = lugnetNumber;
    }
    public int getItemTotalScore() {
        return itemTotalScore;
    }
    public void setItemTotalScore(int itemTotalScore) {
        this.itemTotalScore = itemTotalScore;
    }
    public List<LegoingUserEvaluation> getItemComment() {
        return itemComment;
    }
    public void setItemComment(List<LegoingUserEvaluation> itemComment) {
        this.itemComment = itemComment;
    }

}
