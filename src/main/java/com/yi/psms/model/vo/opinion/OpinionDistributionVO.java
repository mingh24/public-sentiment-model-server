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

}
