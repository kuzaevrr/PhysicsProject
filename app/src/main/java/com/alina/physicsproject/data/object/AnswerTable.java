package com.alina.physicsproject.data.object;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "answer")
public class AnswerTable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameParagraph;
    private String appraisal;

    public AnswerTable(int id, String nameParagraph, String appraisal) {
        this.id = id;
        this.nameParagraph = nameParagraph;
        this.appraisal = appraisal;
    }

    @Ignore
    public AnswerTable(String nameParagraph, String appraisal) {
        this.nameParagraph = nameParagraph;
        this.appraisal = appraisal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameParagraph() {
        return nameParagraph;
    }

    public void setNameParagraph(String nameParagraph) {
        this.nameParagraph = nameParagraph;
    }

    public String getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(String appraisal) {
        this.appraisal = appraisal;
    }
}
