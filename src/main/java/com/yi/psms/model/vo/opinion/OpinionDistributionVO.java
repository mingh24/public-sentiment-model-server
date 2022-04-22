package com.yi.psms.model.vo.opinion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpinionDistributionVO {

    @JsonProperty("attitudeOverallDist")
    private List<OpinionCountVO> attitudeOverallDist;

    @JsonProperty("priceOptionOverallDist")
    private List<OpinionCountVO> priceOptionOverallDist;

    @JsonProperty("lengthOptionOverallDist")
    private List<OpinionCountVO> lengthOptionOverallDist;

    @JsonProperty("viewOverallDist")
    private ViewDistributionVO viewOverallDist;

    @JsonProperty("attitudeIntimateDist")
    private List<OpinionCountVO> attitudeIntimateDist;

    @JsonProperty("priceOptionIntimateDist")
    private List<OpinionCountVO> priceOptionIntimateDist;

    @JsonProperty("lengthOptionIntimateDist")
    private List<OpinionCountVO> lengthOptionIntimateDist;

    @JsonProperty("viewIntimateDist")
    private ViewDistributionVO viewIntimateDist;

}
