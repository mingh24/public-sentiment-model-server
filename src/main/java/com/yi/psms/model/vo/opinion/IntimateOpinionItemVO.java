package com.yi.psms.model.vo.opinion;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IntimateOpinionItemVO implements Comparable<IntimateOpinionItemVO> {

    @SerializedName(value = "studentId")
    private Integer studentId;

    @SerializedName(value = "relationship")
    private String relationship;

    @SerializedName(value = "intimacy")
    private Integer intimacy;

    @SerializedName(value = "attitude")
    private Integer attitude;

    @SerializedName(value = "priceOption")
    private String priceOption;

    @SerializedName(value = "lengthOption")
    private String lengthOption;

    @SerializedName(value = "view")
    private String view;

    public static Integer calculateRelationshipScore(String relationship) {
        int score = 0;

        switch (relationship) {
            case "FRIEND":
                score = 999;
                break;
            case "ROOMMATE":
                score = 99;
                break;
            case "CLASSMATE":
                score = 9;
                break;
        }

        return score;
    }

    @Override
    public int compareTo(IntimateOpinionItemVO o) {
        // 亲密度降序
        if (this.getIntimacy() > o.getIntimacy()) {
            return -1;
        } else if (this.getIntimacy() < o.getIntimacy()) {
            return 1;
        }

        // 关系分值降序
        Integer thisRelationshipScore = calculateRelationshipScore(this.getRelationship());
        Integer oRelationshipScore = calculateRelationshipScore(o.getRelationship());
        if (thisRelationshipScore > oRelationshipScore) {
            return -1;
        } else if (thisRelationshipScore < oRelationshipScore) {
            return 1;
        }

        // 填写了看法优先
        String thisView = this.getView();
        String oView = o.getView();
        if (oView == null) {
            return -1;
        } else if (thisView == null) {
            return 1;
        }

        // 学号升序
        if (this.getStudentId() < o.getStudentId()) {
            return -1;
        } else if (this.getStudentId() > o.getStudentId()) {
            return 1;
        }

        return 0;
    }
}
